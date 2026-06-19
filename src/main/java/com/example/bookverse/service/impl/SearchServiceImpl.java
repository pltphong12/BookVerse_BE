package com.example.bookverse.service.impl;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import com.example.bookverse.domain.Book;
import com.example.bookverse.domain.QBook;
import com.example.bookverse.dto.criteria.CriteriaFilterProduct;
import com.example.bookverse.dto.enums.SortType;
import com.example.bookverse.dto.record.ProductHit;
import com.example.bookverse.dto.record.SearchAutocompleteResponse;
import com.example.bookverse.dto.response.*;
import com.example.bookverse.repository.BookRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.suggest.response.CompletionSuggestion;
import org.springframework.data.elasticsearch.core.suggest.response.Suggest;
import org.springframework.stereotype.Service;

import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import co.elastic.clients.elasticsearch.core.search.Suggester;

import com.example.bookverse.elasticsearch.BookDocument;
import com.example.bookverse.service.SearchService;

@Service
@Slf4j
public class SearchServiceImpl implements SearchService {

    private static final int AUTOCOMPLETE_SUGGEST_LIMIT = 6;
    private static final int AUTOCOMPLETE_PRODUCT_LIMIT = 4;

    private final ElasticsearchOperations elasticsearchOperations;
    private final BookRepository bookRepository;
    private final JPAQueryFactory queryFactory;

    public SearchServiceImpl(ElasticsearchOperations elasticsearchOperations, BookRepository bookRepository,  JPAQueryFactory queryFactory) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.bookRepository = bookRepository;
        this.queryFactory = queryFactory;
    }

    @Override
    public SearchAutocompleteResponse autocomplete(String prefix) {
        CompletableFuture<List<String>> suggestionsFuture =
            CompletableFuture.supplyAsync(() -> {
                try {
                    NativeQuery suggestQuery = NativeQuery.builder()
                        .withMaxResults(0)
                        .withSuggester(Suggester.of(s -> s.suggesters("book_suggest",
                            fs -> fs.prefix(prefix).completion(c -> c.field("suggest")
                                .size(AUTOCOMPLETE_SUGGEST_LIMIT)
                                .skipDuplicates(true)))))
                        .build();

                    SearchHits<BookDocument> suggestHits = elasticsearchOperations.search(suggestQuery, BookDocument.class);
                    return extractCompletionSuggestions(suggestHits);
                } catch (Exception e) {
                    log.error("fetchSuggestions error: {}", e.getMessage());
                    return List.of();
                }
            });

        CompletableFuture<List<ProductHit>> productsFuture =
            CompletableFuture.supplyAsync(() -> {
                try {
                    Pageable productPage = PageRequest.of(0, AUTOCOMPLETE_PRODUCT_LIMIT);
                    NativeQuery productQuery = NativeQuery.builder()
                        .withQuery(qu -> qu.multiMatch(m -> m
                            .query(prefix)
                            .fields("title^3", "authors^2")  // thêm boost authors^2
                            .type(TextQueryType.BoolPrefix)))
                        .withPageable(productPage)
                        .build();

                    SearchHits<BookDocument> productHits = elasticsearchOperations.search(productQuery, BookDocument.class);
                    return productHits.getSearchHits().stream()
                        .map(SearchHit::getContent)
                        .map(this::toProductHit)
                        .toList();
                } catch (Exception e) {
                    log.error("fetchProducts error: {}", e.getMessage());
                    return List.of();
                }
            });

        CompletableFuture.allOf(suggestionsFuture, productsFuture).join();

        try {
            List<String> suggestions = suggestionsFuture.get();
            List<ProductHit> products = productsFuture.get();

            if (suggestions.isEmpty()) {
                suggestions = fallbackSuggestionsFromProducts(products);
            }

            return new SearchAutocompleteResponse(suggestions, products);
        } catch (Exception e) {
            log.error("autocomplete merge error: {}", e.getMessage());
            return new SearchAutocompleteResponse(List.of(), List.of());
        }
    }

    @Override
    public ResPagination searchAllBooksWithPaginationAndFilter(CriteriaFilterProduct criteriaFilterProduct, Pageable pageable) throws Exception {
        Page<Book> pageBook = this.filter(criteriaFilterProduct, pageable);
        ResPagination rs = new ResPagination();
        ResPagination.Meta mt = new ResPagination.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageBook.getSize());

        mt.setPages(pageBook.getTotalPages());
        mt.setTotal(pageBook.getTotalElements());

        rs.setMeta(mt);

        List<Book> books = pageBook.getContent();
        List<ResBookDTO> bookDTOS = new ArrayList<>();
        for (Book book : books) {
            ResBookDTO bookDTO = ResBookDTO.from(book);
            bookDTOS.add(bookDTO);
        }

        rs.setResult(bookDTOS);
        return rs;
    }

    private Page<Book> filter(CriteriaFilterProduct criteriaFilterProduct, Pageable pageable) {
        if (criteriaFilterProduct != null && hasSearchQuery(criteriaFilterProduct)) {
            return filterViaElasticsearch(criteriaFilterProduct, pageable);
        }
        return filterViaQueryDSL(criteriaFilterProduct, pageable);
    }

    private static boolean hasSearchQuery(CriteriaFilterProduct c) {
        return c.getTitle() != null && !c.getTitle().isBlank();
    }

    private Page<Book> filterViaElasticsearch(CriteriaFilterProduct criteria, Pageable pageable) {
        String q = criteria.getTitle().trim();

        var queryBuilder = NativeQuery.builder()
            .withQuery(qu -> qu.bool(bool -> {
                bool.must(m -> m.multiMatch(mm -> mm
                    .query(q)
                    .fields("title^3", "authors^2", "category", "description")
                    .type(TextQueryType.BestFields)
                    .operator(Operator.Or)));
                applyEsFilters(bool, criteria);
                return bool;
            }))
            .withPageable(pageable)
            .withSourceFilter(new FetchSourceFilter(new String[] { "id" }, null));

        for (SortOptions sort : buildEsSort(criteria.getSortType())) {
            queryBuilder.withSort(sort);
        }

        SearchHits<BookDocument> hits = elasticsearchOperations.search(queryBuilder.build(), BookDocument.class);
        long total = hits.getTotalHits();

        List<Long> orderedIds = hits.getSearchHits().stream()
            .map(hit -> Long.parseLong(hit.getContent().getId()))
            .toList();

        if (orderedIds.isEmpty()) {
            return new PageImpl<>(List.of(), pageable, total);
        }

        List<Book> books = bookRepository.findAllById(orderedIds);
        List<Book> orderedBooks = reorderByIds(orderedIds, books);

        return new PageImpl<>(orderedBooks, pageable, total);
    }

    /**
     * Post-filters in ES {@code filter} context (no scoring). Field names match {@link BookDocument}.
     */
    private static void applyEsFilters(BoolQuery.Builder b, CriteriaFilterProduct criteria) {
        if (criteria.getCategoryId() != null && !criteria.getCategoryId().isEmpty()) {
            b.filter(f -> f.terms(t -> t.field("categoryId").terms(
                tv -> tv.value(criteria.getCategoryId().stream().map(FieldValue::of).toList()))));
        }
        if (criteria.getPublisherId() != null && !criteria.getPublisherId().isEmpty()) {
            b.filter(f -> f.terms(t -> t.field("publisherId").terms(
                tv -> tv.value(criteria.getPublisherId().stream().map(FieldValue::of).toList()))));
        }
        if (criteria.getPublishYear() != null && !criteria.getPublishYear().isEmpty()) {
            b.filter(f -> f.terms(t -> t.field("publisherYear").terms(
                tv -> tv.value(criteria.getPublishYear().stream()
                    .map(y -> FieldValue.of(y.longValue()))
                    .toList()))));
        }
        if (criteria.getCoverFormat() != null && !criteria.getCoverFormat().isEmpty()) {
            b.filter(f -> f.terms(t -> t.field("coverFormat").terms(
                tv -> tv.value(criteria.getCoverFormat().stream()
                    .map(cf -> FieldValue.of(cf.name()))
                    .toList()))));
        }
        if (criteria.getMinPrice() != null || criteria.getMaxPrice() != null) {
            b.filter(f -> f.range(r -> r.number(n -> {
                n.field("price");
                if (criteria.getMinPrice() != null) {
                    n.gte(criteria.getMinPrice());
                }
                if (criteria.getMaxPrice() != null) {
                    n.lte(criteria.getMaxPrice());
                }
                return n;
            })));
        }
    }

    /**
     * When {@code sortType} is null and search uses ES, sort by relevance ({@code _score} desc).
     * <p>
     * {@link SortType#NEWEST} uses {@code publisherYear} desc in ES because {@link BookDocument} has no
     * {@code createdAt}; add that field to the index for a true newest-first order.
     */
    private static List<SortOptions> buildEsSort(SortType sortType) {
        if (sortType == null) {
            return List.of(SortOptions.of(s -> s.score(sc -> sc.order(SortOrder.Desc))));
        }
        return List.of(switch (sortType) {
            case NEWEST -> SortOptions.of(s -> s.field(f -> f.field("publisherYear").order(SortOrder.Desc)));
            case SOLD_MOST -> SortOptions.of(s -> s.field(f -> f.field("sold").order(SortOrder.Desc)));
            case PRICE_LOW_TO_HIGH -> SortOptions.of(s -> s.field(f -> f.field("price").order(SortOrder.Asc)));
            case PRICE_HIGH_TO_LOW -> SortOptions.of(s -> s.field(f -> f.field("price").order(SortOrder.Desc)));
        });
    }

    private static List<Book> reorderByIds(List<Long> orderedIds, List<Book> books) {
        Map<Long, Book> byId = new LinkedHashMap<>();
        for (Book book : books) {
            byId.put(book.getId(), book);
        }
        List<Book> ordered = new ArrayList<>(byId.size());
        for (Long id : orderedIds) {
            Book book = byId.get(id);
            if (book != null) {
                ordered.add(book);
            }
        }
        return ordered;
    }

    private Page<Book> filterViaQueryDSL(CriteriaFilterProduct criteriaFilterProduct, Pageable pageable) {
        if (criteriaFilterProduct == null) {
            criteriaFilterProduct = new CriteriaFilterProduct();
        }

        QBook qBook = QBook.book;
        BooleanBuilder builder = new BooleanBuilder();

        if (criteriaFilterProduct.getTitle() != null && !criteriaFilterProduct.getTitle().isBlank()) {
            builder.and(qBook.title.containsIgnoreCase(criteriaFilterProduct.getTitle()));
        }
        if (criteriaFilterProduct.getCategoryId() != null && !criteriaFilterProduct.getCategoryId().isEmpty()) {
            builder.and(qBook.category.id.in(criteriaFilterProduct.getCategoryId()));
        }
        if (criteriaFilterProduct.getPublisherId() != null && !criteriaFilterProduct.getPublisherId().isEmpty()) {
            builder.and(qBook.publisher.id.in(criteriaFilterProduct.getPublisherId()));
        }
        if (criteriaFilterProduct.getPublishYear() != null && !criteriaFilterProduct.getPublishYear().isEmpty()) {
            builder.and(qBook.publishYear.in(criteriaFilterProduct.getPublishYear()));
        }
        if (criteriaFilterProduct.getCoverFormat() != null && !criteriaFilterProduct.getCoverFormat().isEmpty()) {
            builder.and(qBook.coverFormat.in(criteriaFilterProduct.getCoverFormat()));
        }
        if (criteriaFilterProduct.getMinPrice() != null) {
            builder.and(qBook.price.goe(criteriaFilterProduct.getMinPrice()));
        }
        if (criteriaFilterProduct.getMaxPrice() != null) {
            builder.and(qBook.price.loe(criteriaFilterProduct.getMaxPrice()));
        }

        OrderSpecifier<?> orderSpecifier = qBook.createdAt.desc();
        if (criteriaFilterProduct.getSortType() != null) {
            orderSpecifier = switch (criteriaFilterProduct.getSortType()) {
                case NEWEST -> qBook.createdAt.desc();
                case SOLD_MOST -> qBook.sold.desc();
                case PRICE_LOW_TO_HIGH -> qBook.price.asc();
                case PRICE_HIGH_TO_LOW -> qBook.price.desc();
            };
        }

        List<Book> books = queryFactory.selectFrom(qBook)
            .where(builder)
            .orderBy(orderSpecifier)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long total = queryFactory.selectFrom(qBook)
            .where(builder)
            .fetchCount();

        return new PageImpl<>(books, pageable, total);
    }

    private List<String> extractCompletionSuggestions(SearchHits<BookDocument> hits) {
        List<String> out = new ArrayList<>();
        Set<String> seen = new LinkedHashSet<>();
        if (!hits.hasSuggest()) {
            return out;
        }
        Suggest suggest = hits.getSuggest();
        var suggestion = suggest.getSuggestion("book_suggest");
        if (!(suggestion instanceof CompletionSuggestion<?> comp)) {
            return out;
        }
        for (CompletionSuggestion.Entry<?> entry : comp.getEntries()) {
            for (CompletionSuggestion.Entry.Option<?> opt : entry.getOptions()) {
                String text = opt.getText();
                if (text != null && seen.add(text)) {
                    out.add(text);
                }
            }
        }
        return out;
    }

    private List<String> fallbackSuggestionsFromProducts(List<ProductHit> products) {
        List<String> titles = products.stream().map(ProductHit::title).filter(t -> t != null && !t.isBlank()).toList();
        return new ArrayList<>(new LinkedHashSet<>(titles));
    }

    private ProductHit toProductHit(BookDocument doc) {
        long id = Long.parseLong(doc.getId());
        String img = doc.getImageUrl() != null ? doc.getImageUrl() : "";
        return new ProductHit(id, doc.getTitle(), img);
    }
}

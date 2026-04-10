package com.example.bookverse.service.impl;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bookverse.domain.Author;
import com.example.bookverse.domain.Book;
import com.example.bookverse.domain.BookImage;
import com.example.bookverse.domain.QBook;
import com.example.bookverse.dto.criteria.CriteriaFilterBook;
import com.example.bookverse.dto.criteria.CriteriaFilterProduct;
import com.example.bookverse.dto.request.ReqBookImageDTO;
import com.example.bookverse.dto.response.ResBookDTO;
import com.example.bookverse.dto.response.ResPagination;
import com.example.bookverse.exception.global.ExistDataException;
import com.example.bookverse.repository.AuthorRepository;
import com.example.bookverse.repository.BookRepository;
import com.example.bookverse.service.BookService;
import com.example.bookverse.util.EntityValidator;
import com.example.bookverse.util.FindObjectInDataBase;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final JPAQueryFactory queryFactory;

    public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository,
            JPAQueryFactory queryFactory) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.queryFactory = queryFactory;
    }

    @Override
    @Transactional
    public Book create(Book book) throws Exception {
        if (bookRepository.existsByTitle(book.getTitle())) {
            throw new ExistDataException(book.getTitle() + " already exists");
        }
        if (book.getAuthors() != null) {
            List<Long> authorIds = book.getAuthors().stream()
                    .map(Author::getId)
                    .toList();
            EntityValidator.validateIdsExist(authorIds, authorRepository, "Author");
        }

        List<ReqBookImageDTO> imageRequests = book.getImages();
        book.setImages(null);

        if (imageRequests != null && !imageRequests.isEmpty()) {
            List<BookImage> images = new ArrayList<>();
            boolean hasPrimary = false;
            for (int i = 0; i < imageRequests.size(); i++) {
                ReqBookImageDTO req = imageRequests.get(i);
                BookImage img = new BookImage();
                img.setBook(book);
                img.setRelativePath(req.getRelativePath().trim().replace('\\', '/'));
                img.setSortOrder(req.getSortOrder());
                img.setPrimaryImage(req.isPrimary());
                if (req.isPrimary()) {
                    hasPrimary = true;
                }
                images.add(img);
            }
            if (!hasPrimary) {
                images.getFirst().setPrimaryImage(true);
            }
            book.setBookImages(images);
            BookImage primary = images.stream().filter(BookImage::isPrimaryImage)
                    .findFirst().orElse(images.getFirst());
            book.setImage(primary.getRelativePath());
        }

        return this.bookRepository.save(book);
    }

    @Override
    @Transactional
    public Book update(Book book) throws Exception {
        Book bookInDB = FindObjectInDataBase.findByIdOrThrow(bookRepository, book.getId());
        if (book.getTitle() != null && !bookInDB.getTitle().equals(book.getTitle())) {
            if (bookRepository.existsByTitle(book.getTitle())) {
                throw new ExistDataException(book.getTitle() + " already exists");
            }
            bookInDB.setTitle(book.getTitle());
        }
        if (book.getDescription() != null && !bookInDB.getDescription().equals(book.getDescription())) {
            bookInDB.setDescription(book.getDescription());
        }
        if (book.getPublisher() != null && !bookInDB.getPublisher().equals(book.getPublisher())) {
            bookInDB.setPublisher(book.getPublisher());
        }
        if (book.getSupplier() != null && !bookInDB.getSupplier().equals(book.getSupplier())) {
            bookInDB.setSupplier(book.getSupplier());
        }
        if (book.getCategory() != null && !bookInDB.getCategory().equals(book.getCategory())) {
            bookInDB.setCategory(book.getCategory());
        }
        if (book.getPrice() != 0) {
            bookInDB.setPrice(book.getPrice());
        }
        if (book.getDiscount() != 0) {
            bookInDB.setDiscount(book.getDiscount());
        }
        if (book.getQuantity() != 0) {
            bookInDB.setQuantity(book.getQuantity());
        }
        if (book.getAuthors() != null && !bookInDB.getAuthors().equals(book.getAuthors())) {
            List<Long> authorIds = book.getAuthors().stream()
                    .map(Author::getId)
                    .toList();
            EntityValidator.validateIdsExist(authorIds, authorRepository, "Author");
            bookInDB.setAuthors(book.getAuthors());
        }
        if (book.getDimensions() != null && !bookInDB.getDimensions().equals(book.getDimensions())) {
            bookInDB.setDimensions(book.getDimensions());
        }
        if (book.getNumberOfPages() != 0) {
            bookInDB.setNumberOfPages(book.getNumberOfPages());
        }
        if (book.getCoverFormat() != null && !bookInDB.getCoverFormat().equals(book.getCoverFormat())) {
            bookInDB.setCoverFormat(book.getCoverFormat());
        }
        if (book.getPublishYear() != 0) {
            bookInDB.setPublishYear(book.getPublishYear());
        }
        if (book.getWeight() != 0) {
            bookInDB.setWeight(book.getWeight());
        }

        List<ReqBookImageDTO> imageRequests = book.getImages();
        if (imageRequests != null && !imageRequests.isEmpty()) {
            List<BookImage> existingImages = bookInDB.getBookImages();

            if (!isSameImageList(existingImages, imageRequests)) {
                existingImages.clear();

                boolean hasPrimary = false;
                for (ReqBookImageDTO req : imageRequests) {
                    BookImage img = new BookImage();
                    img.setBook(bookInDB);
                    img.setRelativePath(req.getRelativePath().trim().replace('\\', '/'));
                    img.setSortOrder(req.getSortOrder());
                    img.setPrimaryImage(req.isPrimary());
                    if (req.isPrimary()) {
                        hasPrimary = true;
                        bookInDB.setImage(img.getRelativePath());
                    }
                    existingImages.add(img);
                }
                if (!hasPrimary && !existingImages.isEmpty()) {
                    existingImages.getFirst().setPrimaryImage(true);
                    bookInDB.setImage(existingImages.getFirst().getRelativePath());
                }
            }
        }

        return this.bookRepository.save(bookInDB);
    }

    private boolean isSameImageList(List<BookImage> existing, List<ReqBookImageDTO> incoming) {
        if (existing.size() != incoming.size()) {
            return false;
        }
        for (int i = 0; i < existing.size(); i++) {
            BookImage db = existing.get(i);
            ReqBookImageDTO req = incoming.get(i);
            if (!db.getRelativePath().equals(req.getRelativePath().trim().replace('\\', '/'))) {
                return false;
            }
            if (db.getSortOrder() != req.getSortOrder()) {
                return false;
            }
            if (db.isPrimaryImage() != req.isPrimary()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Book fetchBookById(long id) throws Exception {
        return FindObjectInDataBase.findByIdOrThrow(this.bookRepository, id);
    }

    @Override
    public List<Book> fetchAllBooks() throws Exception {
        return this.bookRepository.findAll();
    }

    @Override
    public List<Book> fetchTop5BooksByCreatedAt() throws Exception {
        return this.bookRepository.findTop5ByOrderByCreatedAtDesc();
    }

    public Page<Book> filter(CriteriaFilterBook criteriaFilterBook, Pageable pageable) {
        QBook qBook = QBook.book;

        BooleanBuilder builder = new BooleanBuilder();
        if (criteriaFilterBook.getTitle() != null && !criteriaFilterBook.getTitle().isBlank()) {
            builder.and(qBook.title.containsIgnoreCase(criteriaFilterBook.getTitle()));
        }
        if (criteriaFilterBook.getPublisherId() != 0) {
            builder.and(qBook.publisher.id.eq(criteriaFilterBook.getPublisherId()));
        }
        if (criteriaFilterBook.getAuthorId() != 0) {
            builder.and(qBook.authors.any().id.eq(criteriaFilterBook.getAuthorId()));
        }
        if (criteriaFilterBook.getCategoryId() != 0) {
            builder.and(qBook.category.id.eq(criteriaFilterBook.getCategoryId()));
        }
        if (criteriaFilterBook.getDateFrom() != null) {
            Instant fromInstant = criteriaFilterBook.getDateFrom().atStartOfDay(ZoneId.systemDefault()).toInstant();
            builder.and(qBook.createdAt.goe(fromInstant));
        }
        List<Book> books = queryFactory.selectFrom(qBook)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.selectFrom(qBook)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(books, pageable, total);
    }

    @Override
    public ResPagination fetchAllBooksWithPaginationAndFilter(CriteriaFilterBook criteriaFilterBook,
            Pageable pageable) {
        Page<Book> pageBook = this.filter(criteriaFilterBook, pageable);
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

    @Override
    public void delete(long id) throws Exception {
        FindObjectInDataBase.findByIdOrThrow(this.bookRepository, id);
        this.bookRepository.deleteById(id);
    }

    public Page<Book> filter(CriteriaFilterProduct criteriaFilterProduct, Pageable pageable) {
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

    @Override
    public ResPagination fetchAllBooksWithPaginationAndFilter(CriteriaFilterProduct criteriaFilterProduct,
            Pageable pageable) {
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
}

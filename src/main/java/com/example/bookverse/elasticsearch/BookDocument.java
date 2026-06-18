package com.example.bookverse.elasticsearch;

import com.example.bookverse.domain.Author;
import com.example.bookverse.domain.Book;
import com.example.bookverse.dto.enums.CoverFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.CompletionField;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.core.suggest.Completion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Elasticsearch document cho index {@code books}; không dùng chung với
 * {@link Book} (JPA).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "books", createIndex = false)
public class BookDocument {

    @Id
    private String id;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String title;

    @Field(type = FieldType.Text, analyzer = "standard")
    private List<String> authors;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String category;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String description;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String publisher;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String supplier;

    @Field(type = FieldType.Long)
    private Long quantity;

    @Field(type = FieldType.Integer)
    private Integer discount;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String ragContent;

    private List<Float> embedding;

    @Field(type = FieldType.Long)
    private Long categoryId;

    @Field(type = FieldType.Long)
    private Long publisherId;

    @Field(type = FieldType.Long)
    private Long publisherYear;

    @Field(type = FieldType.Double)
    private Double price;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String coverFormat;

    @Field(name = "imageUrl", type = FieldType.Keyword, index = false)
    private String imageUrl;

    private Long sold;

    @CompletionField
    private Completion suggest;

    // When call repository.save(), you must call this function so that save book
    // into ES
    public static BookDocument fromBook(Book book) {
        List<String> authorNames = Optional.ofNullable(book.getAuthors())
                .orElse(Collections.emptyList())
                .stream()
                .map(Author::getName)
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .distinct()
                .collect(Collectors.toList());

        String categoryName = Optional.ofNullable(book.getCategory())
                .map(c -> c.getName())
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .orElse(null);

        String publisherName = Optional.ofNullable(book.getPublisher())
                .map(p -> p.getName())
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .orElse(null);

        String supplierName = Optional.ofNullable(book.getSupplier())
                .map(s -> s.getName())
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .orElse(null);

        List<String> suggestInputs = new ArrayList<>();
        addSuggestToken(suggestInputs, book.getTitle());
        authorNames.forEach(name -> addSuggestToken(suggestInputs, name));
        addSuggestToken(suggestInputs, categoryName);

        if (suggestInputs.isEmpty()) {
            suggestInputs.add("book-" + book.getId());
        }

        Long categoryId = Optional.ofNullable(book.getCategory()).map(c -> c.getId()).orElse(null);
        Long publisherId = Optional.ofNullable(book.getPublisher()).map(p -> p.getId()).orElse(null);
        String coverFormatStr = Optional.ofNullable(book.getCoverFormat()).map(CoverFormat::name).orElse(null);
        Double price = Optional.of(book.getPrice()).orElse(0.0);
        String ragContent = buildRagContent(book, authorNames, categoryName, publisherName, supplierName,
                coverFormatStr, price);

        return BookDocument.builder()
                .id(String.valueOf(book.getId()))
                .title(book.getTitle())
                .authors(authorNames.isEmpty() ? List.of() : authorNames)
                .category(categoryName)
                .description(book.getDescription())
                .publisher(publisherName)
                .supplier(supplierName)
                .quantity(book.getQuantity())
                .discount(book.getDiscount())
                .ragContent(ragContent)
                .categoryId(categoryId)
                .publisherId(publisherId)
                .publisherYear((long) book.getPublishYear())
                .coverFormat(coverFormatStr)
                .imageUrl(book.getImage())
                .price(price)
                .sold(book.getSold())
                .suggest(new Completion(suggestInputs))
                .build();
    }

    private static String buildRagContent(
            Book book,
            List<String> authorNames,
            String categoryName,
            String publisherName,
            String supplierName,
            String coverFormatStr,
            Double price) {
        StringBuilder content = new StringBuilder();

        appendLine(content, "Tên sách", book.getTitle());
        appendLine(content, "Tác giả",
                authorNames == null || authorNames.isEmpty() ? null : String.join(", ", authorNames));
        appendLine(content, "Danh mục", categoryName);
        appendLine(content, "Nhà xuất bản", publisherName);
        appendLine(content, "Nhà cung cấp", supplierName);
        appendLine(content, "Giá bán", price == null ? null : price + " VND");
        appendLine(content, "Số lượng tồn kho", String.valueOf(book.getQuantity()));
        appendLine(content, "Đã bán", String.valueOf(book.getSold()));
        appendLine(content, "Giảm giá", book.getDiscount() + "%");
        appendLine(content, "Năm xuất bản", String.valueOf(book.getPublishYear()));
        appendLine(content, "Số trang", String.valueOf(book.getNumberOfPages()));
        appendLine(content, "Hình thức bìa", coverFormatStr);
        appendLine(content, "Mô tả", book.getDescription());

        return content.toString().trim();
    }

    private static void appendLine(StringBuilder content, String label, String value) {
        if (value == null || value.isBlank()) {
            return;
        }
        content.append(label).append(": ").append(value.trim()).append('\n');
    }

    private static void addSuggestToken(List<String> inputs, String value) {
        if (value == null) {
            return;
        }
        String t = value.trim();
        if (!t.isEmpty()) {
            inputs.add(t);
        }
    }
}

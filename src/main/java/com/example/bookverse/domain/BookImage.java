package com.example.bookverse.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "book_images",
        indexes = {
                @Index(name = "idx_book_images_book_sort", columnList = "book_id,sort_order")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    @JsonIgnore
    private Book book;

    /**
     * Đường dẫn tương đối so với thư mục upload (ví dụ {@code books/1730000-cover.jpg}),
     * URL hiển thị: {@code /storage/} + relativePath
     */
    @NotBlank
    @Column(name = "relative_path", nullable = false, length = 512)
    private String relativePath;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder = 0;

    @Column(name = "is_primary", nullable = false)
    private boolean primaryImage = false;
}

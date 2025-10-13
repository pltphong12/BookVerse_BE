package com.example.bookverse.domain;

import com.example.bookverse.domain.enums.CoverFormat;
import com.example.bookverse.util.SecurityUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "books")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "title isn't blank")
    private String title;
    private double price;
    private long quantity;
    private long sold;
    private int discount;
    // Thông tin chi tiết
    private int publishYear;
    private double weight;
    private String dimensions;
    private int numberOfPages;
    @Enumerated(EnumType.STRING)
    private CoverFormat coverFormat;
    // Thông tin mô tả
    @NotBlank(message = "description isn't blank")
    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;
    private String image;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "books" })
    @JoinTable(name = "author_book", joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns = @JoinColumn(name = "author_id"))
    private List<Author> authors;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @PrePersist
    public void handleBeforeCreate() {
        createdAt = Instant.now();
        createdBy = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        updatedAt = Instant.now();
        updatedBy = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
    }
}

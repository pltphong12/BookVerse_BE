package com.example.bookverse.domain.response;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.bookverse.domain.Author;
import com.example.bookverse.domain.Book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResAuthorDTO {
    private long id;
    private String name;
    private Date birthday;
    private String nationality;
    private String avatar;
    private List<InfoBookInAuthorDTO> books;

    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InfoBookInAuthorDTO {
        private long id;
        private String title;

        private String publisher;
        private double price;
        private long quantity;
        private String description;
        private String image;
        private String category;
    }

    public static ResAuthorDTO from(Author author) {
        ResAuthorDTO dto = new ResAuthorDTO();
        dto.setId(author.getId());
        dto.setName(author.getName());
        dto.setBirthday(author.getBirthday());
        dto.setNationality(author.getNationality());
        dto.setAvatar(author.getAvatar());
        dto.setCreatedAt(author.getCreatedAt());
        dto.setUpdatedAt(author.getUpdatedAt());
        dto.setCreatedBy(author.getCreatedBy());
        dto.setUpdatedBy(author.getUpdatedBy());
        List<InfoBookInAuthorDTO> books = new ArrayList<>();
        List<Book> bookList = author.getBooks();
        for (Book book : bookList) {
            InfoBookInAuthorDTO bookDTO = new InfoBookInAuthorDTO();
            bookDTO.setId(book.getId());
            bookDTO.setTitle(book.getTitle());
            bookDTO.setPublisher(book.getPublisher().getName());
            bookDTO.setPrice(book.getPrice());
            bookDTO.setQuantity(book.getQuantity());
            bookDTO.setDescription(book.getDescription());
            bookDTO.setImage(book.getImage());
            bookDTO.setCategory(book.getCategory().getName());
            books.add(bookDTO);
        }
        dto.setBooks(books);
        return dto;
    }
}

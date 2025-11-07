package com.example.bookverse.domain.response;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.bookverse.domain.Author;
import com.example.bookverse.domain.Book;
import com.example.bookverse.domain.Category;
import com.example.bookverse.domain.Publisher;
import com.example.bookverse.domain.Supplier;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResBookDTO {
    private long id;
    private String title;

    private Publisher publisher;
    private Supplier supplier;
    private double price;
    private long quantity;
    private long sold;
    private int discount;

    private int publishYear;
    private double weight;
    private String dimensions;
    private int numberOfPages;
    private String coverFormat;

    private String description;
    private String image;

    List<InfoAuthorInBook> authors;

    private Category category;

    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InfoAuthorInBook {
        private long id;
        private String name;
        private Date birthday;
        private String nationality;
    }

    public static ResBookDTO from(Book book) {
        ResBookDTO resBookDTO = new ResBookDTO();
        resBookDTO.setId(book.getId());
        resBookDTO.setTitle(book.getTitle());
        resBookDTO.setPublisher(book.getPublisher());
        resBookDTO.setSupplier(book.getSupplier());
        resBookDTO.setPrice(book.getPrice());
        resBookDTO.setQuantity(book.getQuantity());
        resBookDTO.setSold(book.getSold());
        resBookDTO.setDiscount(book.getDiscount());
        resBookDTO.setPublishYear(book.getPublishYear());
        resBookDTO.setWeight(book.getWeight());
        resBookDTO.setDimensions(book.getDimensions());
        resBookDTO.setNumberOfPages(book.getNumberOfPages());
        resBookDTO.setCoverFormat(book.getCoverFormat().getDisplayName());
        resBookDTO.setDescription(book.getDescription());
        resBookDTO.setImage(book.getImage());
        resBookDTO.setCategory(book.getCategory());
        resBookDTO.setCreatedAt(book.getCreatedAt());
        resBookDTO.setUpdatedAt(book.getUpdatedAt());
        resBookDTO.setCreatedBy(book.getCreatedBy());
        resBookDTO.setUpdatedBy(book.getUpdatedBy());
        List<InfoAuthorInBook> infoAuthorInBookList = new ArrayList<>();
        for (Author author : book.getAuthors()) {
            InfoAuthorInBook infoAuthorInBook = new InfoAuthorInBook();
            infoAuthorInBook.setId(author.getId());
            infoAuthorInBook.setName(author.getName());
            infoAuthorInBook.setBirthday(author.getBirthday());
            infoAuthorInBook.setNationality(author.getNationality());
            infoAuthorInBookList.add(infoAuthorInBook);
        }
        resBookDTO.setAuthors(infoAuthorInBookList);
        return resBookDTO;
    }
}

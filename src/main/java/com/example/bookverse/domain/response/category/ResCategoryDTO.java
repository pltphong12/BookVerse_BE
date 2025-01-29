package com.example.bookverse.domain.response.category;

import com.example.bookverse.domain.Book;
import com.example.bookverse.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResCategoryDTO {
    private Long id;
    private String name;
    private String description;
    private List<InfoBookInCategory> infoBookInCategory;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InfoBookInCategory{
        private long id;
        private String title;

        private String publisher;
        private double price;
        private long quantity;
        private String description;
        private String image;

    }

    public static ResCategoryDTO from(Category category){
        ResCategoryDTO resCategoryDTO = new ResCategoryDTO();
        resCategoryDTO.setId(category.getId());
        resCategoryDTO.setName(category.getName());
        resCategoryDTO.setDescription(category.getDescription());
        resCategoryDTO.setCreatedAt(category.getCreatedAt());
        resCategoryDTO.setUpdatedAt(category.getUpdatedAt());
        resCategoryDTO.setCreatedBy(category.getCreatedBy());
        resCategoryDTO.setUpdatedBy(category.getUpdatedBy());
        List<Book> books = category.getBooks();
        List<ResCategoryDTO.InfoBookInCategory> infoBooks = new ArrayList<>();
        for (Book book : books) {
            ResCategoryDTO.InfoBookInCategory bookInCategoryDTO = new ResCategoryDTO.InfoBookInCategory();
            bookInCategoryDTO.setId(book.getId());
            bookInCategoryDTO.setTitle(book.getTitle());
            bookInCategoryDTO.setPublisher(book.getPublisher());
            bookInCategoryDTO.setPrice(book.getPrice());
            bookInCategoryDTO.setQuantity(book.getQuantity());
            bookInCategoryDTO.setDescription(book.getDescription());
            bookInCategoryDTO.setImage(book.getImage());
            infoBooks.add(bookInCategoryDTO);
        }
        resCategoryDTO.setInfoBookInCategory(infoBooks);
        return resCategoryDTO;
    }
}

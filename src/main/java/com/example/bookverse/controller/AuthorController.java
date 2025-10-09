package com.example.bookverse.controller;

import com.example.bookverse.domain.Author;
import com.example.bookverse.domain.criteria.CriteriaFilterAuthor;
import com.example.bookverse.domain.response.ResPagination;
import com.example.bookverse.domain.response.author.ResAuthorDTO;
import com.example.bookverse.service.AuthorService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping("/authors")
    public ResponseEntity<Author> createAuthor(@Valid @RequestBody Author author) throws Exception {
        Author newAuthor = this.authorService.create(author);
        return ResponseEntity.status(HttpStatus.CREATED).body(newAuthor);
    }

    @PutMapping("/authors")
    public ResponseEntity<Author> updateAuthor(@RequestBody Author author) throws Exception {
        Author updatedAuthor = this.authorService.update(author);
        return ResponseEntity.status(HttpStatus.OK).body(updatedAuthor);
    }

    @GetMapping("/authors/{id}")
    public ResponseEntity<ResAuthorDTO> getAuthor(@PathVariable long id) throws Exception {
        Author author = this.authorService.fetchAuthorById(id);
        ResAuthorDTO authorDTO = ResAuthorDTO.from(author);
        return ResponseEntity.status(HttpStatus.OK).body(authorDTO);
    }

    @GetMapping("/authors")
    public ResponseEntity<List<ResAuthorDTO>> getAuthors() throws Exception {
        List<ResAuthorDTO> resAuthorDTOS = new ArrayList<>();
        List<Author> authors = this.authorService.fetchAllAuthors();
        for (Author author : authors) {
            ResAuthorDTO authorDTO = ResAuthorDTO.from(author);
            resAuthorDTOS.add(authorDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(resAuthorDTOS);
    }

    @GetMapping("/authors/search")
    public ResponseEntity<ResPagination> getAuthorsWithPaginationAndFilter(
            @ModelAttribute CriteriaFilterAuthor criteriaFilterAuthor,
            Pageable pageable) throws Exception {
        ResPagination resPagination = this.authorService.fetchAllAuthorsWithPaginationAndFilter(criteriaFilterAuthor, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(resPagination);
    }

    @DeleteMapping("/authors/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable long id) throws Exception {
        this.authorService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

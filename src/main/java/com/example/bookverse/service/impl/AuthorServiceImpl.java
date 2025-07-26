package com.example.bookverse.service.impl;

import com.example.bookverse.domain.Author;
import com.example.bookverse.domain.Book;
import com.example.bookverse.domain.response.ResPagination;
import com.example.bookverse.domain.response.author.ResAuthorDTO;
import com.example.bookverse.domain.response.book.ResBookDTO;
import com.example.bookverse.exception.author.ExistAuthorNameException;
import com.example.bookverse.exception.global.IdInvalidException;
import com.example.bookverse.repository.AuthorRepository;
import com.example.bookverse.repository.BookRepository;
import com.example.bookverse.service.AuthorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public Author create(Author author) throws Exception {
        if (this.authorRepository.existsByName(author.getName())) {
            throw new ExistAuthorNameException(author.getName() + " already exist");
        }
        return this.authorRepository.save(author);
    }

    @Override
    public Author update(Author author) throws Exception {
        Author authorInDB = this.authorRepository.findById(author.getId()).orElse(null);
        if (authorInDB == null) {
            throw new IdInvalidException("Author not found");
        }
        else {
            if (author.getName() != null && !author.getName().equals(authorInDB.getName())) {
                if (this.authorRepository.existsByName(author.getName())) {
                    throw new ExistAuthorNameException(author.getName() + " already exist");
                }
                authorInDB.setName(author.getName());
            }
            if (author.getBirthday() != null) {
                authorInDB.setBirthday(author.getBirthday());
            }
            if (author.getNationality() != null && !author.getNationality().equals(authorInDB.getNationality())) {
                authorInDB.setNationality(author.getNationality());
            }
            if (author.getAvatar() != null && !author.getAvatar().equals(authorInDB.getAvatar())) {
                authorInDB.setAvatar(author.getAvatar());
            }
            return this.authorRepository.save(authorInDB);
        }
    }

    @Override
    public Author fetchAuthorById(long id) throws Exception {
        if (!this.authorRepository.existsById(id)) {
            throw new IdInvalidException("Author not found");
        }
        return this.authorRepository.findById(id).orElse(null);
    }

    @Override
    public List<Author> fetchAllAuthors() throws Exception {
        return this.authorRepository.findAll();
    }

    @Override
    public ResPagination fetchAllAuthorsWithPaginationAndFilter(String name, String nationality, LocalDate dateFrom, Pageable pageable) throws Exception {
        Page<Author> pageAuthor = this.authorRepository.filter(name, nationality, dateFrom, pageable);
        ResPagination rs = new ResPagination();
        ResPagination.Meta mt = new ResPagination.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageAuthor.getSize());

        mt.setPages(pageAuthor.getTotalPages());
        mt.setTotal(pageAuthor.getTotalElements());

        rs.setMeta(mt);

        List<Author> authors = pageAuthor.getContent();
        List<ResAuthorDTO> authorDTOS = new ArrayList<>();
        for (Author author : authors) {
            ResAuthorDTO authorDTO = ResAuthorDTO.from(author);
            authorDTOS.add(authorDTO);
        }

        rs.setResult(authorDTOS);

        return rs;
    }

    @Override
    public void delete(long id) throws Exception {
        if (!this.authorRepository.existsById(id)) {
            throw new IdInvalidException("Author not found");
        }
        List<Author> author = new ArrayList<>();
        author.add(this.fetchAuthorById(id));
        List<Book> books = this.bookRepository.findByAuthors(author);
        for (Book book : books) {
            book.getAuthors().remove(author.getFirst());
        }
        this.authorRepository.deleteById(id);
    }
}

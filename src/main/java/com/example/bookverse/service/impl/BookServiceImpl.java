package com.example.bookverse.service.impl;

import com.example.bookverse.domain.Book;
import com.example.bookverse.exception.book.ExistTitleException;
import com.example.bookverse.exception.global.IdInvalidException;
import com.example.bookverse.repository.BookRepository;
import com.example.bookverse.service.BookService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book create(Book book) throws Exception {
        if (bookRepository.existsByTitle(book.getTitle())) {
            throw new ExistTitleException(book.getTitle() + " already exists");
        }
        return this.bookRepository.save(book);
    }

    @Override
    public Book update(Book book) throws Exception {
        Book bookInDB = this.bookRepository.findById(book.getId()).orElse(null);
        if ( bookInDB == null) {
            throw new IdInvalidException("Book not found");
        }
        else {
            if (book.getTitle() != null && !bookInDB.getTitle().equals(book.getTitle())) {
                if (bookRepository.existsByTitle(book.getTitle())) {
                    throw new ExistTitleException(book.getTitle() + " already exists");
                }
                bookInDB.setTitle(book.getTitle());
            }
            if (book.getDescription() != null && !bookInDB.getDescription().equals(book.getDescription())) {
                bookInDB.setDescription(book.getDescription());
            }
            if (book.getPublisher() != null && !bookInDB.getPublisher().equals(book.getPublisher())) {
                bookInDB.setPublisher(book.getPublisher());
            }
            if (book.getCategory() != null && !bookInDB.getCategory().equals(book.getCategory())) {
                bookInDB.setCategory(book.getCategory());
            }
            if (book.getPrice() != 0) {
                bookInDB.setPrice(book.getPrice());
            }
            if (book.getQuantity() != 0) {
                bookInDB.setQuantity(book.getQuantity());
            }
            if (book.getAuthors() != null) {
                bookInDB.setAuthors(book.getAuthors());
            }
            if (book.getImage() != null && !bookInDB.getImage().equals(book.getImage())) {
                bookInDB.setImage(book.getImage());
            }
            return this.bookRepository.save(bookInDB);
        }
    }

    @Override
    public Book fetchBookById(long id) throws Exception {
        if (!this.bookRepository.existsById(id)) {
            throw new IdInvalidException("Book not found");
        }
        return this.bookRepository.findById(id).orElse(null);
    }

    @Override
    public List<Book> fetchAllBooks() throws Exception {
        return this.bookRepository.findAll();
    }

    @Override
    public void delete(long id) throws Exception {
        if (!this.bookRepository.existsById(id)) {
            throw new IdInvalidException("Book not found");
        }
        this.bookRepository.deleteById(id);
    }
}

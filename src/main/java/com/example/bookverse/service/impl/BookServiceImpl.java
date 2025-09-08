package com.example.bookverse.service.impl;

import com.example.bookverse.domain.Author;
import com.example.bookverse.domain.Book;
import com.example.bookverse.domain.response.ResPagination;
import com.example.bookverse.domain.response.book.ResBookDTO;
import com.example.bookverse.exception.global.ExistDataException;
import com.example.bookverse.repository.AuthorRepository;
import com.example.bookverse.repository.BookRepository;
import com.example.bookverse.service.BookService;
import com.example.bookverse.util.EntityValidator;
import com.example.bookverse.util.FindObjectInDataBase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public Book create(Book book) throws Exception {
        if (bookRepository.existsByTitle(book.getTitle())) {
            throw new ExistDataException(book.getTitle() + " already exists");
        }
        if (book.getAuthors() != null){
            List<Long> authorIds = book.getAuthors().stream()
                    .map(Author::getId)
                    .toList();
            EntityValidator.validateIdsExist(authorIds,authorRepository,"Author");
            book.setAuthors(book.getAuthors());
        }
        return this.bookRepository.save(book);
    }

    @Override
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
        if (book.getImage() != null) {
            bookInDB.setImage(book.getImage());
        }
        if (book.getAuthors() != null && !bookInDB.getAuthors().equals(book.getAuthors())) {

            List<Long> authorIds = book.getAuthors().stream()
                    .map(Author::getId)
                    .toList();
            EntityValidator.validateIdsExist(authorIds,authorRepository,"Author");
            bookInDB.setAuthors(book.getAuthors());
        }
        return this.bookRepository.save(bookInDB);
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
    public ResPagination fetchAllBooksWithPaginationAndFilter(String title, long publisherId, long authorId, long categoryId, LocalDate dateFrom, Pageable pageable){
        Page<Book> pageBook = this.bookRepository.filter(title, publisherId, authorId, categoryId, dateFrom, pageable);
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
}

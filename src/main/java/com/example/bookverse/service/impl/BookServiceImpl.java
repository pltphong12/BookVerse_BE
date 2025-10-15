package com.example.bookverse.service.impl;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.bookverse.domain.Author;
import com.example.bookverse.domain.Book;
import com.example.bookverse.domain.QBook;
import com.example.bookverse.domain.criteria.CriteriaFilterBook;
import com.example.bookverse.domain.response.ResPagination;
import com.example.bookverse.domain.response.book.ResBookDTO;
import com.example.bookverse.exception.global.ExistDataException;
import com.example.bookverse.repository.AuthorRepository;
import com.example.bookverse.repository.BookRepository;
import com.example.bookverse.service.BookService;
import com.example.bookverse.util.EntityValidator;
import com.example.bookverse.util.FindObjectInDataBase;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final EntityManager entityManager;

    public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository,
            EntityManager entityManager) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.entityManager = entityManager;
    }

    @Override
    public Book create(Book book) throws Exception {
        if (bookRepository.existsByTitle(book.getTitle())) {
            throw new ExistDataException(book.getTitle() + " already exists");
        }
        if (book.getAuthors() != null) {
            List<Long> authorIds = book.getAuthors().stream()
                    .map(Author::getId)
                    .toList();
            EntityValidator.validateIdsExist(authorIds, authorRepository, "Author");
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
        if (book.getSupplier() != null && !bookInDB.getSupplier().equals(book.getSupplier())) {
            bookInDB.setSupplier(book.getSupplier());
        }
        if (book.getCategory() != null && !bookInDB.getCategory().equals(book.getCategory())) {
            bookInDB.setCategory(book.getCategory());
        }
        if (book.getPrice() != 0) {
            bookInDB.setPrice(book.getPrice());
        }
        if (book.getDiscount() != 0) {
            bookInDB.setDiscount(book.getDiscount());
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
            EntityValidator.validateIdsExist(authorIds, authorRepository, "Author");
            bookInDB.setAuthors(book.getAuthors());
        }
        if (book.getDimensions() != null && !bookInDB.getDimensions().equals(book.getDimensions())) {
            bookInDB.setDimensions(book.getDimensions());
        }
        if (book.getNumberOfPages() != 0) {
            bookInDB.setNumberOfPages(book.getNumberOfPages());
        }
        if (book.getCoverFormat() != null && !bookInDB.getCoverFormat().equals(book.getCoverFormat())) {
            bookInDB.setCoverFormat(book.getCoverFormat());
        }
        if (book.getPublishYear() != 0) {
            bookInDB.setPublishYear(book.getPublishYear());
        }
        if (book.getWeight() != 0) {
            bookInDB.setWeight(book.getWeight());
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

    public Page<Book> filter(CriteriaFilterBook criteriaFilterBook, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QBook qBook = QBook.book;

        BooleanBuilder builder = new BooleanBuilder();
        // Filter
        if (criteriaFilterBook.getTitle() != null && !criteriaFilterBook.getTitle().isBlank()) {
            builder.and(qBook.title.containsIgnoreCase(criteriaFilterBook.getTitle()));
        }
        if (criteriaFilterBook.getPublisherId() != 0) {
            builder.and(qBook.publisher.id.eq(criteriaFilterBook.getPublisherId()));
        }
        if (criteriaFilterBook.getAuthorId() != 0) {
            builder.and(qBook.authors.any().id.eq(criteriaFilterBook.getAuthorId()));
        }
        if (criteriaFilterBook.getCategoryId() != 0) {
            builder.and(qBook.category.id.eq(criteriaFilterBook.getCategoryId()));
        }
        if (criteriaFilterBook.getDateFrom() != null) {
            Instant fromInstant = criteriaFilterBook.getDateFrom().atStartOfDay(ZoneId.systemDefault()).toInstant();
            builder.and(qBook.createdAt.goe(fromInstant));
        }
        // Query chính
        List<Book> books = queryFactory.selectFrom(qBook)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Đếm số lượng kết quả
        long total = queryFactory.selectFrom(qBook)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(books, pageable, total);
    }

    @Override
    public ResPagination fetchAllBooksWithPaginationAndFilter(CriteriaFilterBook criteriaFilterBook, Pageable pageable) {
        Page<Book> pageBook = this.filter(criteriaFilterBook, pageable);
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

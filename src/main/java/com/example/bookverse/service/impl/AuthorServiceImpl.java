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
import com.example.bookverse.domain.QAuthor;
import com.example.bookverse.domain.criteria.CriteriaFilterAuthor;
import com.example.bookverse.domain.response.ResPagination;
import com.example.bookverse.domain.response.author.ResAuthorDTO;
import com.example.bookverse.exception.global.ExistDataException;
import com.example.bookverse.repository.AuthorRepository;
import com.example.bookverse.repository.BookRepository;
import com.example.bookverse.service.AuthorService;
import com.example.bookverse.util.FindObjectInDataBase;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final EntityManager entityManager;

    public AuthorServiceImpl(AuthorRepository authorRepository, BookRepository bookRepository, EntityManager entityManager) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.entityManager = entityManager;
    }

    @Override
    public Author create(Author author) throws Exception {
        if (this.authorRepository.existsByName(author.getName())) {
            throw new ExistDataException(author.getName() + " đã tồn tại");
        }
        return this.authorRepository.save(author);
    }

    @Override
    public Author update(Author author) throws Exception {
        Author authorInDB = FindObjectInDataBase.findByIdOrThrow(authorRepository, author.getId());
        if (author.getName() != null && !author.getName().equals(authorInDB.getName())) {
            if (this.authorRepository.existsByName(author.getName())) {
                throw new ExistDataException(author.getName() + " đã tồn tại");
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

    @Override
    public Author fetchAuthorById(long id) throws Exception {
        return FindObjectInDataBase.findByIdOrThrow(authorRepository, id);
    }

    @Override
    public List<Author> fetchAllAuthors() throws Exception {
        return this.authorRepository.findAll();
    }

    private Page<Author> filter(CriteriaFilterAuthor criteriaFilterAuthor, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QAuthor qAuthor = QAuthor.author;

        BooleanBuilder builder = new BooleanBuilder();
        // Filter
        if (criteriaFilterAuthor.getName() != null && !criteriaFilterAuthor.getName().isBlank()) {
            builder.and(qAuthor.name.containsIgnoreCase(criteriaFilterAuthor.getName()));
        }

        if (criteriaFilterAuthor.getNationality() != null && !criteriaFilterAuthor.getNationality().isBlank()) {
            builder.and(qAuthor.nationality.containsIgnoreCase(criteriaFilterAuthor.getNationality()));
        }

        if (criteriaFilterAuthor.getDateFrom() != null) {
            Instant fromInstant = criteriaFilterAuthor.getDateFrom().atStartOfDay(ZoneId.systemDefault()).toInstant();
            builder.and(qAuthor.createdAt.goe(fromInstant));
        }
        // Query chính
        List<Author> authors = queryFactory.selectFrom(qAuthor)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Đếm số lượng kết quả
        long total = queryFactory.selectFrom(qAuthor)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(authors, pageable, total);
    }

    @Override
    public ResPagination fetchAllAuthorsWithPaginationAndFilter(CriteriaFilterAuthor criteriaFilterAuthor, Pageable pageable) throws Exception {
        Page<Author> pageAuthor = this.filter(criteriaFilterAuthor, pageable);
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
        Author author = FindObjectInDataBase.findByIdOrThrow(authorRepository, id);
        List<Author> authors = new ArrayList<>();
        authors.add(author);
        List<Book> books = this.bookRepository.findByAuthors(authors);
        for (Book book : books) {
            book.getAuthors().remove(authors.getFirst());
        }
        this.authorRepository.deleteById(id);
    }
}

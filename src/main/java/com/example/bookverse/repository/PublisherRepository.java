package com.example.bookverse.repository;

import com.example.bookverse.domain.Publisher;
import com.example.bookverse.repository.custom.PublisherRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long>, PublisherRepositoryCustom {
    boolean existsByName(String name);
}

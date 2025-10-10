package com.example.bookverse.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.example.bookverse.domain.Publisher;
import com.example.bookverse.domain.criteria.CriteriaFilterPublisher;
import com.example.bookverse.domain.response.ResPagination;

public interface PublisherService {
    // Create
    Publisher create(Publisher publisher) throws Exception;

    //Update
    Publisher update(Publisher publisher) throws Exception;

    // Fetch a one
    Publisher fetchPublisherById(long id) throws Exception;

    // Fetch all
    List<Publisher> fetchAllPublisher() throws Exception;

    ResPagination fetchAllPublisherWithPaginationAndFilter(CriteriaFilterPublisher criteriaFilterPublisher, Pageable pageable) throws Exception;

    // Delete
    void delete(long id) throws Exception;
}

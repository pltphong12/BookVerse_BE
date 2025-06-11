package com.example.bookverse.service;

import com.example.bookverse.domain.Publisher;

import java.util.List;

public interface PublisherService {
    // Create
    Publisher create(Publisher publisher) throws Exception;

    //Update
    Publisher update(Publisher publisher) throws Exception;

    // Fetch a one
    Publisher fetchPublisherById(long id) throws Exception;

    // Fetch all
    List<Publisher> fetchAllPublisher() throws Exception;

    // Delete
    void delete(long id) throws Exception;
}

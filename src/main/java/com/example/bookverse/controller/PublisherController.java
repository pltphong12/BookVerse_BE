package com.example.bookverse.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bookverse.domain.Publisher;
import com.example.bookverse.domain.criteria.CriteriaFilterPublisher;
import com.example.bookverse.domain.response.ResPagination;
import com.example.bookverse.service.PublisherService;

@RestController
@RequestMapping("/api/v1")
public class PublisherController {
    final private PublisherService publisherService;

    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @PostMapping("/publishers")
    @PreAuthorize("hasAuthority('PUBLISHER_CREATE')")
    public ResponseEntity<Publisher> create(@RequestBody Publisher publisher) throws Exception {
        Publisher newPublisher = this.publisherService.create(publisher);
        return ResponseEntity.status(HttpStatus.CREATED).body(newPublisher);
    }

    @PutMapping("/publishers")
    @PreAuthorize("hasAuthority('PUBLISHER_UPDATE')")
    public ResponseEntity<Publisher> update(@RequestBody Publisher publisher) throws Exception {
        Publisher newPublisher = this.publisherService.update(publisher);
        return ResponseEntity.status(HttpStatus.OK).body(newPublisher);
    }

    @GetMapping("/publishers/{id}")
    @PreAuthorize("hasAuthority('PUBLISHER_VIEW_BY_ID')")
    public ResponseEntity<Publisher> get(@PathVariable long id) throws Exception {
        Publisher publisher = this.publisherService.fetchPublisherById(id);
        return ResponseEntity.status(HttpStatus.OK).body(publisher);
    }

    @GetMapping("/publishers")
    @PreAuthorize("hasAuthority('PUBLISHER_VIEW_ALL')")
    public ResponseEntity<List<Publisher>> getAll() throws Exception {
        List<Publisher> publishers = this.publisherService.fetchAllPublisher();
        return ResponseEntity.status(HttpStatus.OK).body(publishers);
    }

    @GetMapping("/publishers/search")
    @PreAuthorize("hasAuthority('PUBLISHER_VIEW_ALL_WITH_PAGINATION_AND_FILTER')")
    public ResponseEntity<ResPagination> getAllWithPaginationAndFilter(
            @ModelAttribute CriteriaFilterPublisher criteriaFilterPublisher,
            Pageable pageable) throws Exception {
        ResPagination resPagination = this.publisherService.fetchAllPublisherWithPaginationAndFilter(criteriaFilterPublisher, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(resPagination);
    }

    @DeleteMapping("/publishers/{id}")
    @PreAuthorize("hasAuthority('PUBLISHER_DELETE')")
    public ResponseEntity<Void> delete(@PathVariable long id) throws Exception {
        this.publisherService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

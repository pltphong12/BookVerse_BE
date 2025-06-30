package com.example.bookverse.controller;

import com.example.bookverse.domain.Publisher;
import com.example.bookverse.domain.response.ResPagination;
import com.example.bookverse.service.PublisherService;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class PublisherController {
    final private PublisherService publisherService;

    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @PostMapping("/publishers")
    public ResponseEntity<Publisher> create(@RequestBody Publisher publisher) throws Exception {
        Publisher newPublisher = this.publisherService.create(publisher);
        return ResponseEntity.status(HttpStatus.CREATED).body(newPublisher);
    }

    @PutMapping("/publishers")
    public ResponseEntity<Publisher> update(@RequestBody Publisher publisher) throws Exception {
        Publisher newPublisher = this.publisherService.update(publisher);
        return ResponseEntity.status(HttpStatus.OK).body(newPublisher);
    }

    @GetMapping("/publishers/{id}")
    public ResponseEntity<Publisher> get(@PathVariable long id) throws Exception {
        Publisher publisher = this.publisherService.fetchPublisherById(id);
        return ResponseEntity.status(HttpStatus.OK).body(publisher);
    }

    @GetMapping("/publishers")
    public ResponseEntity<List<Publisher>> getAll() throws Exception {
        List<Publisher> publishers = this.publisherService.fetchAllPublisher();
        return ResponseEntity.status(HttpStatus.OK).body(publishers);
    }

    @GetMapping("/publishers/search")
    public ResponseEntity<ResPagination> getAllWithPaginationAndFilter(
            @RequestParam(required = false) String name,
            @RequestParam(name = "date_from", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            Pageable pageable) throws Exception {
        ResPagination resPagination = this.publisherService.fetchAllPublisherWithPaginationAndFilter(name, dateFrom, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(resPagination);
    }

    @DeleteMapping("/publishers/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) throws Exception {
        this.publisherService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

package com.example.bookverse.controller;

import com.example.bookverse.domain.Publisher;
import com.example.bookverse.service.PublisherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/publishers/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) throws Exception {
        this.publisherService.fetchPublisherById(id);
        return ResponseEntity.noContent().build();
    }
}

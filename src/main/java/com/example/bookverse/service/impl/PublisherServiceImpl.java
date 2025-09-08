package com.example.bookverse.service.impl;

import com.example.bookverse.domain.Publisher;
import com.example.bookverse.domain.response.ResPagination;
import com.example.bookverse.exception.global.ExistDataException;
import com.example.bookverse.exception.global.IdInvalidException;
import com.example.bookverse.repository.PublisherRepository;
import com.example.bookverse.service.PublisherService;
import com.example.bookverse.util.FindObjectInDataBase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PublisherServiceImpl implements PublisherService {
    private final PublisherRepository publisherRepository;

    public PublisherServiceImpl(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    // Create
    @Override
    public Publisher create(Publisher publisher) throws Exception {
        if (this.publisherRepository.existsByName(publisher.getName())) {
            throw new ExistDataException(publisher.getName() + " already exists");
        }
        return publisherRepository.save(publisher);
    }

    // Update
    @Override
    public Publisher update(Publisher publisher) throws Exception {
        Publisher publisherInDB = FindObjectInDataBase.findByIdOrThrow(publisherRepository, publisher.getId());
        if (publisher.getName() != null && !publisher.getName().equals(publisherInDB.getName())) {
            publisherInDB.setName(publisher.getName());
        }
        if (publisher.getAddress() != null && !publisher.getAddress().equals(publisherInDB.getAddress())) {
            publisherInDB.setAddress(publisher.getAddress());
        }
        if (publisher.getEmail() != null && !publisher.getEmail().equals(publisherInDB.getEmail())) {
            publisherInDB.setEmail(publisher.getEmail());
        }
        if (publisher.getPhone() != null && !publisher.getPhone().equals(publisherInDB.getPhone())) {
            publisherInDB.setPhone(publisher.getPhone());
        }
        if (publisher.getDescription() != null && !publisher.getDescription().equals(publisherInDB.getDescription())) {
            publisherInDB.setDescription(publisher.getDescription());
        }
        if (publisher.getImage() != null && !publisher.getImage().equals(publisherInDB.getImage())) {
            publisherInDB.setImage(publisher.getImage());
        }
        return publisherRepository.save(publisherInDB);
    }

    //Fetch
    @Override
    public Publisher fetchPublisherById(long id) throws Exception {
        return FindObjectInDataBase.findByIdOrThrow(publisherRepository, id);
    }

    // Fetch all
    @Override
    public List<Publisher> fetchAllPublisher() throws Exception {
        return this.publisherRepository.findAll();
    }

    @Override
    public ResPagination fetchAllPublisherWithPaginationAndFilter(String name, LocalDate dateFrom, Pageable pageable) throws Exception {
        Page<Publisher> pagePublisher = this.publisherRepository.filter(name, dateFrom, pageable);
        ResPagination rs = new ResPagination();
        ResPagination.Meta mt = new ResPagination.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pagePublisher.getSize());

        mt.setPages(pagePublisher.getTotalPages());
        mt.setTotal(pagePublisher.getTotalElements());

        rs.setMeta(mt);

        List<Publisher> publishers = pagePublisher.getContent();
//        List<ResAuthorDTO> authorDTOS = new ArrayList<>();
//        for (Author author : authors) {
//            ResAuthorDTO authorDTO = ResAuthorDTO.from(author);
//            authorDTOS.add(authorDTO);
//        }

        rs.setResult(publishers);

        return rs;
    }

    // Delete
    @Override
    public void delete(long id) throws Exception {
        FindObjectInDataBase.findByIdOrThrow(publisherRepository, id);
        this.publisherRepository.deleteById(id);
    }
}

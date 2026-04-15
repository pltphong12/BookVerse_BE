package com.example.bookverse.repository;

import com.example.bookverse.domain.Book;
import com.example.bookverse.domain.Cart;
import com.example.bookverse.domain.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail, Long> {
    Optional<CartDetail> findByCartAndBook(Cart cart, Book book);
    void deleteAllByCart(Cart cart);
}

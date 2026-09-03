package com.example.bookverse.repository;

import com.example.bookverse.domain.Book;
import com.example.bookverse.domain.Cart;
import com.example.bookverse.domain.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import jakarta.persistence.LockModeType;

import java.util.List;
import java.util.Optional;

public interface CartDetailRepository extends JpaRepository<CartDetail, Long> {
    Optional<CartDetail> findByCartAndBook(Cart cart, Book book);
    void deleteAllByCart(Cart cart);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT cd FROM CartDetail cd JOIN FETCH cd.book WHERE cd.cart = :cart ORDER BY cd.book.id")
    List<CartDetail> findAllByCartForUpdate(Cart cart);
}

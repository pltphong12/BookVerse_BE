package com.example.bookverse.service;

import com.example.bookverse.domain.Cart;
import com.example.bookverse.domain.CartDetail;

public interface CartService {
    // Create a Cart
    Cart create (Cart cart) throws Exception;
    // Create Cart detail
    CartDetail createDetail(CartDetail cartDetail) throws Exception;

    // Update a Cart
    Cart update (Cart cart) throws Exception;
    // Update Cart detail
    CartDetail updateDetail(CartDetail cartDetail) throws Exception;

    // Get a Cart
    Cart fetchCartById(long id) throws Exception;

    // Delete Cart detail
    void deleteDetail(long id) throws Exception;
}

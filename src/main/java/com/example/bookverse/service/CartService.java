package com.example.bookverse.service;

import com.example.bookverse.dto.request.ReqAddToCartDTO;
import com.example.bookverse.dto.response.ResCartDTO;

public interface CartService {
    ResCartDTO fetchCartById() throws Exception;

    ResCartDTO addToCart(ReqAddToCartDTO reqAddToCartDTO) throws Exception;

    ResCartDTO increaseQuantity(long cartDetailId) throws Exception;

    ResCartDTO decreaseQuantity(long cartDetailId) throws Exception;

    ResCartDTO removeCartDetail(long cartDetailId) throws Exception;
}

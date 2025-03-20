package com.example.bookverse.service.impl;

import com.example.bookverse.domain.*;
import com.example.bookverse.exception.global.IdInvalidException;
import com.example.bookverse.repository.BookRepository;
import com.example.bookverse.repository.CartDetailRepository;
import com.example.bookverse.repository.CartRepository;
import com.example.bookverse.repository.UserRepository;
import com.example.bookverse.service.CartService;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CartDetailRepository cartDetailRepository;
    private final BookRepository bookRepository;


    public CartServiceImpl(CartRepository cartRepository, UserRepository userRepository, CartDetailRepository cartDetailRepository, BookRepository bookRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.cartDetailRepository = cartDetailRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public Cart create(Cart cart) throws Exception {
        if (cart.getUser() != null){
            User user = this.userRepository.findById(cart.getUser().getId()).orElse(null);
            cart.setUser(user);
        }
        Cart cart1 = this.cartRepository.save(cart);
        for (CartDetail cartDetail : cart.getCartDetails()) {
            CartDetail cartDetailInDB = this.cartDetailRepository.findById(cartDetail.getId()).orElse(null);
            if (cartDetailInDB != null) {
                cartDetailInDB.setCart(cart);
                this.cartDetailRepository.save(cartDetailInDB);
            }
        }
        return this.cartRepository.save(cart);
    }

    @Override
    public CartDetail createDetail(CartDetail cartDetail) throws Exception {
        if (cartDetail.getBook() != null){
            Book book = this.bookRepository.findById(cartDetail.getBook().getId()).orElse(null);
            cartDetail.setBook(book);
        }
        return this.cartDetailRepository.save(cartDetail);
    }

    @Override
    public Cart update(Cart cart) throws Exception {
        Cart cartInDB = this.cartRepository.findById(cart.getId()).orElse(null);
        if (cartInDB == null) {
            throw new IdInvalidException("Cart not found");
        }
        else {
            if (cart.getUser() != null){
                User user = this.userRepository.findById(cart.getUser().getId()).orElse(null);
                cartInDB.setUser(user);
            }
            if (cart.getSum() != 0){
                cartInDB.setSum(cart.getSum());
            }
            if (cart.getCartDetails() != null){
                cartInDB.setCartDetails(cart.getCartDetails());
                for (CartDetail cartDetail : cart.getCartDetails()) {
                    CartDetail cartDetailInDB = this.cartDetailRepository.findById(cartDetail.getId()).orElse(null);
                    if (cartDetailInDB != null) {
                        cartDetailInDB.setCart(cart);
                        this.cartDetailRepository.save(cartDetailInDB);
                    }
                }
            }
            return this.cartRepository.save(cartInDB);
        }

    }

    @Override
    public CartDetail updateDetail(CartDetail cartDetail) throws Exception {
        CartDetail cartDetailInDB = this.cartDetailRepository.findById(cartDetail.getId()).orElse(null);
        if (cartDetailInDB == null) {
            throw new IdInvalidException("CartDetail not found");
        }
        else {
            if (cartDetail.getBook() != null && !cartDetail.getBook().equals(cartDetailInDB.getBook())) {
                Book book = this.bookRepository.findById(cartDetail.getBook().getId()).orElse(null);
                cartDetailInDB.setBook(book);
            }
            if (cartDetail.getPrice() != 0 && cartDetail.getPrice() != cartDetailInDB.getPrice()) {
                cartDetailInDB.setPrice(cartDetail.getPrice());
            }
            if (cartDetail.getQuantity() != 0 && cartDetail.getQuantity() != cartDetailInDB.getQuantity()) {
                cartDetailInDB.setQuantity(cartDetail.getQuantity());
            }
            return this.cartDetailRepository.save(cartDetailInDB);
        }
    }

    @Override
    public Cart fetchCartById(long id) throws Exception {
        if (!this.cartRepository.existsById(id)) {
            throw new IdInvalidException("Cart not found");
        }
        return this.cartRepository.findById(id).orElse(null);
    }


    @Override
    public void deleteDetail(long id) throws Exception {
        if (!this.cartDetailRepository.existsById(id)) {
            throw new IdInvalidException("CartDetail not found");
        }
        this.cartDetailRepository.deleteById(id);
    }
}

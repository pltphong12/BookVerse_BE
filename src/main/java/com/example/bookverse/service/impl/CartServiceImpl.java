package com.example.bookverse.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bookverse.domain.Book;
import com.example.bookverse.domain.Cart;
import com.example.bookverse.domain.CartDetail;
import com.example.bookverse.domain.Customer;
import com.example.bookverse.dto.request.ReqAddToCartDTO;
import com.example.bookverse.dto.response.ResCartDTO;
import com.example.bookverse.exception.global.IdInvalidException;
import com.example.bookverse.repository.BookRepository;
import com.example.bookverse.repository.CartDetailRepository;
import com.example.bookverse.repository.CartRepository;
import com.example.bookverse.service.CartService;
import com.example.bookverse.util.CurrentCustomerAccessor;
import com.example.bookverse.util.FindObjectInDataBase;

import jakarta.persistence.EntityManager;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final BookRepository bookRepository;
    private final EntityManager entityManager;
    private final CurrentCustomerAccessor currentCustomerAccessor;

    public CartServiceImpl(CartRepository cartRepository,
            CartDetailRepository cartDetailRepository, BookRepository bookRepository,
            EntityManager entityManager, CurrentCustomerAccessor currentCustomerAccessor) {
        this.cartRepository = cartRepository;
        this.cartDetailRepository = cartDetailRepository;
        this.bookRepository = bookRepository;
        this.entityManager = entityManager;
        this.currentCustomerAccessor = currentCustomerAccessor;
    }

    @Override
    public ResCartDTO fetchCartById() throws Exception {
        Cart cart = getCurrentUserCart();
        return ResCartDTO.from(cart);
    }

    private Cart getCurrentUserCart() throws IdInvalidException {
        Customer customer = currentCustomerAccessor.requireCurrentCustomer();
        return cartRepository.findByCustomer(customer)
                .orElseThrow(() -> new IdInvalidException("Giỏ hàng không tồn tại"));
    }

    private void verifyOwnership(CartDetail cartDetail) throws IdInvalidException {
        Cart currentCart = getCurrentUserCart();
        if (cartDetail.getCart().getId() != currentCart.getId()) {
            throw new IdInvalidException("Bạn không có quyền thao tác trên mục này");
        }
    }

    private double calculatePrice(Book book, long quantity) {
        return (book.getPrice() - book.getPrice() * book.getDiscount() / 100) * quantity;
    }

    @Override
    @Transactional
    public ResCartDTO addToCart(ReqAddToCartDTO reqAddToCartDTO) throws Exception {
        Customer customer = currentCustomerAccessor.requireCurrentCustomer();

        Book book = FindObjectInDataBase.findByIdOrThrow(bookRepository, reqAddToCartDTO.getBookId());

        if (book.getQuantity() < reqAddToCartDTO.getQuantity()) {
            throw new IdInvalidException("Số lượng sách trong kho không đủ (còn lại: " + book.getQuantity() + ")");
        }

        Cart cart = cartRepository.findByCustomer(customer)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setCustomer(customer);
                    newCart.setSum(0);
                    return cartRepository.save(newCart);
                });

        Optional<CartDetail> existingDetail = cartDetailRepository.findByCartAndBook(cart, book);

        if (existingDetail.isPresent()) {
            CartDetail cartDetail = existingDetail.get();
            long newQuantity = cartDetail.getQuantity() + reqAddToCartDTO.getQuantity();
            if (newQuantity > book.getQuantity()) {
                throw new IdInvalidException(
                        "Tổng số lượng trong giỏ vượt quá số lượng tồn kho (còn lại: " + book.getQuantity() + ")");
            }
            cartDetail.setQuantity(newQuantity);
            cartDetail.setPrice(calculatePrice(book, newQuantity));
            cartDetailRepository.save(cartDetail);
        } else {
            CartDetail cartDetail = new CartDetail();
            cartDetail.setCart(cart);
            cartDetail.setBook(book);
            cartDetail.setQuantity(reqAddToCartDTO.getQuantity());
            cartDetail.setPrice(calculatePrice(book, reqAddToCartDTO.getQuantity()));
            cartDetailRepository.save(cartDetail);
        }

        return refreshAndReturn(cart);
    }

    @Override
    @Transactional
    public ResCartDTO increaseQuantity(long cartDetailId) throws Exception {
        CartDetail cartDetail = FindObjectInDataBase.findByIdOrThrow(cartDetailRepository, cartDetailId);
        verifyOwnership(cartDetail);

        Cart cart = cartDetail.getCart();
        Book book = cartDetail.getBook();

        long newQuantity = cartDetail.getQuantity() + 1;
        if (newQuantity > book.getQuantity()) {
            throw new IdInvalidException(
                    "Số lượng trong giỏ vượt quá tồn kho (còn lại: " + book.getQuantity() + ")");
        }

        cartDetail.setQuantity(newQuantity);
        cartDetail.setPrice(calculatePrice(book, newQuantity));
        cartDetailRepository.save(cartDetail);

        return refreshAndReturn(cart);
    }

    @Override
    @Transactional
    public ResCartDTO decreaseQuantity(long cartDetailId) throws Exception {
        CartDetail cartDetail = FindObjectInDataBase.findByIdOrThrow(cartDetailRepository, cartDetailId);
        verifyOwnership(cartDetail);

        Cart cart = cartDetail.getCart();

        if (cartDetail.getQuantity() <= 1) {
            cartDetailRepository.delete(cartDetail);
        } else {
            Book book = cartDetail.getBook();
            long newQuantity = cartDetail.getQuantity() - 1;
            cartDetail.setQuantity(newQuantity);
            cartDetail.setPrice(calculatePrice(book, newQuantity));
            cartDetailRepository.save(cartDetail);
        }

        return refreshAndReturn(cart);
    }

    @Override
    @Transactional
    public ResCartDTO removeCartDetail(long cartDetailId) throws Exception {
        CartDetail cartDetail = FindObjectInDataBase.findByIdOrThrow(cartDetailRepository, cartDetailId);
        verifyOwnership(cartDetail);

        Cart cart = cartDetail.getCart();
        cartDetailRepository.delete(cartDetail);

        return refreshAndReturn(cart);
    }

    private ResCartDTO refreshAndReturn(Cart cart) {
        entityManager.flush();
        entityManager.refresh(cart);

        cart.setSum(cart.getCartDetails() != null ? cart.getCartDetails().size() : 0);
        cart = cartRepository.save(cart);

        return ResCartDTO.from(cart);
    }
}

package com.example.bookverse.controller;

import com.example.bookverse.dto.request.ReqAddToCartDTO;
import com.example.bookverse.dto.response.ResCartDTO;
import com.example.bookverse.service.CartService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/carts/items")
    @PreAuthorize("hasAuthority('CART_ADD_TO_CART')")
    public ResponseEntity<ResCartDTO> addToCart(@Valid @RequestBody ReqAddToCartDTO reqAddToCartDTO) throws Exception {
        ResCartDTO resCartDTO = this.cartService.addToCart(reqAddToCartDTO);
        return ResponseEntity.status(HttpStatus.OK).body(resCartDTO);
    }

    @GetMapping("/carts")
    @PreAuthorize("hasAuthority('CART_VIEW_BY_ID')")
    public ResponseEntity<ResCartDTO> getCarts() throws Exception {
        ResCartDTO resCartDTO = this.cartService.fetchCartById();
        return ResponseEntity.status(HttpStatus.OK).body(resCartDTO);
    }

    @PutMapping("/carts/items/{id}/increase")
    @PreAuthorize("hasAuthority('CART_ADD_TO_CART')")
    public ResponseEntity<ResCartDTO> increaseQuantity(@PathVariable long id) throws Exception {
        ResCartDTO resCartDTO = this.cartService.increaseQuantity(id);
        return ResponseEntity.status(HttpStatus.OK).body(resCartDTO);
    }

    @PutMapping("/carts/items/{id}/decrease")
    @PreAuthorize("hasAuthority('CART_ADD_TO_CART')")
    public ResponseEntity<ResCartDTO> decreaseQuantity(@PathVariable long id) throws Exception {
        ResCartDTO resCartDTO = this.cartService.decreaseQuantity(id);
        return ResponseEntity.status(HttpStatus.OK).body(resCartDTO);
    }

    @DeleteMapping("/carts/items/{id}")
    @PreAuthorize("hasAuthority('CART_ADD_TO_CART')")
    public ResponseEntity<ResCartDTO> removeCartDetail(@PathVariable long id) throws Exception {
        ResCartDTO resCartDTO = this.cartService.removeCartDetail(id);
        return ResponseEntity.status(HttpStatus.OK).body(resCartDTO);
    }
}

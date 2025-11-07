package com.example.bookverse.controller;

import com.example.bookverse.domain.Cart;
import com.example.bookverse.domain.CartDetail;
import com.example.bookverse.domain.response.ResCartDTO;
import com.example.bookverse.service.CartService;
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

    @PostMapping("/carts")
    @PreAuthorize("hasAuthority('CART_CREATE')")
    public ResponseEntity<ResCartDTO> createCart(@RequestBody Cart cart) throws Exception {
        Cart newCart = this.cartService.create(cart);
        ResCartDTO resCartDTO = ResCartDTO.from(newCart);
        return ResponseEntity.status(HttpStatus.CREATED).body(resCartDTO);
    }

    @PostMapping("/cart_details")
    @PreAuthorize("hasAuthority('CART_DETAIL_CREATE')")
    public ResponseEntity<ResCartDTO.InfoCartDetailInCart> createCartDetail(@RequestBody CartDetail cartDetail) throws Exception {
        CartDetail newCartDetail = this.cartService.createDetail(cartDetail);
        ResCartDTO.InfoCartDetailInCart res = ResCartDTO.getInfoCartDetailInCart(newCartDetail);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping("/carts")
    @PreAuthorize("hasAuthority('CART_UPDATE')")
    public ResponseEntity<ResCartDTO> updateCart(@RequestBody Cart cart) throws Exception {
        Cart updatedCart = this.cartService.update(cart);
        ResCartDTO resCartDTO = ResCartDTO.from(updatedCart);
        return ResponseEntity.status(HttpStatus.OK).body(resCartDTO);
    }

    @PutMapping("/cart_details")
    @PreAuthorize("hasAuthority('CART_DETAIL_UPDATE')")
    public ResponseEntity<ResCartDTO.InfoCartDetailInCart> updateCartDetail(@RequestBody CartDetail cartDetail) throws Exception {
        CartDetail updatedCartDetail = this.cartService.updateDetail(cartDetail);
        ResCartDTO.InfoCartDetailInCart res = ResCartDTO.getInfoCartDetailInCart(updatedCartDetail);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @GetMapping("/carts/{id}")
    @PreAuthorize("hasAuthority('CART_VIEW_BY_ID')")
    public ResponseEntity<ResCartDTO> getCarts(@PathVariable long id) throws Exception {
        Cart cart = this.cartService.fetchCartById(id);
        ResCartDTO resCartDTO = ResCartDTO.from(cart);
        return ResponseEntity.status(HttpStatus.OK).body(resCartDTO);
    }

    @DeleteMapping("/cart_details/{id}")
    @PreAuthorize("hasAuthority('CART_DETAIL_DELETE')")
    public ResponseEntity<Void> deleteCart(@PathVariable long id) throws Exception {
        this.cartService.deleteDetail(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

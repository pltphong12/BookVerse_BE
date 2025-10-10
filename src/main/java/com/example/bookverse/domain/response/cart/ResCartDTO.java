package com.example.bookverse.domain.response.cart;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.bookverse.domain.Author;
import com.example.bookverse.domain.Cart;
import com.example.bookverse.domain.CartDetail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResCartDTO {
    private long id;
    private int sum;

    private List<InfoCartDetailInCart> cartDetails;

    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InfoCartDetailInCart {
        private long id;
        private double price;
        private long quantity;

        private InfoBookInCart book;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InfoBookInCart {
        private long id;
        private String title;
        private List<InfoAuthorInCart> authors;
        private String publisher;
        private double price;
        private long quantity;
        private String description;
        private String image;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InfoAuthorInCart{
        private long id;
        private String name;
        private int age;
        private Date birthday;
        private String nationality;
    }

    public static ResCartDTO from(Cart cart) {
        ResCartDTO cartDTO = new ResCartDTO();
        cartDTO.setId(cart.getId());
        cartDTO.setSum(cart.getSum());
        cartDTO.setCreatedAt(cart.getCreatedAt());
        cartDTO.setUpdatedAt(cart.getUpdatedAt());
        cartDTO.setCreatedBy(cart.getCreatedBy());
        cartDTO.setUpdatedBy(cart.getUpdatedBy());
        List<InfoCartDetailInCart> cartDetails = new ArrayList<>();
        for (CartDetail cartDetail : cart.getCartDetails()) {
            InfoCartDetailInCart cartDetailInCart = new InfoCartDetailInCart();
            cartDetailInCart = getInfoCartDetailInCart(cartDetail);
            cartDetails.add(cartDetailInCart);
            cartDTO.setCartDetails(cartDetails);
        }
        return cartDTO;
    }

    public static InfoCartDetailInCart getInfoCartDetailInCart(CartDetail cartDetail) {
        InfoCartDetailInCart cartDetailInCart = new InfoCartDetailInCart();
        cartDetailInCart.setId(cartDetail.getId());
        cartDetailInCart.setPrice(cartDetail.getPrice());
        cartDetailInCart.setQuantity(cartDetail.getQuantity());
        InfoBookInCart infoBookInCart = new InfoBookInCart();
        infoBookInCart = getInfoBookInCart(cartDetail);
        cartDetailInCart.setBook(infoBookInCart);
        return cartDetailInCart;
    }

    public static InfoBookInCart getInfoBookInCart(CartDetail cartDetail) {
        InfoBookInCart infoBookInCart = new InfoBookInCart();
        infoBookInCart.setId(cartDetail.getId());
        infoBookInCart.setTitle(cartDetail.getBook().getTitle());
        infoBookInCart.setPublisher(cartDetail.getBook().getPublisher().getName());
        infoBookInCart.setPrice(cartDetail.getBook().getPrice());
        infoBookInCart.setQuantity(cartDetail.getBook().getQuantity());
        infoBookInCart.setDescription(cartDetail.getBook().getDescription());
        infoBookInCart.setImage(cartDetail.getBook().getImage());
        List<InfoAuthorInCart> authors = new ArrayList<>();
        for (Author author : cartDetail.getBook().getAuthors()) {
            InfoAuthorInCart infoAuthorInCart = new InfoAuthorInCart();
            infoAuthorInCart.setId(author.getId());
            infoAuthorInCart.setName(author.getName());
            infoAuthorInCart.setBirthday(author.getBirthday());
            infoAuthorInCart.setNationality(author.getNationality());
            authors.add(infoAuthorInCart);
        }
        infoBookInCart.setAuthors(authors);
        return infoBookInCart;
    }
}

package com.example.bookverse.dto.response;

import com.example.bookverse.domain.Author;
import com.example.bookverse.domain.Order;
import com.example.bookverse.domain.OrderDetail;
import com.example.bookverse.dto.enums.OrderStatus;
import com.example.bookverse.dto.enums.PaymentMethod;
import com.example.bookverse.dto.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResOrderDTO {
    private long id;
    private String orderCode;
    private double totalPrice;
    private double subtotal;
    private double shippingFee;
    private double discountTotal;
    private String receiverName;
    private String receiverAddress;
    private String receiverPhone;
    private String receiverEmail;
    private OrderStatus status;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private Instant paidAt;
    private Long customerId;

    List<InfoOrderDetailInOrder> orderDetails;

    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InfoOrderDetailInOrder {
        private long id;
        private double price;
        private long quantity;

        private InfoBookInOrder book;
    }



    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InfoBookInOrder {
        private long id;
        private String title;
        private List<InfoAuthorInOrder> authors;
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
    public static class InfoAuthorInOrder{
        private long id;
        private String name;
        private int age;
        private Date birthday;
        private String nationality;
    }

    public static ResOrderDTO from(Order order) {
        ResOrderDTO resOrderDTO = new ResOrderDTO();
        resOrderDTO.setId(order.getId());
        resOrderDTO.setOrderCode(order.getOrderCode());
        resOrderDTO.setTotalPrice(order.getTotalPrice());
        resOrderDTO.setSubtotal(order.getSubtotal());
        resOrderDTO.setShippingFee(order.getShippingFee());
        resOrderDTO.setDiscountTotal(order.getDiscountTotal());
        resOrderDTO.setReceiverName(order.getReceiverName());
        resOrderDTO.setReceiverAddress(order.getReceiverAddress());
        resOrderDTO.setReceiverPhone(order.getReceiverPhone());
        resOrderDTO.setReceiverEmail(order.getReceiverEmail());
        resOrderDTO.setStatus(order.getStatus());
        resOrderDTO.setPaymentMethod(order.getPaymentMethod());
        resOrderDTO.setPaymentStatus(order.getPaymentStatus());
        resOrderDTO.setPaidAt(order.getPaidAt());
        if (order.getCustomer() != null) {
            resOrderDTO.setCustomerId(order.getCustomer().getId());
        }
        resOrderDTO.setCreatedAt(order.getCreatedAt());
        resOrderDTO.setUpdatedAt(order.getUpdatedAt());
        resOrderDTO.setCreatedBy(order.getCreatedBy());
        resOrderDTO.setUpdatedBy(order.getUpdatedBy());
        List<OrderDetail> orderDetails = order.getOrderDetails() != null ? order.getOrderDetails() : List.of();
        List<InfoOrderDetailInOrder> orderDetailInOrders = new ArrayList<>();
        for (OrderDetail orderDetail : orderDetails) {
            InfoOrderDetailInOrder infoOrderDetailInOrder = getInfoOrderDetailInOrder(orderDetail);
            orderDetailInOrders.add(infoOrderDetailInOrder);
        }
        resOrderDTO.setOrderDetails(orderDetailInOrders);
        return resOrderDTO;
    }


    public static InfoOrderDetailInOrder getInfoOrderDetailInOrder(OrderDetail orderDetail) {
        InfoOrderDetailInOrder infoOrderDetailInOrder = new InfoOrderDetailInOrder();
        infoOrderDetailInOrder.setId(orderDetail.getId());
        infoOrderDetailInOrder.setPrice(orderDetail.getPrice());
        infoOrderDetailInOrder.setQuantity(orderDetail.getQuantity());
        InfoBookInOrder infoBookInOrder = getInfoBookInOrder(orderDetail);
        infoOrderDetailInOrder.setBook(infoBookInOrder);
        return infoOrderDetailInOrder;
    }

    public static InfoBookInOrder getInfoBookInOrder(OrderDetail orderDetail) {
        InfoBookInOrder infoBookInOrder = new InfoBookInOrder();
        infoBookInOrder.setId(orderDetail.getBook().getId());
        infoBookInOrder.setTitle(orderDetail.getBook().getTitle());
        if (orderDetail.getBook().getPublisher() != null) {
            infoBookInOrder.setPublisher(orderDetail.getBook().getPublisher().getName());
        }
        infoBookInOrder.setPrice(orderDetail.getBook().getPrice());
        infoBookInOrder.setQuantity(orderDetail.getBook().getQuantity());
        infoBookInOrder.setDescription(orderDetail.getBook().getDescription());
        infoBookInOrder.setImage(orderDetail.getBook().getImage());
        List<InfoAuthorInOrder> authorInOrders = new ArrayList<>();
        List<Author> authors = orderDetail.getBook().getAuthors();
        if (authors == null) {
            infoBookInOrder.setAuthors(authorInOrders);
            return infoBookInOrder;
        }
        for (Author author : authors) {
            InfoAuthorInOrder infoAuthorInOrder = new InfoAuthorInOrder();
            infoAuthorInOrder.setId(author.getId());
            infoAuthorInOrder.setName(author.getName());
            infoAuthorInOrder.setBirthday(author.getBirthday());
            infoAuthorInOrder.setNationality(author.getNationality());
            authorInOrders.add(infoAuthorInOrder);
        }
        infoBookInOrder.setAuthors(authorInOrders);
        return infoBookInOrder;
    }
}

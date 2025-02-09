package com.example.bookverse.service;

import com.example.bookverse.domain.Order;
import com.example.bookverse.domain.OrderDetail;

import java.util.List;

public interface OrderService {
    // Create an order
    Order create (Order order) throws Exception;
    // Create order detail
    OrderDetail createDetail(OrderDetail orderDetail) throws Exception;

    // Update an order
    Order update (Order order) throws Exception;
    // Update order detail
    OrderDetail updateDetail(OrderDetail orderDetail) throws Exception;

    // Get an order
    Order fetchOrderById(long id) throws Exception;

    // Get all order
    List<Order> fetchAllOrders() throws Exception;


    // Delete an order
    void delete(long id) throws Exception;
    // Delete order detail
    void deleteDetail(long id) throws Exception;
}


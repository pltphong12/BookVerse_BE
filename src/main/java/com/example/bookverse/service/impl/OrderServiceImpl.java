package com.example.bookverse.service.impl;

import com.example.bookverse.domain.Book;
import com.example.bookverse.domain.Order;
import com.example.bookverse.domain.OrderDetail;
import com.example.bookverse.domain.User;
import com.example.bookverse.exception.global.IdInvalidException;
import com.example.bookverse.repository.BookRepository;
import com.example.bookverse.repository.OrderDetailRepository;
import com.example.bookverse.repository.OrderRepository;
import com.example.bookverse.repository.UserRepository;
import com.example.bookverse.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final BookRepository bookRepository;

    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository, OrderDetailRepository orderDetailRepository, BookRepository bookRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public Order create(Order order) throws Exception {
        if (order.getUser() != null){
            User user = this.userRepository.findById(order.getUser().getId()).orElse(null);
            order.setUser(user);
        }
        Order newOrder = this.orderRepository.save(order);
        for (OrderDetail orderDetail : order.getOrderDetails()) {
            OrderDetail orderDetailInDB = this.orderDetailRepository.findById(orderDetail.getId()).orElse(null);
            if (orderDetailInDB != null) {
                orderDetailInDB.setOrder(order);
                this.orderDetailRepository.save(orderDetailInDB);
            }
        }
        return this.orderRepository.save(order);
    }

    @Override
    public OrderDetail createDetail(OrderDetail orderDetail) throws Exception {
        if (orderDetail.getBook() != null){
            Book book = this.bookRepository.findById(orderDetail.getBook().getId()).orElse(null);
            orderDetail.setBook(book);
        }
        return this.orderDetailRepository.save(orderDetail);
    }

    @Override
    public Order update(Order order) throws Exception {
        Order orderInDB = this.orderRepository.findById(order.getId()).orElse(null);
        if (orderInDB == null) {
            throw new IdInvalidException("Order not found");
        }
        else {
            if (order.getTotalPrice() != 0 && order.getTotalPrice() != orderInDB.getTotalPrice()) {
                orderInDB.setTotalPrice(order.getTotalPrice());
            }
            if (order.getReceiverAddress() != null && !order.getReceiverAddress().equals(orderInDB.getReceiverAddress())) {
                orderInDB.setReceiverAddress(order.getReceiverAddress());
            }
            if (order.getReceiverName() != null && !order.getReceiverName().equals(orderInDB.getReceiverName())) {
                orderInDB.setReceiverName(order.getReceiverName());
            }
            if (order.getReceiverPhone() != null && !order.getReceiverPhone().equals(orderInDB.getReceiverPhone())) {
                orderInDB.setReceiverPhone(order.getReceiverPhone());
            }
            if (order.getReceiverEmail() != null && !order.getReceiverEmail().equals(orderInDB.getReceiverEmail())) {
                orderInDB.setReceiverEmail(order.getReceiverEmail());
            }
            if (order.getStatus() != null && !order.getStatus().equals(orderInDB.getStatus())) {
                orderInDB.setStatus(order.getStatus());
            }
            if (order.getOrderDetails() != null){
                orderInDB.setOrderDetails(order.getOrderDetails());
                for (OrderDetail orderDetail : order.getOrderDetails()) {
                    OrderDetail orderDetailInDB = this.orderDetailRepository.findById(orderDetail.getId()).orElse(null);
                    if (orderDetailInDB != null) {
                        orderDetailInDB.setOrder(order);
                        this.orderDetailRepository.save(orderDetailInDB);
                    }
                }
            }
            if (order.getUser() != null){
                User user = this.userRepository.findById(order.getUser().getId()).orElse(null);
                orderInDB.setUser(user);
            }
            return this.orderRepository.save(orderInDB);
        }
    }

    @Override
    public OrderDetail updateDetail(OrderDetail orderDetail) throws Exception {
        OrderDetail orderDetailInDB = this.orderDetailRepository.findById(orderDetail.getId()).orElse(null);
        if (orderDetailInDB == null) {
            throw new IdInvalidException("OrderDetail not found");
        }
        else {
            if (orderDetail.getBook() != null && !orderDetail.getBook().equals(orderDetailInDB.getBook())) {
                Book book = this.bookRepository.findById(orderDetail.getBook().getId()).orElse(null);
                orderDetailInDB.setBook(book);
            }
            if (orderDetail.getPrice() != 0 && orderDetail.getPrice() != orderDetailInDB.getPrice()) {
                orderDetailInDB.setPrice(orderDetail.getPrice());
            }
            if (orderDetail.getQuantity() != 0 && orderDetail.getQuantity() != orderDetailInDB.getQuantity()) {
                orderDetailInDB.setQuantity(orderDetail.getQuantity());
            }
            return this.orderDetailRepository.save(orderDetailInDB);
        }
    }

    @Override
    public Order fetchOrderById(long id) throws Exception {
        if (!this.orderRepository.existsById(id)) {
            throw new IdInvalidException("Order not found");
        }
        Order order = this.orderRepository.findById(id).orElse(null);
        return this.orderRepository.findById(id).orElse(null);
    }

    @Override
    public List<Order> fetchAllOrders() throws Exception {
        return this.orderRepository.findAll();
    }

    @Override
    public void delete(long id) throws Exception {
        if (!this.orderRepository.existsById(id)) {
            throw new IdInvalidException("Order not found");
        }
        Order order = this.orderRepository.findById(id).orElse(null);
        if (order.getOrderDetails() != null) {
            for (OrderDetail orderDetail : order.getOrderDetails()) {
                deleteDetail(orderDetail.getId());
            }
        }
        this.orderRepository.deleteById(id);
    }

    @Override
    public void deleteDetail(long id) throws Exception {
        if (!this.orderDetailRepository.existsById(id)) {
            throw new IdInvalidException("OrderDetail not found");
        }
        this.orderDetailRepository.deleteById(id);
    }
}

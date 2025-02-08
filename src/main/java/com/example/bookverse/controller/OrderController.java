package com.example.bookverse.controller;

import com.example.bookverse.domain.Order;
import com.example.bookverse.domain.OrderDetail;
import com.example.bookverse.domain.response.order.ResOrderDTO;
import com.example.bookverse.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Create order and order detail
    @PostMapping("/orders")
    public ResponseEntity<Order> createOrder(@RequestBody Order order) throws Exception {
        Order newOrder = this.orderService.create(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
    }
    @PostMapping("/order_details")
    public ResponseEntity<ResOrderDTO.InfoOrderDetailInOrder> createOrderDetail(@RequestBody OrderDetail orderDetail) throws Exception {
        OrderDetail newOrderDetail = this.orderService.createDetail(orderDetail);
        ResOrderDTO.InfoOrderDetailInOrder orderDetailInOrder = ResOrderDTO.getInfoOrderDetailInOrder(newOrderDetail);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderDetailInOrder);
    }

    // Update order and order detail
    @PutMapping("/orders")
    public ResponseEntity<Order> updateOrder(@RequestBody Order order) throws Exception {
        Order updatedOrder = this.orderService.update(order);
        return ResponseEntity.status(HttpStatus.OK).body(updatedOrder);
    }
    @PutMapping("/order_details")
    public ResponseEntity<ResOrderDTO.InfoOrderDetailInOrder> updateOrderDetail(@RequestBody OrderDetail orderDetail) throws Exception {
        OrderDetail updatedOrderDetail = this.orderService.updateDetail(orderDetail);
        ResOrderDTO.InfoOrderDetailInOrder orderDetailInOrder = ResOrderDTO.getInfoOrderDetailInOrder(updatedOrderDetail);
        return ResponseEntity.status(HttpStatus.OK).body(orderDetailInOrder);
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<ResOrderDTO> getOrder(@PathVariable Long id) throws Exception {
        Order order = this.orderService.fetchOrderById(id);
        ResOrderDTO resOrderDTO = ResOrderDTO.from(order);
        return ResponseEntity.status(HttpStatus.OK).body(resOrderDTO);
    }

    
    @GetMapping("/orders")
    public ResponseEntity<List<ResOrderDTO>> getAllOrders() throws Exception {
        List<Order> orders = this.orderService.fetchAllOrders();
        List<ResOrderDTO> resOrderDTOS = new ArrayList<>();
        for (Order order : orders) {
            ResOrderDTO resOrderDTO = ResOrderDTO.from(order);
            resOrderDTOS.add(resOrderDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(resOrderDTOS);
    }

    // Delete order and order detail
    @DeleteMapping("/orders/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) throws Exception {
        this.orderService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @DeleteMapping("/order_details/{id}")
    public ResponseEntity<Void> deleteOrderDetail(@PathVariable Long id) throws Exception {
        this.orderService.deleteDetail(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

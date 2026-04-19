package com.example.bookverse.controller;

import com.example.bookverse.dto.criteria.CriteriaFilterOrder;
import com.example.bookverse.dto.request.ReqCreateOrderDTO;
import com.example.bookverse.dto.request.ReqUpdateOrderDTO;
import com.example.bookverse.dto.response.ResOrderDTO;
import com.example.bookverse.dto.response.ResPagination;
import com.example.bookverse.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@Validated
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    @PreAuthorize("hasAuthority('ORDER_CREATE')")
    public ResponseEntity<ResOrderDTO> createOrder(@Valid @RequestBody ReqCreateOrderDTO req,
                                                   HttpServletRequest httpRequest) throws Exception {
        String ip = httpRequest.getHeader("X-Forwarded-For");
        if (ip == null || ip.isBlank()) {
            ip = httpRequest.getRemoteAddr();
        }
        req.setClientIpAddress(ip);

        ResOrderDTO created = orderService.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/orders")
    @PreAuthorize("hasAuthority('ORDER_UPDATE')")
    public ResponseEntity<ResOrderDTO> updateOrder(@Valid @RequestBody ReqUpdateOrderDTO req) throws Exception {
        return ResponseEntity.ok(orderService.update(req));
    }

    @GetMapping("/orders/{id}")
    @PreAuthorize("hasAuthority('ORDER_VIEW_BY_ID')")
    public ResponseEntity<ResOrderDTO> getOrder(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(orderService.getById(id));
    }

    @GetMapping("/orders/me")
    @PreAuthorize("hasAuthority('ORDER_VIEW_MINE')")
    public ResponseEntity<ResPagination> listMyOrders(
            @PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable)
            throws Exception {
        return ResponseEntity.ok(orderService.listMine(pageable));
    }

    @GetMapping("/orders/search")
    @PreAuthorize("hasAuthority('ORDER_VIEW_ALL_WITH_PAGINATION_AND_FILTER')")
    public ResponseEntity<ResPagination> searchOrders(
            @ModelAttribute CriteriaFilterOrder criteriaFilterOrder,
            Pageable pageable) throws Exception {
        return ResponseEntity.ok(orderService.fetchAllOrdersWithPaginationAndFilter(criteriaFilterOrder, pageable));
    }

    /**
     * Hủy đơn: cập nhật {@code status = CANCELLED} (không xóa bản ghi).
     */
    @DeleteMapping("/orders/{id}")
    @PreAuthorize("hasAuthority('ORDER_CANCEL')")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id) throws Exception {
        orderService.cancel(id);
        return ResponseEntity.noContent().build();
    }
}

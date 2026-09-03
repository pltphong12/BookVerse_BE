package com.example.bookverse.service.impl;

import com.example.bookverse.config.VnpayProperties;
import com.example.bookverse.domain.Book;
import com.example.bookverse.domain.Customer;
import com.example.bookverse.domain.Order;
import com.example.bookverse.domain.OrderDetail;
import com.example.bookverse.domain.OrderPayment;
import com.example.bookverse.dto.enums.OrderPaymentStatus;
import com.example.bookverse.dto.enums.OrderStatus;
import com.example.bookverse.dto.enums.PaymentMethod;
import com.example.bookverse.dto.enums.PaymentStatus;
import com.example.bookverse.repository.BookRepository;
import com.example.bookverse.repository.OrderPaymentRepository;
import com.example.bookverse.repository.OrderRepository;
import com.example.bookverse.service.CustomerService;
import com.example.bookverse.util.VnpayUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VnpayPaymentServiceImplTest {
    private static final String HASH_SECRET = "test-vnpay-secret";

    @Mock private OrderPaymentRepository orderPaymentRepository;
    @Mock private OrderRepository orderRepository;
    @Mock private BookRepository bookRepository;
    @Mock private CustomerService customerService;

    private VnpayPaymentServiceImpl paymentService;
    private OrderPayment payment;
    private Order order;
    private Book book;

    @BeforeEach
    void setUp() {
        VnpayProperties properties = new VnpayProperties();
        properties.setHashSecret(HASH_SECRET);
        paymentService = new VnpayPaymentServiceImpl(
                properties, orderPaymentRepository, orderRepository, bookRepository, customerService);

        book = new Book();
        book.setId(10);
        book.setQuantity(0);
        book.setReservedQuantity(2);
        book.setSold(0);

        OrderDetail detail = new OrderDetail();
        detail.setBook(book);
        detail.setQuantity(2);

        Customer customer = new Customer();
        customer.setId(1);
        order = new Order();
        order.setId(100);
        order.setCustomer(customer);
        order.setTotalPrice(180000);
        order.setStockReserved(true);
        order.setOrderDetails(java.util.List.of(detail));

        payment = new OrderPayment();
        payment.setProviderRef("BV100T1");
        payment.setAmount(180000);
        payment.setMethod(PaymentMethod.VNPAY);
        payment.setStatus(OrderPaymentStatus.INITIATED);
        payment.setOrder(order);
    }

    @Test
    void successfulIpn_convertsReservedStockToSoldStock() {
        Map<String, String> params = signedIpnParams("00");
        when(orderPaymentRepository.findByProviderRef(payment.getProviderRef())).thenReturn(Optional.of(payment));
        when(orderPaymentRepository.findByProviderRefForUpdate(payment.getProviderRef())).thenReturn(Optional.of(payment));
        when(bookRepository.findByIdForUpdate(book.getId())).thenReturn(Optional.of(book));

        Map<String, String> response = paymentService.handleIpn(params);

        assertEquals("00", response.get("RspCode"));
        assertEquals(OrderPaymentStatus.SUCCESS, payment.getStatus());
        assertEquals(OrderStatus.CONFIRMED, order.getStatus());
        assertEquals(PaymentStatus.PAID, order.getPaymentStatus());
        assertEquals(0, book.getReservedQuantity());
        assertEquals(2, book.getSold());
        assertEquals(0, book.getQuantity());
        verify(customerService).updateTotalSpendingAndLevel(1L, 180000);
        verify(bookRepository).save(book);
    }

    @Test
    void failedIpn_releasesReservedStockAndCancelsOrder() {
        Map<String, String> params = signedIpnParams("24");
        when(orderPaymentRepository.findByProviderRef(payment.getProviderRef())).thenReturn(Optional.of(payment));
        when(orderPaymentRepository.findByProviderRefForUpdate(payment.getProviderRef())).thenReturn(Optional.of(payment));
        when(bookRepository.findByIdForUpdate(book.getId())).thenReturn(Optional.of(book));

        paymentService.handleIpn(params);

        assertEquals(OrderPaymentStatus.FAILED, payment.getStatus());
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
        assertEquals(PaymentStatus.FAILED, order.getPaymentStatus());
        assertEquals(0, book.getReservedQuantity());
        assertEquals(2, book.getQuantity());
        assertEquals(0, book.getSold());
        verify(bookRepository).save(book);
    }

    private Map<String, String> signedIpnParams(String responseCode) {
        Map<String, String> params = new HashMap<>();
        params.put("vnp_TxnRef", payment.getProviderRef());
        params.put("vnp_Amount", "18000000");
        params.put("vnp_ResponseCode", responseCode);
        params.put("vnp_TransactionNo", "14123456");
        params.put("vnp_SecureHash", VnpayUtil.hmacSha512(HASH_SECRET, VnpayUtil.buildQueryString(params)));
        return params;
    }
}

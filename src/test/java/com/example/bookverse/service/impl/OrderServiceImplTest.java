package com.example.bookverse.service.impl;

import com.example.bookverse.config.ShippingProperties;
import com.example.bookverse.config.VnpayProperties;
import com.example.bookverse.domain.Book;
import com.example.bookverse.domain.Cart;
import com.example.bookverse.domain.CartDetail;
import com.example.bookverse.domain.Customer;
import com.example.bookverse.domain.CustomerAddress;
import com.example.bookverse.domain.Order;
import com.example.bookverse.domain.OrderDetail;
import com.example.bookverse.domain.OrderPayment;
import com.example.bookverse.domain.User;
import com.example.bookverse.dto.enums.CustomerLevel;
import com.example.bookverse.dto.enums.OrderPaymentStatus;
import com.example.bookverse.dto.enums.OrderStatus;
import com.example.bookverse.dto.enums.PaymentMethod;
import com.example.bookverse.dto.enums.PaymentStatus;
import com.example.bookverse.dto.request.ReqCheckoutFromCartDTO;
import com.example.bookverse.dto.response.ResOrderDTO;
import com.example.bookverse.exception.global.IdInvalidException;
import com.example.bookverse.repository.BookRepository;
import com.example.bookverse.repository.CartDetailRepository;
import com.example.bookverse.repository.CartRepository;
import com.example.bookverse.repository.CustomerAddressRepository;
import com.example.bookverse.repository.CustomerRepository;
import com.example.bookverse.repository.OrderPaymentRepository;
import com.example.bookverse.repository.OrderRepository;
import com.example.bookverse.service.CustomerService;
import com.example.bookverse.util.CurrentCustomerAccessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    @Mock private OrderRepository orderRepository;
    @Mock private BookRepository bookRepository;
    @Mock private CustomerRepository customerRepository;
    @Mock private OrderPaymentRepository orderPaymentRepository;
    @Mock private CurrentCustomerAccessor currentCustomerAccessor;
    @Mock private CartDetailRepository cartDetailRepository;
    @Mock private CartRepository cartRepository;
    @Mock private CustomerService customerService;
    @Mock private CustomerAddressRepository customerAddressRepository;

    private OrderServiceImpl orderService;
    private Customer customer;
    private Cart cart;
    private Book book;

    @BeforeEach
    void setUp() {
        ShippingProperties shippingProperties = new ShippingProperties();
        shippingProperties.setStandardFee(30000);
        shippingProperties.setFreeShippingThreshold(500000);
        VnpayProperties vnpayProperties = new VnpayProperties();
        vnpayProperties.setTmnCode("TEST");
        vnpayProperties.setHashSecret("test-secret");
        vnpayProperties.setPaymentUrl("https://payment.example.test");
        vnpayProperties.setReturnUrl("https://bookverse.example.test/payment-return");
        orderService = new OrderServiceImpl(
                orderRepository, bookRepository, customerRepository, orderPaymentRepository,
                currentCustomerAccessor, null, vnpayProperties, cartDetailRepository,
                cartRepository, customerService, customerAddressRepository, shippingProperties);

        customer = new Customer();
        customer.setId(1);
        customer.setCustomerLevel(CustomerLevel.BRONZE);
        User user = new User();
        user.setEmail("customer@bookverse.test");
        customer.setUser(user);

        cart = new Cart();
        cart.setId(1);
        cart.setCustomer(customer);
        customer.setCart(cart);

        book = new Book();
        book.setId(10);
        book.setTitle("Clean Code");
        book.setPrice(100000);
        book.setDiscount(10);
        book.setQuantity(3);
        book.setSold(0);
        book.setReservedQuantity(0);
    }

    @Test
    void checkoutCod_deductsStockAndSnapshotsAddress() throws Exception {
        CustomerAddress address = address();
        CartDetail cartDetail = cartDetail(2);
        ReqCheckoutFromCartDTO request = new ReqCheckoutFromCartDTO(5L, PaymentMethod.COD, "Gọi trước", "127.0.0.1");

        when(currentCustomerAccessor.requireCurrentCustomer()).thenReturn(customer);
        when(customerAddressRepository.findByIdAndCustomer(5L, customer)).thenReturn(Optional.of(address));
        when(cartRepository.findByCustomerForUpdate(customer)).thenReturn(Optional.of(cart));
        when(cartDetailRepository.findAllByCartForUpdate(cart)).thenReturn(List.of(cartDetail));
        when(bookRepository.findByIdForUpdate(10L)).thenReturn(Optional.of(book));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResOrderDTO result = orderService.checkoutFromCart(request);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());
        Order saved = orderCaptor.getValue();
        assertEquals("Nguyễn Văn A", saved.getReceiverName());
        assertEquals("12 Nguyễn Huệ, Phường Bến Nghé, Hồ Chí Minh", saved.getReceiverAddress());
        assertEquals("0900000000", saved.getReceiverPhone());
        assertEquals(200000, saved.getSubtotal());
        assertEquals(20000, saved.getProductDiscountTotal());
        assertEquals(30000, saved.getShippingFee());
        assertEquals(210000, saved.getTotalPrice());
        assertEquals(1, book.getQuantity());
        assertEquals(2, book.getSold());
        assertEquals(0, book.getReservedQuantity());
        assertEquals(210000, result.getTotalPrice());
        verify(cartDetailRepository).deleteAllByCart(cart);
    }

    @Test
    void checkoutVnpay_reservesStockBeforeRedirectingToGateway() throws Exception {
        CustomerAddress address = address();
        CartDetail cartDetail = cartDetail(2);
        ReqCheckoutFromCartDTO request = new ReqCheckoutFromCartDTO(5L, PaymentMethod.VNPAY, null, "127.0.0.1");

        when(currentCustomerAccessor.requireCurrentCustomer()).thenReturn(customer);
        when(customerAddressRepository.findByIdAndCustomer(5L, customer)).thenReturn(Optional.of(address));
        when(cartRepository.findByCustomerForUpdate(customer)).thenReturn(Optional.of(cart));
        when(cartDetailRepository.findAllByCartForUpdate(cart)).thenReturn(List.of(cartDetail));
        when(bookRepository.findByIdForUpdate(10L)).thenReturn(Optional.of(book));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResOrderDTO result = orderService.checkoutFromCart(request);

        assertEquals(1, book.getQuantity());
        assertEquals(0, book.getSold());
        assertEquals(2, book.getReservedQuantity());
        assertTrue(result.getPaymentUrl().startsWith("https://payment.example.test?"));
        verify(customerRepository).save(customer);
    }

    @Test
    void checkout_rejectsWhenCurrentStockIsInsufficient() throws Exception {
        CustomerAddress address = address();
        CartDetail cartDetail = cartDetail(4);
        ReqCheckoutFromCartDTO request = new ReqCheckoutFromCartDTO(5L, PaymentMethod.COD, null, "127.0.0.1");

        when(currentCustomerAccessor.requireCurrentCustomer()).thenReturn(customer);
        when(customerAddressRepository.findByIdAndCustomer(5L, customer)).thenReturn(Optional.of(address));
        when(cartRepository.findByCustomerForUpdate(customer)).thenReturn(Optional.of(cart));
        when(cartDetailRepository.findAllByCartForUpdate(cart)).thenReturn(List.of(cartDetail));
        when(bookRepository.findByIdForUpdate(10L)).thenReturn(Optional.of(book));

        assertThrows(IdInvalidException.class, () -> orderService.checkoutFromCart(request));

        assertEquals(3, book.getQuantity());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void cancelPendingVnpayOrder_releasesReservedStock() throws Exception {
        Order order = new Order();
        order.setId(100);
        order.setCustomer(customer);
        order.setPaymentMethod(PaymentMethod.VNPAY);
        order.setPaymentStatus(PaymentStatus.PENDING);
        order.setStatus(OrderStatus.PENDING);
        order.setStockReserved(true);
        order.setOrderDetails(List.of(orderDetail(2)));
        book.setQuantity(1);
        book.setReservedQuantity(2);

        OrderPayment payment = new OrderPayment();
        payment.setStatus(OrderPaymentStatus.INITIATED);
        when(orderRepository.findById(100L)).thenReturn(Optional.of(order));
        when(currentCustomerAccessor.requireCurrentCustomer()).thenReturn(customer);
        when(bookRepository.findByIdForUpdate(10L)).thenReturn(Optional.of(book));
        when(orderPaymentRepository.findByOrder(order)).thenReturn(Optional.of(payment));

        orderService.cancel(100);

        assertEquals(OrderStatus.CANCELLED, order.getStatus());
        assertEquals(OrderPaymentStatus.CANCELLED, payment.getStatus());
        assertEquals(3, book.getQuantity());
        assertEquals(0, book.getReservedQuantity());
    }

    private CustomerAddress address() {
        CustomerAddress address = new CustomerAddress();
        address.setId(5);
        address.setCustomer(customer);
        address.setReceiverName("Nguyễn Văn A");
        address.setReceiverPhone("0900000000");
        address.setAddressLine("12 Nguyễn Huệ");
        address.setWard("Phường Bến Nghé");
        address.setProvince("Hồ Chí Minh");
        return address;
    }

    private CartDetail cartDetail(long quantity) {
        CartDetail cartDetail = new CartDetail();
        cartDetail.setCart(cart);
        cartDetail.setBook(book);
        cartDetail.setQuantity(quantity);
        return cartDetail;
    }

    private OrderDetail orderDetail(long quantity) {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setBook(book);
        orderDetail.setQuantity(quantity);
        return orderDetail;
    }
}

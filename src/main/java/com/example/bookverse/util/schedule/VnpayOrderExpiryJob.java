package com.example.bookverse.util.schedule;

import com.example.bookverse.domain.Order;
import com.example.bookverse.domain.OrderPayment;
import com.example.bookverse.dto.enums.OrderPaymentStatus;
import com.example.bookverse.dto.enums.OrderStatus;
import com.example.bookverse.dto.enums.PaymentMethod;
import com.example.bookverse.dto.enums.PaymentStatus;
import com.example.bookverse.repository.OrderPaymentRepository;
import com.example.bookverse.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class VnpayOrderExpiryJob {

    private static final Logger log = LoggerFactory.getLogger(VnpayOrderExpiryJob.class);

    /** Đơn VNPAY chưa thanh toán quá 15 phút sẽ bị huỷ. */
    private static final int EXPIRE_MINUTES = 15;

    private final OrderRepository orderRepository;
    private final OrderPaymentRepository orderPaymentRepository;

    public VnpayOrderExpiryJob(OrderRepository orderRepository,
                               OrderPaymentRepository orderPaymentRepository) {
        this.orderRepository = orderRepository;
        this.orderPaymentRepository = orderPaymentRepository;
    }

    /** Chạy mỗi 2 phút. */
    @Scheduled(fixedRate = 2 * 60 * 1000)
    @Transactional
    public void cancelExpiredVnpayOrders() {
        Instant cutoff = Instant.now().minus(EXPIRE_MINUTES, ChronoUnit.MINUTES);

        List<Order> expiredOrders = orderRepository
                .findByPaymentMethodAndPaymentStatusAndCreatedAtBefore(
                        PaymentMethod.VNPAY, PaymentStatus.PENDING, cutoff);

        for (Order order : expiredOrders) {
            order.setStatus(OrderStatus.CANCELLED);
            order.setPaymentStatus(PaymentStatus.FAILED);
            orderRepository.save(order);

            if (order.getOrderPayments() != null) {
                for (OrderPayment payment : order.getOrderPayments()) {
                    if (payment.getStatus() == OrderPaymentStatus.INITIATED) {
                        payment.setStatus(OrderPaymentStatus.CANCELLED);
                        orderPaymentRepository.save(payment);
                    }
                }
            }

            log.info("Huỷ đơn VNPAY quá hạn: orderId={}, orderCode={}", order.getId(), order.getOrderCode());
        }

        if (!expiredOrders.isEmpty()) {
            log.info("Đã huỷ {} đơn VNPAY quá hạn thanh toán", expiredOrders.size());
        }
    }
}

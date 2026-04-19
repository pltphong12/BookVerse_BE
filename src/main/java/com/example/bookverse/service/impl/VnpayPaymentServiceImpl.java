package com.example.bookverse.service.impl;

import com.example.bookverse.config.VnpayProperties;
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
import com.example.bookverse.service.VnpayPaymentService;
import com.example.bookverse.util.VnpayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class VnpayPaymentServiceImpl implements VnpayPaymentService {

    private static final Logger log = LoggerFactory.getLogger(VnpayPaymentServiceImpl.class);

    private final VnpayProperties vnpayProperties;
    private final OrderPaymentRepository orderPaymentRepository;
    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;
    private final CustomerService customerService;

    public VnpayPaymentServiceImpl(VnpayProperties vnpayProperties,
                                   OrderPaymentRepository orderPaymentRepository,
                                   OrderRepository orderRepository,
                                   BookRepository bookRepository,
                                   CustomerService customerService) {
        this.vnpayProperties = vnpayProperties;
        this.orderPaymentRepository = orderPaymentRepository;
        this.orderRepository = orderRepository;
        this.bookRepository = bookRepository;
        this.customerService = customerService;
    }

    // ─── Return URL (browser redirect) ───────────────────────────────

    @Override
    @Transactional
    public String handleReturn(Map<String, String> params) {
        String txnRef = params.get("vnp_TxnRef");
        String orderCode = extractOrderCode(params);

        if (!VnpayUtil.verifySignature(params, vnpayProperties.getHashSecret())) {
            log.warn("Return: chữ ký không hợp lệ, vnp_TxnRef={}", txnRef);
            return buildFrontendUrl(false, orderCode);
        }

        String responseCode = params.get("vnp_ResponseCode");
        boolean success = "00".equals(responseCode);

        confirmPayment(params, success);

        return buildFrontendUrl(success, orderCode);
    }

    // ─── IPN (server-to-server) ──────────────────────────────────────

    @Override
    @Transactional
    public Map<String, String> handleIpn(Map<String, String> params) {
        String txnRef = params.get("vnp_TxnRef");

        if (!VnpayUtil.verifySignature(params, vnpayProperties.getHashSecret())) {
            log.warn("IPN: chữ ký không hợp lệ, vnp_TxnRef={}", txnRef);
            return ipnResponse("97", "Invalid Checksum");
        }

        OrderPayment payment = orderPaymentRepository.findByProviderRef(txnRef).orElse(null);
        if (payment == null) {
            log.warn("IPN: không tìm thấy OrderPayment, vnp_TxnRef={}", txnRef);
            return ipnResponse("01", "Order not found");
        }

        // Idempotent: đã xử lý rồi
        if (payment.getStatus() == OrderPaymentStatus.SUCCESS
                || payment.getStatus() == OrderPaymentStatus.FAILED) {
            log.info("IPN: đã xử lý trước đó, vnp_TxnRef={}, status={}", txnRef, payment.getStatus());
            return ipnResponse("00", "Confirm Success");
        }

        // Kiểm tra số tiền khớp
        long expectedAmount = Math.round(payment.getAmount() * 100);
        long receivedAmount = Long.parseLong(params.getOrDefault("vnp_Amount", "0"));
        if (expectedAmount != receivedAmount) {
            log.warn("IPN: số tiền không khớp, vnp_TxnRef={}, expected={}, received={}",
                    txnRef, expectedAmount, receivedAmount);
            return ipnResponse("04", "Invalid Amount");
        }

        String responseCode = params.get("vnp_ResponseCode");
        boolean success = "00".equals(responseCode);

        confirmPayment(params, success);

        return ipnResponse("00", "Confirm Success");
    }

    // ─── Logic chung: xác nhận thanh toán (idempotent) ───────────────

    private void confirmPayment(Map<String, String> params, boolean success) {
        String txnRef = params.get("vnp_TxnRef");

        OrderPayment payment = orderPaymentRepository.findByProviderRef(txnRef).orElse(null);
        if (payment == null) {
            log.warn("confirmPayment: không tìm thấy OrderPayment, vnp_TxnRef={}", txnRef);
            return;
        }

        // Idempotent: đã xử lý rồi thì bỏ qua
        if (payment.getStatus() == OrderPaymentStatus.SUCCESS
                || payment.getStatus() == OrderPaymentStatus.FAILED) {
            return;
        }

        payment.setGatewayTransactionId(params.get("vnp_TransactionNo"));
        payment.setGatewayResponseCode(params.get("vnp_ResponseCode"));
        payment.setRawPayload(params.toString());
        payment.setCompletedAt(Instant.now());

        Order order = payment.getOrder();

        if (success) {
            payment.setStatus(OrderPaymentStatus.SUCCESS);
            order.setStatus(OrderStatus.CONFIRMED);
            order.setPaymentStatus(PaymentStatus.PAID);
            order.setPaidAt(Instant.now());
            // Thực hiện việc cộng tiền
            customerService.updateTotalSpendingAndLevel(order.getCustomer().getId(), order.getTotalPrice());

            deductStockForOrder(order);

            log.info("Thanh toán thành công: vnp_TxnRef={}, orderId={}", txnRef, order.getId());
        } else {
            payment.setStatus(OrderPaymentStatus.FAILED);
            order.setStatus(OrderStatus.CANCELLED);
            order.setPaymentStatus(PaymentStatus.FAILED);
            log.info("Thanh toán thất bại: vnp_TxnRef={}, responseCode={}", txnRef, params.get("vnp_ResponseCode"));
        }

        orderPaymentRepository.save(payment);
        orderRepository.save(order);
    }

    // ─── Trừ kho khi thanh toán VNPAY thành công ────────────────────

    private void deductStockForOrder(Order order) {
        if (order.getOrderDetails() == null) return;
        for (OrderDetail detail : order.getOrderDetails()) {
            if (detail.getBook() != null) {
                detail.getBook().setQuantity(detail.getBook().getQuantity() - detail.getQuantity());
                detail.getBook().setSold(detail.getBook().getSold() + detail.getQuantity());
                bookRepository.save(detail.getBook());
            }
        }
    }

    // ─── Helpers ─────────────────────────────────────────────────────

    private String extractOrderCode(Map<String, String> params) {
        String orderInfo = params.getOrDefault("vnp_OrderInfo", "");
        return orderInfo.replace("Thanh toan don hang ", "").trim();
    }

    private String buildFrontendUrl(boolean success, String orderCode) {
        String base = success
                ? vnpayProperties.getFrontendSuccessUrl()
                : vnpayProperties.getFrontendFailUrl();
        return base + "?orderCode=" + (orderCode != null ? orderCode : "") + "&method=" + PaymentMethod.VNPAY.name();
    }

    private Map<String, String> ipnResponse(String rspCode, String message) {
        Map<String, String> resp = new HashMap<>();
        resp.put("RspCode", rspCode);
        resp.put("Message", message);
        return resp;
    }
}

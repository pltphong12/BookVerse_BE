package com.example.bookverse.controller;

import com.example.bookverse.service.VnpayPaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payments/vnpay")
public class VnpayPaymentController {

    private final VnpayPaymentService vnpayPaymentService;

    public VnpayPaymentController(VnpayPaymentService vnpayPaymentService) {
        this.vnpayPaymentService = vnpayPaymentService;
    }

    /**
     * VNPAY redirect browser về đây sau khi khách thanh toán.
     * Backend verify → redirect sang frontend (success/fail).
     */
    @GetMapping("/return")
    public ResponseEntity<Void> vnpayReturn(@RequestParam Map<String, String> params) {
        String redirectUrl = vnpayPaymentService.handleReturn(params);
        return ResponseEntity.status(302)
                .location(URI.create(redirectUrl))
                .build();
    }

    /**
     * VNPAY gọi server-to-server (IPN) để xác nhận giao dịch.
     * Trả JSON {RspCode, Message} theo format VNPAY yêu cầu.
     */
    @GetMapping("/ipn")
    public ResponseEntity<Map<String, String>> vnpayIpn(@RequestParam Map<String, String> params) {
        Map<String, String> result = vnpayPaymentService.handleIpn(params);
        return ResponseEntity.ok(result);
    }
}

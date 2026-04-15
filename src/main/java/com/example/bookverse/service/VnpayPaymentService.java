package com.example.bookverse.service;

import java.util.Map;

public interface VnpayPaymentService {

    /**
     * Xử lý callback Return URL (browser redirect).
     * @return URL frontend để redirect khách (success hoặc fail).
     */
    String handleReturn(Map<String, String> params);

    /**
     * Xử lý callback IPN (server-to-server).
     * @return Map chứa RspCode + Message theo format VNPAY yêu cầu.
     */
    Map<String, String> handleIpn(Map<String, String> params);
}

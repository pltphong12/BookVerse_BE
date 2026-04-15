package com.example.bookverse.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public final class VnpayUtil {

    private static final String HMAC_SHA512 = "HmacSHA512";
    private static final DateTimeFormatter VNP_DATE_FMT =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final ZoneId VN_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");

    private VnpayUtil() {}

    /**
     * Tạo chuỗi query đã sắp xếp theo alphabet (không bao gồm SecureHash).
     * Mỗi value được URL-encode UTF-8.
     */
    public static String buildQueryString(Map<String, String> params) {
        SortedMap<String, String> sorted = new TreeMap<>(params);
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : sorted.entrySet()) {
            if (entry.getValue() != null && !entry.getValue().isEmpty()) {
                if (!sb.isEmpty()) {
                    sb.append('&');
                }
                sb.append(URLEncoder.encode(entry.getKey(), StandardCharsets.US_ASCII))
                  .append('=')
                  .append(URLEncoder.encode(entry.getValue(), StandardCharsets.US_ASCII));
            }
        }
        return sb.toString();
    }

    /**
     * Ký HMAC-SHA512 lên chuỗi data bằng secret, trả hex lowercase.
     */
    public static String hmacSha512(String secret, String data) {
        try {
            Mac mac = Mac.getInstance(HMAC_SHA512);
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_SHA512));
            byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi ký HMAC-SHA512", e);
        }
    }

    /**
     * Tạo URL thanh toán hoàn chỉnh (baseUrl + query + SecureHash).
     */
    public static String buildPaymentUrl(String baseUrl, Map<String, String> params, String hashSecret) {
        String queryString = buildQueryString(params);
        String secureHash = hmacSha512(hashSecret, queryString);
        return baseUrl + "?" + queryString
                + "&vnp_SecureHash=" + secureHash;
    }

    /**
     * Verify chữ ký callback từ VNPAY.
     * Loại bỏ vnp_SecureHash / vnp_SecureHashType, build lại query, ký, so sánh.
     */
    public static boolean verifySignature(Map<String, String> params, String hashSecret) {
        String receivedHash = params.get("vnp_SecureHash");
        if (receivedHash == null || receivedHash.isBlank()) {
            return false;
        }

        Map<String, String> filtered = new TreeMap<>(params);
        filtered.remove("vnp_SecureHash");
        filtered.remove("vnp_SecureHashType");

        String queryString = buildQueryString(filtered);
        String computed = hmacSha512(hashSecret, queryString);
        return computed.equalsIgnoreCase(receivedHash);
    }

    /**
     * Lấy thời gian hiện tại theo múi giờ Việt Nam, format yyyyMMddHHmmss
     * (đúng định dạng vnp_CreateDate / vnp_ExpireDate).
     */
    public static String now() {
        return ZonedDateTime.now(VN_ZONE).format(VNP_DATE_FMT);
    }

    /**
     * Lấy thời gian hiện tại + N phút (dùng cho vnp_ExpireDate).
     */
    public static String nowPlusMinutes(int minutes) {
        return ZonedDateTime.now(VN_ZONE).plusMinutes(minutes).format(VNP_DATE_FMT);
    }
}

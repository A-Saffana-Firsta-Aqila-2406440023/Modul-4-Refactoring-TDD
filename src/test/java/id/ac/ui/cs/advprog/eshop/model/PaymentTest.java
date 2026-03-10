package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {
    Map<String, String> paymentData;

    @BeforeEach
    void setUp() {
        paymentData = new HashMap<>();
    }

    @Test
    void testCreatePaymentWithVoucherCodeDefaultStatus() {
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment("pay-001", "VOUCHER_CODE", paymentData);

        assertEquals("pay-001", payment.getId());
        assertEquals("VOUCHER_CODE", payment.getMethod());
        assertEquals("PENDING", payment.getStatus());
        assertEquals("ESHOP1234ABC5678", payment.getPaymentData().get("voucherCode"));
    }

    @Test
    void testCreatePaymentWithCashOnDeliveryDefaultStatus() {
        paymentData.put("address", "Jl. Merdeka No. 1");
        paymentData.put("deliveryFee", "10000");
        Payment payment = new Payment("pay-002", "CASH_ON_DELIVERY", paymentData);

        assertEquals("pay-002", payment.getId());
        assertEquals("CASH_ON_DELIVERY", payment.getMethod());
        assertEquals("PENDING", payment.getStatus());
        assertEquals("Jl. Merdeka No. 1", payment.getPaymentData().get("address"));
        assertEquals("10000", payment.getPaymentData().get("deliveryFee"));
    }

    @Test
    void testSetStatusSuccess() {
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment("pay-004", "VOUCHER_CODE", paymentData);
        payment.setStatus("SUCCESS");

        assertEquals("SUCCESS", payment.getStatus());
    }

    @Test
    void testSetStatusRejected() {
        paymentData.put("voucherCode", "INVALIDCODE");
        Payment payment = new Payment("pay-005", "VOUCHER_CODE", paymentData);
        payment.setStatus("REJECTED");

        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testSetStatusInvalidThrowsException() {
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment("pay-006", "VOUCHER_CODE", paymentData);

        assertThrows(IllegalArgumentException.class, () -> payment.setStatus("MEOW"));
    }
}
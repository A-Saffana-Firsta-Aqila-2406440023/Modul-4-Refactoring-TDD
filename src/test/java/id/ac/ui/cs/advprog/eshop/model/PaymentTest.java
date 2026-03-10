package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {
    Map<String, String> paymentDataVoucher;
    Map<String, String> paymentDataCOD;

    @BeforeEach
    void setUp() {
        paymentDataVoucher = new HashMap<>();
        paymentDataVoucher.put("voucherCode", "ESHOP1234ABC5678");

        paymentDataCOD = new HashMap<>();
        paymentDataCOD.put("address", "Jalan Margonda Raya");
        paymentDataCOD.put("deliveryFee", "15000");
    }

    @Test
    void testCreatePaymentVoucherSuccess() {
        Payment payment = new Payment("1", "VOUCHER", paymentDataVoucher);
        assertEquals("1", payment.getId());
        assertEquals("VOUCHER", payment.getMethod());
        assertEquals(paymentDataVoucher, payment.getPaymentData());
        assertEquals("SUCCESS", payment.getStatus());
    }

    @Test
    void testCreatePaymentVoucherRejected_InvalidLength() {
        paymentDataVoucher.put("voucherCode", "ESHOP123");
        Payment payment = new Payment("2", "VOUCHER", paymentDataVoucher);
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testCreatePaymentVoucherRejected_NotStartWithEshop() {
        paymentDataVoucher.put("voucherCode", "DISKO1234ABC5678");
        Payment payment = new Payment("3", "VOUCHER", paymentDataVoucher);
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testCreatePaymentVoucherRejected_NotEightNumerics() {
        paymentDataVoucher.put("voucherCode", "ESHOP12ABCDEFGH");
        Payment payment = new Payment("4", "VOUCHER", paymentDataVoucher);
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testCreatePaymentCODSuccess() {
        Payment payment = new Payment("5", "CASH_ON_DELIVERY", paymentDataCOD);
        assertEquals("5", payment.getId());
        assertEquals("CASH_ON_DELIVERY", payment.getMethod());
        assertEquals(paymentDataCOD, payment.getPaymentData());
        assertEquals("SUCCESS", payment.getStatus());
    }

    @Test
    void testCreatePaymentCODRejected_EmptyAddress() {
        paymentDataCOD.put("address", "");
        Payment payment = new Payment("6", "CASH_ON_DELIVERY", paymentDataCOD);
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testCreatePaymentCODRejected_NullDeliveryFee() {
        paymentDataCOD.put("deliveryFee", null);
        Payment payment = new Payment("7", "CASH_ON_DELIVERY", paymentDataCOD);
        assertEquals("REJECTED", payment.getStatus());
    }
}
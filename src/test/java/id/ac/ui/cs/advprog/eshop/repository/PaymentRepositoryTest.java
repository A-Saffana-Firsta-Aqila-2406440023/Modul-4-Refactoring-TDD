package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PaymentRepositoryTest {
    PaymentRepository paymentRepository;
    Map<String, String> paymentData;

    @BeforeEach
    void setUp() {
        paymentRepository = new PaymentRepository();
        paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
    }

    @Test
    void testSaveCreate() {
        Payment payment = new Payment("1", "VOUCHER", paymentData);
        Payment result = paymentRepository.save(payment);

        Payment findResult = paymentRepository.findById("1");
        assertEquals(payment.getId(), result.getId());
        assertEquals(payment.getId(), findResult.getId());
        assertEquals(payment.getMethod(), findResult.getMethod());
        assertEquals(payment.getStatus(), findResult.getStatus());
    }

    @Test
    void testSaveUpdate() {
        Payment payment = new Payment("1", "VOUCHER", paymentData);
        paymentRepository.save(payment);

        Payment updatedPayment = new Payment("1", "VOUCHER", paymentData);
        paymentRepository.save(updatedPayment);

        Payment findResult = paymentRepository.findById("1");
        assertEquals(1, paymentRepository.findAll().size());
        assertEquals(updatedPayment.getId(), findResult.getId());
    }

    @Test
    void testFindByIdIfIdFound() {
        Payment payment = new Payment("1", "VOUCHER", paymentData);
        paymentRepository.save(payment);

        Payment findResult = paymentRepository.findById("1");
        assertEquals("1", findResult.getId());
    }

    @Test
    void testFindByIdIfIdNotFound() {
        Payment findResult = paymentRepository.findById("not-exist");
        assertNull(findResult);
    }

    @Test
    void testFindAll() {
        Payment payment1 = new Payment("1", "VOUCHER", paymentData);
        paymentRepository.save(payment1);

        Payment payment2 = new Payment("2", "CASH_ON_DELIVERY", paymentData);
        paymentRepository.save(payment2);

        List<Payment> paymentList = paymentRepository.findAll();
        assertEquals(2, paymentList.size());
        assertTrue(paymentList.contains(payment1));
        assertTrue(paymentList.contains(payment2));
    }
}
package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    PaymentRepository paymentRepository;

    @Mock
    OrderService orderService;

    @InjectMocks
    PaymentServiceImpl paymentService;

    Order order;
    Map<String, String> paymentData;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        products.add(new Product()); // Asumsi minimal ada 1 product agar tidak IllegalArgumentException
        order = new Order("order-1", products, 123456789L, "Safira");

        paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
    }

    @Test
    void testAddPayment() {
        Payment payment = new Payment("pay-1", "VOUCHER", paymentData);
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment createdPayment = paymentService.addPayment(order, "VOUCHER", paymentData);

        assertNotNull(createdPayment);
        assertEquals("VOUCHER", createdPayment.getMethod());
        assertEquals("order-1", paymentData.get("orderId"));
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testSetStatusSuccess() {
        paymentData.put("orderId", "order-1");
        Payment payment = new Payment("pay-1", "VOUCHER", paymentData);
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment updatedPayment = paymentService.setStatus(payment, "SUCCESS");

        assertEquals("SUCCESS", updatedPayment.getStatus());
        verify(orderService, times(1)).updateStatus("order-1", OrderStatus.SUCCESS.getValue());
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void testSetStatusRejected() {
        paymentData.put("orderId", "order-1");
        Payment payment = new Payment("pay-1", "VOUCHER", paymentData);
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment updatedPayment = paymentService.setStatus(payment, "REJECTED");

        assertEquals("REJECTED", updatedPayment.getStatus());
        verify(orderService, times(1)).updateStatus("order-1", OrderStatus.FAILED.getValue());
        verify(paymentRepository, times(1)).save(payment);
    }
}
package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
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
    Payment payment;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(2);
        products.add(product);
        order = new Order("order-135", products, 1708560000L, "Safira Riyapandzan");

        paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        payment = new Payment("payment-1", "VOUCHER", paymentData);
    }

    @Test
    void testAddPaymentSuccess() {
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment createdPayment = paymentService.addPayment(order, "VOUCHER", paymentData);

        assertNotNull(createdPayment);
        assertEquals("VOUCHER", createdPayment.getMethod());
        assertEquals("order-135", paymentData.get("orderId"));
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testSetStatusSuccess() {
        paymentData.put("orderId", order.getId());
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment updatedPayment = paymentService.setStatus(payment, PaymentStatus.SUCCESS.getValue());

        assertEquals(PaymentStatus.SUCCESS.getValue(), updatedPayment.getStatus());
        verify(orderService, times(1)).updateStatus(order.getId(), OrderStatus.SUCCESS.getValue());
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void testSetStatusRejected() {
        paymentData.put("orderId", order.getId());
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment updatedPayment = paymentService.setStatus(payment, PaymentStatus.REJECTED.getValue());

        assertEquals(PaymentStatus.REJECTED.getValue(), updatedPayment.getStatus());
        verify(orderService, times(1)).updateStatus(order.getId(), OrderStatus.FAILED.getValue());
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void testSetInvalidStatus() {
        paymentData.put("orderId", order.getId());

        assertThrows(IllegalArgumentException.class, () -> {
            paymentService.setStatus(payment, "STATUS_NGASAL_TIDAK_VALID");
        });

        verify(orderService, never()).updateStatus(anyString(), anyString());
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    void testGetPaymentFound() {
        when(paymentRepository.findById("payment-1")).thenReturn(payment);

        Payment foundPayment = paymentService.getPayment("payment-1");

        assertNotNull(foundPayment);
        assertEquals("payment-1", foundPayment.getId());
        verify(paymentRepository, times(1)).findById("payment-1");
    }

    @Test
    void testGetPaymentNotFound() {
        when(paymentRepository.findById("payment-ngasal")).thenReturn(null);

        Payment foundPayment = paymentService.getPayment("payment-ngasal");

        assertNull(foundPayment);
        verify(paymentRepository, times(1)).findById("payment-ngasal");
    }

    @Test
    void testGetAllPayments() {
        List<Payment> paymentList = new ArrayList<>();
        paymentList.add(payment);
        when(paymentRepository.findAll()).thenReturn(paymentList);

        List<Payment> foundPayments = paymentService.getAllPayments();

        assertNotNull(foundPayments);
        assertEquals(1, foundPayments.size());
        assertEquals("payment-1", foundPayments.get(0).getId());
        verify(paymentRepository, times(1)).findAll();
    }

    @Test
    void testGetAllPaymentsEmpty() {
        when(paymentRepository.findAll()).thenReturn(new ArrayList<>());

        List<Payment> foundPayments = paymentService.getAllPayments();

        assertNotNull(foundPayments);
        assertTrue(foundPayments.isEmpty());
        verify(paymentRepository, times(1)).findAll();
    }
}
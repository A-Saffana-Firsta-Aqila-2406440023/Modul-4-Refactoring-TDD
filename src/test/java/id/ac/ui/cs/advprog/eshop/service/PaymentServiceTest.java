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
class PaymentServiceImplTest {

    @InjectMocks
    PaymentServiceImpl paymentService;

    @Mock
    PaymentRepository paymentRepository;

    Order order;
    Map<String, String> voucherData;
    Map<String, String> codData;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("prod-001");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(1);
        products.add(product);

        order = new Order("o-001", products, 1708560000L, "Safira Sudrajat");

        voucherData = new HashMap<>();
        voucherData.put("voucherCode", "ESHOP1234ABC5678");

        codData = new HashMap<>();
        codData.put("address", "Jl. Merdeka No. 1");
        codData.put("deliveryFee", "10000");
    }

    @Test
    void testAddPaymentVoucherValid() {
        Payment payment = new Payment("p-001", "VOUCHER", voucherData);
        doReturn(payment).when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.addPayment(order, "VOUCHER", voucherData);

        verify(paymentRepository, times(1)).save(any(Payment.class));
        assertEquals(PaymentStatus.SUCCESS.getValue(), result.getStatus());
    }

    @Test
    void testAddPaymentCODValid() {
        Payment payment = new Payment("p-002", "CASH_ON_DELIVERY", codData);
        doReturn(payment).when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.addPayment(order, "CASH_ON_DELIVERY", codData);

        verify(paymentRepository, times(1)).save(any(Payment.class));
        assertEquals(PaymentStatus.SUCCESS.getValue(), result.getStatus());
    }

    @Test
    void testSetStatusSuccess() {
        Payment payment = new Payment("p-001", "VOUCHER", voucherData);
        Payment result = paymentService.setStatus(payment, PaymentStatus.SUCCESS.getValue());

        assertEquals(PaymentStatus.SUCCESS.getValue(), result.getStatus());
        assertEquals(OrderStatus.SUCCESS.getValue(), order.getStatus());
    }

    @Test
    void testSetStatusRejected() {
        Payment payment = new Payment("p-001", "VOUCHER", voucherData);
        Payment result = paymentService.setStatus(payment, PaymentStatus.REJECTED.getValue());

        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
        assertEquals(OrderStatus.FAILED.getValue(), order.getStatus());
    }

    @Test
    void testGetPaymentFound() {
        Payment payment = new Payment("p-001", "VOUCHER", voucherData);
        doReturn(payment).when(paymentRepository).findById("p-001");

        Payment result = paymentService.getPayment("p-001");
        assertEquals("p-001", result.getId());
    }

    @Test
    void testGetPaymentNotFound() {
        doReturn(null).when(paymentRepository).findById("p-999");
        Payment result = paymentService.getPayment("p-999");
        assertNull(result);
    }

    @Test
    void testGetAllPayments() {
        List<Payment> payments = new ArrayList<>();
        payments.add(new Payment("p-001", "VOUCHER", voucherData));
        doReturn(payments).when(paymentRepository).findAll();

        List<Payment> result = paymentService.getAllPayments();
        assertEquals(1, result.size());
    }
}
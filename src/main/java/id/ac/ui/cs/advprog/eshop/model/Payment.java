package id.ac.ui.cs.advprog.eshop.model;

import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class Payment {
    String id;
    String method;
    String status;
    Map<String, String> paymentData;

    public Payment(String id, String method, Map<String, String> paymentData) {
        this.id = id;
        this.method = method;
        this.paymentData = paymentData;

        if ("VOUCHER".equals(method)) {
            this.status = validateVoucher() ? PaymentStatus.SUCCESS.getValue() : PaymentStatus.REJECTED.getValue();
        } else if ("CASH_ON_DELIVERY".equals(method)) {
            this.status = validateCOD() ? PaymentStatus.SUCCESS.getValue() : PaymentStatus.REJECTED.getValue();
        } else {
            this.status = PaymentStatus.REJECTED.getValue();
        }
    }

    private boolean validateVoucher() {
        String voucherCode = paymentData.get("voucherCode");

        if (voucherCode == null || voucherCode.length() != 16 || !voucherCode.startsWith("ESHOP")) {
            return false;
        }

        long digitCount = voucherCode.chars().filter(Character::isDigit).count();
        return digitCount == 8;
    }

    private boolean validateCOD() {
        String address = paymentData.get("address");
        String deliveryFee = paymentData.get("deliveryFee");

        return address != null && !address.trim().isEmpty() &&
                deliveryFee != null && !deliveryFee.trim().isEmpty();
    }
}
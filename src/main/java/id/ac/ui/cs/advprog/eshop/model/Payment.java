package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;

import java.util.Map;

@Getter
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
            this.status = validateVoucher() ? "SUCCESS" : "REJECTED";
        } else if ("CASH_ON_DELIVERY".equals(method)) {
            this.status = validateCOD() ? "SUCCESS" : "REJECTED";
        } else {
            this.status = "REJECTED";
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
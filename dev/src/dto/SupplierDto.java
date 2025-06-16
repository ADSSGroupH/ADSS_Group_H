package dto;

import java.util.List;

public class SupplierDto {
    private String supplierId;
    private String name;
    private String bankAccount;
    private String paymentMethod; // נרשם כ-String (enum name)
    private String deliveryAddress;
    private String agreementIds;     // "AG001,AG002,AG003"
    private String deliveryDays;     // "SUNDAY,WEDNESDAY"
    private String contactNumbers;;

    public SupplierDto() {}

    public SupplierDto(String supplierId, String name, String bankAccount, String paymentMethod,
                       String deliveryAddress, String contactNumbers,
                       String deliveryDays, String agreementIds) {
        this.supplierId = supplierId;
        this.name = name;
        this.bankAccount = bankAccount;
        this.paymentMethod = paymentMethod;
        this.deliveryAddress = deliveryAddress;
        this.contactNumbers = contactNumbers;
        this.deliveryDays = deliveryDays;
        this.agreementIds = agreementIds;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getContactPeople() {
        return contactNumbers;
    }

    public void setContactPeople(String contactPeople) {
        this.contactNumbers = contactPeople;
    }

    public String getDeliveryDays() {
        return deliveryDays;
    }

    public void setDeliveryDays(String deliveryDays) {
        this.deliveryDays = deliveryDays;
    }

    public String getAgreements() {
        return agreementIds;
    }

    public void setAgreements(String agreements) {
        this.agreementIds = agreements;
    }
}

package Domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Supplier {
    private String name;             // Supplier name
    private String deliveryAddress;  // deliveryAddress
    private String SupplierId;        // SupplierID
    private String bankAccount;      // supplier bankAccount
    private List<ContactPerson> contactPeople;  // supplier contacts list
    private List<Agreement> agreements; //Supplier Agreements list
    private PaymentMethod paymentMethod; // Supplier payment method
    private List<DeliveryWeekday> deliveryDays = new ArrayList<>(); //



    public Supplier(String SupplierId,String name, String bankAccount,  PaymentMethod paymentMethod, String deliveryAddress) {
        this.SupplierId = SupplierId;
        this.name = name;
        this.deliveryAddress = deliveryAddress;
        this.bankAccount = bankAccount;
        this.paymentMethod = paymentMethod;
        this.agreements = new ArrayList<>();
        this.contactPeople = new ArrayList<>();
    }

    public void addAgreement(String agreementId, List<DeliveryWeekday> deliveryDaysIndices,Boolean supportsDelivery,
                             Map<AgreementItem, Double> items) {

        Agreement agreement = new Agreement(
                agreementId,
                SupplierId,
                deliveryDaysIndices,
                supportsDelivery,
                items
        );

        agreements.add(agreement);
    }

    public void addAgreement(Agreement agreement) {
        agreements.add(agreement);
    }


    public List<Agreement> getAgreements() {
        return agreements;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getSupplierId() {
        return SupplierId;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public List<ContactPerson> getContactPeople() {
        return contactPeople;
    }

    public ContactPerson getOneContactPerson() {
        if (contactPeople != null && !contactPeople.isEmpty()) {
            return contactPeople.get(0);
        }
        return null;
    }


    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryDays(List<DeliveryWeekday> days) {
        this.deliveryDays = days;
    }

    public List<DeliveryWeekday> getDeliveryDays() {
        return deliveryDays;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setSupplierId(String SupplierId) {
        this.SupplierId = SupplierId;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }


    // Methods to manage contact people
    public void addContactPerson(ContactPerson contact) {
        contactPeople.add(contact);
    }

    public void removeContactPerson(ContactPerson contact) {
        contactPeople.remove(contact);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" SupplierId='").append(SupplierId).append("'\n");
        sb.append(" Name='").append(name).append("'\n");
        sb.append(" Delivery Address='").append(deliveryAddress).append("'\n");
        sb.append(" Bank Account='").append(bankAccount).append("'\n");
        sb.append(" Payment Terms='").append(paymentMethod).append("'\n");

        sb.append(" Contact Persons:\n");
        for (int i = 0; i < contactPeople.size(); i++) {
            ContactPerson c = contactPeople.get(i);
            sb.append(" Contact Person ").append(i + 1).append(":\n");
            sb.append("   Name: ").append(c.getName()).append("\n");
            sb.append("   Phone: ").append(c.getPhoneNumber()).append("\n");
            sb.append("   Email: ").append(c.getEmail()).append("\n");
        }
        return sb.toString();
    }

    public String getAgreementIdsAsString() {
        return agreements.stream()
                .map(Agreement::getAgreementId)
                .collect(Collectors.joining(","));
    }

    public String getDeliveryDaysAsString() {
        return deliveryDays.stream()
                .map(Enum::name)
                .collect(Collectors.joining(","));
    }

    public String getContactNumbersAsString() {
        return contactPeople.stream()
                .map(ContactPerson::getPhoneNumber)
                .collect(Collectors.joining(","));
    }
}



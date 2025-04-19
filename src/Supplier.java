import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Supplier {
    private String name;             // שם הספק
    private String deliveryAddress;  // כתובת ספק
    private String SupplierId;        // מספר ח"פ
    private String bankAccount;      // פרטי חשבון בנק
    private List<ContactPerson> contactPeople;  // רשימת אנשי קשר
    private List<Agreement> agreements;
    private PaymentMethod paymentMethod;


    public Supplier(String SupplierId,String name, String bankAccount, String paymentTerms, String deliveryAddress) {
        this.SupplierId = SupplierId;
        this.name = name;
        this.deliveryAddress = deliveryAddress;
        this.bankAccount = bankAccount;
        this.paymentMethod = paymentMethod;
        this.agreements = new ArrayList<>();
        this.contactPeople = new ArrayList<>();
          // ברירת מחדל: כל הימים false
    }

    public void addAgreement(String agreementId, List<DeliveryWeekday> deliveryDaysIndices,Boolean supportsDelivery,
                             String startDate, String endDate,
                             Map<AgreementItem, Double> items) {

        Agreement agreement = new Agreement(
                agreementId,
                SupplierId,
                deliveryDaysIndices,
                supportsDelivery,
                startDate,
                endDate,
                items
        );

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

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

//    public boolean[] getDeliveryDays() {
//        return deliveryDays;
//    }
//
//    public boolean getSupportsDelivery() {
//        return supportsDelivery;
//    }

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
}



import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Agreement {
    private String agreementId;
    private String supplierId;
    private String startDate;
    private String endDate;
    private Map<AgreementItem, Double> items;  // פריטים והמחירים
    private boolean supportsDelivery;          // האם קיימת אספקה
    private List<DeliveryWeekday> deliveryDays; // ימי האספקה הקבועים

    // Constructor
    public Agreement(String agreementId, String supplierId, List<DeliveryWeekday> deliveryDays, boolean supportsDelivery,
                     String startDate, String endDate, Map<AgreementItem, Double> items) {
        this.agreementId = agreementId;
        this.supplierId = supplierId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.items = items;
        this.supportsDelivery = supportsDelivery;
        this.deliveryDays = (deliveryDays != null) ? deliveryDays : List.of();
    }

    // בדיקה האם יש אספקה ביום מסוים
    public boolean isDeliveryAvailableOn(DeliveryWeekday day) {
        return supportsDelivery && deliveryDays.contains(day);
    }

    // בדיקה אם אין בכלל אספקה
    public boolean isPickupOnly() {
        return !supportsDelivery;
    }

    // Getters
    public String getAgreementId() {
        return agreementId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public Map<AgreementItem, Double> getItems() {
        return items;
    }

    public boolean getSupportsDelivery() {
        return supportsDelivery;
    }

    public List<DeliveryWeekday> getDeliveryDays() {
        return deliveryDays;
    }

    // Setters
    public void setAgreementId(String agreementId) {
        this.agreementId = agreementId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setSupportsDelivery(boolean supportsDelivery) {
        this.supportsDelivery = supportsDelivery;
    }

    public void setItems(Map<AgreementItem, Double> items) {
        this.items = items;
    }

    public void setDeliveryDays(List<DeliveryWeekday> deliveryDays) {
        this.deliveryDays = deliveryDays;
    }

    // הצגה
    @Override
    public String toString() {
        String daysString = deliveryDays.isEmpty()
                ? "None"
                : deliveryDays.stream()
                .map(day -> day.getDay_Name() + " (" + day.name() + ")")
                .collect(Collectors.joining(", "));

        return "- ID: " + agreementId + "\n" +
                "- Supplier ID: " + supplierId + "\n" +
                "- Supports Delivery: " + supportsDelivery + "\n" +
                "- Delivery Days: " + daysString + "\n" +
                "- Start Date: " + startDate + "\n" +
                "- End Date: " + endDate + "\n" +
                "- Items: " + (items.isEmpty() ? "None" : items.toString());
    }
}


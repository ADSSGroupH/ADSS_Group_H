package Domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class Agreement {
    private String agreementId;
    private String supplierId;
    private LocalDate startDate;
    private Map<AgreementItem, Double> items;  // פריטים והמחירים
    private boolean supportsDelivery;          // האם קיימת אספקה
    private List<DeliveryWeekday> deliveryDays; // ימי האספקה הקבועים

    // Constructor
    public Agreement(String agreementId, String supplierId, List<DeliveryWeekday> deliveryDays, boolean supportsDelivery,
                     Map<AgreementItem, Double> items) {
        this.agreementId = agreementId;
        this.supplierId = supplierId;
        this.startDate = LocalDate.now();
        this.items = items;
        this.supportsDelivery = supportsDelivery;
        this.deliveryDays = (deliveryDays != null) ? deliveryDays : List.of();
    }

    // Check if delivery is available on a specific day
    public boolean isDeliveryAvailableOn(DeliveryWeekday day) {
        return supportsDelivery && deliveryDays.contains(day);
    }

    // Check if the agreement is pickup-only (no delivery)
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

    public LocalDate getStartDate() {
        return startDate;
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

    public void setSupportsDelivery(boolean supportsDelivery) {
        this.supportsDelivery = supportsDelivery;
    }

    public void setItems(Map<AgreementItem, Double> items) {
        this.items = items;
    }

    public void setDeliveryDays(List<DeliveryWeekday> deliveryDays) {
        this.deliveryDays = deliveryDays;
    }

    public void addItem(AgreementItem item, double price) {
        if (items != null) {
            items.put(item, price);
        }
    }

    public void removeItem(AgreementItem item) {
        this.items.remove(item);
    }

    // presentation
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("- ID: ").append(agreementId).append("\n");
        sb.append("- Supplier ID: ").append(supplierId).append("\n");
        sb.append("- Supports Delivery: ").append(supportsDelivery).append("\n");

        if (supportsDelivery && deliveryDays != null && !deliveryDays.isEmpty()) {
            sb.append("- Delivery Days: ");
            for (DeliveryWeekday day : deliveryDays) {
                sb.append(day.getDay_Name()).append(" (").append(day.name()).append("), ");
            }
            sb.setLength(sb.length() - 2); // להסיר פסיק אחרון
            sb.append("\n");
        } else {
            sb.append("- Delivery Days: None\n");
        }

        sb.append("- Start Date: ").append(startDate).append("\n");

        if (items == null || items.isEmpty()) {
            sb.append("- Items: None\n");
        } else {
            sb.append("- Items:\n");
            int i = 1;
            for (Map.Entry<AgreementItem, Double> entry : items.entrySet()) {
                AgreementItem item = entry.getKey();
                double price = entry.getValue();
                sb.append("  Item ").append(i++).append(":\n");
                sb.append("    itemId: ").append(item.getItemId()).append("\n");
                sb.append("    catalogNumber: ").append(item.getCatalogNumber()).append("\n");
                sb.append("    price: ").append(price).append("\n");
                sb.append("    discount: ").append(item.getDiscount()).append("%\n");
                sb.append("    quantityForDiscount: ").append(item.getquantityForDiscount()).append("\n");
            }
        }

        return sb.toString();
    }
}

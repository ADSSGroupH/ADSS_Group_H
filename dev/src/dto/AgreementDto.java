package dto;

public class AgreementDto {
    private String agreementId;
    private String supplierId;
    private boolean supportsDelivery;
    private String deliveryDays; // מופרד בפסיקים, לדוגמה: "MONDAY,WEDNESDAY"
    private String items;        // מופרד בפסיקים עם מקפים, לדוגמה: "item1-12.5,item2-7.0"
    private String startDate;    // נרשם כמחרוזת (LocalDate.toString()) – נוח ל־JSON וטפסים

    public AgreementDto() {}

    public AgreementDto(String agreementId, String supplierId, boolean supportsDelivery,
                        String deliveryDays, String items, String startDate) {
        this.agreementId = agreementId;
        this.supplierId = supplierId;
        this.supportsDelivery = supportsDelivery;
        this.deliveryDays = deliveryDays;
        this.items = items;
        this.startDate = startDate;
    }

    public String getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(String agreementId) {
        this.agreementId = agreementId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public boolean isSupportsDelivery() {
        return supportsDelivery;
    }

    public void setSupportsDelivery(boolean supportsDelivery) {
        this.supportsDelivery = supportsDelivery;
    }

    public String getDeliveryDays() {
        return deliveryDays;
    }

    public void setDeliveryDays(String deliveryDays) {
        this.deliveryDays = deliveryDays;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}

public class AgreementItem {
    private String itemId;          // shared ID in the supermarket
    private String catalogNumber;   // supplier-specific catalog number
    private float price;
    private float discount = 0;
    private int quantity;

    public AgreementItem(String itemId, String catalogNumber, float price, float discount, int quantity) {
        this.itemId = itemId;
        this.catalogNumber = catalogNumber;
        this.price = price;
        this.discount = discount;
        this.quantity = quantity;
    }

    // Getters
    public String getItemId() {
        return itemId;
    }

    public String getCatalogNumber() {
        return catalogNumber;
    }

    public float getPrice(int quantity) {
        if (this.quantity <= quantity) {
            return price - (price * discount);
        }
        return price;
    }

    public float getDiscount() {
        return discount;
    }

    public int getquantityForDiscount() {
        return quantity;
    }

    // Setters
    public void setPrice(float price) {
        this.price = price;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

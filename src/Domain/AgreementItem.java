package Domain;

public class AgreementItem {
    private String itemId;          // shared ID in the supermarket
    private String catalogNumber;   // supplier-specific catalog number
    private float price;
    private float discount = 0;
    private int quantity;
    private String name;

    public AgreementItem(String itemId,String catalogNumber, float price, float discount, int quantity,String name) {
        this.itemId = itemId;
        this.catalogNumber = catalogNumber;
        this.price = price;
        this.discount = discount;
        this.quantity = quantity;
        this.name = name;
    }

    // Getters
    public String getItemId() {
        return itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public float get_basic_price(){
        return price;
    }

    public String getCatalogNumber() {
        return catalogNumber;
    }



    public float getPrice(int quantity) {
        if (this.quantity <= quantity && discount > 0 && discount < 100) {
            return price - (price * (discount / 100));
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

    @Override
    public String toString() {
        return "\n AgreementItem \n" +
                "  itemId='" + itemId + "',\n" +
                "  catalogNumber='" + catalogNumber + "',\n" +
                "  price=" + price + ",\n" +
                "  discount=" + (discount) + "%,\n" +
                "  quantityForDiscount=" + quantity + "\n";
    }
}

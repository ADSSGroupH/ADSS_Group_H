package DTO.Transportation;


public class ItemDTO {
    private int itemsDocumentId;
    private int itemId;
    private String name;
    private int weight;
    private int quantity;

    public ItemDTO(int itemsDocumentId, int itemId, String name, int weight, int quantity) {
        this.itemsDocumentId = itemsDocumentId;
        this.itemId = itemId;
        this.name = name;
        this.weight = weight;
        this.quantity = quantity;
    }

    public int getItemsDocumentId() {
        return itemsDocumentId;
    }

    public void setItemsDocumentId(int itemsDocumentId) {
        this.itemsDocumentId = itemsDocumentId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
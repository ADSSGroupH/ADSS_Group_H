package DomainLayer.Transportation;

import DTO.Transportation.ItemDTO;


public class Item {
    private int id;
    private String name;
    private int weight;
    private int quantity;

    public Item(int id, String name, int weight, int quantity) {
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.quantity = quantity;
    }

    public Item(ItemDTO itemDTO) {
        this.id = itemDTO.getItemId();
        this.name = itemDTO.getName();
        this.weight = itemDTO.getWeight();
        this.quantity = itemDTO.getQuantity();
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getWeight() {
        return weight;
    }
    public int getQuantity() {
        return quantity;
    }
    public void rduceQuantity(int quantity) {
        this.quantity -= quantity;
    }
    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }

    public ItemDTO toDTO(int itemsDocumentId) {
        return new ItemDTO(itemsDocumentId, id, name, weight, quantity);
    }

    public int getTotalWeight() {
        return this.quantity * this.weight;
    }
}

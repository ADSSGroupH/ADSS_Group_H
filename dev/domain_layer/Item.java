package dev.domain_layer;

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
}

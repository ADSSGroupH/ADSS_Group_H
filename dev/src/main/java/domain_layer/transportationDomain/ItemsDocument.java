package domain_layer.transportationDomain;

import java.util.List;

public class ItemsDocument {
    
    private int id;
    private Site destination;
    private List<Item> items;

    public ItemsDocument(int id, Site destination, List<Item> items) {
        this.id = id;
        this.destination = destination;
        this.items = items;
    }

    public Site getDestination() {
        return destination;
    }

    public List<Item> getItems() {
        return items;
    }    

    public void setDestination(Site destination) {
        this.destination = destination;
    }
    public void setItems(List<Item> items) {
        this.items = items;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String display() {
        StringBuilder sb = new StringBuilder();
        sb.append("Items Document:\n");
        sb.append("Destination: ").append(destination.getName()).append("\n");
        sb.append("Items:\n");
        for (Item item : items) {
            sb.append(item.getName()).append(" - ").append(item.getQuantity()).append("\n");
        }
        return sb.toString();
    }
}

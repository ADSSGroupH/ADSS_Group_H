package domain_layer;

import java.util.List;

public class ItemsDocument {
    
    private Site destination;
    private List<Item> items;

    public ItemsDocument(Site destination, List<Item> items) {
        this.destination = destination;
        this.items = items;
    }

    public Site getDestination() {
        return destination;
    }

    public List<Item> getItems() {
        return items;
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

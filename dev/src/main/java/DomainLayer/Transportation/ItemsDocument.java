package DomainLayer.Transportation;

import DTO.Transportation.ItemsDocumentDTO;

import java.time.LocalTime;
import java.util.List;

public class ItemsDocument {

    private int id;
    private Site destination;
    private List<Item> items;
    private LocalTime arrivalTime;

    public ItemsDocument(int id, Site destination, LocalTime arrivalTime, List<Item> items) {
        this.id = id;
        this.destination = destination;
        this.items = items;
        this.arrivalTime = arrivalTime;
    }

    public ItemsDocument(ItemsDocumentDTO itemsDocumentDTO, Site destination, List<Item> items) {
        this.id = itemsDocumentDTO.getId();
        this.destination = destination;
        this.arrivalTime = LocalTime.parse(itemsDocumentDTO.getArrivalTime());
        this.items = items;
    }

    public Site getDestination() {
        return destination;
    }

    public List<Item> getItems() {
        return items;
    }
    public LocalTime getArrivalTime() {
        return arrivalTime;
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
    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
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

    public ItemsDocumentDTO toDTO(int transportationId) {
        return new ItemsDocumentDTO(id, transportationId, destination.getShipmentAreaId(), destination.getName(), arrivalTime.toString());
    }

}

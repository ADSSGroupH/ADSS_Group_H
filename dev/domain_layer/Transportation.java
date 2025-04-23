package dev.domain_layer;

import java.util.List;

public class Transportation {
    private int id;
    private String date;
    private String departureTime;
    private String truckPlateNumber;
    private int driverID;
    private boolean succeeded;
    private List<ItemsDocument> itemsDocument; 
    private List<Integer> shipmentAreasID;
    private Site origin;
    private String accident;

    public Transportation(int id, String date, String departureTime,
                          String truckPlateNumber, int driverID,
                          List<ItemsDocument> itemsDocument, List<Integer> shipmentAreasID,
                          Site origin) {
        this.id = id;
        this.date = date;
        this.departureTime = departureTime;
        this.truckPlateNumber = truckPlateNumber;
        this.driverID = driverID;
        this.succeeded = false;
        this.itemsDocument = itemsDocument;
        this.shipmentAreasID = shipmentAreasID;
        this.origin = origin;
        this.accident = "No accidents reported";
    }
    public void setDate(String newDate) {
        this.date = newDate;
    }

    public void setDepartureTime(String newDepartureTime) {
        this.departureTime = newDepartureTime;
    }

    public void setTruckPlateNumber(String newPlate) {
        this.truckPlateNumber = newPlate;
    }

    public void setDriverID(int newDriverID) {
        this.driverID = newDriverID;
    }

    public void setSucceeded(boolean newSucceeded) {
        this.succeeded = newSucceeded;
    }

    public void setItemsDocument(List<ItemsDocument> newItemsDocument) {
        this.itemsDocument = newItemsDocument;
    }

    public void setShipmentAreasID(List<Integer> newShipmentAreasID) {
        this.shipmentAreasID = newShipmentAreasID;
    }

    public void setOrigin(Site newOrigin) {
        this.origin = newOrigin;
    }

    public int getId() {
        return id;
    }

    public String getTruckPlateNumber() {
        return truckPlateNumber;
    }

    public String getDate() {
        return date;
    }
    public String getDepartureTime() {
        return departureTime;
    }

    public int getDriverID() {
        return driverID;
    }
    public boolean isSucceeded() {
        return succeeded;
    }
    public List<ItemsDocument> getItemsDocument() {
        return itemsDocument;
    }
    public List<Integer> getShipmentAreasID() {
        return shipmentAreasID;
    }
    public Site getOrigin() {
        return origin;
    }
    public String getAccident() {
        return accident;
    }
    public void setAccident(String accident) {
        this.accident = accident;
    }
    public String display() {
        StringBuilder sb = new StringBuilder();
        sb.append("Transportation ID: ").append(id).append("\n");
        sb.append("Date: ").append(date).append("\n");
        sb.append("Departure Time: ").append(departureTime).append("\n");
        sb.append("Truck Plate Number: ").append(truckPlateNumber).append("\n");
        sb.append("Driver ID: ").append(driverID).append("\n");
        sb.append("Accident Report: ").append(accident).append("\n");
        sb.append("Origin: ").append(origin.getName()).append("\n");
        sb.append("Items Document:\n");
        for (ItemsDocument doc : itemsDocument) {
            sb.append(doc.display()).append("\n");
        }
        return sb.toString();
    }



    
}

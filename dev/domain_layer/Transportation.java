package dev.domain_layer;

import java.util.List;

public class Transportation {
    private int id;
    private String date;
    private String departureTime;
    private String truckPlateNumber;
    private int driverID;
    private boolean succeeded;
    private List<Item> itemsDocument; 
    private List<Integer> shipmentAreasID;
    private Site origin;
    private Site [] destination;
    private String accident;

    public Transportation(int id, String date, String departureTime,
                          String truckPlateNumber, int driverID,
                          List<Item> itemsDocument, List<Integer> shipmentAreasID,
                          Site origin, Site [] destination) {
        this.id = id;
        this.date = date;
        this.departureTime = departureTime;
        this.truckPlateNumber = truckPlateNumber;
        this.driverID = driverID;
        this.succeeded = false;
        this.itemsDocument = itemsDocument;
        this.shipmentAreasID = shipmentAreasID;
        this.origin = origin;
        this.destination = destination;
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

    public void setItemsDocument(List<Item> newItemsDocument) {
        this.itemsDocument = newItemsDocument;
    }

    public void setShipmentAreasID(List<Integer> newShipmentAreasID) {
        this.shipmentAreasID = newShipmentAreasID;
    }

    public void setOrigin(Site newOrigin) {
        this.origin = newOrigin;
    }

    public void setDestination(Site [] newDestination) {
        this.destination = newDestination;
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
    public List<Item> getItemsDocument() {
        return itemsDocument;
    }
    public List<Integer> getShipmentAreasID() {
        return shipmentAreasID;
    }
    public Site getOrigin() {
        return origin;
    }
    public Site[] getDestination() {
        return destination;
    }
    public String getAccident() {
        return accident;
    }
    public void setAccident(String accident) {
        this.accident = accident;
    }



    
}

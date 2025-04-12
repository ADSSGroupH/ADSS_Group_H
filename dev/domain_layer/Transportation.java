package dev.domain_layer;

import java.util.Dictionary;
import java.util.List;

public class Transportation {
    private int id;
    private String date;
    private String departureTime;
    private int transportationManagerID;
    private String truckPlateNumber;
    private int driverID;
    private boolean succeeded;
    private Dictionary<Integer, List<Item>> itemsDocument;
    private List<Integer> shipmentAreasID;
    private Site origin;
    private Site destination;

    public Transportation(int id, String date, String departureTime, int transportationManagerID,
                          String truckPlateNumber, int driverID, boolean succeeded,
                          Dictionary<Integer, List<Item>> itemsDocument, List<Integer> shipmentAreasID,
                          Site origin, Site destination) {
        this.id = id;
        this.date = date;
        this.departureTime = departureTime;
        this.transportationManagerID = transportationManagerID;
        this.truckPlateNumber = truckPlateNumber;
        this.driverID = driverID;
        this.succeeded = succeeded;
        this.itemsDocument = itemsDocument;
        this.shipmentAreasID = shipmentAreasID;
        this.origin = origin;
        this.destination = destination;
    }
    public void setDate(String newDate) {
        this.date = newDate;
    }

    public void setDepartureTime(String newDepartureTime) {
        this.departureTime = newDepartureTime;
    }

    public void setTransportationManagerID(int newID) {
        this.transportationManagerID = newID;
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

    public void setItemsDocument(Dictionary<Integer, List<Item>> newItemsDocument) {
        this.itemsDocument = newItemsDocument;
    }

    public void setShipmentAreasID(List<Integer> newShipmentAreasID) {
        this.shipmentAreasID = newShipmentAreasID;
    }

    public void setOrigin(Site newOrigin) {
        this.origin = newOrigin;
    }

    public void setDestination(Site newDestination) {
        this.destination = newDestination;
    }

    public int getId() {
        return id;
    }
}

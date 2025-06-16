package DTO.Transportation;

import java.time.LocalTime;

public class ItemsDocumentDTO {
    private int id;
    private int transportationId;
    private int shipmentAreaId;
    private String destinationName;
    private String arrivalTime;

    public ItemsDocumentDTO(int id, int transportationId, int shipmentAreaId, String destinationName, String arrivalTime) {
        this.id = id;
        this.transportationId = transportationId;
        this.shipmentAreaId = shipmentAreaId;
        this.destinationName = destinationName;
        this.arrivalTime = arrivalTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTransportationId() {
        return transportationId;
    }

    public void setTransportationId(int transportationId) {
        this.transportationId = transportationId;
    }

    public int getShipmentAreaId() {
        return shipmentAreaId;
    }

    public void setShipmentAreaId(int shipmentAreaId) {
        this.shipmentAreaId = shipmentAreaId;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
}

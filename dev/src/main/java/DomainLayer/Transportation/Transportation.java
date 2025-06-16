package DomainLayer.Transportation;

import DTO.Transportation.TransportationDTO;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


public class Transportation {
    private int id;
    private LocalDate date;
    private LocalTime departureTime;
    private String truckPlateNumber;
    private String driverName;
    private boolean succeeded;
    private List<ItemsDocument> itemsDocument;
    private List<Integer> shipmentAreasID;
    private Site origin;
    private String accident;

    public Transportation(int id, LocalDate date, LocalTime departureTime,
                          String truckPlateNumber, String driverName,
                          List<ItemsDocument> itemsDocument, List<Integer> shipmentAreasID,
                          Site origin) {
        this.id = id;
        this.date = date;
        this.departureTime = departureTime;
        this.truckPlateNumber = truckPlateNumber;
        this.driverName = driverName;
        this.succeeded = false;
        this.itemsDocument = itemsDocument;
        this.shipmentAreasID = shipmentAreasID;
        this.origin = origin;
        this.accident = "No accidents reported";
    }

    public Transportation(TransportationDTO transportationDTO, Site origin, List<ItemsDocument> itemsDocument, List<Integer> shipmentAreasID) {
        this.id = transportationDTO.getId();
        this.date = LocalDate.parse(transportationDTO.getDate());
        this.departureTime = LocalTime.parse(transportationDTO.getDepartureTime());
        this.truckPlateNumber = transportationDTO.getTruckPlateNumber();
        this.driverName = transportationDTO.getDriverName();
        this.succeeded = transportationDTO.isSucceeded();
        this.origin = origin;
        this.accident = transportationDTO.getAccident();
        this.itemsDocument = itemsDocument;
    }
    public void setDate(LocalDate newDate) {
        this.date = newDate;
    }

    public void setDepartureTime(LocalTime newDepartureTime) {
        this.departureTime = newDepartureTime;
    }

    public void setTruckPlateNumber(String newPlate) {
        this.truckPlateNumber = newPlate;
    }

    public void setDriverName(String newDriverName) {
        this.driverName = newDriverName;
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

    public LocalDate getDate() {
        return date;
    }
    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public String getDriverName() {
        return driverName;
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
        sb.append("Driver name: ").append(driverName).append("\n");
        sb.append("Accident Report: ").append(accident).append("\n");
        sb.append("Origin: ").append(origin.getName()).append("\n");
        for (ItemsDocument doc : itemsDocument) {
            sb.append(doc.display()).append("\n");
        }
        return sb.toString();
    }

    public ItemsDocument getItemsDocumentById(int id) {
        for (ItemsDocument doc : itemsDocument) {
            if (doc.getId() == id) {
                return doc;
            }
        }
        return null;
    }
    public void addItemsDocument(ItemsDocument doc) {
        this.itemsDocument.add(doc);
    }
    public void removeItemsDocument(ItemsDocument doc) {
        this.itemsDocument.remove(doc);
    }

    public TransportationDTO toDTO() {
        return new TransportationDTO(id, date.toString(), departureTime.toString(),
                truckPlateNumber, driverName, succeeded, origin.getName(), origin.getShipmentAreaId(), accident);
    }


}

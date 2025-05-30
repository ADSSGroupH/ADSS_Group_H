package dto_folder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import domain_layer.transportationDomain.Site;

public class TransportationDTO {
    private int id;
    private LocalDate date;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private String truckPlateNumber;
    private String driverName;
    private boolean succeeded;
    private List<Integer> itemsDocumentsId; 
    private String originName;
    private int originShipmentAreaId;
    private String accident;

    public TransportationDTO(int id, LocalDate date, LocalTime departureTime, LocalTime arrivalTime,
                            String truckPlateNumber, String driverName, boolean succeeded,
                            List<Integer> itemsDocumentsId, Site origin, String accident) {
        this.id = id;
        this.date = date;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.truckPlateNumber = truckPlateNumber;
        this.driverName = driverName;
        this.succeeded = succeeded;
        this.itemsDocumentsId = itemsDocumentsId;
        this.originName = origin.getName();
        this.originShipmentAreaId = origin.getShipmentAreaId();
        this.accident = accident;
    }

    public int getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public String getTruckPlateNumber() {
        return truckPlateNumber;
    }

    public String getDriverName() {
        return driverName;
    }

    public boolean isSucceeded() {
        return succeeded;
    }

    public List<Integer> getItemsDocumentsId() {
        return itemsDocumentsId;
    }

    public String getOriginName() {
        return originName;
    }

    public int getOriginShipmentAreaId() {
        return originShipmentAreaId;
    }

    public String getAccident() {
        return accident;
    }
}

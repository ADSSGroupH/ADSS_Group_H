package DTO.Transportation;



public class TransportationDTO {
    private int id;
    private String date;
    private String departureTime;
    private String truckPlateNumber;
    private String driverName;
    private boolean succeeded;
    private String originName;
    private int originShipmentAreaId;
    private String accident;

    public TransportationDTO(int id, String date, String departureTime,
                             String truckPlateNumber, String driverName, boolean succeeded,
                             String originName, int shipmentAreaId, String accident) {
        this.id = id;
        this.date = date;
        this.departureTime = departureTime;
        this.truckPlateNumber = truckPlateNumber;
        this.driverName = driverName;
        this.succeeded = succeeded;
        this.originName = originName;
        this.originShipmentAreaId = shipmentAreaId;
        this.accident = accident;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getDepartureTime() {
        return departureTime;
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

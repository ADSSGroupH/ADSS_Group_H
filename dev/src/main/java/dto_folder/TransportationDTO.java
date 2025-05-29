package dto_folder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import domain_layer.transportationDomain.ItemsDocument;
import domain_layer.transportationDomain.Site;

public class TransportationDTO {
    private int id;
    private LocalDate date;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private String truckPlateNumber;
    private String driverName;
    private boolean succeeded;
    private List<ItemsDocument> itemsDocument; 
    private Site origin;
    private String accident;
}

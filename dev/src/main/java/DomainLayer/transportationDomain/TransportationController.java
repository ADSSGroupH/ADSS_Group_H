package DomainLayer.transportationDomain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import DTO.LicenseType;
import DomainLayer.HR_TransportationController;


public class TransportationController {

    private TransportationRepository transportationRep;
    private ShipmentAreaRepository shipmentAreaRep;
    private DriverRepository driverRep;
    private TruckRepository truckRep;
    private HR_TransportationController hrTransportationController;

    public TransportationController() {
        transportationRep = new TransportationRepository();
        shipmentAreaRep = new ShipmentAreaRepository();
        driverRep = new DriverRepository();
        truckRep = new TruckRepository();
        hrTransportationController = new HR_TransportationController();
    }

    public String makeShipmentArea(int id, String name){
        // Check if the shipment area already exists
        if (shipmentAreaRep.shipmentAreaExists(id)) {
            return "Shipment area with ID " + id + " already exists.";
        }
        ShipmentArea shipmentArea = new ShipmentArea(id, name, new ArrayList<>()); 
        shipmentAreaRep.addShipmentArea(shipmentArea);
        return "Shipment area created with ID " + id + " and name " + name;
    }
    
    public String changeShipmentArea(int id, String newName){
        // Check if the shipment area exists
        if (shipmentAreaRep.shipmentAreaExists(id)) {
            shipmentAreaRep.getShipmentArea(id).setName(newName);
            return "Shipment area with ID " + id + " changed to " + newName;
        } else {
            return "Shipment area with ID " + id + " not found.";
        }
    }

    public String makeTransportation(int id, LocalDate date, LocalTime departureTime, String truckPlateNumber, String drivername, List<ItemsDocument> itemsDocument, List<Integer> shipmentAreasID, Site origin){
        // Check if the transportation already exists
        if (transportationRep.transportationExists(id)) {
            return "Transportation with ID " + id + " already exists.";
        }

        // Check if the driver exists ← מוקדם!
        if (!driverRep.driverExists(drivername)) {
            return "Driver with ID " + drivername + " does not exist.";
        }

        // Check if the truck exists ← גם מוקדם כי נשתמש בו בהמשך
        if (!truckRep.truckExists(truckPlateNumber)) {
            return "Truck with plate number " + truckPlateNumber + " does not exist.";
        }

        List<LocalTime> arrivalTime = new ArrayList<>();
        for (ItemsDocument itemsDocument1 : itemsDocument) {
            arrivalTime.add(itemsDocument1.getArrivalTime());
        }

        // בדיקת זמינות עובדים במחסן אחרי ווידוא נהג ומשאית
        try {
            if (!hrTransportationController.isWarehouseWorkerAvailable(date, departureTime, arrivalTime)) {
                return "There are no available warehouse workers for the specified time.";
            }
        } catch (Exception e) {
            return "Error checking warehouse worker availability: " + e.getMessage();
        }

        // Check if the shipment areas exist
        for (int areaID : shipmentAreasID) {
            if (!shipmentAreaRep.shipmentAreaExists(areaID)) {
                return "Shipment area with ID " + areaID + " does not exist.";
            }
        }

        // Check if the site exists
        if (!shipmentAreaRep.checkSiteExists(origin.getName(), shipmentAreasID)) {
            return "Site with name " + origin.getName() + " doesn't exist in the shipment areas.";
        }

        if (!checkTruckAvailability(truckPlateNumber, date, departureTime, arrivalTime))
            return "Truck with plate number " + truckPlateNumber + " is already occupied during the specified time.";

        if (!checkAvalableDrivers(truckRep.getTruck(truckPlateNumber).getLicenseType())) {
            return "No available drivers with the required license type for truck " + truckPlateNumber + ".";
        }

        if (!checkDriverAvailability(drivername, date, departureTime, arrivalTime)) {
            return "Driver with ID " + drivername + " is already occupied.";
        }

        if (!driverRep.getDriver(drivername).getLicenseType().equals(truckRep.getTruck(truckPlateNumber).getLicenseType())) {
            return "Driver with ID " + drivername + " does not have the required license type for truck " + truckPlateNumber + ".";
        }

        Transportation t = new Transportation(id, date, departureTime, truckPlateNumber, drivername, itemsDocument, shipmentAreasID, origin);

        if (calculateItemsWeight(itemsDocument) > truckRep.getTruck(truckPlateNumber).getMaxWeight()) {
            return "Items weight exceeds the truck's maximum weight.";
        }

        transportationRep.addTransportation(id, t);

        String areasNotification = "";
        if (shipmentAreasID.size() > 1){
            areasNotification += "\n The transportation needs to go through more than one shipment area";
        }

        return "Transportation created with ID " + id + ", Date: " + date + ", Departure Time: " + departureTime + ", Truck Plate Number: " + truckPlateNumber + ", Driver name: " + drivername + areasNotification;
    }



    public void deleteTransportation(int id){
        Transportation t = transportationRep.getTransportation(id);
        if (t != null) 
            transportationRep.removeTransportation(id);
    }
    
    public String changeDate(int id, LocalDate newDate) {
        Transportation t = transportationRep.getTransportation(id);
        if (t != null){
            t.setDate(newDate);
            return "Transportation date changed to " + newDate + " for ID " + id;
        } 
        else {
            return "Transportation with ID " + id + " not found.";
        } 
    }

    public String changeDepartureTime(int id, LocalTime newDepartureTime) {
        Transportation t = transportationRep.getTransportation(id);
        if (t != null) {
            t.setDepartureTime(newDepartureTime);
            return "Departure time changed to " + newDepartureTime + " for ID " + id;
        } else {
            return "Transportation with ID " + id + " not found.";
        }
    }
    
    public String changeTruckPlateNumber(int id, String newPlate) {
        Transportation t = transportationRep.getTransportation(id);
        if(truckRep.truckExists(newPlate)){
            if (t != null) {
                t.setTruckPlateNumber(newPlate);
                return "Truck plate number changed to " + newPlate + " for ID " + id;
            } else {
                return "Transportation with ID " + id + " not found.";
            }
        }else{
            return "Truck with plate number " + newPlate + " does not exist.";
        }
    }
    
    public String changeDriverName(int id, String newDriverName) {
        // Check if the driver exists
        if (!driverRep.driverExists(newDriverName)) {
            return "Driver with ID " + newDriverName + " does not exist.";
        }
        // Check if the transportation exists
        if (!transportationRep.transportationExists(id)) {
            return "Transportation with ID " + id + " does not exist.";
        }
        Transportation t = transportationRep.getTransportation(id);
        if (t == null) {
            return "Transportation with ID " + id + " not found.";
        }
        Driver driver = driverRep.getDriver(newDriverName);
        List<ItemsDocument> itemsDocument = t.getItemsDocument();
        List<LocalTime> arrivalTime = new ArrayList<>();
        for (ItemsDocument itemsDocument1 : itemsDocument) {
            arrivalTime.add(itemsDocument1.getArrivalTime());
        }
        // Check if the driver is available
        if (!checkDriverAvailability(newDriverName, t.getDate(), t.getDepartureTime(), arrivalTime)) {
            return "Driver with name " + newDriverName + " is already occupied.";
        }
        // Set the driver as occupied
        if (driver.getLicenseType().equals(truckRep.getTruck(t.getTruckPlateNumber()).getLicenseType())) {
            return "Driver with name " + newDriverName + " does not have the required license type for truck " + t.getTruckPlateNumber() + ".";
        }
        t.setDriverName(newDriverName);
        return "Driver name changed to " + newDriverName + " for Transportation ID " + id;
        
    }
    
    public String changeSucceeded(int id, boolean newSucceeded) {
        Transportation t = transportationRep.getTransportation(id);
        if (t != null) {
            t.setSucceeded(newSucceeded);
            return "Success status changed to " + newSucceeded + " for ID " + id;
        } else {
            return "Transportation with ID " + id + " not found.";
        }
    }
    
    public String changeItemsDocument(int id, List<ItemsDocument> newItemsDocument) {
        Transportation t = transportationRep.getTransportation(id);
        if (t != null) {
            // Check if the items weight is less than the truck's max weight
            if (calculateItemsWeight(newItemsDocument) > truckRep.getTruck(t.getTruckPlateNumber()).getMaxWeight()) {
                return "Items weight exceeds the truck's maximum weight.";
            }
            t.setItemsDocument(newItemsDocument);
            return "Items document updated for ID " + id;
        } else {
            return "Transportation with ID " + id + " not found.";
        }
    }
    
    public String changeShipmentAreasID(int id, List<Integer> newShipmentAreasID) {
        Transportation t = transportationRep.getTransportation(id);
        if (t != null) {
            t.setShipmentAreasID(newShipmentAreasID);
            return "Shipment areas updated for ID " + id;
        } else {
            return "Transportation with ID " + id + " not found.";
        }
    }
    
    public String changeOrigin(int id, Site newOrigin) {
        Transportation t = transportationRep.getTransportation(id);
        if (t != null) {
            t.setOrigin(newOrigin);
            return "Origin changed for ID " + id;
        } else {
            return "Transportation with ID " + id + " not found.";
        }
    }
    
    public String reportAccident(int transportationID, String accident){
        Transportation t = transportationRep.getTransportation(transportationID);
        if (t == null) {
            return "Transportation with ID " + transportationID + " not found.";
        }
        t.setAccident(accident);
        return "Accident reported";
    }

    public String addDriver(String name, LicenseType licenseType) {
        Driver driver = new Driver(name, licenseType);
        // Check if the driver already exists
        if (driverRep.driverExists(name)) {
            return "Driver with name " + name + " already exists.";
        }
        driverRep.addDriver(name, driver);
        return "Driver added with Name: " + name + ", License Type: " + licenseType;
    }

    public String addTruck(String plateNumber, String model, int netWeight, int maxWeight, LicenseType licenseType) {
        Truck truck = new Truck(plateNumber, model, netWeight, maxWeight, licenseType);
        // Check if the truck already exists
        if (truckRep.truckExists(plateNumber)) {
            return "Truck with plate number " + plateNumber + " already exists.";
        }
        truckRep.addTruck(plateNumber, truck);
        return "Truck added with plate number " + plateNumber + ", License Type: " + licenseType;
    }

    public String displayAllTransportations() {
        return transportationRep.displayAllTransportations();
    }

    private int calculateItemsWeight(List<ItemsDocument> itemsDocument) {
        // Calculate the total weight of items in the items document
        int totalWeight = 0;   
        for (ItemsDocument itemDocument : itemsDocument) {
            for (Item item : itemDocument.getItems()) {
                totalWeight = totalWeight + item.getWeight() * item.getQuantity();
            }
        }  
        return totalWeight;
    }

    public String removeItems(int transportationId, int itemsDocumentId) {
        Transportation t = transportationRep.getTransportation(transportationId);
        if (t != null) {
            ItemsDocument itemsToRemove = t.getItemsDocumentById(itemsDocumentId);
            if (itemsToRemove == null) {
                return "Items document with ID " + itemsDocumentId + " not found in transportation ID " + transportationId;
            }
            List<ItemsDocument> itemsDocument = t.getItemsDocument();
            itemsDocument.remove(itemsToRemove);
            t.setItemsDocument(itemsDocument);
            return "Items removed from transportation ID " + transportationId;
        } else {
            return "Transportation with ID " + transportationId + " not found.";
        }
    }

    public String addItems(int id, ItemsDocument itemsToAdd) {
        Transportation t = transportationRep.getTransportation(id);
        if (t != null) {
            List<ItemsDocument> itemsDocument = t.getItemsDocument();
            
            // Check if the items weight is less than the truck's max weight
            if (calculateItemsWeight(itemsDocument) > truckRep.getTruck(t.getTruckPlateNumber()).getMaxWeight()) {
                return "Items weight exceeds the truck's maximum weight.";
            }
            itemsDocument.add(itemsToAdd);
            t.setItemsDocument(itemsDocument);
            return "Items added to transportation ID " + id;
        } else {
            return "Transportation with ID " + id + " not found.";
        }
    }

    public String displayTrucks(){
        return truckRep.displayTrucks();
    }

    public String displayDrivers(){
        return driverRep.displayDrivers();
    }

    public boolean checkAvalableDrivers(LicenseType licenseType) {
        return driverRep.checkAvalableDrivers(licenseType);
    }

    public String displayItemsList(Transportation t) {
        StringBuilder sb = new StringBuilder();
        List<ItemsDocument> itemsDocument = t.getItemsDocument();
        for (ItemsDocument itemdDocument : itemsDocument) {
            sb.append(itemdDocument.display()).append("\n");
        }
        return sb.toString();
    }
    public String displayTransportationDocument(int transportationID) {
        Transportation t = transportationRep.getTransportation(transportationID);
        if (t == null) {
            return "Transportation with ID " + transportationID + " not found.";
        }
        return t.display();
    }

    public String addSite(String name, String address, String phoneNumber, String contactPersonName, int shipmentAreaId) {
        Site site = new Site(name, address, phoneNumber, contactPersonName, shipmentAreaId);

        ShipmentArea area = shipmentAreaRep.getShipmentArea(shipmentAreaId);
        if (area == null) {
            return "Shipment area with ID " + shipmentAreaId + " does not exist.";
        }
        // Check if the site already exists in the shipment area
        if (shipmentAreaRep.siteExistsInShipmentArea(shipmentAreaId, name)) {
            return "Site with name " + name + " already exists in the shipment area.";
        }

        shipmentAreaRep.addSiteToShipmentArea(shipmentAreaId, site);
        return "Site added with Name: " + name;
    }

    public String reportTransportationSuccess(int id) {
        Transportation t = transportationRep.getTransportation(id);
        if (t != null) {
            t.setSucceeded(true);
            return "Transportation with ID " + id + " marked as successful.";
        } else {
            return "Transportation with ID " + id + " not found.";
        }
    }
    public String removeTruck(String plateNumber) {
        if (truckRep.truckExists(plateNumber)) {
            truckRep.removeTruck(plateNumber);
            return "Truck with plate number " + plateNumber + " removed.";
        } else {
            return "Truck with plate number " + plateNumber + " not found.";
        }
    }
    public String removeDriver(String username) {
        if (driverRep.driverExists(username)) {
            driverRep.removeDriver(username);
            return "Driver with username " + username + " removed.";
        } else {
            return "Driver with username " + username + " not found.";
        }
    }
    public ShipmentArea findShipmentAreaById(int id) {
        ShipmentArea shipmentArea = shipmentAreaRep.getShipmentArea(id);
        if (shipmentArea != null) {
            return shipmentArea;
        } else {
            return null;
        }
    }
    public Transportation findTransportationById(int id) {
        Transportation transportation = transportationRep.getTransportation(id);
        if (transportation != null) {
            return transportation;
        } else {
            return null;
        }
    }

    public boolean checkDriverAvailability(String driverName, LocalDate date, LocalTime departureTime, List<LocalTime> arrivalTime) {
        List<Transportation> transportations = transportationRep.getTransportationsByDriverName(driverName);
        for (Transportation transportation : transportations) {
            if (transportation.getDate().equals(date)) {
                LocalTime tranDeparture = transportation.getDepartureTime();
                for (LocalTime arrival : arrivalTime) {
                    // Check if the new transportation overlaps with the existing one
                    if ((departureTime.isBefore(arrival) && departureTime.isAfter(tranDeparture)) ||
                    (arrival.isBefore(arrival) && arrival.isAfter(tranDeparture))) {
                        return false; // Driver is not available
                    }
                }
            }
            
        }
        return true;
    }

    public boolean checkTruckAvailability(String truckPlateNumber, LocalDate date, LocalTime departureTime, List<LocalTime> arrivalTime) {
        List<Transportation> transportations = transportationRep.getTransportationsByPlateNumber(truckPlateNumber);
        for (Transportation transportation : transportations) {
            if (transportation.getDate().equals(date)) {
                LocalTime tranDeparture = transportation.getDepartureTime();
                for (LocalTime arrival : arrivalTime) {
                    // Check if the new transportation overlaps with the existing one
                    if ((departureTime.isBefore(arrival) && departureTime.isAfter(tranDeparture)) ||
                    (arrival.isBefore(arrival) && arrival.isAfter(tranDeparture))) {
                        return false; // Driver is not available
                    }
                }
            }
        }
        return true;
    }

    public List<ShipmentArea> getAllShipmentAreas() {
        return shipmentAreaRep.getAllShipmentAreas();
    }


}
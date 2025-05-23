package domain_layer.transportationDomain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import domain_layer.transportationDomain.Driver.LicenseType;


public class TransportationController {

    private Map<Integer, Transportation> transportationMap;
    private Map<Integer, ShipmentArea> shipmentAreaMap; 
    private Map<String, Driver> driverMap; 
    private Map<String, Truck> truckMap;

    public TransportationController() {
        transportationMap = new HashMap<>();
        shipmentAreaMap = new HashMap<>();
        driverMap = new HashMap<>();
        truckMap = new HashMap<>();
    }

    public String makeShipmentArea(int id, String name){
        // Check if the shipment area already exists
        if (shipmentAreaMap.containsKey(id)) {
            return "Shipment area with ID " + id + " already exists.";
        }
        ShipmentArea shipmentArea = new ShipmentArea(id, name, new ArrayList<>()); 
        shipmentAreaMap.put(id, shipmentArea);
        return "Shipment area created with ID " + id + " and name " + name;
    }
    
    public String changeShipmentArea(int id, String newName){
        // Check if the shipment area exists
        if (shipmentAreaMap.containsKey(id)) {
            shipmentAreaMap.get(id).setName(newName);
            return "Shipment area with ID " + id + " changed to " + newName;
        } else {
            return "Shipment area with ID " + id + " not found.";
        }
    }

    public String makeTransportation(int id, LocalDate date, LocalTime departureTime,LocalTime arrivalTime, String truckPlateNumber, String drivername, List<ItemsDocument> itemsDocument, List<Integer> shipmentAreasID, Site origin){
        // Check if the transportation already exists
        if (transportationMap.containsKey(id)) {
            return "Transportation with ID " + id + " already exists.";
        }
        // Check if the shipment areas exist
        for (int areaID : shipmentAreasID) {
            if (!shipmentAreaMap.containsKey(areaID)) {
                return "Shipment area with ID " + areaID + " does not exist.";
            }
        }
        // Check if the site exists
        if (!checkSiteExists(origin.getName(), shipmentAreasID)) {
            return "Site with name " + origin.getName() + " doesn't exist in the shipment areas.";
        }
        for (ItemsDocument item : itemsDocument) {
            if (!checkSiteExists(item.getDestination().getName(), shipmentAreasID)) {
                return "Destination site with name " + item.getDestination().getName() + " doesn't exist in the shipment areas.";
            }
        }
        // Check if the truck exists
        if (!truckMap.containsKey(truckPlateNumber)) {
            return "Truck with plate number " + truckPlateNumber + " does not exist.";
        }
        // Check if there are available drivers with the required license type
        if (!checkAvalableDrivers(truckPlateNumber)) {
            return "No available drivers with the required license type for truck " + truckPlateNumber + ".";
        }
        // Check if the driver exists
        if (!driverMap.containsKey(drivername)) {
            return "Driver with ID " + drivername + " does not exist.";
        }
        // Check if the driver is available
        if (driverMap.get(drivername).isOccupied()) {
            return "Driver with ID " + drivername + " is already occupied.";
        }
        // Check if the driver has a matching license type
        if (!driverMap.get(drivername).getLicenseType().equals(truckMap.get(truckPlateNumber).getLicenseType())) {
            return "Driver with ID " + drivername + " does not have the required license type for truck " + truckPlateNumber + ".";
        }

        Transportation t = new Transportation(id, date, departureTime, arrivalTime, truckPlateNumber, drivername, itemsDocument, shipmentAreasID, origin);

        // Check if the items weight is less than the truck's max weight
        if (calculateItemsWeight(itemsDocument) > truckMap.get(truckPlateNumber).getMaxWeight()) {
            return "Items weight exceeds the truck's maximum weight.";
        }

        driverMap.get(drivername).setOccupied(true);
        transportationMap.put(id, t);
        String areasNotification = "";
        if (shipmentAreasID.size() > 1){
            areasNotification += "\n The transportation needs to go through more than one shipment area";
        }
        return "Transportation created with ID " + id + ", Date: " + date + ", Departure Time: " + departureTime + ", Truck Plate Number: " + truckPlateNumber + ", Driver name: " + drivername + areasNotification;
    }


    public void deleteTransportation(int id){
        Transportation t = findTransportationById(id);
        if (t != null) 
            transportationMap.remove(id);
    }

    public Transportation findTransportationById(int id) {
        return transportationMap.get(id);
    }
    
    public String changeDate(int id, LocalDate newDate) {
        Transportation t = findTransportationById(id);
        if (t != null){
            t.setDate(newDate);
            return "Transportation date changed to " + newDate + " for ID " + id;
        } 
        else {
            return "Transportation with ID " + id + " not found.";
        } 
    }

    public String changeDepartureTime(int id, LocalTime newDepartureTime) {
        Transportation t = findTransportationById(id);
        if (t != null) {
            t.setDepartureTime(newDepartureTime);
            return "Departure time changed to " + newDepartureTime + " for ID " + id;
        } else {
            return "Transportation with ID " + id + " not found.";
        }
    }
    
    public String changeTruckPlateNumber(int id, String newPlate) {
        Transportation t = findTransportationById(id);
        if (t != null) {
            t.setTruckPlateNumber(newPlate);
            return "Truck plate number changed to " + newPlate + " for ID " + id;
        } else {
            return "Transportation with ID " + id + " not found.";
        }
    }
    
    public String changeDriverName(int id, String newDriverName) {
        // Check if the driver exists
        if (!driverMap.containsKey(newDriverName)) {
            return "Driver with ID " + newDriverName + " does not exist.";
        }
        // Check if the transportation exists
        if (!transportationMap.containsKey(id)) {
            return "Transportation with ID " + id + " does not exist.";
        }
        Transportation t = findTransportationById(id);
        if (t == null) {
            return "Transportation with ID " + id + " not found.";
        }
        Driver driver = driverMap.get(newDriverName);
        // Check if the driver is available
        if (driver.isOccupied()) {
            return "Driver with name " + newDriverName + " is already occupied.";
        }
        // Set the driver as occupied
        driver.setOccupied(true);
        if (driver.getLicenseType().equals(truckMap.get(t.getTruckPlateNumber()).getLicenseType())) {
            return "Driver with name " + newDriverName + " does not have the required license type for truck " + t.getTruckPlateNumber() + ".";
        }
        t.setDriverName(newDriverName);
        return "Driver name changed to " + newDriverName + " for Transportation ID " + id;
        
    }
    
    public String changeSucceeded(int id, boolean newSucceeded) {
        Transportation t = findTransportationById(id);
        if (t != null) {
            t.setSucceeded(newSucceeded);
            return "Success status changed to " + newSucceeded + " for ID " + id;
        } else {
            return "Transportation with ID " + id + " not found.";
        }
    }
    
    public String changeItemsDocument(int id, List<ItemsDocument> newItemsDocument) {
        Transportation t = findTransportationById(id);
        if (t != null) {
            // Check if the items weight is less than the truck's max weight
            if (calculateItemsWeight(newItemsDocument) > truckMap.get(t.getTruckPlateNumber()).getMaxWeight()) {
                return "Items weight exceeds the truck's maximum weight.";
            }
            t.setItemsDocument(newItemsDocument);
            return "Items document updated for ID " + id;
        } else {
            return "Transportation with ID " + id + " not found.";
        }
    }
    
    public String changeShipmentAreasID(int id, List<Integer> newShipmentAreasID) {
        Transportation t = findTransportationById(id);
        if (t != null) {
            t.setShipmentAreasID(newShipmentAreasID);
            return "Shipment areas updated for ID " + id;
        } else {
            return "Transportation with ID " + id + " not found.";
        }
    }
    
    public String changeOrigin(int id, Site newOrigin) {
        Transportation t = findTransportationById(id);
        if (t != null) {
            t.setOrigin(newOrigin);
            return "Origin changed for ID " + id;
        } else {
            return "Transportation with ID " + id + " not found.";
        }
    }
    
    public String reportAccident(int transportationID, String accident){
        Transportation t = findTransportationById(transportationID);
        if (t == null) {
            return "Transportation with ID " + transportationID + " not found.";
        }
        t.setAccident(accident);
        return "Accident reported";
    }

    public String addDriver(String name, LicenseType licenseType) {
        Driver driver = new Driver(name, licenseType);
        // Check if the driver already exists
        if (driverMap.containsKey(name)) {
            return "Driver with name " + name + " already exists.";
        }
        driverMap.put(name, driver);
        return "Driver added with Name: " + name + ", License Type: " + licenseType;
    }

    public String addTruck(String plateNumber, String model, int netWeight, int maxWeight, LicenseType licenseType) {
        Truck truck = new Truck(plateNumber, model, netWeight, maxWeight, licenseType);
        // Check if the truck already exists
        if (truckMap.containsKey(plateNumber)) {
            return "Truck with plate number " + plateNumber + " already exists.";
        }
        truckMap.put(plateNumber, truck);
        return "Truck added with plate number " + plateNumber + ", License Type: " + licenseType;
    }

    public String displayAllTransportations() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, Transportation> entry : transportationMap.entrySet()) {
            Transportation t = entry.getValue();
            sb.append("ID: ").append(t.getId()).append(", Date: ").append(t.getDate())
              .append(", Departure Time: ").append(t.getDepartureTime())
              .append(", Truck Plate Number: ").append(t.getTruckPlateNumber())
              .append(", Driver Name: ").append(t.getDriverName()).append("\n");
        }
        return sb.toString();
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
        Transportation t = findTransportationById(transportationId);
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
        Transportation t = findTransportationById(id);
        if (t != null) {
            List<ItemsDocument> itemsDocument = t.getItemsDocument();
            
            // Check if the items weight is less than the truck's max weight
            if (calculateItemsWeight(itemsDocument) > truckMap.get(t.getTruckPlateNumber()).getMaxWeight()) {
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
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Truck> entry : truckMap.entrySet()) {
            Truck truck = entry.getValue();
            sb.append(truck.display()).append("\n");
        }
        return sb.toString();
    }

    public String displayDrivers(){
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Driver> entry : driverMap.entrySet()) {
            Driver driver = entry.getValue();
            sb.append(driver.display()).append("\n");
        }
        return sb.toString();
    }

    public boolean checkAvalableDrivers(String truckPlateNumber) {
        Truck truck = truckMap.get(truckPlateNumber);
        for (Map.Entry<String, Driver> entry : driverMap.entrySet()) {
            Driver driver = entry.getValue();
            if (!driver.isOccupied() && driver.getLicenseType().equals(truck.getLicenseType())) {
                return true; // Found an available driver with the required license type
            }
        }
        return false;
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
        Transportation t = findTransportationById(transportationID);
        if (t == null) {
            return "Transportation with ID " + transportationID + " not found.";
        }
        return t.display();
    }

    public String addSite(String name, String address, String phoneNumber, String contactPersonName, int shipmentAreaId) {
        Site site = new Site(name, address, phoneNumber, contactPersonName, shipmentAreaId);

        ShipmentArea area = shipmentAreaMap.get(shipmentAreaId);
        if (area == null) {
            return "Shipment area with ID " + shipmentAreaId + " does not exist.";
        }

        area.addSite(site);
        return "Site added with Name: " + name;
    }


    public boolean checkSiteExists(String name, List<Integer> shipmentAreaId) {
        for (int id : shipmentAreaId) {
            if (shipmentAreaMap.containsKey(id)) {
                ShipmentArea shipmentArea = shipmentAreaMap.get(id);
                for (Site site : shipmentArea.getSites()) {
                    if (site.getName().equals(name)) {
                        return true; // Site with the same name exists in the shipment area
                    }
                }
            }
        }
        return false; // Site with the same name does not exist in the shipment area
    }

    public String reportTransportationSuccess(int id) {
        Transportation t = findTransportationById(id);
        if (t != null) {
            t.setSucceeded(true);
            return "Transportation with ID " + id + " marked as successful.";
        } else {
            return "Transportation with ID " + id + " not found.";
        }
    }
    public String removeTruck(String plateNumber) {
        if (truckMap.containsKey(plateNumber)) {
            truckMap.remove(plateNumber);
            return "Truck with plate number " + plateNumber + " removed.";
        } else {
            return "Truck with plate number " + plateNumber + " not found.";
        }
    }
    public String removeDriver(String username) {
        if (driverMap.containsKey(username)) {
            driverMap.remove(username);
            return "Driver with username " + username + " removed.";
        } else {
            return "Driver with username " + username + " not found.";
        }
    }
    public ShipmentArea findShipmentAreaById(int id) {
        ShipmentArea shipmentArea = shipmentAreaMap.get(id);
        if (shipmentArea != null) {
            return shipmentArea;
        } else {
            return null;
        }
    }
}

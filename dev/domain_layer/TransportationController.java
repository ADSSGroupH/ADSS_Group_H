package dev.domain_layer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TransportationController {

    private Map<Integer, Transportation> transportationMap;
    private Map<Integer, ShipmentArea> shipmentAreaMap; 
    private Map<Integer, Driver> driverMap; 
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

    public String makeTransportation(int id, String date, String departureTime, String truckPlateNumber, int driverID, List<Item> itemsDocument, List<Integer> shipmentAreasID, Site origin, Site [] destination){ 
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
        //Check if the site exists
        if (!checkSiteExists(origin.getName(), shipmentAreasID)) {
            return "Site with name " + origin.getName() + " doesn't exists in the shipment areas.";
        }
        for (int i=0; i<destination.length; i++){
            if (!checkSiteExists(destination[i].getName(), shipmentAreasID)) {
                return "Site with name " + destination[i].getName() + " doesn't exists in the shipment areas.";
            }
        }
        // Check if the truck exists
        if (!truckMap.containsKey(truckPlateNumber)) {
            return "Truck with plate number " + truckPlateNumber + " does not exist.";
        }
        // Check if there are available drivers with the required license type
        if (checkAvalableDrivers(truckPlateNumber)) {
            return "No available drivers with the required license type for truck " + truckPlateNumber + ".";
        }
        // Check if the driver exists
        if (!driverMap.containsKey(driverID)) {
            return "Driver with ID " + driverID + " does not exist.";
        }
        // Check if the driver is available
        if (driverMap.get(driverID).isOccupied()) {
            return "Driver with ID " + driverID + " is already occupied.";
        }    
        if (driverMap.get(driverID).getLicenseType().equals(truckMap.get(truckPlateNumber).getLicenseType())) {
            return "Driver with ID " + driverID + " does not have the required license type for truck " + truckPlateNumber + ".";
        }
        Transportation t = new Transportation(id, date, departureTime, truckPlateNumber, driverID, itemsDocument, shipmentAreasID, origin, destination);
        // Check if the items weight is less than the truck's max weight
        if (calculateItemsWeight(itemsDocument) > truckMap.get(truckPlateNumber).getMaxWeight()) {
            return "Items weight exceeds the truck's maximum weight.";
        }
        driverMap.get(driverID).setOccupied(true);
        transportationMap.put(id, t);
        String areasNotification = "";
        if (shipmentAreasID.size() > 1){
            areasNotification += "\n The transportation needs to go through more than one shipment area";
        }
        return "Transportation created with ID " + id + ", Date: " + date + ", Departure Time: " + departureTime + ", Truck Plate Number: " + truckPlateNumber + ", Driver ID: " + driverID + areasNotification;
    }

    public void deleteTransportation(int id){
        Transportation t = findTransportationById(id);
        if (t != null) 
            transportationMap.remove(id);
    }

    private Transportation findTransportationById(int id) {
        return transportationMap.get(id);
    }
    
    public String changeDate(int id, String newDate) {
        Transportation t = findTransportationById(id);
        if (t != null){
            t.setDate(newDate);
            return "Transportation date changed to " + newDate + " for ID " + id;
        } 
        else {
            return "Transportation with ID " + id + " not found.";
        } 
    }

    public String changeDepartureTime(int id, String newDepartureTime) {
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
    
    public String changeDriverID(int id, int newDriverID) {
        // Check if the driver exists
        if (!driverMap.containsKey(newDriverID)) {
            return "Driver with ID " + newDriverID + " does not exist.";
        }
        // Check if the transportation exists
        if (!transportationMap.containsKey(id)) {
            return "Transportation with ID " + id + " does not exist.";
        }
        Transportation t = findTransportationById(id);
        if (t == null) {
            return "Transportation with ID " + id + " not found.";
        }
        Driver driver = driverMap.get(newDriverID);
        // Check if the driver is available
        if (driver.isOccupied()) {
            return "Driver with ID " + newDriverID + " is already occupied.";
        }
        // Set the driver as occupied
        driver.setOccupied(true);
        if (driver.getLicenseType().equals(truckMap.get(t.getTruckPlateNumber()).getLicenseType())) {
            return "Driver with ID " + newDriverID + " does not have the required license type for truck " + t.getTruckPlateNumber() + ".";
        }
        t.setDriverID(newDriverID);
        return "Driver ID changed to " + newDriverID + " for Transportation ID " + id;
        
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
    
    public String changeItemsDocument(int id, List<Item> newItemsDocument) {
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
    
    public String changeDestination(int id, Site[] newDestination) {
        Transportation t = findTransportationById(id);
        if (t != null) {
            t.setDestination(newDestination);
            return "Destination changed for ID " + id;
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

    public String addDriver(int id, String name, String licenseType) {
        Driver driver = new Driver(name, licenseType);
        // Check if the driver already exists
        if (driverMap.containsKey(id)) {
            return "Driver with ID " + id + " already exists.";
        }
        driverMap.put(id, driver);
        return "Driver added with ID " + id + ", Name: " + name + ", License Type: " + licenseType;
    }

    public String addTruck(String plateNumber, String model, int netWeight, int maxWeight, String licenseType) {
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
              .append(", Driver ID: ").append(t.getDriverID()).append("\n");
        }
        return sb.toString();
    }

    private int calculateItemsWeight(List<Item> itemsDocument) {
        // Calculate the total weight of items in the items document
        int totalWeight = 0;   
        for (Item item : itemsDocument) {
            totalWeight = totalWeight + item.getWeight() * item.getQuantity();
        }  
        return totalWeight;
    }

    public String removeItems(int id, List<Item> itemsToRemove) {
        Transportation t = findTransportationById(id);
        if (t != null) {
            List<Item> itemsDocument = t.getItemsDocument();
            itemsDocument.removeAll(itemsToRemove);
            t.setItemsDocument(itemsDocument);
            return "Items removed from transportation ID " + id;
        } else {
            return "Transportation with ID " + id + " not found.";
        }
    }

    public String addItems(int id, List<Item> itemsToAdd) {
        Transportation t = findTransportationById(id);
        if (t != null) {
            List<Item> itemsDocument = t.getItemsDocument();
            itemsDocument.addAll(itemsToAdd);
            // Check if the items weight is less than the truck's max weight
            if (calculateItemsWeight(itemsDocument) > truckMap.get(t.getTruckPlateNumber()).getMaxWeight()) {
                return "Items weight exceeds the truck's maximum weight.";
            }
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
            sb.append("Plate Number: ").append(truck.getPlateNumber())
              .append(", Model: ").append(truck.getModel())
              .append(", Net Weight: ").append(truck.getNetWeight())
              .append(", Max Weight: ").append(truck.getMaxWeight())
              .append(", License Type: ").append(truck.getLicenseType()).append("\n");
        }
        return sb.toString();
    }

    public String displayDrivers(){
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, Driver> entry : driverMap.entrySet()) {
            Driver driver = entry.getValue();
            sb.append("ID: ").append(entry.getKey())
              .append(", Name: ").append(driver.getName())
              .append(", License Type: ").append(driver.getLicenseType())
              .append(", Occupied: ").append(driver.isOccupied()).append("\n");
        }
        return sb.toString();
    }

    public boolean checkAvalableDrivers(String truckPlateNumber) {
        Truck truck = truckMap.get(truckPlateNumber);
        for (Map.Entry<Integer, Driver> entry : driverMap.entrySet()) {
            Driver driver = entry.getValue();
            if (!driver.isOccupied() && driver.getLicenseType().equals(truck.getLicenseType())) {
                return true; // Found an available driver with the required license type
            }
        }
        return false;
    }

    public String displayItemsList(Transportation t) {
        StringBuilder sb = new StringBuilder();
        List<Item> itemsDocument = t.getItemsDocument();
        for (Item item : itemsDocument) {
            sb.append(", Name: ").append(item.getName())
              .append(", quantity: ").append(item.getQuantity()).append("\n");
        }
        return sb.toString();
    }
    public String displayTransportationDocument(int transportationID) {
        Transportation t = findTransportationById(transportationID);
        if (t == null) {
            return "Transportation with ID " + transportationID + " not found.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Date: ").append(t.getDate()).append("\n")
          .append("Departure Time: ").append(t.getDepartureTime()).append("\n")
          .append("Truck Plate Number: ").append(t.getTruckPlateNumber()).append("\n")
          .append("Driver ID: ").append(t.getDriverID()).append("\n")
          .append("Origin: ").append(t.getOrigin()).append("\n")
          .append("Destination: ").append(t.getDestination()).append("\n")
          .append("Items Document: ").append(displayItemsList(t)).append("\n");
        return sb.toString();
    }

    public String addSite(String name, String address, String phoneNumber, String contactPersonName, int shipmentAreaId) {
        Site site = new Site(name, address, phoneNumber, contactPersonName, shipmentAreaId);
        // Check if the site already exists
        if (shipmentAreaMap.containsKey(shipmentAreaId)) {
            return "Site with ID " + shipmentAreaId + " already exists.";
        }
        shipmentAreaMap.get(shipmentAreaId).addSite(site);
        return "Site added with Name: " + name + ", Address: " + address;
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
}

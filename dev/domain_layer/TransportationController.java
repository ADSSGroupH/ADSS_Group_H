package dev.domain_layer;

import java.util.Dictionary;
import java.util.List;


public class TransportationController {

    public void makeShipmentArea(String name){
        // needs to be implemented
    }
    
    public void changeShipmentArea(int id, String newName){
        // needs to be implemented
    }

    public void makeTransportation(int id, String date, String departureTime, int transportationManagerID, String truckPlateNumber, int driverID, boolean succeded, Dictionary<Integer,List<Item>> itemsDocument, List<Integer> shipmentAreasID, Site origin, Site destination){    
        // needs to be implemented
    }

    public void deleteTransportation(int id){
        // needs to be implemented
    }
    
    public void changeDate(int id, String newDate) {
        // needs to be implemented
    }

    public void changeDepartureTime(int id, String newDepartureTime) {
        // needs to be implemented
    }

    public void changeTransportationManagerID(int id, int newManagerID) {
        // needs to be implemented
    }

    public void changeTruckPlateNumber(int id, String newPlate) {
        // needs to be implemented
    }

    public void changeDriverID(int id, int newDriverID) {
        // needs to be implemented
    }

    public void changeSucceeded(int id, boolean newSucceeded) {
        // needs to be implemented
    }

    public void changeItemsDocument(int id, Dictionary<Integer, List<Item>> newItemsDocument) {
       // needs to be implemented
    }

    public void changeShipmentAreasID(int id, List<Integer> newShipmentAreasID) {
        // needs to be implemented
    }

    public void changeOrigin(int id, Site newOrigin) {
        // needs to be implemented
    }

    public void changeDestination(int id, Site newDestination) {
        // needs to be implemented
    }

    public String reportAccident(){
        // needs to be implemented
        return "Accident reported";
    }
}

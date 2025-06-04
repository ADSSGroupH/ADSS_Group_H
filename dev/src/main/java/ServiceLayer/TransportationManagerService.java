package ServiceLayer;
<<<<<<< HEAD
=======

import DomainLayer.transportationDomain.*;
import DTO.LicenseType;
>>>>>>> 52972888becfac149da20316b226d9cf1f21354e

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import DTO.LicenseType;
import DomainLayer.transportationDomain.*;

public class TransportationManagerService {
    private final TransportationController controller = new TransportationController();

    public String addTruck(String plateNumber, String model, int netWeight, int maxWeight, LicenseType licenseType) {
        return controller.addTruck(plateNumber, model, netWeight, maxWeight, licenseType);
    }

    public String createShipmentArea(int id, String name) {
        return controller.makeShipmentArea(id, name);
    }

    public String changeShipmentAreaName(int id, String newName) {
        return controller.changeShipmentArea(id, newName);
    }

    public String makeTransportation(int id, LocalDate date, LocalTime departureTime, LocalTime arrivalTime,
                                     String truckPlate, String driverName, List<ItemsDocument> itemsDocument,
                                     List<Integer> shipmentAreaIds, Site origin) {
        return controller.makeTransportation(id, date, departureTime, arrivalTime, truckPlate, driverName, itemsDocument, shipmentAreaIds, origin);
    }

    public String changeTransportationDate(int id, LocalDate newDate) {
        return controller.changeDate(id, newDate);
    }

    public String changeDepartureTime(int id, LocalTime newTime) {
        return controller.changeDepartureTime(id, newTime);
    }

    public String changeTruck(int transportationId, String newPlate) {
        return controller.changeTruckPlateNumber(transportationId, newPlate);
    }

    public String changeDriver(int transportationId, String newDriverName) {
        return controller.changeDriverName(transportationId, newDriverName);
    }

    public String changeShipmentAreas(int transportationId, List<Integer> newShipmentAreas) {
        return controller.changeShipmentAreasID(transportationId, newShipmentAreas);
    }

    public String changeOrigin(int transportationId, Site newOrigin) {
        return controller.changeOrigin(transportationId, newOrigin);
    }

    public String changeSuccessStatus(int id, boolean succeeded) {
        return controller.changeSucceeded(id, succeeded);
    }

    public String addItems(int transportationId, ItemsDocument itemsToAdd) {
        return controller.addItems(transportationId, itemsToAdd);
    }

    public String removeItems(int transportationId, int itemsDocumentId) {
        return controller.removeItems(transportationId, itemsDocumentId);
    }

    public String displayTransportationDocument(int transportationId) {
        return controller.displayTransportationDocument(transportationId);
    }

    public String displayAllTransportations() {
        return controller.displayAllTransportations();
    }

    public String displayAllTrucks() {
        return controller.displayTrucks();
    }

    public String displayAllDrivers() {
        return controller.displayDrivers();
    }

    public String reportSuccess(int transportationId) {
        return controller.reportTransportationSuccess(transportationId);
    }

    public String addSite(String name, String address, String phone, String contactPerson, int shipmentAreaId) {
        return controller.addSite(name, address, phone, contactPerson, shipmentAreaId);
    }

    public String removeTruck(String plateNumber) {
        return controller.removeTruck(plateNumber);
    }

    public Transportation getTransportationById(int id) {
        return controller.findTransportationById(id);
    }

    public ShipmentArea getShipmentAreaById(int id) {
        return controller.findShipmentAreaById(id);
    }
}

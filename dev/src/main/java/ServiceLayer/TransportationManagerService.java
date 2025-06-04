// TransportationManagerService.java
package ServiceLayer;

import DomainLayer.transportationDomain.*;
import DTO.LicenseType;
import DomainLayer.transportationDomain.ItemsDocument;
import DTO.ItemDTO;
import DTO.ItemsDocumentDTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class TransportationManagerService {
    private final TransportationController controller = new TransportationController();

    public void addTruckUI(Scanner scanner) {
        System.out.print("Truck ID: ");
        String id = scanner.nextLine();
        System.out.print("Model: ");
        String model = scanner.nextLine();
        System.out.print("Net weight: ");
        int net = Integer.parseInt(scanner.nextLine());
        System.out.print("Max weight: ");
        int max = Integer.parseInt(scanner.nextLine());
        System.out.print("License type: ");
        String license = scanner.nextLine();
        System.out.println(controller.addTruck(id, model, net, max, LicenseType.valueOf(license)));
    }

    public void addSiteUI(Scanner scanner) {
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Address: ");
        String address = scanner.nextLine();
        System.out.print("Phone: ");
        String phone = scanner.nextLine();
        System.out.print("Contact: ");
        String contact = scanner.nextLine();
        System.out.print("Shipment Area ID: ");
        int areaId = Integer.parseInt(scanner.nextLine());
        System.out.println(controller.addSite(name, address, phone, contact, areaId));
    }

    public void addItemsUI(Scanner scanner) {
        System.out.print("Transportation ID: ");
        int transportationId = Integer.parseInt(scanner.nextLine());

        System.out.print("Document ID: ");
        int docId = Integer.parseInt(scanner.nextLine());

        System.out.print("Destination Site Name: ");
        String destinationName = scanner.nextLine();

        Site destination = null;
        for (ShipmentArea area : controller.getAllShipmentAreas()) {
            for (Site site : area.getSites()) {
                if (site.getName().equals(destinationName)) {
                    destination = site;
                    break;
                }
            }
            if (destination != null) break;
        }

        if (destination == null) {
            System.out.println("Destination site not found.");
            return;
        }

        System.out.print("Arrival Time (HH:mm): ");
        LocalTime arrivalTime = LocalTime.parse(scanner.nextLine());

        System.out.print("Item ID: ");
        int itemId = Integer.parseInt(scanner.nextLine());
        System.out.print("Item name: ");
        String itemName = scanner.nextLine();
        System.out.print("Weight: ");
        int weight = Integer.parseInt(scanner.nextLine());
        System.out.print("Quantity: ");
        int quantity = Integer.parseInt(scanner.nextLine());

        Item item = new Item(itemId, itemName, weight, quantity);
        List<Item> items = new ArrayList<>();
        items.add(item);

        ItemsDocument doc = new ItemsDocument(docId, destination, arrivalTime, items);
        System.out.println(controller.addItems(transportationId, doc));
    }



    public void removeItemsUI(Scanner scanner) {
        System.out.print("Transportation ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Document ID to remove: ");
        int docId = Integer.parseInt(scanner.nextLine());
        System.out.println(controller.removeItems(id, docId));
    }

    public void createShipmentAreaUI(Scanner scanner) {
        System.out.print("ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.println(controller.makeShipmentArea(id, name));
    }

    public void removeTruckUI(Scanner scanner) {
        System.out.print("Truck ID: ");
        String id = scanner.nextLine();
        System.out.println(controller.removeTruck(id));
    }

    public void displayTransportationDocumentUI(Scanner scanner) {
        System.out.print("Transportation ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.println(controller.displayTransportationDocument(id));
    }

    public void displayAllTransportations() {
        System.out.println(controller.displayAllTransportations());
    }

    public void displayAllTrucks() {
        System.out.println(controller.displayTrucks());
    }

    public void displayAllDrivers() {
        System.out.println(controller.displayDrivers());
    }

    public void reportSuccessUI(Scanner scanner) {
        System.out.print("Transportation ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.println(controller.reportTransportationSuccess(id));
    }

    public void changeSuccessStatusUI(Scanner scanner) {
        System.out.print("Transportation ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Succeeded? (true/false): ");
        boolean success = Boolean.parseBoolean(scanner.nextLine());
        System.out.println(controller.changeSucceeded(id, success));
    }

    public void changeShipmentAreaNameUI(Scanner scanner) {
        System.out.print("Shipment Area ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("New Name: ");
        String name = scanner.nextLine();
        System.out.println(controller.changeShipmentArea(id, name));
    }

    public void changeTransportationDateUI(Scanner scanner) {
        System.out.print("Transportation ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("New Date (YYYY-MM-DD): ");
        LocalDate date = LocalDate.parse(scanner.nextLine());
        System.out.println(controller.changeDate(id, date));
    }

    public void changeDepartureTimeUI(Scanner scanner) {
        System.out.print("Transportation ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("New Departure Time (HH:mm): ");
        LocalTime time = LocalTime.parse(scanner.nextLine());
        System.out.println(controller.changeDepartureTime(id, time));
    }

    public void changeTruckUI(Scanner scanner) {
        System.out.print("Transportation ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("New Truck ID: ");
        String truckId = scanner.nextLine();
        System.out.println(controller.changeTruckPlateNumber(id, truckId));
    }

    public void changeDriverUI(Scanner scanner) {
        System.out.print("Transportation ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("New Driver ID: ");
        String driverId = scanner.nextLine();
        System.out.println(controller.changeDriverName(id, driverId));
    }

    public void changeShipmentAreasUI(Scanner scanner) {
        System.out.print("Transportation ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter shipment area IDs separated by commas: ");
        String[] areaStrs = scanner.nextLine().split(",");
        List<Integer> areas = new ArrayList<>();
        for (String s : areaStrs)
            areas.add(Integer.parseInt(s.trim()));
        System.out.println(controller.changeShipmentAreasID(id, areas));
    }

    public void changeOriginUI(Scanner scanner) {
        System.out.print("Transportation ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("New Origin Site Name: ");
        String siteName = scanner.nextLine();
        Site origin = null;
        for (ShipmentArea area : controller.getAllShipmentAreas()) {
            for (Site site : area.getSites()) {
                if (site.getName().equals(siteName)) {
                    origin = site;
                    break;
                }
            }
            if (origin != null) break;
        }
        if (origin == null) {
            System.out.println("Origin site not found.");
            return;
        }
        System.out.println(controller.changeOrigin(id, origin));
    }

    public void makeTransportationUI(Scanner scanner) {
        try {
            System.out.print("ID: ");
            int id = Integer.parseInt(scanner.nextLine());
            System.out.print("Date (YYYY-MM-DD): ");
            LocalDate date = LocalDate.parse(scanner.nextLine());
            System.out.print("Departure (HH:mm): ");
            LocalTime dep = LocalTime.parse(scanner.nextLine());
            System.out.print("Arrival (HH:mm): ");
            LocalTime arr = LocalTime.parse(scanner.nextLine());
            System.out.print("Truck ID: ");
            String truck = scanner.nextLine();
            System.out.print("Driver ID: ");
            String driver = scanner.nextLine();
            System.out.print("Origin Site Name: ");
            String originName = scanner.nextLine();
            System.out.print("Shipment Area IDs (comma-separated): ");
            String[] shipmentAreaStrings = scanner.nextLine().split(",");
            List<Integer> shipmentAreas = new ArrayList<>();
            for (String s : shipmentAreaStrings) {
                shipmentAreas.add(Integer.parseInt(s.trim()));
            }
            Site origin = null;
            for (int areaId : shipmentAreas) {
                ShipmentArea sa = controller.findShipmentAreaById(areaId);
                if (sa != null) {
                    for (Site site : sa.getSites()) {
                        if (site.getName().equals(originName)) {
                            origin = site;
                            break;
                        }
                    }
                }
                if (origin != null) break;
            }
            if (origin == null) {
                System.out.println("Origin site not found in any of the provided shipment areas.");
                return;
            }
            System.out.println(controller.makeTransportation(id, date, dep, arr, truck, driver, new ArrayList<>(), shipmentAreas, origin));
        } catch (Exception e) {
            System.out.println("Failed to make transportation: " + e.getMessage());
        }
    }
}

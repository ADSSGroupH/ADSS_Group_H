import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import database.Database;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import DTO.LicenseType;
import DomainLayer.User;
import DomainLayer.UserController;
import DomainLayer.transportationDomain.*;


public class TransportationTests {

    @Test
    public void testItemAddAndReduceQuantity() {
        Item item = new Item(1, "Box", 5, 10);
        item.addQuantity(5);
        assertEquals(15, item.getQuantity());
        item.rduceQuantity(3);
        assertEquals(12, item.getQuantity());
    }

    @Test
    public void testItemsDocumentDisplayIncludesItems() {
        Site site = new Site("SiteA", "Address", "1234", "Contact", 1);
        LocalTime arrivalTime = LocalTime.parse("11:00");
        List<Item> items = Arrays.asList(new Item(1, "Box", 2, 4), new Item(2, "Chair", 5, 2));
        ItemsDocument doc = new ItemsDocument(1, site, arrivalTime, items);
        String output = doc.display();

        assertTrue(output.contains("Box"));
        assertTrue(output.contains("Chair"));
    }

    @Test
    public void testSiteDisplayIncludesDetails() {
        Site site = new Site("Site1", "123 St", "0500000000", "Alice", 2);
        String output = site.display();
        assertTrue(output.contains("Site1"));
        assertTrue(output.contains("Alice"));
    }

    @Test
    public void testShipmentAreaDoesNotAddDuplicateSite() {
        Site site1 = new Site("A", "Addr", "123", "John", 1);
        ShipmentArea area = new ShipmentArea(1, "North", new ArrayList<>());
        area.addSite(site1);
        area.addSite(site1); // Should not add again
        assertEquals(1, area.getSites().size());
    }

    @Test
    public void testAddDuplicateUserFails() {
        UserController uc = new UserController();
        String result = uc.addUser("Admin", "123", User.Role.SystemManager);
        assertEquals("User already exists", result);
    }

    @Test
    public void testTransportationDisplayIncludesBasicFields() {
        Site origin = new Site("Origin", "Addr", "123", "Rep", 1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String input = "2025-05-01";
        LocalDate userDate = LocalDate.parse(input, formatter);
        String input3 = "09:00";
        LocalTime departuretime = LocalTime.parse(input3);
        String input2 = "11:00";
        LocalTime arrivaltime = LocalTime.parse(input2);
        Transportation t = new Transportation(1, userDate, departuretime, arrivaltime, "ABC123", "afg", new ArrayList<>(), Arrays.asList(1), origin);
        String display = t.display();
        assertTrue(display.contains("Transportation ID: 1"));
        assertTrue(display.contains("Origin: Origin"));
    }

    @Test
    public void testAddItemsAndRemoveItemsLogic() {
        TransportationController tc = new TransportationController();
        tc.makeShipmentArea(1, "Area1");

        // Set up data
        Site origin = new Site("Origin", "123 Main St", "111-222-3333", "Alice", 1);
        Site destination = new Site("Dest", "456 Side St", "999-888-7777", "Bob", 1);

        tc.addSite(origin.getName(), origin.getAddress(), origin.getPhoneNumber(), origin.getContactPersonName(), origin.getShipmentAreaId());
        tc.addSite(destination.getName(), destination.getAddress(), destination.getPhoneNumber(), destination.getContactPersonName(), destination.getShipmentAreaId());

        tc.addTruck("TR1", "Volvo", 1000, 5000, LicenseType.B);
        tc.addDriver("Driver1", LicenseType.B);

        // Create initial items document
        List<Item> items = new ArrayList<>();
        items.add(new Item(1, "Box", 10, 2)); // total weight = 20
        LocalTime arrivalTime = LocalTime.parse("12:00");
        ItemsDocument doc = new ItemsDocument(2, destination, arrivalTime, items);
        List<ItemsDocument> docs = new ArrayList<>();
        docs.add(doc);

        List<Integer> shipmentAreas = new ArrayList<>();
        shipmentAreas.add(1);

        LocalDate userDate = LocalDate.parse("2025-05-01");
        LocalTime departuretime = LocalTime.parse("09:00");
        LocalTime arrivaltime = LocalTime.parse("11:00");
        // Create the transportation
        String creationResult = tc.makeTransportation(100, userDate, departuretime, arrivaltime, "TR1", "Driver1", docs, shipmentAreas, origin);
        assertTrue(creationResult.startsWith("Transportation created"));

        // Add another document
        List<Item> extraItems = new ArrayList<>();
        extraItems.add(new Item(2, "Crate", 5, 3)); // total weight = 15
        LocalTime arrivalTime1 = LocalTime.parse("14:00");
        ItemsDocument extraDoc = new ItemsDocument(99, destination, arrivalTime1, extraItems);  // ID = 99
        String addResult = tc.addItems(100, extraDoc);
        assertEquals("Items added to transportation ID 100", addResult);

        // Verify total documents count increased
        Transportation t = tc.findTransportationById(100);
        assertEquals(2, t.getItemsDocument().size());

        // Remove the newly added document by ID = 99
        String removeResult = tc.removeItems(100, 99);
        assertEquals("Items removed from transportation ID 100", removeResult);

        // Verify it's back to original state
        assertEquals(1, t.getItemsDocument().size());
    }





    @Test
    public void testChangeSucceededAndCheckStatus() {
        TransportationController tc = new TransportationController();
        Site origin = new Site("O", "A", "1", "C", 1);
        tc.makeShipmentArea(1, "Area1");
        tc.addTruck("TR1", "ModelX", 1000, 2000, LicenseType.C);
        tc.addDriver("D1", LicenseType.C);
        tc.addSite("O", "A", "1", "C", 1);

        List<Item> items = Arrays.asList(new Item(1, "Box", 5, 2));
        LocalTime arrivalTime2 = LocalTime.parse("13:00");

        ItemsDocument doc = new ItemsDocument(4, origin,arrivalTime2, items);
        List<ItemsDocument> docs = Arrays.asList(doc);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String input = "2025-05-01";
        LocalDate userDate = LocalDate.parse(input, formatter);
        String input3 = "09:00";
        LocalTime departuretime = LocalTime.parse(input3);
        String input2 = "11:00";
        LocalTime arrivaltime = LocalTime.parse(input2);
        String result = tc.makeTransportation(1, userDate, departuretime, arrivaltime, "TR1", "D1", docs, Arrays.asList(1), origin);
        System.out.println(result);

        Transportation t = tc.findTransportationById(1);

        tc.changeSucceeded(1, true);
        assertTrue(t.isSucceeded());
    }

    @Test
    public void testCheckSiteExistsPositiveAndNegative() {
        TransportationController tc = new TransportationController();
        tc.makeShipmentArea(1, "Area1");
        tc.addSite("A", "Addr", "123", "C", 1);
        ShipmentAreaRepository shipmentAreaRepository = new ShipmentAreaRepository();
        assertTrue(shipmentAreaRepository.checkSiteExists("A", Arrays.asList(1)));
        assertFalse(shipmentAreaRepository.checkSiteExists("B", Arrays.asList(1)));
    }

    @Test
    public void testRemoveExistingDriver() {
        TransportationController tc = new TransportationController();
        tc.addDriver("name", LicenseType.B);
        String result = tc.removeDriver("name");
        assertEquals("Driver with username name removed.", result);
    }
    @Test
    public void testAddTruck(){
        TruckRepository rep1 = new TruckRepository();
        Truck truck = new Truck("1234", "wefd", 123, 1243, LicenseType.B);
        rep1.addTruck("13413", truck);
        TruckRepository rep2 = new TruckRepository();
        assertEquals(rep2.getTruck("1234").getMaxWeight(), 1243);
    }

    @Test
    public void testItemTotalWeightCalculation() {
        Item item = new Item(101, "Crate", 4, 2); // quantity = 4, weight = 2kg
        int expectedTotalWeight = 8;
        assertEquals(expectedTotalWeight, item.getTotalWeight());
    }

    @Test
    public void testFullTransportationCreationFlow() {
        TransportationController tc = new TransportationController();


        tc.makeShipmentArea(10, "Central");
        Site origin = new Site("Warehouse", "Main Street", "050-0000000", "Eli", 10);
        tc.addSite(origin.getName(), origin.getAddress(), origin.getPhoneNumber(), origin.getContactPersonName(), origin.getShipmentAreaId());


        tc.addTruck("TR99", "Mercedes", 1500, 5000, LicenseType.C);
        tc.addDriver("driver99", LicenseType.C);


        List<Item> items = List.of(new Item(201, "Box", 3, 5)); // 15kg
        ItemsDocument doc = new ItemsDocument(301, origin, LocalTime.of(12, 0), items);

        //  makeTransportation
        String result = tc.makeTransportation(
                999, LocalDate.of(2025, 6, 1), LocalTime.of(8, 0), LocalTime.of(10, 0),
                "TR99", "driver99", List.of(doc), List.of(10), origin
        );

        assertTrue(result.startsWith("Transportation created with ID 999"));
    }

    @Test
    public void testAddDuplicateTruckFails() {
        TransportationController tc = new TransportationController();
        tc.addTruck("123456", "Volvo", 20,100, LicenseType.C);
        String result = tc.addTruck("123456", "Volvo", 20,100, LicenseType.C);
        assertEquals("Truck with plate number 123456 already exists.", result);
    }

    @Test
    public void testCreateDuplicateShipmentArea() {
        TransportationController tc = new TransportationController();
        tc.makeShipmentArea(1, "North");
        String result = tc.makeShipmentArea(1, "South");
        assertEquals("Shipment area with ID 1 already exists.", result);
    }

    @Test
    public void testMakeTransportationWithMissingDriver() {
        TransportationController tc = new TransportationController();
        tc.makeShipmentArea(1, "Area1");
        Site origin = new Site("Origin", "1 Street", "111", "Alice", 1);
        Site destination = new Site("Dest", "2 Street", "222", "Bob", 1);

        tc.addSite(origin.getName(), origin.getAddress(), origin.getPhoneNumber(), origin.getContactPersonName(), origin.getShipmentAreaId());
        tc.addSite(destination.getName(), destination.getAddress(), destination.getPhoneNumber(), destination.getContactPersonName(), destination.getShipmentAreaId());

        tc.addTruck("TR1", "Volvo", 20,100, LicenseType.C);

        LocalDate userDate = LocalDate.parse("2025-05-01");
        LocalTime departuretime = LocalTime.parse("09:00");
        LocalTime arrivaltime = LocalTime.parse("11:00");
        List<ItemsDocument> docs = new ArrayList<>();
        List<Integer> shipmentAreas = new ArrayList<>();
        shipmentAreas.add(1);

        String result = tc.makeTransportation(100, userDate, departuretime, arrivaltime, "TR1", "Driver1", docs, shipmentAreas, origin);
        System.out.println(result);
        assertTrue(result.contains("No available drivers with the required license type for truck TR1."));
    }




/*
    @BeforeEach
    void resetDb() throws Exception {
        // Try to close existing connection if still open
        Database.closeConnection(); // תוסיף מתודה סטטית כזו למחלקת Database

        // Delete the file
        Files.deleteIfExists(Paths.get("database.db"));

        // Re-initialize schema
        Database.init(); // גם מתודה סטטית שתפעיל מחדש את ה־CREATE TABLE וכו'
    }

 */





}

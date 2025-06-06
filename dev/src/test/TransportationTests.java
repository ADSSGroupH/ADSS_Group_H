import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import DTO.Transportation.LicenseType;
import DomainLayer.Transportation.*;
import DomainLayer.Transportation.Controllers.TransportationController;
import DomainLayer.Transportation.Repositories.ShipmentAreaRepository;
import DomainLayer.Transportation.Repositories.TruckRepository;
import database.Database;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


import org.junit.jupiter.api.Test;



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
    public void testTransportationDisplayIncludesBasicFields() {
        Site origin = new Site("Origin", "Addr", "123", "Rep", 1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String input = "2025-05-01";
        LocalDate userDate = LocalDate.parse(input, formatter);
        String input3 = "09:00";
        LocalTime departuretime = LocalTime.parse(input3);
        String input2 = "11:00";
        LocalTime arrivaltime = LocalTime.parse(input2);
        Transportation t = new Transportation(1, userDate, departuretime, "ABC123", "afg", new ArrayList<>(), Arrays.asList(1), origin);
        String display = t.display();
        assertTrue(display.contains("Transportation ID: 1"));
        assertTrue(display.contains("Origin: Origin"));
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
    public void testRemoveExistingDriver() throws SQLException {
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
    public void testMakeTransportationWithMissingDriver() throws SQLException {
        TransportationController tc = new TransportationController();
        tc.makeShipmentArea(1, "Area1");

        Site origin = new Site("Origin", "1 Street", "111", "Alice", 1);
        Site destination = new Site("Dest", "2 Street", "222", "Bob", 1);

        tc.addSite(origin.getName(), origin.getAddress(), origin.getPhoneNumber(), origin.getContactPersonName(), origin.getShipmentAreaId());
        tc.addSite(destination.getName(), destination.getAddress(), destination.getPhoneNumber(), destination.getContactPersonName(), destination.getShipmentAreaId());

        tc.addTruck("TR1", "Volvo", 20, 100, LicenseType.C);

        LocalDate userDate = LocalDate.parse("2025-05-01");
        LocalTime departureTime = LocalTime.parse("09:00");

        List<ItemsDocument> docs = new ArrayList<>();
        List<Integer> shipmentAreas = List.of(1);

        String result = tc.makeTransportation(100, userDate, departureTime, "TR1", "DriverNotExists", docs, shipmentAreas, origin);
        System.out.println("Result: " + result);

        // השורה הזו תעבור רק אם תנאי הבדיקה מבוצע לפני בדיקות זמינות
        assertTrue(result.contains("Driver with name DriverNotExists was not found at this time."));
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
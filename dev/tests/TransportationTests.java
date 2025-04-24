package dev.tests;

import dev.domain_layer.*;
import dev.domain_layer.Driver.LicenseType;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

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
        List<Item> items = List.of(new Item(1, "Box", 2, 4), new Item(2, "Chair", 5, 2));
        ItemsDocument doc = new ItemsDocument(site, items);
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
        String result = uc.addUser("Tal", "123", User.Role.SystemManager);
        assertEquals("User already exists", result);
    }

    @Test
    public void testTransportationDisplayIncludesBasicFields() {
        Site origin = new Site("Origin", "Addr", "123", "Rep", 1);
        Transportation t = new Transportation(1, "2025-05-01", "09:00", "ABC123", 10, new ArrayList<>(), List.of(1), origin);
        String display = t.display();
        assertTrue(display.contains("Transportation ID: 1"));
        assertTrue(display.contains("Origin: Origin"));
    }

    @Test
    public void testAddItemsAndRemoveItemsLogic() {
        TransportationController tc = new TransportationController();
        Site origin = new Site("O", "A", "1", "C", 1);
        tc.makeShipmentArea(1, "Area1");
        tc.addTruck("TR1", "ModelX", 1000, 2000, LicenseType.B);
        tc.addDriver(1, "D1", LicenseType.B);
        tc.addSite("O", "A", "1", "C", 1);

        List<Item> items = new ArrayList<>();
        items.add(new Item(1, "Box", 5, 2));
        ItemsDocument doc = new ItemsDocument(origin, items);
        List<ItemsDocument> docs = new ArrayList<>();
        docs.add(doc);

        tc.makeTransportation(1, "2025-05-01", "09:00", "TR1", 1, docs, List.of(1), origin);
        assertTrue(tc.addItems(1, docs).contains("Items added"));
        assertTrue(tc.removeItems(1, docs).contains("Items removed"));
    }



    @Test
    public void testChangeSucceededAndCheckStatus() {
        TransportationController tc = new TransportationController();
        Site origin = new Site("O", "A", "1", "C", 1);
        tc.makeShipmentArea(1, "Area1");
        tc.addTruck("TR1", "ModelX", 1000, 2000, LicenseType.C);
        tc.addDriver(1, "D1", LicenseType.C);
        tc.addSite("O", "A", "1", "C", 1);

        // הוספת פריט כדי שהמסמך לא יהיה ריק
        List<Item> items = List.of(new Item(1, "Box", 5, 2));
        ItemsDocument doc = new ItemsDocument(origin, items);
        List<ItemsDocument> docs = List.of(doc);

        String result = tc.makeTransportation(1, "2025-05-01", "09:00", "TR1", 1, docs, List.of(1), origin);
        System.out.println(result);

        Transportation t = tc.findTransportationById(1);
        assertNotNull(t, "Transportation not created!");

        tc.changeSucceeded(1, true);
        assertTrue(t.isSucceeded());
    }

    @Test
    public void testCheckSiteExistsPositiveAndNegative() {
        TransportationController tc = new TransportationController();
        tc.makeShipmentArea(1, "Area1");
        tc.addSite("A", "Addr", "123", "C", 1);
        assertTrue(tc.checkSiteExists("A", List.of(1)));
        assertFalse(tc.checkSiteExists("B", List.of(1)));
    }
}




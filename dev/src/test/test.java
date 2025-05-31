package test;

import Domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AgreementTest {
    private Agreement agreement;
    private AgreementItem milkItem;
    private AgreementItem breadItem;

    @BeforeEach
    void setUp() {
        milkItem = new AgreementItem("milk01", "CAT123", 5.0f, 10.0f, 10, "Milk");
        breadItem = new AgreementItem("bread01", "CAT124", 3.0f, 5.0f, 5, "Bread");

        Map<AgreementItem, Double> items = new HashMap<>();
        items.put(milkItem, 5.0);
        items.put(breadItem, 3.0);

        agreement = new Agreement("AG001", "SUP01",
                List.of(DeliveryWeekday.SUNDAY, DeliveryWeekday.THURSDAY),
                true,
                items);
    }

    @Test
    void testGetAgreementId() {
        assertEquals("AG001", agreement.getAgreementId());
    }

    @Test
    void testIsDeliveryAvailableOn_DayIncluded() {
        assertTrue(agreement.isDeliveryAvailableOn(DeliveryWeekday.SUNDAY));
    }

    @Test
    void testIsDeliveryAvailableOn_DayNotIncluded() {
        assertFalse(agreement.isDeliveryAvailableOn(DeliveryWeekday.MONDAY));
    }

    @Test
    void testIsPickupOnly() {
        assertFalse(agreement.isPickupOnly());
    }

    @Test
    void testGetDeliveryDays() {
        List<DeliveryWeekday> days = agreement.getDeliveryDays();
        assertTrue(days.contains(DeliveryWeekday.THURSDAY));
    }

    @Test
    void testAddItem() {
        AgreementItem cheeseItem = new AgreementItem("cheese01", "CAT125", 7.0f, 8.0f, 4, "Cheese");
        agreement.addItem(cheeseItem, 7.0);
        assertTrue(agreement.getItems().containsKey(cheeseItem));
    }

    @Test
    void testRemoveItem() {
        agreement.removeItem(breadItem);
        assertFalse(agreement.getItems().containsKey(breadItem));
    }

    @Test
    void testGetSupportsDelivery() {
        assertTrue(agreement.getSupportsDelivery());
    }

    @Test
    void testGetItems_NotEmpty() {
        assertEquals(2, agreement.getItems().size());
    }

    @Test
    void testToString_NotNull() {
        String output = agreement.toString();
        assertNotNull(output);
        assertTrue(output.contains("Supplier ID: SUP01"));
    }
}

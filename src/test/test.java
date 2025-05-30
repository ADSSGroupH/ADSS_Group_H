//package test;
//import Domain.*;
//import org.junit.jupiter.api.*;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class test {
//
//    @Nested
//    class SupplierTests {
//        private Supplier supplier;
//
//        @BeforeEach
//        void setUp() {
//            // Setting up a new supplier before each test
//            supplier = new Supplier("s1", "Supplier One", "123 Main St", PaymentMethod.CASH, "tel aviv");
//        }
//
//        @Test
//        void testSupplierCreation() {
//            // Verifying supplier creation with correct details
//            assertEquals("s1", supplier.getSupplierId());
//            assertEquals("Supplier One", supplier.getName());
//            assertEquals("tel aviv", supplier.getDeliveryAddress());
//            assertEquals("123 Main St", supplier.getBankAccount());
//        }
//
//        @Test
//        void testAddAndRemoveContactPerson() {
//            // Adding and removing a contact person
//            ContactPerson contact = new ContactPerson("John Doe", "050-1234567", "john@example.com");
//            supplier.addContactPerson(contact);
//            assertEquals(1, supplier.getContactPeople().size());
//            supplier.removeContactPerson(contact);
//            assertTrue(supplier.getContactPeople().isEmpty());
//        }
//    }
//
//    @Nested
//    class ContactPersonTests {
//        private ContactPerson contact;
//
//        @BeforeEach
//        void setUp() {
//            // Setting up a new contact person before each test
//            contact = new ContactPerson("Alice", "052-9876543", "alice@example.com");
//        }
//
//        @Test
//        void testContactPersonCreation() {
//            // Verifying contact person creation
//            assertEquals("Alice", contact.getName());
//            assertEquals("052-9876543", contact.getPhoneNumber());
//        }
//
//        @Test
//        void testUpdateContactPerson() {
//            // Updating contact person details
//            contact.setName("Alice Smith");
//            contact.setPhoneNumber("053-1111111");
//            contact.setEmail("alice.smith@example.com");
//
//            assertEquals("Alice Smith", contact.getName());
//            assertEquals("053-1111111", contact.getPhoneNumber());
//            assertEquals("alice.smith@example.com", contact.getEmail());
//        }
//    }
//
//    @Nested
//    class AgreementTests {
//        private Agreement agreement;
//
//        @BeforeEach
//        void setUp() {
//            // Setting up a new agreement before each test
//            agreement = new Agreement("a1", "s1", List.of(DeliveryWeekday.MONDAY), true, new HashMap<>());
//        }
//
//        @Test
//        void testAgreementCreation() {
//            // Verifying agreement creation
//            assertEquals("a1", agreement.getAgreementId());
//            assertEquals("s1", agreement.getSupplierId());
//            assertTrue(agreement.getSupportsDelivery());
//        }
//
//        @Test
//        void testAddAndRemoveItem() {
//            // Adding and removing an item in an agreement
//            AgreementItem item = new AgreementItem("item1", "cat1", 10f, 5f, 10, "Milk");
//            agreement.addItem(item, 10.0);
//            assertEquals(1, agreement.getItems().size());
//
//            agreement.removeItem(item);
//            assertTrue(agreement.getItems().isEmpty());
//        }
//
//        @Test
//        void testDeliveryAvailable() {
//            // Checking if delivery is available on a specific day
//            assertTrue(agreement.isDeliveryAvailableOn(DeliveryWeekday.MONDAY));
//            assertFalse(agreement.isDeliveryAvailableOn(DeliveryWeekday.TUESDAY));
//        }
//
//        @Test
//        void testPickupOnly() {
//            // Checking if the agreement is pickup only
//            Agreement pickupAgreement = new Agreement("a2", "s2", List.of(), false, Map.of());
//            assertTrue(pickupAgreement.isPickupOnly());
//        }
//    }
//
//    @Nested
//    class AgreementItemTests {
//        private AgreementItem item;
//
//        @BeforeEach
//        void setUp() {
//            // Setting up a new agreement item before each test
//            item = new AgreementItem("item1", "cat1", 10f, 5f, 10, "Milk");
//        }
//
//        @Test
//        void testAgreementItemCreation() {
//            // Verifying agreement item creation
//            assertEquals("item1", item.getItemId());
//            assertEquals("Milk", item.getName());
//            assertEquals(10f, item.getPrice(1));
//        }
//
//        @Test
//        void testUpdateAgreementItem() {
//            // Updating discount and quantity for an agreement item
//            item.setDiscount(15f);
//            item.setQuantity(20);
//            assertEquals(15f, item.getDiscount());
//            assertEquals(20, item.getquantityForDiscount());
//        }
//    }
//}

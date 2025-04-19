import java.util.*;

public class SupplierService {
    private Map<String, Supplier> suppliers = new HashMap<>();
    private Scanner scanner;
    private ItemService item_service;

    public SupplierService(Scanner scanner, ItemService itemService) {
        this.scanner = scanner;
        this.item_service = itemService;
    }


    public boolean supplierExists(String id) {
        return suppliers.containsKey(id);
    }

    public void addSupplier(Supplier s) {
        suppliers.put(s.getSupplierId(), s);
    }

    public Supplier createSupplier(String id, String name, String address, String bank, PaymentMethod method, List<ContactPerson> contacts) {
        Supplier s = new Supplier(id, name, bank, method.toString(), address);
        for (ContactPerson c : contacts) {
            s.addContactPerson(c);
        }
        return s;
    }

    public Supplier getSupplierById(String id) {
        return suppliers.get(id);
    }

    public List<Supplier> getAllSuppliers() {
        return new ArrayList<>(suppliers.values());
    }

    public void editSupplierDetails(Supplier s, String name, String address, String bank, PaymentMethod method) {
        s.setName(name);
        s.setDeliveryAddress(address);
        s.setBankAccount(bank);
        s.setPaymentMethod(method);
    }

    public void addContactPerson(Supplier s, ContactPerson c) {
        s.addContactPerson(c);
    }

    public boolean removeContactPerson(Supplier s, int index) {
        List<ContactPerson> contacts = s.getContactPeople();
        if (index >= 0 && index < contacts.size()) {
            s.removeContactPerson(contacts.get(index));
            return true;
        }
        return false;
    }

    public void addAgreementToSupplier() {
        System.out.print("Supplier Id: ");
        String supplier_id = scanner.nextLine();
        Supplier s = suppliers.get(supplier_id);
        if (s == null) {
            System.out.println("Supplier not found.");
            return;
        }

        System.out.print("Agreement ID: ");
        String agreementId = scanner.nextLine();
        System.out.print("Start Date (yyyy-mm-dd): ");
        String start = scanner.nextLine();
        System.out.print("End Date (yyyy-mm-dd): ");
        String end = scanner.nextLine();
        System.out.print("Supports delivery? (true/false): ");
        boolean supportsDelivery = Boolean.parseBoolean(scanner.nextLine());

        List<DeliveryWeekday> days = new ArrayList<>();
        if (supportsDelivery) {
            System.out.println("Select delivery days (y/n):");
            for (DeliveryWeekday day : DeliveryWeekday.values()) {
                System.out.print(day.name().toLowerCase() + ": ");
                String input = scanner.nextLine().trim().toLowerCase();
                if (input.equals("y") || input.equals("yes")) {
                    days.add(day);
                }
            }
        }


        Map<AgreementItem, Double> items = new HashMap<>();
        int itemCount = 1;
        while (true) {
            System.out.print("Would you like to add item " + itemCount + "? (yes/no): ");
            String answer = scanner.nextLine().trim().toLowerCase();
            if (answer.equals("no")) break;
            if (!answer.equals("yes")) continue;

            System.out.print("Supermarket Item ID: ");
            String itemId = scanner.nextLine();

            System.out.print("Catalog Number (supplier): ");
            String catalog = scanner.nextLine();
            System.out.print("Price: ");
            float price = Float.parseFloat(scanner.nextLine());
            System.out.print("Discount (0-1): ");
            float discount = Float.parseFloat(scanner.nextLine());
            System.out.print("Min Quantity for Discount: ");
            int quantity = Integer.parseInt(scanner.nextLine());

            AgreementItem ai = new AgreementItem(itemId, catalog, price, discount, quantity);
            items.put(ai, (double) price);

            // check if item exists in system
            if (item_service.getItemById(itemId) == null) {
                System.out.print("Item name (for new item): ");
                String itemName = scanner.nextLine();
                item_service.addItem(itemId, itemName);
            }

            // link supplier to item
            Item item = item_service.getItemById(itemId);
            item.getSuppliers().put(s, catalog);

            itemCount++;
        }

        s.addAgreement(agreementId, days, supportsDelivery, start, end, items);
        System.out.println("Agreement added to supplier " + s.getName());
    }

}


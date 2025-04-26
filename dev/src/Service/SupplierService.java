package Service;

import Domain.ContactPerson;
import Domain.PaymentMethod;
import Domain.Supplier;

import java.util.*;

public class SupplierService {
    private Map<String, Supplier> suppliers = new HashMap<>();

    // Check if a supplier exists by ID
    public boolean supplierExists(String id) {
        return suppliers.containsKey(id);
    }

    // Add a new supplier to the system
    public void addSupplier(Supplier s) {
        suppliers.put(s.getSupplierId(), s);
    }

    // Create a new supplier object
    public Supplier createSupplier(String id, String name, String address, String bank, PaymentMethod method, List<ContactPerson> contacts) {
        Supplier s = new Supplier(id, name, bank, method, address);
        for (ContactPerson c : contacts) {
            s.addContactPerson(c);
        }
        return s;
    }

    // Get a supplier by ID
    public Supplier getSupplierById(String id) {
        return suppliers.get(id);
    }

    // Get a list of all suppliers
    public List<Supplier> getAllSuppliers() {
        return new ArrayList<>(suppliers.values());
    }

    // Edit supplier details (name, address, bank account, payment method)
    public void editSupplierDetails(Supplier s, String name, String address, String bank, PaymentMethod method) {
        s.setName(name);
        s.setDeliveryAddress(address);
        s.setBankAccount(bank);
        s.setPaymentMethod(method);
    }

    // Add a new contact person to a supplier
    public void addContactPerson(Supplier s, ContactPerson c) {
        s.addContactPerson(c);
    }

    // Create a new contact person
    public ContactPerson createContactPerson(String name, String phone, String email) {
        return new ContactPerson(name, phone, email);
    }

    // Remove a contact person by index from a supplier
    public boolean removeContactPerson(Supplier s, int index) {
        List<ContactPerson> contacts = s.getContactPeople();
        if (index >= 0 && index < contacts.size()) {
            s.removeContactPerson(contacts.get(index));
            return true;
        }
        return false;
    }
}

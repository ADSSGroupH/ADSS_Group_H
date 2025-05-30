package Domain;

import java.util.*;

/**
 * Repository for managing Supplier objects in memory.
 * Singleton pattern ensures a single shared instance.
 */
public class SupplierRepository {

    private static SupplierRepository instance = null;
    private final Map<String, Supplier> suppliers = new HashMap<>();

    private SupplierRepository() {}

    public static SupplierRepository getInstance() {
        if (instance == null) {
            instance = new SupplierRepository();
        }
        return instance;
    }

    public boolean search(String id) {
        return suppliers.containsKey(id);
    }

    public void add(Supplier supplier) {
        suppliers.put(supplier.getSupplierId(), supplier);
    }

    public boolean remove(String id) {
        return suppliers.remove(id) != null;
    }

    public Supplier get(String id) {
        return suppliers.get(id);
    }

    public Map<String, Supplier> getAll() {
        return suppliers;
    }

    public Supplier createSupplier(String id, String name, String address, String bank, PaymentMethod method, List<ContactPerson> contacts) {
        Supplier s = new Supplier(id, name, bank, method, address);
        for (ContactPerson c : contacts) {
            s.addContactPerson(c);
        }
        return s;
    }
}

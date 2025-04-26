package controller;

import Domain.*;
import Service.*;

import java.util.*;

public class SystemController {
    private final SupplierService supplierService;
    private final ItemService itemService;
    private final OrderService orderService;
    private final AgreementService agreementService;

    // Constructor - initializing all services
    public SystemController() {
        this.supplierService = new SupplierService();
        this.itemService = new ItemService();
        this.orderService = new OrderService();
        this.agreementService = new AgreementService();
    }

    // Get the base price of an agreement item
    public float getBasePrice(AgreementItem item) {
        return item.get_basic_price();
    }

    // ===== SUPPLIER OPERATIONS =====

    // Add a new supplier to the system
    public boolean addSupplier(String id, String name, String address, String bank, PaymentMethod method, List<ContactPerson> contacts) {
        if (supplierService.supplierExists(id)) return false;
        Supplier supplier = supplierService.createSupplier(id, name, address, bank, method, contacts);
        supplierService.addSupplier(supplier);
        return true;
    }

    // Create a new contact person
    public ContactPerson createContactPerson(String name, String phone, String email){
        return supplierService.createContactPerson(name, phone, email);
    }

    // Get supplier by ID
    public Supplier getSupplierById(String supplierId) {
        return supplierService.getSupplierById(supplierId);
    }

    // Get a list of all suppliers
    public List<Supplier> getAllSuppliers() {
        return supplierService.getAllSuppliers();
    }

    // Check if a supplier exists
    public boolean supplierExist(String id){
        return supplierService.supplierExists(id);
    }

    // Add an agreement to a supplier
    public boolean addAgreementToSupplier(String supplierId, String agreementId,
                                          boolean supportsDelivery, List<DeliveryWeekday> days,
                                          Map<AgreementItem, Double> items) {
        Supplier supplier = supplierService.getSupplierById(supplierId);
        if (supplier == null) return false;

        Agreement agreement = agreementService.createAgreement(agreementId, supplierId, days, supportsDelivery, items);
        supplier.addAgreement(agreement);

        for (AgreementItem ai : items.keySet()) {
            if (itemService.getItemById(ai.getItemId()) == null) {
                itemService.addItem(ai.getItemId(), ai.getName());
            }
            itemService.getItemById(ai.getItemId()).getSuppliers().put(supplier, ai.getCatalogNumber());
        }

        return true;
    }

    // Update supplier's name
    public void updateSupplierName(Supplier supplier, String newName) {
        supplier.setName(newName);
    }

    // Update supplier's delivery address
    public void updateSupplierAddress(Supplier supplier, String newAddress) {
        supplier.setDeliveryAddress(newAddress);
    }

    // Update supplier's bank account
    public void updateSupplierBankAccount(Supplier supplier, String newBankAccount) {
        supplier.setBankAccount(newBankAccount);
    }

    // Update supplier's payment method
    public void updateSupplierPaymentMethod(Supplier supplier, PaymentMethod method) {
        supplier.setPaymentMethod(method);
    }

    // ===== ITEM OPERATIONS =====

    // Add a new item to the system
    public boolean addItem(String id, String name) {
        if (itemService.getItemById(id) != null) return false;
        itemService.addItem(id, name);
        return true;
    }

    // Create a new order
    public Order CreateOrder(String orderId, Supplier supplier, Map<String, Integer> items,
                             String orderDate, int totalPrice, OrderStatus status){
        return orderService.CreateOrder(orderId, supplier, items, orderDate, totalPrice, status);
    }

    // Get item by ID
    public Item getItemById(String id) {
        return itemService.getItemById(id);
    }

    // Get a list of all items
    public Collection<Item> getAllItems() {
        return itemService.getAllItems();
    }

    // Get suppliers that provide a specific item (with supplied list)
    public Map<Supplier, String> getSuppliersForItem(String itemId, List<Supplier> suppliers) {
        return itemService.getSuppliersForItem(itemId, suppliers);
    }

    // Ensure item exists or add it if missing
    public void ensureItemExists(String itemId, String itemName) {
        itemService.ensureItemExists(itemId, itemName);
    }

    // ===== ORDER OPERATIONS =====

    // Place a new order
    public void placeOrder(Order order) {
        orderService.placeOrder(order);
    }

    // Get all orders in the system
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    // Get order by ID
    public Order getOrderById(String id) {
        return orderService.getOrderById(id);
    }

    // Update order status (only if current status is PENDING)
    public boolean updateOrderStatus(Order order, OrderStatus newStatus) {
        OrderStatus current = order.getStatus();
        if (current == OrderStatus.PENDING) {
            orderService.updateOrderStatus(order, newStatus);
            return true;
        }
        return false;
    }

    // Create a new agreement item
    public AgreementItem createAgreementItem(String itemId, String catalog, float price, float discount, int quantity, String name) {
        return agreementService.createAgreementItem(itemId, catalog, price, discount, quantity, name);
    }

    // Get suppliers for an item across all suppliers
    public Map<Supplier, String> getSuppliersForItem(String itemId) {
        Map<Supplier, String> result = new HashMap<>();
        for (Supplier supplier : supplierService.getAllSuppliers()) {
            for (Agreement agreement : supplier.getAgreements()) {
                for (AgreementItem ai : agreement.getItems().keySet()) {
                    if (ai.getItemId().equals(itemId)) {
                        result.put(supplier, ai.getCatalogNumber());
                    }
                }
            }
        }
        return result;
    }

    // Update the ID of an item
    public boolean updateItemId(Item item, String newId) {
        Map<String, Item> itemMap = itemService.getItemsMap();

        if (itemMap.containsKey(newId)) {
            System.out.println("Error: ID already exists.");
            return false;
        }

        itemMap.remove(item.getItemId());
        item.setItemId(newId);
        itemMap.put(newId, item);

        return true;
    }
}

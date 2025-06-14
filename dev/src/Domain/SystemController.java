package Domain;


import dao.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;

public class SystemController {

    private final Scanner scanner = new Scanner(System.in);
    private static SystemController instance = null;
    private final IRepositoryManager repositories;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static final Scanner scan = new Scanner(System.in);

    private final SupplierDao supplierDao = new SupplierDao();
    private final ProductDao productDao = new ProductDao();
    private final ItemDao itemDao = new ItemDao();
    private final OrderDao orderDao = new OrderDao();


    // בנאי פרטי - מונע יצירה מבחוץ
    private SystemController(IRepositoryManager repositories) {
        this.repositories = repositories;
    }

    // גישה יחידה למופע של המחלקה
    public static SystemController getInstance(IRepositoryManager repositories) {
        if (instance == null) {
            instance = new SystemController(repositories);
        }
        return instance;
    }

    public float getBasePrice(AgreementItem item) {
        return item.get_basic_price();
    }

    public void initializeSampleData() {

        ContactPerson contact1 = new ContactPerson("Alice Levi", "050-1234567", "alice@example.com");
        ContactPerson contact2 = new ContactPerson("Boaz Cohen", "052-9876543", "boaz@example.com");
        ContactPerson contact3 = new ContactPerson("Carmit Azulai", "053-5554321", "carmit@example.com");

// הוספת אנשי קשר לספקים
        Supplier sup1 = repositories.getSupplierRepository().get("SUP1");
        Supplier sup2 = repositories.getSupplierRepository().get("SUP2");
        Supplier sup3 = repositories.getSupplierRepository().get("SUP3");

        sup1.addContactPerson(contact1);
        sup2.addContactPerson(contact2);
        sup3.addContactPerson(contact3);

        Map<AgreementItem, Double> items = new HashMap<>();
        AgreementItem ai = createAgreementItem("P1", "C1", 8, 20, 20, "Milk");
        items.put(ai,  8.0);
        AgreementItem ai2 = createAgreementItem("P2", "C22", 8, 20, 20, "Bread");
        items.put(ai2,  8.0);
        AgreementItem ai3 = createAgreementItem("P3", "C3", 8, 20, 20, "Shampoo");
        items.put(ai3,  8.0);


        List<DeliveryWeekday> days = new ArrayList<>();
        days.add(DeliveryWeekday.SUNDAY);
        days.add(DeliveryWeekday.TUESDAY);
        days.add(DeliveryWeekday.THURSDAY);

        addAgreementToSupplier("SUP1", "A1", true, days, items);

        Map<AgreementItem, Double> items1 = new HashMap<>();
        AgreementItem ai1 = createAgreementItem("P1", "C2", 6, 20, 20, "Milk");
        items1.put(ai1,  6.0);
        AgreementItem ai22 = createAgreementItem("P2", "C222", 10, 10, 20, "Bread");
        items1.put(ai22,  8.0);
        AgreementItem ai33 = createAgreementItem("P3", "C333", 20, 5, 20, "Shampoo");
        items1.put(ai33,  8.0);

        addAgreementToSupplier("SUP2", "A2", true, days, items1);
    }



    public int parseIntSafe(String s) {
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return -1;
        }
    }

    public String promptString(String prompt) {
        System.out.print(prompt);
        return scan.nextLine().trim();
    }

    public Set<Double> promptDoubleSet(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scan.nextLine().trim();
            if (line.isBlank()) {
                return Collections.emptySet();
            }
            String[] toks = line.split("\\s+");
            Set<Double> out = new HashSet<>();
            boolean allValid = true;
            for (String t : toks) {
                try {
                    out.add(Double.parseDouble(t));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid size '" + t + "'. Please enter decimal numbers.");
                    allValid = false;
                    break;
                }
            }
            if (allValid) {
                return out;
            }
        }
    }

    public void addProduct() {
        System.out.println("=== Add product ===");
        System.out.print("Product ID: ");
        String pid = scan.nextLine().trim();
        System.out.print("Name: ");
        String name = scan.nextLine().trim();

        Product product = repositories.getProductRepository().getProductByName(name);
        if (product == null) {
            System.out.print("Cost Price: ");
            double costPrice = Double.parseDouble(scan.nextLine().trim());
            System.out.print("Sale Price: ");
            double salePrice = Double.parseDouble(scan.nextLine().trim());
            System.out.print("Manufacturer: ");
            String manufacturer = scan.nextLine().trim();
            System.out.print("Min Quantity: ");
            int minQuantity = Integer.parseInt(scan.nextLine().trim());

            product = new Product(pid, name, costPrice, salePrice, manufacturer,
                    new ArrayList<>(), new ArrayList<>(), minQuantity);
            repositories.getProductRepository().addProduct(product);
            boolean ok = productDao.addProduct(product);
            if (!ok) {
                System.err.println("⚠️ Failed to write new product to Products.sql");
            } else {
                System.out.println("Product is successfully added");
            }
        }
    }

    // מקום הגדרת ה‑ProductDao

    // קריאה אינטראקטיבית להוספת מוצר (עם persist)
    public boolean addProduct(String pid, String name, double costPrice, double salePrice, String manu, int minQty) {
        // יצירת אובייקט והוספה לזיכרון
        Product p = repositories.getProductRepository().createProduct(pid, name, costPrice, salePrice, manu, minQty);
        repositories.getProductRepository().addProduct(p);
        // כתיבה ל‑SQL
        boolean ok = productDao.addProduct(p);
        if (!ok) System.err.println("⚠️ Failed to write new product to Products.sql");
        return ok;
    }

    // ליבת העדכון שמקבלת מוצר קיים לאחר שינוי שדות
    public boolean persistProductChanges(Product p) {
        boolean ok = productDao.updateProduct(p);
        if (!ok) System.err.println("⚠️ Failed to update Products.sql for ID " + p.getPid());
        return ok;
    }

    // שיטות עדכון ספציפיות שמשתמשות ב‑persistProductChanges
    public boolean updateProductName(Product p, String newName) {
        p.setName(newName);
        return persistProductChanges(p);
    }

    public boolean updateProductSalePrice(Product p, double newPrice) {
        p.setSalePrice(newPrice);
        return persistProductChanges(p);
    }

    public boolean updateProductCostPrice(Product p, double newCost) {
        p.setCostPrice(newCost);
        return persistProductChanges(p);
    }

// … ושאר ה‑setters (manufacturer, minQuantity) באותה צורה …

    // מחיקת מוצר
    public boolean deleteProduct(String pid) {
        boolean removed = repositories.getProductRepository().removeProductById(pid);
        if (!removed) return false;
        boolean ok = productDao.deleteProduct(pid);
        if (!ok) System.err.println("⚠️ Failed to delete product from Products.sql");
        return ok;
    }


    public void removeProduct() {
        System.out.println("=== Remove product ===");
        System.out.print("Name: ");
        String name = scan.nextLine().trim();
        boolean removed = repositories.getProductRepository().removeProductByName(name);
        System.out.println(removed ? "Product successfully removed" : "Product does not exist");
    }

    public void addItem() {
        try {
            System.out.println("=== Add item ===");

            // 1. קלט מזהה הפריט (Item ID) ושם הפריט
            System.out.print("Item ID: ");
            String iid = scan.nextLine().trim();

            System.out.print("Name: ");
            String iname = scan.nextLine().trim();

            // 2. וידוא שקיים מוצר מתאים
            Product product;
            while (true) {
                System.out.print("Product Name: ");
                String productName = scan.nextLine().trim();
                product = repositories.getProductRepository().getProductByName(productName);
                if (product != null) break;
                System.out.println("❌ Product \"" + productName + "\" not found. Please enter an existing product name.");
            }

            // 3. קלט פרטי Classification
            System.out.print("Classification ID: ");
            String classId = scan.nextLine().trim();
            System.out.print("Category: ");
            String category = scan.nextLine().trim();
            System.out.print("Subcategory: ");
            String subcategory = scan.nextLine().trim();
            System.out.print("Size: ");
            double size = Double.parseDouble(scan.nextLine().trim());
            Classification cls = new Classification(classId, category, subcategory, size);

            // 4. קלט מיקום
            System.out.print("Location (WareHouse=0, Store=1): ");
            int locChoice = parseIntSafe(scan.nextLine().trim());
            Location loc = (locChoice == 0) ? Location.WareHouse : Location.Store;

            // 5. קלט תאריך התפוגה
            Date exp;
            while (true) {
                System.out.print("Expiration Date (yyyy-MM-dd): ");
                String dateStr = scan.nextLine().trim();
                try {
                    exp = sdf.parse(dateStr);
                    break;
                } catch (ParseException pe) {
                    System.out.println("❌ Invalid date format. Please enter a date in yyyy-MM-dd format.");
                }
            }

            // 6. יצירת ה־Item והוספה לזיכרון
            Item item = new Item(iid, iname, loc, exp, cls, product);
            repositories.getItemRepository().addItem(item);

            // 7. **כתיבה ל‑Items.sql באמצעות DAO**
            boolean ok = itemDao.addItem(item);
            if (!ok) {
                System.err.println("⚠️ Failed to write new item to Items.sql");
            } else {
                System.out.println("✅ Item \"" + iname + "\" added successfully to Items.sql");
            }

            // 8. בדיקת מלאי מינימלי
            if (product.getStockQuantity() < product.getMinQuantity()) {
                new Alert(product, new Date()).printShortageMessage();
            }

        } catch (Exception e) {
            System.out.println("❌ Unexpected error while adding item: " + e.getMessage());
        }
    }


    public void addAgreementToSupplier() {
        System.out.print("Supplier ID: ");
        String supplierId = scanner.nextLine().trim();

        Supplier supplier = getSupplierById(supplierId);
        if (supplier == null) {
            System.out.println("❌ Supplier not found.");
            return;
        }

        System.out.print("Agreement ID: ");
        String agreementId = scanner.nextLine().trim();
        if (agreementId.isEmpty()) {
            System.out.println("❌ Agreement ID cannot be empty.");
            return;
        }

        boolean supportsDelivery = askYesNo("Supports delivery?");

        List<DeliveryWeekday> days = new ArrayList<>();
        if (supportsDelivery) {
            for (DeliveryWeekday day : DeliveryWeekday.values()) {
                if (askYesNo("Delivery on " + day.name().toLowerCase() + "?")) {
                    days.add(day);
                }
            }
        }

        Map<AgreementItem, Double> items = new HashMap<>();
        int itemCount = 1;

        while (askYesNo("Add product " + itemCount + " to agreement?")) {
            try {
                System.out.print("Product ID: ");
                String itemId = scanner.nextLine().trim();
                if (itemId.isEmpty()) {
                    System.out.println("❌ Product ID cannot be empty.");
                    continue;
                }

                System.out.print("Name: ");
                String name = scanner.nextLine().trim();
                if (name.isEmpty()) {
                    System.out.println("❌ Product name cannot be empty.");
                    continue;
                }
                double price=0;
                // בדיקה אם המוצר כבר קיים
                Product product = repositories.getProductRepository().getProductById(itemId);
                if (product == null) {
                    System.out.println("🔔 Product does not exist. Please enter the details to create it.");

                    System.out.print("Price: ");
                    double costPrice = Double.parseDouble(scanner.nextLine().trim());

                    System.out.print("Sale Price: ");
                    double salePrice = Double.parseDouble(scanner.nextLine().trim());

                    System.out.print("Manufacturer: ");
                    String manufacturer = scanner.nextLine().trim();

                    System.out.print("Min Quantity: ");
                    int minQuantity = Integer.parseInt(scanner.nextLine().trim());

                    product = new Product(itemId, name, costPrice, salePrice, manufacturer,
                            new ArrayList<>(), new ArrayList<>(), minQuantity);

                    repositories.getProductRepository().addProduct(product);
                    System.out.println("✅ Product added to system.");

                    price = costPrice;
                }else{
                    System.out.print("Price: ");
                     price = Float.parseFloat(scanner.nextLine().trim());
                    if (price < 0) {
                        System.out.println("❌ Price cannot be negative.");
                        continue;
                    }
                }

                System.out.print("Catalog Number: ");
                String catalog = scanner.nextLine().trim();
                if (catalog.isEmpty()) {
                    System.out.println("❌ Catalog number cannot be empty.");
                    continue;
                }

                System.out.print("Discount (0–100%): ");
                float discount = Float.parseFloat(scanner.nextLine().trim());
                if (discount < 0 || discount > 100) {
                    System.out.println("❌ Discount must be between 0 and 100.");
                    continue;
                }

                System.out.print("Min Quantity for Discount: ");
                int quantity = Integer.parseInt(scanner.nextLine().trim());
                if (quantity < 0) {
                    System.out.println("❌ Minimum quantity cannot be negative.");
                    continue;
                }

                AgreementItem ai = createAgreementItem(itemId, catalog, (float) price, discount, quantity, name);
                items.put(ai,  price);
                itemCount++;

            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid number. Please try again.");
            } catch (Exception e) {
                System.out.println("❌ Unexpected error: " + e.getMessage());
            }
        }

        boolean success = addAgreementToSupplier(supplierId, agreementId, supportsDelivery, days, items);
        if (success) {
            System.out.println("✅ Agreement added successfully.");
        } else {
            System.out.println("❌ Failed to add agreement. Please check the supplier and try again.");
        }
    }

    private boolean askYesNo(String prompt) {
        while (true) {
            System.out.print(prompt + " (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y") || input.equals("yes")) return true;
            if (input.equals("n") || input.equals("no")) return false;
            System.out.println("Please enter 'y' or 'n'.");
        }
    }

    public void removeItem() {
        System.out.println("=== Remove item ===");
        System.out.print("Item Name: ");
        String name = scan.nextLine().trim();

        // 1. מצא את ה‑Item לפי השם
        Item item = repositories.getItemRepository().getItemByName(name);
        if (item == null) {
            System.out.println("❌ Item does not exist");
            return;
        }
        String iid = item.getIid();

        // 2. הסר מהריפוזיטורי בזיכרון
        boolean memRemoved = repositories.getItemRepository().removeItemByName(name);
        if (!memRemoved) {
            System.out.println("❌ Failed to remove item from memory");
            return;
        }

        // 3. הסר גם מקובץ ה‑SQL דרך ה‑DAO
        boolean ok = itemDao.deleteItem(iid);
        if (ok) {
            System.out.println("Item \"" + name + "\" removed successfully");
        } else {
            System.err.println("Failed to delete item from Items.sql for ID " + iid);
        }
    }


    public void showProductsInStock() {
        System.out.println("=== Products in stock ===");
        repositories.getProductRepository().display();
        System.out.println("\n");
    }

    public void showItemDetails() {
        System.out.println("=== Item details ===");
        try {
            System.out.print("Item Name: ");
            String pname = scan.nextLine().trim();
            repositories.getItemRepository().displayItemDetails(pname);
        } catch (Exception e) {
            System.out.println("Error in item details: " + e.getMessage());
        }
    }

    public Product getProductById(String pid) {
        return repositories.getProductRepository().getProductById(pid);
    }

    /**
     * משנה את ה‑minQuantity של המוצר ומעדכן גם ב‑Products.sql
     */
    public boolean updateProductMinQuantity(Product p, int newMin) {
        p.setMinQuantity(newMin);
        return persistProductChanges(p);
    }


    public void updateMinQuantity() {
        System.out.println("=== Update Min Quantity ===");
        System.out.print("Enter Product ID: ");
        String pid = scan.nextLine().trim();
        Product p = repositories.getProductRepository().getAllProducts().stream()
                .filter(x -> x.getPid().equals(pid))
                .findFirst().orElse(null);
        if (p == null) {
            System.out.println("Product does not exist");
            return;
        }
        System.out.print("Current Min Quantity: " + p.getMinQuantity() + ". Enter new Min Quantity: ");
        try {
            int newMin = Integer.parseInt(scan.nextLine().trim());
            p.setMinQuantity(newMin);
            System.out.println("Min Quantity updated to " + p.getMinQuantity());
        } catch (Exception e) {
            System.out.println("Invalid number format");
        }
    }


    public boolean updateProductId(Product product, String newId) {
        Map<String, Product> productMap = new HashMap<>();
        for (Product i : repositories.getProductRepository().getAllProducts()) {
            productMap.put(i.getPid(), i);
        }

        if (productMap.containsKey(newId)) {
            System.out.println("Error: ID already exists.");
            return false;
        }

        productMap.remove(product.getPid());
        product.setPId(newId);
        productMap.put(newId, product);
        return true;
    }

    public boolean isValidPercentage(double perc) {
        if (perc < 0 || perc > 100) {
            System.out.println("Percentage must be between 0 and 100.");
            return false;
        }
        return true;
    }

    public boolean isValidId(String id) {
        if (id == null || id.trim().isEmpty()) {
            System.out.println("ID cannot be empty.");
            return false;
        }
        return true;
    }

    public void addSupplierDiscount() {
        System.out.println("=== Add Supplier Discount ===");
        try {
            System.out.print("Product Name: ");
            String name = scan.nextLine().trim();
            ProductRepository repo = repositories.getProductRepository();
            Product p = repositories.getProductRepository().getProductByName(name);
            if (p == null) {
                System.out.println("Product does not exist");
                return;
            }
            System.out.print("Discount ID: ");
            String disID = scan.nextLine().trim();
            System.out.print("Supplier Name: ");
            String supName = scan.nextLine().trim();
            System.out.print("Discount Percentage: ");
            double dp = Double.parseDouble(scan.nextLine().trim());

            if (!isValidPercentage(dp)) {
                return;
            }

            System.out.print("Start Date (yyyy-MM-dd): ");
            LocalDate sd = LocalDate.parse(scan.nextLine().trim());
            System.out.print("End Date   (yyyy-MM-dd): ");
            LocalDate ed = LocalDate.parse(scan.nextLine().trim());

            if (sd.isAfter(ed)) {
                System.out.println("Start Date must be on or before End Date");
                return;
            }
            SupplierDiscount discount = new SupplierDiscount(disID, supName, dp, sd, ed);
            boolean success = repo.addSupplierDiscountToProduct(name, discount);
            if (success)
                System.out.println("Supplier Discount successfully added");
            else
                System.out.println("Failed to add discount");

        } catch (Exception e) {
            System.out.println("Error in Update Supplier Discount: " + e.getMessage());
        }
    }

    public void removeSupplierDiscount() {
        System.out.println("=== Remove Supplier Discount ===");
        System.out.print("Product Name: ");
        String name = scan.nextLine().trim();

        ProductRepository repo = repositories.getProductRepository();
        Product p = repositories.getProductRepository().getProductByName(name);
        if (p == null) {
            System.out.println("Product does not exist");
            return;
        }

        System.out.print("Discount ID to remove: ");
        String disID = scan.nextLine().trim();

        boolean removed = repo.removeSupplierDiscountFromProduct(name, disID);
        System.out.println(removed
                ? "Supplier Discount removed successfully"
                : "Discount ID not found");
    }

    public void addPromotion() {
        System.out.println("=== Add Promotion ===");
        System.out.print("Product Name: ");
        String name = scan.nextLine().trim();
        Product p = repositories.getProductRepository().getProductByName(name);
        if (p == null) {
            System.out.println("Product does not exist");
            return;
        }
        System.out.print("Promotion ID: ");
        String proID = scan.nextLine().trim();
        if (!isValidId(proID)) {
            return;
        }
        System.out.print("Discount Percentage: ");
        double dp = Double.parseDouble(scan.nextLine().trim());

        if (!isValidPercentage(dp)) {
            return;
        }

        System.out.print("Start Date (yyyy-MM-dd): ");
        LocalDate sd = LocalDate.parse(scan.nextLine().trim());
        System.out.print("End Date   (yyyy-MM-dd): ");
        LocalDate ed = LocalDate.parse(scan.nextLine().trim());

        if (sd.isAfter(ed)) {
            System.out.println("Start Date must be on or before End Date");
            return;
        }
        p.addPromotion(new Promotion(proID, dp, sd, ed, p, null));
        System.out.println("Promotion successfully added");
    }

    public void removePromotion() {
        System.out.println("=== Remove Promotion ===");
        System.out.print("Product Name: ");
        String name = scan.nextLine().trim();
        Product p = repositories.getProductRepository().getProductByName(name);
        if (p == null) {
            System.out.println("Product does not exist");
            return;
        }
        System.out.print("Promotion ID to remove: ");
        String proID = scan.nextLine().trim();
        boolean removed = p.getPromotions().removeIf(pr -> pr.getProID().equals(proID));
        System.out.println(removed ? "Promotion removed" : "Promotion not found");
    }

    public void generateCategoryReport() {
        System.out.println("=== Inventory report by category/subcategory/size ===");
        // Prompt for categories
        String lineCats = promptString("Enter categories (space-separated, or blank for all): ");
        Set<String> cats = new HashSet<>();
        if (!lineCats.isBlank()) {
            cats.addAll(Arrays.asList(lineCats.split("\\s+")));
        }

        // Prompt for subcategories
        String lineSubcats = promptString("Enter subcategories (space-separated, or blank for all): ");
        Set<String> subCats = new HashSet<>();
        if (!lineSubcats.isBlank()) {
            subCats.addAll(Arrays.asList(lineSubcats.split("\\s+")));
        }

        // Prompt for sizes
        Set<Double> sizes = promptDoubleSet("Enter sizes (space-separated, or blank for all): ");

        // Filter products
        Set<Product> filtered = new LinkedHashSet<>();
        for (Item it : repositories.getItemRepository().getAllItems()) {
            Classification cls = it.getClassification();
            String cat = cls.getCategory();
            String sub = cls.getSubcategory();
            double sz = cls.getsize();

            if ((!cats.isEmpty() && cats.contains(cat)) ||
                    (!subCats.isEmpty() && subCats.contains(sub)) ||
                    (!sizes.isEmpty() && sizes.contains(sz))) {
                filtered.add(it.getProduct());
            }
        }

        new ReportsView(
                new StockReport(UUID.randomUUID().toString(), new Date(), new ArrayList<>(filtered))
        ).display();
    }

    public void markDefective() {
        System.out.println("=== Mark item defective ===");
        String name = promptString("Item Name: ");
        boolean ok = repositories.getItemRepository().markItemDefective(name);
        System.out.println(ok ? "Item marked defective" : "Item not found");
    }

    public void markExpired() {
        System.out.println("=== Mark item expired ===");
        String name = promptString("Item Name: ");
        Item item = repositories.getItemRepository().getItemByName(name);
        if (item == null) {
            System.out.println("Item not found");
            return;
        }
        long millisInDay = 24L * 60 * 60 * 1000;
        item.setExpirationDate(new Date(System.currentTimeMillis() - millisInDay));
        System.out.println("Item marked as expired.");
    }

    public void generateDefectiveReport() {
        Report rep = new DefectReport(
                UUID.randomUUID().toString(), new Date(),
                repositories.getItemRepository().getAllItems());
        new ReportsView(rep).display();
    }

    public void generateExpiredReport() {
        System.out.println("=== Expired items report ===");
        Date now = new Date();
        List<Item> expired = new ArrayList<>();
        for (Item it : repositories.getItemRepository().getAllItems()) {
            if (it.getExpirationDate().before(now)) {
                expired.add(it);
            }
        }
        Report rep = new ExpiredReport(UUID.randomUUID().toString(), now, expired);
        new ReportsView(rep).display();
    }

    public void showProductPrice() {
        System.out.println("=== Show product price ===");
        System.out.print("Enter Product ID: ");
        String pid = scan.nextLine().trim();
        Product found = repositories.getProductRepository().getAllProducts().stream()
                .filter(x -> x.getPid().equals(pid))
                .findFirst().orElse(null);
        if (found == null) {
            System.out.println("Product does not exist");
            return;
        }
        System.out.println("Name: " + found.getName() + " | Price: " + found.getSalePrice());
    }

    public void updateProductPrice() {
        System.out.println("=== Update product price ===");
        System.out.print("Enter Product ID: ");
        String pid = scan.nextLine().trim();
        Product found = repositories.getProductRepository().getAllProducts().stream()
                .filter(x -> x.getPid().equals(pid))
                .findFirst().orElse(null);
        if (found == null) {
            System.out.println("Product does not exist");
            return;
        }
        System.out.print("Current: " + found.getSalePrice() + ". Enter new Price: ");
        try {
            double np = Double.parseDouble(scan.nextLine().trim());
            found.setSalePrice(np);
            System.out.println("Updated to " + found.getSalePrice());
        } catch (Exception e) {
            System.out.println("Invalid format");
        }
    }

// ב־SystemController, אחרי הפניות לפוליסטרטים
    /**
     * קריאה אינטראקטיבית להוספת ספק – תמיד כותבים גם לקובץ
     */
    public boolean addSupplier(String id, String name, String address, String bank, PaymentMethod method, List<ContactPerson> contacts) {
        return addSupplier(id, name, address, bank, method, contacts, true);
    }

    /**
     * הליבה שמטפלת בהוספה למאגר הזיכרוני ובכתיבה (או לא) ל-SQL
     */
    public boolean addSupplier(String id, String name, String address, String bank, PaymentMethod method, List<ContactPerson> contacts, boolean persist) {
        // 1. בדיקה שאין כפילות
        if (repositories.getSupplierRepository().search(id)) {
            return false;
        }
        // 2. יצירת האובייקט והוספה לזיכרון
        Supplier supplier = repositories.getSupplierRepository()
                .createSupplier(id, name, address, bank, method, contacts);
        repositories.getSupplierRepository().add(supplier);

        // 3. כתיבה לקובץ אם צריך
        if (persist) {
            boolean ok = supplierDao.addSupplier(supplier);
            if (!ok) System.err.println("⚠️ שגיאה בכתיבה ל־Suppliers.sql");
            return ok;
        }
        return true;
    }



    public void addPeriodicOrder(Scanner scanner) {
        // moved to repository context - implement as needed
    }

    public void createBestPriceOrders(String orderDate, Map<String, Integer> itemQuantities) {
        Map<String, Supplier> bestSuppliers = new HashMap<>();
        Map<String, Double> bestPrices = new HashMap<>();

        for (String itemId : itemQuantities.keySet()) {
            int quantity = itemQuantities.get(itemId);
            double minPrice = Double.MAX_VALUE;
            Supplier bestSupplier = null;

            Map<Supplier, String> suppliersForItem = getSuppliersForItem(itemId);

            for (Supplier supplier : suppliersForItem.keySet()) {
                for (Agreement agreement : supplier.getAgreements()) {
                    for (Map.Entry<AgreementItem, Double> entry : agreement.getItems().entrySet()) {
                        AgreementItem item = entry.getKey();
                        if (item.getItemId().equals(itemId)) {
                            double unitPrice = item.getPrice(quantity);
                            if (unitPrice < minPrice) {
                                minPrice = unitPrice;
                                bestSupplier = supplier;
                            }
                        }
                    }
                }
            }

            if (bestSupplier != null) {
                bestSuppliers.put(itemId, bestSupplier);
                bestPrices.put(itemId, minPrice);
            } else {
                System.out.println("No supplier found for item: " + itemId);
            }
        }

        Map<Supplier, Map<String, Integer>> supplierOrders = new HashMap<>();
        for (String itemId : bestSuppliers.keySet()) {
            Supplier supplier = bestSuppliers.get(itemId);
            int quantity = itemQuantities.get(itemId);
            supplierOrders.putIfAbsent(supplier, new HashMap<>());
            supplierOrders.get(supplier).put(itemId, quantity);
        }

        for (Map.Entry<Supplier, Map<String, Integer>> entry : supplierOrders.entrySet()) {
            Supplier supplier = entry.getKey();
            Map<String, Integer> items = entry.getValue();

            int totalPrice = 0;
            for (Map.Entry<String, Integer> itemEntry : items.entrySet()) {
                String itemId = itemEntry.getKey();
                int quantity = itemEntry.getValue();
                double unitPrice = bestPrices.get(itemId);
                totalPrice += unitPrice * quantity;
            }

            Order order = repositories.getOrderRepository().createOrder(
                    UUID.randomUUID().toString(), supplier, items, orderDate, totalPrice, OrderStatus.PENDING);
            repositories.getOrderRepository().add(order);
        }
    }


    public ContactPerson createContactPerson(String name, String phone, String email) {
        return new ContactPerson(name, phone, email);
    }

    public Supplier getSupplierById(String supplierId) {
        return repositories.getSupplierRepository().get(supplierId);
    }

    public List<Supplier> getAllSuppliers() {
        return new ArrayList<>(repositories.getSupplierRepository().getAll().values());
    }

    public boolean supplierExist(String id) {
        return repositories.getSupplierRepository().search(id);
    }




    public boolean addAgreementToSupplier(String supplierId, String agreementId,
                                          boolean supportsDelivery, List<DeliveryWeekday> days,
                                          Map<AgreementItem, Double> items) {
        Supplier supplier = repositories.getSupplierRepository().get(supplierId);
        if (supplier == null) return false;

        Agreement agreement = repositories.getAgreementRepository().createAgreement(
                agreementId, supplierId, days, supportsDelivery, items);
        supplier.addAgreement(agreement);

        for (AgreementItem ai : items.keySet()) {
            Product p = repositories.getProductRepository().getProductById(ai.getItemId());
            if (p != null) {
                repositories.getProductRepository().addProduct(p);
                p.getSuppliers().put(supplier, ai.getCatalogNumber());

            }
//                repositories.getProductRepository().getProductById(ai.getItemId())
//                        .getSuppliers().put(supplier, ai.getCatalogNumber());
        }
        return true;
    }

    /**
     * לאחר קריאה ל־supplier.setXXX(...) יש לקרוא ל־persistSupplierChanges
     */
    public boolean persistSupplierChanges(Supplier supplier) {
        boolean ok = supplierDao.updateSupplier(supplier);
        if (!ok) {
            System.err.println("⚠️ Failed to update Suppliers.sql for ID " + supplier.getSupplierId());
        }
        return ok;
    }

    public boolean updateSupplierName(Supplier supplier, String newName) {
        supplier.setName(newName);
        return persistSupplierChanges(supplier);
    }

    public boolean updateSupplierAddress(Supplier supplier, String newAddress) {
        supplier.setDeliveryAddress(newAddress);
        return persistSupplierChanges(supplier);
    }

    public boolean updateSupplierBankAccount(Supplier supplier, String newBankAccount) {
        supplier.setBankAccount(newBankAccount);
        return persistSupplierChanges(supplier);
    }

    public boolean updateSupplierPaymentMethod(Supplier supplier, PaymentMethod method) {
        supplier.setPaymentMethod(method);
        return persistSupplierChanges(supplier);
    }

    public boolean deleteSupplier(String supplierId) {
        boolean removed = repositories.getSupplierRepository().remove(supplierId);
        if (!removed) {
            System.err.println("❌ Supplier with ID " + supplierId + " not found in memory");
            return false;
        }
        // 2. מחיקה מהקובץ
        boolean ok = supplierDao.deleteSupplier(supplierId);
        if (!ok) {
            System.err.println("⚠️ Failed to delete supplier in Suppliers.sql");
        }
        return ok;
    }


    public boolean addItem(String id, String name) {
        addProduct();
        return true;
    }

    public PeriodicOrder CreatePeriodicOrder(String orderId,
                                             Supplier supplier,
                                             Map<String, Integer> products,
                                             String address,
                                             OrderStatus status,
                                             Period deliveryPeriod,
                                             LocalDateTime nextDeliveryTime) {

        return new PeriodicOrder(orderId, supplier, products, address, status, deliveryPeriod, nextDeliveryTime);
    }


    public Order CreateOrder(String orderId, Supplier supplier, Map<String, Integer> items,
                             String orderDate, int totalPrice, OrderStatus status) {
        return repositories.getOrderRepository().createOrder(orderId, supplier, items, orderDate, totalPrice, status);
    }

    public Product getItemById(String id) {
        return repositories.getProductRepository().getProductById(id);
    }

    public Collection<Product> getAllItems() {
        return repositories.getProductRepository().getAllProducts();
    }

    public Map<Supplier, String> getSuppliersForItem(String itemId, List<Supplier> suppliers) {
        Map<Supplier, String> result = new HashMap<>();
        for (Supplier supplier : suppliers) {
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


    public void placeOrder(Order order) {
        repositories.getOrderRepository().add(order);
    }

    public List<Order> getAllOrders() {
        return new ArrayList<>(repositories.getOrderRepository().getAll().values());
    }

    public Order getOrderById(String id) {
        return repositories.getOrderRepository().get(id);
    }

    public boolean updateOrderStatus(Order order, OrderStatus newStatus) {
        if (order.getStatus() == OrderStatus.PENDING && newStatus == OrderStatus.COLLECTED) {
            Scanner scanner = new Scanner(System.in);
            int counter = 1;

            for (Map.Entry<String, Integer> entry : order.getItems().entrySet()) {
                String productId = entry.getKey();
                int quantity = entry.getValue();
                Product product = repositories.getProductRepository().getProductById(productId);

                if (product != null) {
                    System.out.println("🛠 Creating items for product: " + product.getName());

                    System.out.print("Category: ");
                    String category = scanner.nextLine().trim();
                    System.out.print("Subcategory: ");
                    String subcategory = scanner.nextLine().trim();
                    System.out.print("Size: ");
                    double size = Double.parseDouble(scanner.nextLine().trim());
                    System.out.print("Expiration Date (yyyy-MM-dd): ");
                    Date expirationDate;
                    try {
                        expirationDate = new SimpleDateFormat("yyyy-MM-dd").parse(scanner.nextLine().trim());
                    } catch (ParseException e) {
                        System.out.println("Invalid date format.");
                        continue;
                    }

                    System.out.print("How many items to Store? ");
                    int toStore = Integer.parseInt(scanner.nextLine().trim());
                    int toWarehouse = quantity - toStore;

                    for (int i = 0; i < quantity; i++) {
                        String itemId = UUID.randomUUID().toString();
                        String itemName = product.getName() + "-" + (counter++);
                        String classificationId = "CLS-" + UUID.randomUUID().toString().substring(0, 5);

                        Classification cls = new Classification(classificationId, category, subcategory, size);
                        Location location = (i < toStore) ? Location.Store : Location.WareHouse;

                        Item item = new Item(itemId, itemName, location, expirationDate, cls, product);
                        repositories.getItemRepository().addItem(item);
                    }

                    if (product.getStockQuantity() < product.getMinQuantity()) {
                        Alert alert = new Alert(product, new Date());
                        alert.printShortageMessage();
                    }

                } else {
                    System.out.println("⚠️ Product with ID " + productId + " not found in inventory.");
                }
            }

            order.setStatus(newStatus);
            return true;
        } else if (order.getStatus() == OrderStatus.PENDING) {
            order.setStatus(newStatus);
            return true;
        }

        return false;
    }

    public AgreementItem createAgreementItem(String itemId, String catalog, float price, float discount, int quantity, String name) {
        return repositories.getAgreementRepository().createAgreementItem(itemId, catalog, price, discount, quantity, name);
    }

    public Map<Supplier, String> getSuppliersForItem(String itemId) {
        Map<Supplier, String> result = new HashMap<>();
        for (Supplier supplier : getAllSuppliers()) {
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

    public int promptInt(Scanner scanner) {
        while (true) {
            System.out.print("Your choice: ");
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format, please try again.");
            }
        }
    }

    // View all suppliers that sell a specific item
    public void viewSuppliersForProducts(Scanner scanner) {
        System.out.print("Enter Item ID: ");
        String productsId = scanner.nextLine();

        Map<Supplier, String> suppliers = getSuppliersForItem(productsId);

        if (suppliers.isEmpty()) {
            System.out.println("No suppliers found for this products.");
        } else {
            System.out.println("Suppliers for products " + productsId + ":");

            for (Map.Entry<Supplier, String> entry : suppliers.entrySet()) {
                Supplier supplier = entry.getKey();
                String catalogNumber = entry.getValue();
                float price = -1;

                // Try to find the price from the supplier's agreements
                for (Agreement agreement : supplier.getAgreements()) {
                    for (AgreementItem ai : agreement.getItems().keySet()) {
                        if (ai.getItemId().equals(productsId)) {
                            price = ai.getPrice(1);
                            break;
                        }
                    }
                }

                if (price != -1) {
                    System.out.printf("Supplier id: %s | Name: %s | Catalog Number: %s | Price: %.2f\n",
                            supplier.getSupplierId(),
                            supplier.getName(),
                            catalogNumber,
                            price);
                } else {
                    System.out.printf("Supplier id: %s | Name: %s | Catalog Number: %s | Price: Not found\n",
                            supplier.getSupplierId(),
                            supplier.getName(),
                            catalogNumber);
                }
            }
        }
    }

    public boolean persistItemChanges(Item item) {
        boolean ok = itemDao.updateItem(item);
        if (!ok) System.err.println("⚠️ Failed to update Items table for ID " + item.getIid());
        return ok;
    }

    public boolean updateItemName(Item item, String newName) {
        item.setName(newName);
        return persistItemChanges(item);
    }

    public boolean updateItemLocation(Item item, Location newLoc) {
        item.setLocation(newLoc);
        return persistItemChanges(item);
    }

    public boolean updateItemExpirationDate(Item item, Date newDate) {
        item.setExpirationDate(newDate);
        return persistItemChanges(item);
    }

    public boolean updateItemClassification(Item item, Classification newCls) {
        item.setClassification(newCls);
        return persistItemChanges(item);
    }

    public boolean updateItemDefectStatus(Item item, boolean isDefect) {
        item.setDefect(isDefect);
        return persistItemChanges(item);
    }


    public boolean deleteItem(String iid) {
        // 1. מחיקה מהריפוזיטורי בזיכרון
        Item toRemove = repositories.getItemRepository()
                .getAllItems().stream()
                .filter(i -> i.getIid().equals(iid))
                .findFirst().orElse(null);
        if (toRemove == null) return false;
        boolean removed = repositories.getItemRepository().removeItemByName(toRemove.getName());
        if (!removed) return false;

        boolean ok = itemDao.deleteItem(iid);
        if (!ok) {
            System.err.println("⚠️ Failed to delete item from Items.sql for ID " + iid);
        }
        return ok;
    }

    public Item getItemByIid(String iid) {
        return repositories.getItemRepository()
                .getAllItems()
                .stream()
                .filter(i -> i.getIid().equals(iid))
                .findFirst()
                .orElse(null);
    }

}



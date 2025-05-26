package inventory.presentation;

import inventory.domain.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class MenuWithData {
    private static final Scanner scan = new Scanner(System.in);
    private static final ProductRepository productRepo = ProductRepository.getInstance();
    private static final ItemRepository itemRepo = ItemRepository.getInstance();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) throws ParseException {
        System.out.print("Initialize with sample data? (Y/N): ");
        String ans = scan.nextLine().trim();
        if (ans.equalsIgnoreCase("Y")) {
            initializeSampleData();
        }

        int mainChoice;
        do {
            printMainMenu();
            mainChoice = promptInt("Choose an option (1-5): ");
            switch (mainChoice) {
                case 1 -> handleProductsMenu();
                case 2 -> handleItemsMenu();
                case 3 -> handleDiscountsMenu();
                case 4 -> handleReportsMenu();
                case 5 -> System.out.println("Goodbye!");
                default -> System.out.println("Invalid choice, please try again.");
            }
        } while (mainChoice != 5);

        scan.close();
    }

    private static void printMainMenu() {
        System.out.println("Main Menu:");
        System.out.println("1. Manage Products");
        System.out.println("2. Manage Items");
        System.out.println("3. Manage Discounts & Promotions");
        System.out.println("4. Reports");
        System.out.println("5. Exit");
    }

    private static void handleProductsMenu() {
        int choice;
        do {
            System.out.println("Manage Products:");
            System.out.println("1. Add product");
            System.out.println("2. Remove product");
            System.out.println("3. Update Min Quantity");
            System.out.println("4. Show product price by ID");
            System.out.println("5. Update product price by ID");
            System.out.println("6. Back to Main Menu");
            choice = promptInt("Choose an option (1-6): ");
            switch (choice) {
                case 1 -> addProduct();
                case 2 -> removeProduct();
                case 3 -> updateMinQuantity();
                case 4 -> showProductPrice();
                case 5 -> updateProductPrice();
                case 6 -> {}
                default -> System.out.println("Invalid choice, please try again.");
            }
        } while (choice != 6);
    }

    private static void handleItemsMenu() throws ParseException {
        int choice;
        do {
            System.out.println("Manage Items:");
            System.out.println("1. Add item");
            System.out.println("2. Remove item");
            System.out.println("3. Show item details");
            System.out.println("4. Mark item as defective");
            System.out.println("5. Mark item as expired");
            System.out.println("6. Back to Main Menu");
            choice = promptInt("Choose an option (1-6): ");
            switch (choice) {
                case 1 -> addItem();
                case 2 -> removeItem();
                case 3 -> showItemDetails();
                case 4 -> markDefective();
                case 5 -> markExpired();
                case 6 -> {}
                default -> System.out.println("Invalid choice, please try again.");
            }
        } while (choice != 6);
    }

    private static void handleDiscountsMenu() {
        int choice;
        do {
            System.out.println("Manage Discounts & Promotions:");
            System.out.println("1. Add Supplier Discount");
            System.out.println("2. Remove Supplier Discount");
            System.out.println("3. Add Promotion");
            System.out.println("4. Remove Promotion");
            System.out.println("5. Back to Main Menu");
            choice = promptInt("Choose an option (1-5): ");
            switch (choice) {
                case 1 -> addSupplierDiscount();
                case 2 -> removeSupplierDiscount();
                case 3 -> addPromotion();
                case 4 -> removePromotion();
                case 5 -> {}
                default -> System.out.println("Invalid choice, please try again.");
            }
        } while (choice != 5);
    }

    private static void handleReportsMenu() {
        int choice;
        do {
            System.out.println("Reports:");
            System.out.println("1. List all products in stock");
            System.out.println("2. Inventory report by category");
            System.out.println("3. Defective items report");
            System.out.println("4. Expired items report");
            System.out.println("5. Back to Main Menu");
            choice = promptInt("Choose an option (1-5): ");
            switch (choice) {
                case 1 -> showProductsInStock();
                case 2 -> generateCategoryReport();
                case 3 -> generateDefectiveReport();
                case 4 -> generateExpiredReport();
                case 5 -> {}
                default -> System.out.println("Invalid choice, please try again.");
            }
        } while (choice != 5);
    }

    private static void initializeSampleData() {
        // Create example products
        Product p1 = new Product("P1", "Milk", 2.0, 3.5, "Tnuva", new ArrayList<>(), new ArrayList<>(), 5);
        Product p2 = new Product("P2", "Bread", 1.0, 2.5, "Angel", new ArrayList<>(), new ArrayList<>(), 3);
        Product p3 = new Product("P3", "Shampoo", 10.0, 15.0, "Head&Shoulders", new ArrayList<>(), new ArrayList<>(), 2);

        productRepo.addProduct(p1);
        productRepo.addProduct(p2);
        productRepo.addProduct(p3);

        // Create example classifications
        Classification c1 = new Classification("C1", "Dairy", "Milk", 1.0);
        Classification c2 = new Classification("C2", "Bakery", "Bread", 0.5);
        Classification c3 = new Classification("C3", "Toiletries", "Hair", 0.3);

        // Create example items
        itemRepo.addItem(new Item("I1", "Milk1", Location.Store, new Date(System.currentTimeMillis() + 86400000), c1, p1));
        itemRepo.addItem(new Item("I2", "Bread1", Location.WareHouse, new Date(System.currentTimeMillis() + 172800000), c2, p2));
        itemRepo.addItem(new Item("I3", "Shampoo1", Location.Store, new Date(System.currentTimeMillis() + 259200000), c3, p3));

        p1.setStockQuantity(1); p1.setShelfQuantity(1);
        p2.setStockQuantity(1); p2.setWarehouseQuantity(1);
        p3.setStockQuantity(1); p3.setShelfQuantity(1);

        // Mark an item as defective
        itemRepo.markItemDefective("Shampoo1");

        // Print initial alerts if any
        for (Product p : productRepo.getAllProducts()) {
            if (p.getStockQuantity() < p.getMinQuantity()) {
                new Alert(p, new Date()).printShortageMessage();
            }
        }
    }

    private static void addInitItem(String iid, String name, Location loc,
                                    String expDateStr, Classification cls,
                                    Product prod) throws ParseException {
        Date exp = sdf.parse(expDateStr);
        Item it = new Item(iid, name, loc, exp, cls, prod);
        itemRepo.addItem(it);
        prod.setStockQuantity(prod.getStockQuantity() + 1);
        if (loc == Location.WareHouse) {
            prod.setWarehouseQuantity(prod.getWarehouseQuantity() + 1);
        } else {
            prod.setShelfQuantity(prod.getShelfQuantity() + 1);
        }
    }

    // --- Prompt Helpers ---

    private static int promptInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scan.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format, please try again.");
            }
        }
    }

    private static double promptDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scan.nextLine().trim();
            try {
                return Double.parseDouble(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid decimal number, please try again.");
            }
        }
    }

    private static Date promptDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scan.nextLine().trim();
            try {
                return sdf.parse(line);
            } catch (ParseException e) {
                System.out.println("Invalid date (use yyyy-MM-dd), please try again.");
            }
        }
    }

    private static String promptString(String prompt) {
        System.out.print(prompt);
        return scan.nextLine().trim();
    }

    /**
     * Prompt the user for a set of doubles (space-separated), retrying until all valid.
     * An empty input line returns an empty set (matches all sizes).
     */
    private static Set<Double> promptDoubleSet(String prompt) {
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

    private static int parseIntSafe(String s) {
        try { return Integer.parseInt(s.trim()); }
        catch (Exception e) { return -1; }
    }

    // --- Core Operations ---

    private static Product getProductById(String pid) {
        return productRepo.getAllProducts()
                .stream()
                .filter(p -> p.getPid().equals(pid))
                .findFirst()
                .orElse(null);
    }

    private static void addProduct() {
        System.out.println("=== Add product ===");
        System.out.print("Product ID: "); String pid = scan.nextLine().trim();
        System.out.print("Name: "); String name = scan.nextLine().trim();

        Product product = productRepo.getProductByName(name);
        if (product == null) {
            System.out.print("Cost Price: "); double costPrice = Double.parseDouble(scan.nextLine().trim());
            System.out.print("Sale Price: "); double salePrice = Double.parseDouble(scan.nextLine().trim());
            System.out.print("Manufacturer: "); String manufacturer = scan.nextLine().trim();
            System.out.print("Min Quantity: "); int minQuantity = Integer.parseInt(scan.nextLine().trim());

            product = new Product(pid, name, costPrice, salePrice, manufacturer,
                    new ArrayList<>(), new ArrayList<>(), minQuantity);
            productRepo.addProduct(product);
            System.out.println("Product is successfully added");
        } else {
            System.out.println("Product already exists");
        }
    }

    private static void removeProduct() {
        System.out.println("=== Remove product ===");
        System.out.print("Name: "); String name = scan.nextLine().trim();
        boolean removed = productRepo.removeProductByName(name);
        System.out.println(removed ? "Product successfully removed" : "Product does not exist");
    }

    private static void addItem() throws ParseException {
        System.out.print("Item ID: "); String iid = scan.nextLine().trim();
        System.out.print("Name: "); String iname = scan.nextLine().trim();
        System.out.print("Product Name: "); String name = scan.nextLine().trim();
        Product product = productRepo.getProductByName(name);

        System.out.print("Classification ID: "); String classId = scan.nextLine().trim();
        System.out.print("Category: "); String category = scan.nextLine().trim();
        System.out.print("Subcategory: "); String subcategory = scan.nextLine().trim();
        System.out.print("Size: "); double size = Double.parseDouble(scan.nextLine().trim());
        Classification cls = new Classification(classId, category, subcategory, size);

        System.out.print("Location (WareHouse=0, Store=1): ");
        Location loc = parseIntSafe(scan.nextLine()) == 0 ? Location.WareHouse : Location.Store;
        System.out.print("Expiration Date (yyyy-MM-dd): "); Date exp = sdf.parse(scan.nextLine().trim());

        Item item = new Item(iid, iname, loc, exp, cls, product);
        itemRepo.addItem(item);

        if (product.getStockQuantity() < product.getMinQuantity()) {
            Alert alert = new Alert(product, new Date());
            alert.printShortageMessage();
        }
    }

    private static void removeItem() {
        System.out.println("=== Remove item ===");
        System.out.print("Name: "); String name = scan.nextLine().trim();
        Item item = itemRepo.getItemByName(name);
        if (item == null) {
            System.out.println("Item does not exist");
            return;
        }
        Product p = item.getProduct();
        p.setStockQuantity(p.getStockQuantity() - 1);
        if (item.getLocation() == Location.WareHouse) {
            p.setWarehouseQuantity(p.getWarehouseQuantity() - 1);
        } else {
            p.setShelfQuantity(p.getShelfQuantity() - 1);
        }
        boolean removed = itemRepo.removeItemByName(name);
        System.out.println(removed ? "Item successfully removed" : "Failed to remove item");
    }

    private static void showProductsInStock() {
        System.out.println("=== Products in stock ===");
        productRepo.display();
    }

    private static void showItemDetails() {
        System.out.println("=== Item details ===");
        try {
            System.out.print("Item Name: "); String pname = scan.nextLine().trim();
            itemRepo.displayItemDetails(pname);
        } catch (Exception e) {
            System.out.println("Error in item details: " + e.getMessage());
        }
    }

    private static void updateMinQuantity() {
        System.out.println("=== Update Min Quantity ===");
        System.out.print("Enter Product ID: ");
        String pid = scan.nextLine().trim();
        Product p = productRepo.getAllProducts().stream()
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

    private static boolean isValidPercentage(double perc) {
        if (perc < 0 || perc > 100) {
            System.out.println("Percentage must be between 0 and 100.");
            return false;
        }
        return true;
    }

    private static boolean isValidId(String id) {
        if (id == null || id.trim().isEmpty()) {
            System.out.println("ID cannot be empty.");
            return false;
        }
        return true;
    }

    private static void addSupplierDiscount() {
        System.out.println("=== Add Supplier Discount ===");
        try {
            System.out.print("Product Name: "); String name = scan.nextLine().trim();
            ProductRepository repo = ProductRepository.getInstance();
            Product p = repo.getProductByName(name);
            if (p == null) {
                System.out.println("Product does not exist");
                return;
            }
            System.out.print("Discount ID: "); String disID = scan.nextLine().trim();
            System.out.print("Supplier Name: "); String supName = scan.nextLine().trim();
            System.out.print("Discount Percentage: "); double dp = Double.parseDouble(scan.nextLine().trim());

        if (!isValidPercentage(dp)) {
            return;
        }

            System.out.print("Start Date (yyyy-MM-dd): "); LocalDate sd = LocalDate.parse(scan.nextLine().trim());
            System.out.print("End Date   (yyyy-MM-dd): "); LocalDate ed = LocalDate.parse(scan.nextLine().trim());

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

    private static void removeSupplierDiscount() {
        System.out.println("=== Remove Supplier Discount ===");
        System.out.print("Product Name: ");
        String name = scan.nextLine().trim();

        ProductRepository repo = ProductRepository.getInstance();
        Product p = repo.getProductByName(name);
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

    private static void addPromotion() {
        System.out.println("=== Add Promotion ===");
        System.out.print("Product Name: "); String name = scan.nextLine().trim();
        Product p = productRepo.getProductByName(name);
        if (p == null) {
            System.out.println("Product does not exist");
            return;
        }
        System.out.print("Promotion ID: "); String proID = scan.nextLine().trim();
        if (!isValidId(proID)) {
            return;
        }
        System.out.print("Discount Percentage: "); double dp = Double.parseDouble(scan.nextLine().trim());

        if (!isValidPercentage(dp)) {
            return;
        }

        System.out.print("Start Date (yyyy-MM-dd): "); LocalDate sd = LocalDate.parse(scan.nextLine().trim());
        System.out.print("End Date   (yyyy-MM-dd): "); LocalDate ed = LocalDate.parse(scan.nextLine().trim());

        if (sd.isAfter(ed)) {
            System.out.println("Start Date must be on or before End Date");
            return;
        }
        p.addPromotion(new Promotion(proID, dp, sd, ed, p, null));
        System.out.println("Promotion successfully added");
    }

    private static void removePromotion() {
        System.out.println("=== Remove Promotion ===");
        System.out.print("Product Name: "); String name = scan.nextLine().trim();
        Product p = productRepo.getProductByName(name);
        if (p == null) {
            System.out.println("Product does not exist");
            return;
        }
        System.out.print("Promotion ID to remove: "); String proID = scan.nextLine().trim();
        boolean removed = p.getPromotions().removeIf(pr -> pr.getProID().equals(proID));
        System.out.println(removed ? "Promotion removed" : "Promotion not found");
    }

    private static void generateCategoryReport() {
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
        for (Item it : itemRepo.getAllItems()) {
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

    private static void markDefective() {
        System.out.println("=== Mark item defective ===");
        String name = promptString("Item Name: ");
        boolean ok = itemRepo.markItemDefective(name);
        System.out.println(ok ? "Item marked defective" : "Item not found");
    }

    private static void markExpired() {
        System.out.println("=== Mark item expired ===");
        String name = promptString("Item Name: ");
        Item item = itemRepo.getItemByName(name);
        if (item == null) {
            System.out.println("Item not found");
            return;
        }
        long millisInDay = 24L * 60 * 60 * 1000;
        item.setExpirationDate(new Date(System.currentTimeMillis() - millisInDay));
        System.out.println("Item marked as expired.");
    }

    private static void generateDefectiveReport() {
        Report rep = new DefectReport(
                UUID.randomUUID().toString(), new Date(),
                itemRepo.getAllItems());
        new ReportsView(rep).display();
    }

    private static void generateExpiredReport() {
        System.out.println("=== Expired items report ===");
        Date now = new Date();
        List<Item> expired = new ArrayList<>();
        for (Item it : itemRepo.getAllItems()) {
            if (it.getExpirationDate().before(now)) {
                expired.add(it);
            }
        }
        Report rep = new ExpiredReport(UUID.randomUUID().toString(), now, expired);
        new ReportsView(rep).display();
    }

    private static void showProductPrice() {
        System.out.println("=== Show product price ===");
        System.out.print("Enter Product ID: "); String pid = scan.nextLine().trim();
        Product found = productRepo.getAllProducts().stream()
                .filter(x -> x.getPid().equals(pid))
                .findFirst().orElse(null);
        if (found == null) {
            System.out.println("Product does not exist"); return;
        }
        System.out.println("Name: " + found.getName() + " | Price: " + found.getSalePrice());
    }

    private static void updateProductPrice() {
        System.out.println("=== Update product price ===");
        System.out.print("Enter Product ID: "); String pid = scan.nextLine().trim();
        Product found = productRepo.getAllProducts().stream()
                .filter(x -> x.getPid().equals(pid))
                .findFirst().orElse(null);
        if (found == null) {
            System.out.println("Product does not exist"); return;
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
}

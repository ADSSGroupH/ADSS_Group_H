package dao;

import Domain.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ItemDao {

    private static final String FILE_PATH = "C:\\Users\\eladt\\OneDrive\\Desktop\\Data\\Items.sql";
    private final ProductDao productDao;

    public ItemDao() {
        this.productDao = new ProductDao(); // נשתמש כדי לקשר בין item ל-product
    }

    public List<Item> getAll() {
        System.out.println("✅ Loaded item: " );
        List<Item> items = new ArrayList<>();

        // טען את כל המוצרים למפה לזיהוי מהיר לפי Product ID
        Map<String, Product> productMap = new HashMap<>();
        for (Product product : productDao.getAll()) {
            productMap.put(product.getPid(), product);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            while ((line = reader.readLine()) != null) {
                if (!line.trim().toUpperCase().startsWith("INSERT")) continue;

                String values = line.substring(line.indexOf('(') + 1, line.lastIndexOf(')'));
                String[] parts = values.split(",");

                for (int i = 0; i < parts.length; i++) {
                    parts[i] = parts[i].trim().replace("'", "");
                }

                // נניח שהשדות לפי הסדר הבא:
                // iid, name, location, expirationDate, category, subcategory, size, productId, isDefect

                String iid = parts[0];
                String name = parts[1];
                Location location = Location.valueOf(parts[2].trim());
                Date expirationDate = sdf.parse(parts[3]);
                String category = parts[4];
                String subcategory = parts[5];
                double size = Double.parseDouble(parts[6]);
                String productId = parts[7];
                boolean isDefect = Boolean.parseBoolean(parts[8]);

                // בניית הקשרים:
                Classification classification = new Classification(null, category, subcategory, size);
                Product product = productMap.get(productId);

                if (product == null) {
                    throw new IllegalArgumentException("❌ Product with ID " + productId + " not found!");
                }

                Item item = new Item(iid, name, location, expirationDate, classification, product);
                item.setDefect(isDefect);

                items.add(item);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return items;
    }

}

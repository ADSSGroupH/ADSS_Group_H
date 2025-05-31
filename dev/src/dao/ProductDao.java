package dao;

import Domain.Product;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ProductDao {

    private static final String FILE_PATH = "C:\\Users\\eladt\\OneDrive\\Desktop\\Data\\Products.sql";

    public List<Product> getAll() {
        List<Product> products = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (!line.trim().toUpperCase().startsWith("INSERT")) continue;

                String values = line.substring(line.indexOf('(') + 1, line.lastIndexOf(')'));
                String[] parts = values.split(",");

                for (int i = 0; i < parts.length; i++) {
                    parts[i] = parts[i].trim().replace("'", "");
                }

                Product p = new Product(
                        parts[0],
                        parts[1],
                        Double.parseDouble(parts[2]),
                        Double.parseDouble(parts[3]),
                        parts[4],
                        new ArrayList<>(), // עכשיו רשימת promotions איננה null
                        new ArrayList<>(), // עכשיו רשימת supplierDiscounts איננה null
                        Integer.parseInt(parts[7])
                );
//                p.setStockQuantity(Integer.parseInt(parts[7]));
//                p.setWarehouseQuantity(Integer.parseInt(parts[9]));
//                p.setShelfQuantity(Integer.parseInt(parts[10]));
                products.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;
    }


    public Product getById(String pid) {
        // נניח שהמוצרים נטענים מתוך קובץ Products.sql בדיוק כמו Items
        String FILE_PATH = "C:/Users/eladt/Desktop/קורסים/שנה ב/sql/Products.sql";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().toUpperCase().startsWith("INSERT")) continue;

                String values = line.substring(line.indexOf('(') + 1, line.lastIndexOf(')'));
                String[] parts = values.split(",");

                for (int i = 0; i < parts.length; i++) {
                    parts[i] = parts[i].trim().replace("'", "");
                }

                String id = parts[0];
                if (!id.equals(pid)) continue;

                String name = parts[1];
                double costPrice = Double.parseDouble(parts[2]);
                double salePrice = Double.parseDouble(parts[3]);
                String manufacturer = parts[4];
                int minQuantity = Integer.parseInt(parts[5]);
                int stockQuantity = Integer.parseInt(parts[6]);
                int warehouseQuantity = Integer.parseInt(parts[7]);
                int shelfQuantity = Integer.parseInt(parts[8]);

                Product p = new Product(id, name, costPrice, salePrice, manufacturer, null, null, minQuantity);
                p.setStockQuantity(stockQuantity);
                p.setWarehouseQuantity(warehouseQuantity);
                p.setShelfQuantity(shelfQuantity);

                return p;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


}

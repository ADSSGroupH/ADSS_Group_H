package dao;

import Domain.Product;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProductDao {

    private static final String FILE_PATH = Paths.get("Data", "Products.sql").toAbsolutePath().toString();

    /** 1. קבלת כל המוצרים מתוך הקובץ */
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();

        try {
            List<String> lines = Files.readAllLines(Paths.get(FILE_PATH));
            for (String raw : lines) {
                String line = raw.trim();
                if (!line.toUpperCase().startsWith("INSERT")) continue;

                // חילוץ התוכן שבתוך הסוגריים
                String valuesPart = line.substring(line.indexOf('(') + 1, line.lastIndexOf(')'));
                String[] parts = valuesPart.split(",");

                // ניקוי מרכאות ורווחים
                for (int i = 0; i < parts.length; i++) {
                    parts[i] = parts[i].trim().replace("'", "");
                }

                // לפי הסדר: pid, name, costPrice, salePrice, manufacturer, minQty
                String pid          = parts[0];
                String name         = parts[1];
                double costPrice    = parseNullableDouble(parts[2]);
                double salePrice    = parseNullableDouble(parts[3]);
                String manufacturer = parts[4];
                int minQty          = parseNullableInt(parts.length > 5 ? parts[5] : null);

                products.add(new Product(
                        pid, name, costPrice, salePrice, manufacturer,
                        new ArrayList<>(), new ArrayList<>(), minQty
                ));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return products;
    }

    /** 2. הוספת מוצר חדש בסוף הקובץ */
    public boolean addProduct(Product p) {
        String insertLine = buildInsertLine(p);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(insertLine);
            writer.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** 3. עדכון מוצר קיים לפי pid */
    public boolean updateProduct(Product p) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(FILE_PATH));
            boolean found = false;
            String newLine = buildInsertLine(p);

            for (int i = 0; i < lines.size(); i++) {
                String raw = lines.get(i).trim();
                if (!raw.toUpperCase().startsWith("INSERT")) continue;

                String valuesPart = raw.substring(raw.indexOf('(') + 1, raw.lastIndexOf(')'));
                String id = valuesPart.split(",")[0].trim().replace("'", "");
                if (id.equals(p.getPid())) {
                    lines.set(i, newLine);
                    found = true;
                    break;
                }
            }

            if (!found) return false;

            Files.write(
                    Paths.get(FILE_PATH),
                    lines,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** 4. מחיקת מוצר לפי pid */
    public boolean deleteProduct(String pid) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(FILE_PATH));
            boolean found = false;

            Iterator<String> iter = lines.iterator();
            while (iter.hasNext()) {
                String raw = iter.next().trim();
                if (!raw.toUpperCase().startsWith("INSERT")) continue;

                String valuesPart = raw.substring(raw.indexOf('(') + 1, raw.lastIndexOf(')'));
                String id = valuesPart.split(",")[0].trim().replace("'", "");
                if (id.equals(pid)) {
                    iter.remove();
                    found = true;
                    break;
                }
            }

            if (!found) return false;

            Files.write(
                    Paths.get(FILE_PATH),
                    lines,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** בונה שורת INSERT בהתאם למוצר */
    private String buildInsertLine(Product p) {
        return String.format(
                "INSERT INTO \"Products\" VALUES ('%s','%s',%s,%s,'%s',%d);",
                p.getPid(),
                p.getName().replace("'", "''"),
                p.getCostPrice(),
                p.getSalePrice(),
                p.getManufacturer().replace("'", "''"),
                p.getMinQuantity()
        );
    }

    /** עזר לפרסינג של Integer */
    private int parseNullableInt(String s) {
        if (s == null || s.equalsIgnoreCase("NULL") || s.trim().isEmpty()) return 0;
        return Integer.parseInt(s);
    }

    /** עזר לפרסינג של Double */
    private double parseNullableDouble(String s) {
        if (s == null || s.equalsIgnoreCase("NULL") || s.trim().isEmpty()) return 0.0;
        return Double.parseDouble(s);
    }

    public Product getProductById(String pid) {
        // פשוט עוברים על כל המוצרים שקוראים מ‑getAllProducts()
        for (Product p : getAllProducts()) {
            if (p.getPid().equals(pid)) {
                return p;
            }
        }
        return null;
    }
}

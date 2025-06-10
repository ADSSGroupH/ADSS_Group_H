package dao;

import Domain.Supplier;
import Domain.PaymentMethod;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDao {

    private static final String FILE_PATH = "C:/Users/eladt/IdeaProjects/Supplier New/dev/src/Data/Suppliers.sql";

    public List<Supplier> getAllSuppliers() {
        List<Supplier> suppliers = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (!line.trim().toUpperCase().startsWith("INSERT")) continue;

                // חילוץ התוך-סוגריים
                String valuesPart = line.substring(line.indexOf('(') + 1, line.lastIndexOf(')'));
                String[] parts = valuesPart.split(",");

                for (int i = 0; i < parts.length; i++) {
                    parts[i] = parts[i].trim().replace("'", "");
                }

                Supplier s = new Supplier(
                        parts[0],
                        parts[1],
                        parts[2],
                        PaymentMethod.valueOf(parts[3]),
                        parts[4]
                );
                suppliers.add(s);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return suppliers;
    }

    // 1. הוספת ספק חדש בסוף הקובץ
    public boolean addSupplier(Supplier s) {
        String insertLine = buildInsertLine(s);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(insertLine);
            writer.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. עדכון שורה קיימת על פי SupplierId
    public boolean updateSupplier(Supplier s) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(FILE_PATH));
            boolean found = false;
            String newLine = buildInsertLine(s);

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (!line.toUpperCase().startsWith("INSERT")) continue;

                String valuesPart = line.substring(line.indexOf('(') + 1, line.lastIndexOf(')'));
                String id = valuesPart.split(",")[0].trim().replace("'", "");
                if (id.equals(s.getSupplierId())) {
                    lines.set(i, newLine);
                    found = true;
                    break;
                }
            }

            if (!found) return false;

            Files.write(Paths.get(FILE_PATH), lines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. מחיקת שורה על פי SupplierId
    public boolean deleteSupplier(String supplierId) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(FILE_PATH));
            boolean found = false;

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (!line.toUpperCase().startsWith("INSERT")) continue;

                String valuesPart = line.substring(line.indexOf('(') + 1, line.lastIndexOf(')'));
                String id = valuesPart.split(",")[0].trim().replace("'", "");
                if (id.equals(supplierId)) {
                    lines.remove(i);
                    found = true;
                    break;
                }
            }

            if (!found) return false;

            Files.write(Paths.get(FILE_PATH), lines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // בונה את מחרוזת ה-INSERT המתאימה לשורה חדשה או מתעדכנת
    private String buildInsertLine(Supplier s) {
        return String.format(
                "INSERT INTO \"Suppliers\" VALUES ('%s', '%s', '%s', '%s', '%s');",
                s.getSupplierId(),
                s.getName(),
                s.getBankAccount(),
                s.getPaymentMethod().name(),
                s.getDeliveryAddress().replace("'", "''")  // ' escaping
        );
    }

    public Supplier getById(String supplierId) {
        for (Supplier s : getAllSuppliers()) {
            if (s.getSupplierId().equals(supplierId)) {
                return s;
            }
        }
        return null;
    }
}

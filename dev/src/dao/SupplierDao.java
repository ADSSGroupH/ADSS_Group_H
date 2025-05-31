package dao;

import Domain.Supplier;
import Domain.PaymentMethod;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDao {

    private static final String FILE_PATH = "C:/Users/eladt/OneDrive/Desktop/Data/Suppliers.sql";
    ;

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
}

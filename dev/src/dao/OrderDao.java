package dao;

import Domain.Order;
import Domain.OrderStatus;
import Domain.Supplier;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class OrderDao {
    private static final String FILE_PATH = Paths.get("Data", "Orders.sql").toAbsolutePath().toString();;
    private final SupplierDao supplierDao = new SupplierDao();

    /** 1. כל ההזמנות */
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(FILE_PATH));
            for (String raw : lines) {
                String line = raw.trim();
                if (!line.toUpperCase().startsWith("INSERT")) continue;

                // מוציא את התוכן שבין הסוגריים
                String valuesPart = line.substring(line.indexOf('(') + 1, line.lastIndexOf(')'));
                String[] parts = valuesPart.split(",");

                // ניקוי מרכאות ורווחים
                for (int i = 0; i < parts.length; i++) {
                    parts[i] = parts[i].trim().replace("'", "");
                }

                String orderId     = parts[0];
                String supplierId  = parts[1];
                String orderDate   = parts[2];
                double totalPrice  = parseNullableDouble(parts[3]);
                OrderStatus status = OrderStatus.valueOf(parts[4]);
                String arrivalDate = parts.length > 5 ? parts[5] : null;

                Supplier supplier = supplierDao.getById(supplierId);
                Order o = new Order(orderId, supplier, Collections.emptyMap(), orderDate, status);
                o.setTotalPrice(totalPrice);
                o.setArrivalDate(arrivalDate == null || arrivalDate.isEmpty() ? null : arrivalDate);
                orders.add(o);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return orders;
    }

    /** 2. הזמנה לפי מזהה */
    public Order getOrderById(String oid) {
        for (Order o : getAllOrders()) {
            if (o.getOrderId().equals(oid)) return o;
        }
        return null;
    }

    /** 3. הוספת הזמנה חדשה */
    public boolean addOrder(Order o) {
        String line = buildInsertLine(o);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(line);
            writer.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** 4. עדכון קיימת לפי orderId */
    public boolean updateOrder(Order o) {
        try {
            Path path = Paths.get(FILE_PATH);
            List<String> lines = Files.readAllLines(path);
            boolean found = false;
            String newLine = buildInsertLine(o);

            for (int i = 0; i < lines.size(); i++) {
                String raw = lines.get(i).trim();
                if (!raw.toUpperCase().startsWith("INSERT")) continue;
                String id = raw.substring(raw.indexOf('(')+1, raw.indexOf(',')).replace("'", "").trim();
                if (id.equals(o.getOrderId())) {
                    lines.set(i, newLine);
                    found = true;
                    break;
                }
            }
            if (!found) return false;
            Files.write(path, lines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** 5. מחיקת הזמנה */
    public boolean deleteOrder(String oid) {
        try {
            Path path = Paths.get(FILE_PATH);
            List<String> lines = Files.readAllLines(path);
            boolean found = false;
            Iterator<String> iter = lines.iterator();
            while (iter.hasNext()) {
                String raw = iter.next().trim();
                if (!raw.toUpperCase().startsWith("INSERT")) continue;
                String id = raw.substring(raw.indexOf('(')+1, raw.indexOf(',')).replace("'", "").trim();
                if (id.equals(oid)) {
                    iter.remove();
                    found = true;
                    break;
                }
            }
            if (!found) return false;
            Files.write(path, lines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** בונה שורת INSERT לפי השדות (בלי המפה) */
    private String buildInsertLine(Order o) {
        return String.format(
                "INSERT INTO \"Orders\" VALUES ('%s','%s','%s',%.2f,'%s','%s');",
                o.getOrderId(),
                o.getSupplier() != null ? o.getSupplier().getSupplierId() : "",
                o.getOrderDate(),
                o.getTotalPrice(),
                o.getStatus().name(),
                o.getArrivalDate() != null ? o.getArrivalDate() : "NULL"
        );
    }

    private double parseNullableDouble(String s) {
        if (s == null || s.equalsIgnoreCase("NULL") || s.trim().isEmpty()) return 0.0;
        return Double.parseDouble(s);
    }
}

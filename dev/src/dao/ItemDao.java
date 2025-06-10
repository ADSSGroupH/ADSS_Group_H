package dao;

import Domain.*;
import java.io.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ItemDao {

    // 1. כוון לנתיב המדויק שבו אתה רוצה שה־.sql יתעדכן
    private static final String FILE_PATH =
            "C:/Users/eladt/IdeaProjects/Supplier New/dev/src/Data/Items.sql";

    private final ProductDao productDao;

    public ItemDao() {
        this.productDao = new ProductDao();
    }

    public List<Item> getAll() {
        List<Item> items = new ArrayList<>();
        Map<String,Product> productMap = new HashMap<>();
        for (Product p : productDao.getAllProducts()) {
            productMap.put(p.getPid(), p);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            while ((line = reader.readLine()) != null) {
                if (!line.trim().toUpperCase().startsWith("INSERT")) continue;

                String values = line.substring(line.indexOf('(')+1, line.lastIndexOf(')'));
                String[] parts = values.split(",");

                for (int i = 0; i < parts.length; i++) {
                    parts[i] = parts[i].trim().replace("'", "");
                }

                // לפי הסדר: iid, name, location, expirationDate,
                // category, subcategory, size, productId, isDefect
                String iid         = parts[0];
                String name        = parts[1];
                Location loc       = Location.valueOf(parts[2]);
                Date expDate       = sdf.parse(parts[3]);
                String category    = parts[4];
                String subcat      = parts[5];
                double size        = Double.parseDouble(parts[6]);
                String productId   = parts[7];
                boolean isDefect   = Boolean.parseBoolean(parts[8]);

                Classification cls = new Classification(null, category, subcat, size);
                Product product    = productMap.get(productId);
                if (product == null) {
                    throw new IllegalStateException("Product ID not found: "+productId);
                }

                Item item = new Item(iid, name, loc, expDate, cls, product);
                item.setDefect(isDefect);
                items.add(item);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return items;
    }

    /** UPDATE */
    public boolean updateItem(Item item) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(FILE_PATH));
            String newLine = buildInsertLine(item);
            boolean found = false;

            for (int i = 0; i < lines.size(); i++) {
                String raw = lines.get(i).trim();
                if (!raw.toUpperCase().startsWith("INSERT")) continue;
                String id = raw.substring(raw.indexOf('(')+1, raw.indexOf(',')).replace("'", "").trim();
                if (id.equals(item.getIid())) {
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

    /** CREATE */
    public boolean addItem(Item item) {
        String line = buildInsertLine(item);
        try (BufferedWriter w = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            w.write(line);
            w.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** DELETE */
    public boolean deleteItem(String iid) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(FILE_PATH));
            boolean found = false;
            Iterator<String> iter = lines.iterator();

            while (iter.hasNext()) {
                String raw = iter.next().trim();
                if (!raw.toUpperCase().startsWith("INSERT")) continue;
                String id = raw.substring(raw.indexOf('(')+1, raw.indexOf(',')).replace("'", "").trim();
                if (id.equals(iid)) {
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

    /** הנוסח המלא של שורת ה‑INSERT, כולל כל 9 השדות */
    private String buildInsertLine(Item item) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return String.format(
                "INSERT INTO \"Items\" VALUES ('%s','%s','%s','%s','%s','%s',%s,'%s',%s);",
                item.getIid(),
                item.getName().replace("'", "''"),
                item.getLocation().name(),
                sdf.format(item.getExpirationDate()),
                item.getClassification().getCategory(),
                item.getClassification().getSubcategory(),
                // הקריאה הנכונה ל‑size:
                String.valueOf(item.getClassification().getsize()),
                item.getProduct().getPid(),
                item.isDefect()
        );
    }

}

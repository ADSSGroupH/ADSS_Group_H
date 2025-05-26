package inventory.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExpiredReport extends Report {
    private final List<Item> items;

    public ExpiredReport(String reportID, Date date,
                         List<Item> items) {
        super(reportID, date);
        this.items = items;
    }

    @Override
    public String generate() {
        StringBuilder sb = new StringBuilder();
        sb.append("Expired Report:\n");

        List<Item> expired = new ArrayList<>();
        Date now = new Date();
        for (Item item : items) {
            if (item.getExpirationDate().before(now)) {
                expired.add(item);
            }
        }

        if (expired.isEmpty()) {
            sb.append("No expired items.\n");
        } else {
            for (Item it : expired) {
                sb.append(it.getName())
                        .append(" (ID: ")
                        .append(it.getIid())
                        .append(")\n");
            }
        }
        return sb.toString();
    }
}

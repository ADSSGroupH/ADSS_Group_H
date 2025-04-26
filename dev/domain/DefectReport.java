package inventory.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DefectReport extends Report {
    private List<Item> items;

    public DefectReport(String reportID, Date date,
                        List<Item> items) {
        super(reportID, date);
        this.items = items;
    }

    @Override
    public String generate() {
        StringBuilder sb = new StringBuilder();
        sb.append("Defect Report:\n");

        List<Item> defects = new ArrayList<>();
        for (Item p : items) {
            if (p.isDefect()) {
                defects.add(p);
            }
        }
        if (defects.isEmpty()) {
            sb.append("No defective products.\n");
        } else {
            defects.forEach(p -> sb.append(p.getName())
                    .append(" (ID: ")
                    .append(p.getIid())
                    .append(")\n"));
        }
        return sb.toString();
    }
}
package Domain;

import java.util.Date;
import java.util.List;

/**
 * Report listing all products and their stock quantities.
 */
public class StockReport extends Report {
    private List<Product> products;

    public StockReport(String reportID, Date date, List<Product> products) {
        super(reportID, date);
        this.products = products;
    }

    @Override
    public String generate() {
        StringBuilder sb = new StringBuilder();
        sb.append("Stock Report:\n");
        for (Product p : products) {
            sb.append("- ")
                    .append(p.getName())
                    .append(" (ID: ").append(p.getPid()).append(") : ")
                    .append(p.getStockQuantity())
                    .append("\n");
        }
        return sb.toString();
    }
}

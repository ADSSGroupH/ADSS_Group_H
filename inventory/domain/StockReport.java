package inventory.domain;

import java.util.Date;
import java.util.List;

public class StockReport extends Report {
    public StockReport(String reportID, Date date,
                       List<Product> products,
                       List<Category> categories) {
        super(reportID, date, products, categories);
    }

    @Override
    public String generate() {
        return "StockReport: " + this.getProducts();
    }
}


package inventory.domain;

import java.util.Date;
import java.util.List;

public class DefectReport extends Report {
    public DefectReport(String reportID, Date date,
                        List<Product> products,
                        List<Category> categories) {
        super(reportID, date, products, categories);
    }

    @Override
    public String generate() {
        return "DefectReport: " + this.getProducts();
    }
}

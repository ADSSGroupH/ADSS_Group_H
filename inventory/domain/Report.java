package inventory.domain;

import java.util.Date;
import java.util.List;

/** abstract base for all reports */
public abstract class Report {
    private String reportID;
    private Date date;
    private List<Product> products;
    private List<Category> categories;

    public Report(String reportID, Date date,
                  List<Product> products,
                  List<Category> categories) {
        this.reportID   = reportID;
        this.date       = date;
        this.products   = products;
        this.categories = categories;
    }
}

package inventory.domain;

import java.util.Date;
import java.util.List;

/** abstract base for all reports */
public abstract class Report {
    private String reportID;
    private Date date;
    private List<Product> products;
    private List<Category> categories;

    public Report(String reportID,
                  Date date,
                  List<Product> products,
                  List<Category> categories) {
        this.reportID   = reportID;
        this.date       = date;
        this.products   = products;
        this.categories = categories;
    }

    public abstract String generate();

    public String getReportID() {
        return reportID;
    }
    public void setReportID(String reportID) {
        this.reportID = reportID;
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public List<Product> getProducts() {
        return products;
    }
    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Category> getCategories() {
        return categories;
    }
    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}

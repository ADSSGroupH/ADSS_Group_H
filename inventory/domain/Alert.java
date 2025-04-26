package inventory.domain;

import java.util.Date;

public class Alert {
    private String message;
    private Product product;
    private Date date;
    private String pid;

    public Alert(Product product, Date date) {
        this.product = product;
        this.date    = date;
        this.pid    = product.getPid();
        this.message = "Shortage alert: product " + product.getName() +
                " (ID: " + product.getPid() + ") is low in stock.";
    }
    public Product getProduct() { return product; }

    public void printShortageMessage() {
        System.out.println(this.message);
    }

    @Override
    public String toString() {
        return this.message;
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public String getPid() {
        return pid;
    }
    public void setPid(String pid) {
        this.pid = pid;
    }
}

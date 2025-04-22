package inventory.domain;

import java.util.Date;

public class Alert {
    private String message;
    private Date date;
    private String pid;

    public Alert(String message, Date date) {
        this.message = message;
        this.date    = date;
        this.pid    = pid;
    }
}

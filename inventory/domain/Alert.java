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

    
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
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

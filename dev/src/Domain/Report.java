package Domain;

import java.util.Date;
import java.util.List;

/**
 * Abstract base class for all reports.
 */
public abstract class Report {
    private String reportID;
    private Date date;


    public Report(String reportID, Date date) {
        this.reportID = reportID;
        this.date = date;
    }

    /**
     * Generates the report content as a String.
     */
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
}

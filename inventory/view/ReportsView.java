package inventory.view;

import inventory.domain.Report;
import java.util.List;

public class ReportsView {
    private List<Report> reports;
    public void setReports(List<Report> reports) {
        this.reports = reports;
    }
    public List<Report> getReports() { return reports; }
}
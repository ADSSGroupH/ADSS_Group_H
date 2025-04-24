package inventory.view;

import inventory.domain.Report;
import java.util.List;

public class ReportsView {
    private String reportContent;
    public ReportsView(String reportContent) {
        this.reportContent = reportContent;
    }
    public void display() {
        System.out.println("=== Report ===");
        System.out.println(reportContent);
    }
}

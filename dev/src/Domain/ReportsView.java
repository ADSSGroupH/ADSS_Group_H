package Domain;

import java.util.Objects;

/**
 * View for displaying generated reports.
 */
public class ReportsView {
    private final Report report;

    /**
     * @param report the report to display (must not be null)
     * @throws NullPointerException if report is null
     */
    public ReportsView(Report report) {
        this.report = Objects.requireNonNull(report, "Report cannot be null");
    }

    /**
     * Displays the report ID, creation date, and generated content.
     */
    public void display() {
        System.out.println("=== Report: " + report.getReportID() + " ===");
        System.out.println("Date: " + report.getDate());
        System.out.println(report.generate());
    }
}

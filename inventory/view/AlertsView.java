package inventory.view;

import inventory.domain.Alert;

import java.util.List;

public class AlertsView {
    private List<Alert> alerts;
    public AlertsView(List<Alert> alerts) {
        this.alerts = alerts;
    }
    public void display() {
        System.out.println("=== Alerts ===");
        for (Alert a : alerts) {
            a.printShortageMessage();
        }
    }
}

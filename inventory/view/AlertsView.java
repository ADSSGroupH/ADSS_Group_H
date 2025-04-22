package inventory.view;

import inventory.domain.Alert;

import java.util.List;

public class AlertsView {
    private List<Alert> alerts;
    public void setAlerts(List<Alert> alerts) {
        this.alerts = alerts;
    }
    public List<Alert> getAlerts() { return alerts; }
}

package inventory.controller;

import inventory.domain.InventoryManager;
import inventory.domain.*;
import inventory.view.*;
import java.util.*;

public class AlertsController {
    private InventoryManager manager;

    public AlertsController(InventoryManager manager) {
        this.manager = manager;
    }

    public void checkAlerts() {
        manager.checkAndNotify();
    }

    public void showAlerts() {
        List<Alert> alerts = manager.getAllAlerts();
        AlertsView view = new AlertsView(alerts);
        view.display();
    }
}

package ServiceLayer;

import DomainLayer.transportationDomain.Transportation;
import DomainLayer.transportationDomain.TransportationController;

public class DriverTransportationService {
    private final TransportationController controller = new TransportationController();

    public String getTransportationDocument(int transportationId) {
        return controller.displayTransportationDocument(transportationId);
    }

    public String getItemsForTransportation(int transportationId) {
        Transportation t = controller.findTransportationById(transportationId);
        if (t == null) return "Transportation not found.";
        return controller.displayItemsList(t);
    }

    public boolean reportAccident(int transportationId) {
        // בעתיד תוכל להוסיף דיווח אמיתי
        System.out.println("Accident reported for transportation ID: " + transportationId);
        return true;
    }
}

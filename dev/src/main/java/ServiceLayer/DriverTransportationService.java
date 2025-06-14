package ServiceLayer;

import DomainLayer.Transportation.Controllers.TransportationController;
import DomainLayer.Transportation.Transportation;


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

    public String reportAccident(int transportationId, String accident) {
        // בעתיד תוכל להוסיף דיווח אמיתי
        controller.reportAccident(transportationId, accident);
        return "Accident reported for transportation ID: " + transportationId;
    }
}
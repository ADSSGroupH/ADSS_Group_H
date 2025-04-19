// AgreementService.java
import java.util.*;

public class AgreementService {
    public Agreement createAgreement(String agreementId, String supplierId,
                                     List<DeliveryWeekday> deliveryDays,
                                     boolean supportsDelivery,
                                     String startDate, String endDate,
                                     Map<AgreementItem, Double> items) {
        return new Agreement(agreementId, supplierId, deliveryDays, supportsDelivery, startDate, endDate, items);
    }


    public AgreementItem createAgreementItem(String itemId, String catalog, float price, float discount, int quantity) {
        return new AgreementItem(itemId, catalog, price, discount, quantity);
    }
}
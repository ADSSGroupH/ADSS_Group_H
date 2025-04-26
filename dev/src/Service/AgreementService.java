package Service;

import Domain.Agreement;
import Domain.AgreementItem;
import Domain.DeliveryWeekday;

import java.time.LocalDate;
import java.util.*;

public class AgreementService {

    // Create a new agreement between a supplier and the system
    public Agreement createAgreement(String agreementId, String supplierId,
                                     List<DeliveryWeekday> deliveryDays,
                                     boolean supportsDelivery,
                                     Map<AgreementItem, Double> items) {
        return new Agreement(agreementId, supplierId, deliveryDays, supportsDelivery, items);
    }

    // Create a new agreement item with pricing and discount details
    public AgreementItem createAgreementItem(String itemId, String catalog, float price, float discount, int quantity, String name) {
        return new AgreementItem(itemId, catalog, price, discount, quantity, name);
    }
}

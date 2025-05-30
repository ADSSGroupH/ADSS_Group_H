package Domain;

import java.util.*;

/**
 * Repository for managing Agreement objects in memory.
 * Singleton pattern ensures a single shared instance.
 */
public class AgreementRepository {

    private static AgreementRepository instance = null;
    private final Map<String, Agreement> agreements = new HashMap<>();

    private AgreementRepository() {}

    public static AgreementRepository getInstance() {
        if (instance == null) {
            instance = new AgreementRepository();
        }
        return instance;
    }

    public void add(Agreement agreement) {
        agreements.put(agreement.getAgreementId(), agreement);
    }

    public boolean remove(String id) {
        return agreements.remove(id) != null;
    }

    public boolean search(String id) {
        return agreements.containsKey(id);
    }

    public Agreement get(String id) {
        return agreements.get(id);
    }

    public Map<String, Agreement> getAll() {
        return agreements;
    }

    public Agreement createAgreement(String agreementId, String supplierId,
                                     List<DeliveryWeekday> deliveryDays,
                                     boolean supportsDelivery,
                                     Map<AgreementItem, Double> items) {
        return new Agreement(agreementId, supplierId, deliveryDays, supportsDelivery, items);
    }

    public AgreementItem createAgreementItem(String itemId, String catalog, float price, float discount, int quantity, String name) {
        return new AgreementItem(itemId, catalog, price, discount, quantity, name);
    }
}

package Domain;

/**
 * Interface for accessing all repository objects in the system.
 */
public interface IRepositoryManager {
    SupplierRepository getSupplierRepository();
    OrderRepository getOrderRepository();
    AgreementRepository getAgreementRepository();
    ItemRepository getItemRepository();
    UserRepository getUserRepository();
    ProductRepository getProductRepository();

}

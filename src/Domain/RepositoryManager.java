package Domain;

/**
 * Singleton class implementing IRepositoryManager to provide access to all repositories.
 */
public class RepositoryManager implements IRepositoryManager {

    private static RepositoryManager instance = null;

    private final SupplierRepository supplierRepository;
    private final OrderRepository orderRepository;
    private final AgreementRepository agreementRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository = UserRepository.getInstance();
    private final ProductRepository productRepository;


    @Override
    public UserRepository getUserRepository() {
        return userRepository;
    }
    private RepositoryManager() {
        supplierRepository = SupplierRepository.getInstance();
        orderRepository = OrderRepository.getInstance();
        agreementRepository = AgreementRepository.getInstance();
        itemRepository = ItemRepository.getInstance();
        this.productRepository = ProductRepository.getInstance();

    }

    public static RepositoryManager getInstance() {
        if (instance == null) {
            instance = new RepositoryManager();
        }
        return instance;
    }

    @Override
    public SupplierRepository getSupplierRepository() {
        return supplierRepository;
    }

    @Override
    public OrderRepository getOrderRepository() {
        return orderRepository;
    }

    @Override
    public AgreementRepository getAgreementRepository() {
        return agreementRepository;
    }

    @Override
    public ItemRepository getItemRepository() {
        return itemRepository;
    }

    @Override
    public ProductRepository getProductRepository() {
        return productRepository;
    }
}

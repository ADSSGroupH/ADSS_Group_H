package DomainLayer.Repositories;

import DomainLayer.EmployeeContract;
import java.util.HashMap;
import java.util.Map;

public class ContractsRepository {

    private static ContractsRepository instance;

    private final Map<String, EmployeeContract> contracts;

    private ContractsRepository() {
        this.contracts = new HashMap<>();
    }

    public static ContractsRepository getInstance() {
        if (instance == null) {
            instance = new ContractsRepository();
        }
        return instance;
    }

    public void addContract(EmployeeContract contract) {
        contracts.put(contract.getEmployeeID(), contract);
    }

    public EmployeeContract getContractByEmployeeId(String employeeId) {
        return contracts.get(employeeId);
    }

    public void updateContract(EmployeeContract contract) {
        contracts.put(contract.getEmployeeID(), contract);
    }

    public void removeContract(String employeeId) {
        contracts.remove(employeeId);
    }

    public Map<String, EmployeeContract> getAllContracts() {
        return new HashMap<>(contracts);
    }
}

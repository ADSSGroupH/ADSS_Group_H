package DomainLayer.HR.Repositories;

import DTO.HR.EmployeeContractDTO;
import Dal.HR.JdbcContractDAO;
import DomainLayer.HR.EmployeeContract;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ContractsRepository {
    private static ContractsRepository instance;

    private Map<String, EmployeeContract> contracts;
    private JdbcContractDAO jdbcContractDAO;

    private ContractsRepository() {
        this.contracts = new HashMap<>();
        this.jdbcContractDAO = new JdbcContractDAO();
    }

    public static ContractsRepository getInstance() {
        if (instance == null) {
            synchronized (ContractsRepository.class) {
                if (instance == null) {
                    instance = new ContractsRepository();
                }
            }
        }
        return instance;
    }

    // המרת Entity ל-DTO
    public EmployeeContractDTO fromEntity(EmployeeContract contract) {
        if (contract == null) return null;

        EmployeeContractDTO dto = new EmployeeContractDTO();
        dto.setId(contract.getId());
        dto.setEmployeeID(contract.getEmployeeID());
        dto.setStartDate(contract.getStartDate() != null ? contract.getStartDate().toString() : null);
        dto.setFreeDays(contract.getFreeDays());
        dto.setSicknessDays(contract.getSicknessDays());
        dto.setMonthlyWorkHours(contract.getMonthlyWorkHours());
        dto.setSocialContributions(contract.getSocialContributions());
        dto.setAdvancedStudyFund(contract.getAdvancedStudyFund());
        dto.setSalary(contract.getSalary());
        dto.setArchivedAt(contract.getArchivedAt() != null ? contract.getArchivedAt().toString() : null);
        dto.setArchived(contract.isArchived());

        return dto;
    }

    // המרת DTO ל-Entity
    public EmployeeContract toEntity(EmployeeContractDTO dto) {
        if (dto == null) return null;

        EmployeeContract contract = new EmployeeContract(null,null,0,0,0,null,null,0,null,false);
        contract.SetId(dto.getId());
        contract.setEmployeeID(dto.getEmployeeID());
        contract.setStartDate(dto.getStartDate() != null ? LocalDate.parse(dto.getStartDate()) : null);
        contract.setFreeDays(dto.getFreeDays());
        contract.setSicknessDays(dto.getSicknessDays());
        contract.setMonthlyWorkHours(dto.getMonthlyWorkHours());
        contract.setSocialContributions(dto.getSocialContributions());
        contract.setAdvancedStudyFund(dto.getAdvancedStudyFund());
        contract.setSalary(dto.getSalary());
        contract.setArchivedAt(dto.getArchivedAt() != null ? (dto.getArchivedAt()) : null);
        contract.setArchived(dto.isArchived());

        return contract;
    }


    public void addContract(EmployeeContract contract) {
        contracts.put(contract.getEmployeeID(), contract);
        try {
            EmployeeContractDTO contractDTO = fromEntity(contract);
            jdbcContractDAO.save(contractDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public EmployeeContract getContractByEmployeeId(String employeeId) {
        if (contracts.containsKey(employeeId)) {
            return contracts.get(employeeId);
        }
        try {
            var tempContract = jdbcContractDAO.findByEmployeeId(employeeId);
            if (!tempContract.isPresent()) {
                return null;
            }
            EmployeeContract contract = toEntity(tempContract.get());
            contracts.put(employeeId, contract);
            return contract;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public EmployeeContract getContractById(String id) {
        for (EmployeeContract contract : contracts.values()) {
            if (contract.getId().equals(id)) {
                return contract;
            }
        }
        try {
            var tempContract = jdbcContractDAO.findById(id);
            if (!tempContract.isPresent()) {
                return null;
            }
            EmployeeContract contract = toEntity(tempContract.get());
            contracts.put(contract.getEmployeeID(), contract);
            return contract;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateContract(EmployeeContract contract) {
        contracts.put(contract.getEmployeeID(), contract);
        try {
            EmployeeContractDTO contractDTO = fromEntity(contract);
            jdbcContractDAO.update(contractDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeContract(String employeeId) {
        contracts.remove(employeeId);
        try {
            jdbcContractDAO.deleteByEmployeeId(employeeId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeContractById(String id) {
        contracts.entrySet().removeIf(entry -> entry.getValue().getId().equals(id));
        try {
            jdbcContractDAO.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, EmployeeContract> getAllContracts() {
        try {
            List<EmployeeContractDTO> contractDTOs = jdbcContractDAO.findAll();
            Map<String, EmployeeContract> allContracts = new HashMap<>(contracts);

            for (EmployeeContractDTO dto : contractDTOs) {
                EmployeeContract contract = toEntity(dto);
                allContracts.put(contract.getEmployeeID(), contract);
                contracts.put(contract.getEmployeeID(), contract);
            }

            return allContracts;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>(contracts);
        }
    }

    public List<EmployeeContract> getActiveContracts() {
        try {
            List<EmployeeContractDTO> contractDTOs = jdbcContractDAO.findActiveContracts();
            return contractDTOs.stream()
                    .map(this::toEntity)
                    .peek(contract -> contracts.put(contract.getEmployeeID(), contract))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public List<EmployeeContract> getContractsByStartDate(LocalDate startDate) {
        try {
            List<EmployeeContractDTO> contractDTOs = jdbcContractDAO.findByStartDate(String.valueOf(startDate));
            return contractDTOs.stream()
                    .map(this::toEntity)
                    .peek(contract -> contracts.put(contract.getEmployeeID(), contract))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public List<EmployeeContract> getContractsByDateRange(String startDate, String endDate) {
        try {
            List<EmployeeContractDTO> contractDTOs = jdbcContractDAO.findByDateRange(startDate, endDate);
            return contractDTOs.stream()
                    .map(this::toEntity)
                    .peek(contract -> contracts.put(contract.getEmployeeID(), contract))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public boolean contractExists(String employeeId) {
        if (contracts.containsKey(employeeId)) {
            return true;
        }
        try {
            return jdbcContractDAO.existsByEmployeeId(employeeId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean contractExistsById(String id) {
        for (EmployeeContract contract : contracts.values()) {
            if (contract.getId().equals(id)) {
                return true;
            }
        }
        try {
            return jdbcContractDAO.exists(id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getContractCount() {
        try {
            return jdbcContractDAO.getContractCount();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}

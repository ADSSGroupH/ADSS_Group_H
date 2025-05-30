package DomainLayer;

import DomainLayer.Repositories.ContractsRepository;
import DomainLayer.Repositories.EmployeeRepository;
import DomainLayer.Repositories.RoleRepository;
import DomainLayer.Repositories.WeeklyPreferencesRepository;

import java.util.*;
import java.time.LocalDate;

public class ManagerController {

    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final ContractsRepository contractRepository;
    private final WeeklyPreferencesRepository availabilityRepository;

    public ManagerController(EmployeeRepository employeeRepository, RoleRepository roleRepository,
                             ContractsRepository contractRepository, WeeklyPreferencesRepository availabilityRepository) {
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
        this.contractRepository = contractRepository;
        this.availabilityRepository = availabilityRepository;
    }

    public void addEmployee(String id, String name, String phoneNumber, Branch branch, Set<Role> roles,
                            String bankDetails, boolean isManager, String password) {
        Employee newEmployee = new Employee(id, name, phoneNumber, branch, roles, null,
                bankDetails, false, null, isManager, password);
        employeeRepository.addEmployee(newEmployee);
        branch.UpdateEmployees(newEmployee);
    }

    public ManagerController_Status deleteEmployee(String id) {
        Employee emp = employeeRepository.getEmployeeById(id);
        if (emp != null) {
            emp.setArchived(true);
            emp.setArchivedAt(LocalDate.now());
            employeeRepository.updateEmployee(emp);
            return ManagerController_Status.EmployeeIsArchived;
        }
        return ManagerController_Status.EmployeeNotFound;
    }

    public ManagerController_Status addRoleToEmployee(String employeeId, Role newRole) {
        Employee emp = employeeRepository.getEmployeeById(employeeId);
        if (emp != null) {
            Set<Role> curr_roles = emp.getRoles();
            curr_roles.add(newRole);
            emp.setRoles(curr_roles);
            employeeRepository.updateEmployee(emp);
            return ManagerController_Status.RoleAddedToEmployee;
        }
        return ManagerController_Status.EmployeeNotFound;
    }

    public Employee getEmployeeById(String id) {
        return employeeRepository.getEmployeeById(id.trim());
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.getAllEmployees();
    }

    public EmployeeContract createContract(String employeeId, LocalDate startDate, int freeDays, int sicknessDays,
                                           int monthlyWorkHours, String socialContributions,
                                           String advancedStudyFund, int salary) {
        Employee emp = employeeRepository.getEmployeeById(employeeId);
        if (emp == null) {
            return null;
        }

        if (emp.getContract() != null) {
            EmployeeContract oldContract = emp.getContract();
            oldContract.setArchived(true);
            oldContract.setArchivedAt(LocalDate.now().toString());
            contractRepository.updateContract(oldContract);
        }

        EmployeeContract newContract = new EmployeeContract(emp.getId(), startDate, freeDays, sicknessDays,
                monthlyWorkHours, socialContributions, advancedStudyFund, salary, null, false);

        emp.setContract(newContract);
        contractRepository.addContract(newContract);
        employeeRepository.updateEmployee(emp);
        return newContract;
    }

    public ManagerController_Status deleteContract(String employeeId) {
        Employee emp = employeeRepository.getEmployeeById(employeeId);
        if (emp == null || emp.getContract() == null) {
            return ManagerController_Status.EmployeeOrContractNotFound;
        }

        EmployeeContract contract = emp.getContract();
        contract.setArchived(true);
        contract.setArchivedAt(LocalDate.now().toString());
        contractRepository.updateContract(contract);

        emp.setContract(null);
        employeeRepository.updateEmployee(emp);

        return ManagerController_Status.ContractDeleted;
    }

    public EmployeeContract getContractByEmployeeId(String employeeId) {
        Employee emp = employeeRepository.getEmployeeById(employeeId);
        return emp != null ? emp.getContract() : null;
    }

    public ManagerController_Status archiveContract(String employeeId) {
        Employee emp = employeeRepository.getEmployeeById(employeeId);
        if (emp == null) {
            return ManagerController_Status.EmployeeNotFound;
        }

        EmployeeContract contract = emp.getContract();
        if (contract == null) {
            return ManagerController_Status.NoActiveContract;
        }

        contract.setArchived(true);
        contract.setArchivedAt(LocalDate.now().toString());
        contractRepository.updateContract(contract);

        emp.setContract(null);
        employeeRepository.updateEmployee(emp);

        return ManagerController_Status.ContractArchived;
    }

    public ManagerController_Status updateEmployeeField(String employeeId, String fieldName, String newValue) {
        Employee emp = employeeRepository.getEmployeeById(employeeId);
        if (emp == null) {
            return ManagerController_Status.EmployeeNotFound;
        }

        switch (fieldName.toLowerCase()) {
            case "name":
                emp.setName(newValue);
                break;
            case "phonenumber":
                emp.setPhoneNumber(newValue);
                break;
            case "bankdetails":
                emp.setBankDetails(newValue);
                break;
            case "password":
                emp.setPassword(newValue);
                break;
            case "roles":
                String[] roleNames = newValue.split(",");
                Set<Role> newRoles = new HashSet<>();
                for (String roleName : roleNames) {
                    Role matched = roleRepository.getRoleByName(roleName.trim());
                    if (matched != null) {
                        newRoles.add(matched);
                    } else {
                        System.out.println("Role not found: " + roleName.trim());
                    }
                }
                emp.setRoles(newRoles);
                break;
            default:
                return ManagerController_Status.InvalidFormat;
        }

        employeeRepository.updateEmployee(emp);
        return ManagerController_Status.Updated;
    }

    public List<Employee> getAllEmployeesByRole(String RoleName) {
        // בדיקת תקינות הפרמטר
        if (RoleName == null || RoleName.trim().isEmpty()) {
            return new ArrayList<>(); // החזרת רשימה ריקה במקום null
        }

        Role role = roleRepository.getRoleByName(RoleName.trim());
        if (role == null) {
            return new ArrayList<>(); // החזרת רשימה ריקה במקום null
        }

        List<Employee> result = new ArrayList<>();
        for (Employee employee : employeeRepository.getAllEmployees()) {
            // שימוש בפונקציית findQualified במקום contains
            if (employeeRepository.findQualified(employee, role)) {
                result.add(employee);
            }
        }

        return result;
    }

    public List<Employee> getEmployeesByAvailability(LocalDate date, String shiftType) {
        List<Employee> availableEmployees = new ArrayList<>();
        Map<String, List<Shift>> preferences = availabilityRepository.getAllWeeklyPreferences();

        for (Map.Entry<String, List<Shift>> entry : preferences.entrySet()) {
            String employeeId = entry.getKey();
            List<Shift> preferredShifts = entry.getValue();

            for (Shift shift : preferredShifts) {
                if (shift.getDate().equals(date.toString()) && shift.getType().equalsIgnoreCase(shiftType)) {
                    Employee emp = employeeRepository.getEmployeeById(employeeId);
                    if (emp != null) {
                        availableEmployees.add(emp);
                    }
                    break;
                }
            }
        }

        return availableEmployees;
    }
}

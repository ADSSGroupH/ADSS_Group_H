package DomainLayer.HR.Controllers;

import DomainLayer.HR.*;
import DomainLayer.HR.Enums.ManagerController_Status;
import DomainLayer.HR.Repositories.*;

import java.sql.SQLException;
import java.util.*;
import java.time.LocalDate;

public class ManagerController {

    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final ContractsRepository contractRepository;
    private final WeeklyPreferencesRepository availabilityRepository;
    private final BranchRepository branchRepository;

    public ManagerController(EmployeeRepository employeeRepository, RoleRepository roleRepository,
                             ContractsRepository contractRepository, WeeklyPreferencesRepository availabilityRepository, BranchRepository branchRepository) {
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
        this.contractRepository = contractRepository;
        this.availabilityRepository = availabilityRepository;
        this.branchRepository = branchRepository;
    }

    public void addEmployee(String id, String name, String phoneNumber, String branchId, Set<Role> roles,EmployeeContract contract, String bankDetails, boolean isManager, String password) throws SQLException {
        Employee newEmployee = new Employee(id, name, phoneNumber, branchId, roles, contract,
                bankDetails, false, null, isManager, password);
        employeeRepository.addEmployee(newEmployee);
        Branch branch = branchRepository.getBranch(branchId);
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

    public List<Employee> getAllEmployees() throws SQLException {
        return employeeRepository.getAllEmployees();
    }

    public EmployeeContract createContract(String employeeId, LocalDate startDate, int freeDays, int sicknessDays,
                                           int monthlyWorkHours, String socialContributions,
                                           String advancedStudyFund, int salary) {

        EmployeeContract newContract = new EmployeeContract(employeeId, startDate, freeDays, sicknessDays,
                monthlyWorkHours, socialContributions, advancedStudyFund, salary, null, false);

        contractRepository.addContract(newContract);
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

    public List<Employee> getAllEmployeesByRole(String RoleName) throws SQLException {
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
            List<Employee> listOfQualified = employeeRepository.findQualified(employee.getBranchId(),role.getId());
            for (Employee emp : listOfQualified) {
                if (emp.getId().equals(employee.getId())) {
                    result.add(employee);
                }
            }
        }

        return result;
    }

    public List<Employee> getEmployeesByAvailability(LocalDate date, String shiftType) throws SQLException {
        List<Employee> availableEmployees = new ArrayList<>();
        Map<String, List<Shift>> preferences = availabilityRepository.getAllWeeklyPreferences();

        for (Map.Entry<String, List<Shift>> entry : preferences.entrySet()) {
            String employeeId = entry.getKey();
            List<Shift> preferredShifts = entry.getValue();

            for (Shift shift : preferredShifts) {
                if (shift.getDate().toString().equals(date.toString()) && shift.getType().equalsIgnoreCase(shiftType)) {
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

    public void addRoleToTheSystem (Role newRole) {
        roleRepository.addRole(newRole);
    }

    public List<Role> getAllRoles () {
        return roleRepository.getAllRoles();
    }

    public void addBranchToTheSystem (Branch newBranch) {
        branchRepository.addBranch(newBranch);
    }

    public List<Branch> getAllBranches () {
        return branchRepository.getAllBranches();
    }
}

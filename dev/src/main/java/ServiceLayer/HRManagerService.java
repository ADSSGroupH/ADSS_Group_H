package ServiceLayer;

import DomainLayer.HR.Branch;
import DomainLayer.HR.Controllers.ManagerController;
import DomainLayer.HR.Employee;
import DomainLayer.HR.EmployeeContract;
import DomainLayer.HR.Enums.ManagerController_Status;
import DomainLayer.HR.Repositories.*;
import DomainLayer.HR.Role;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class HRManagerService {

    private ManagerController managerController;

    public HRManagerService() {
        // יצירת מופעי הרפוזיטוריים
        EmployeeRepository employeeRepository = EmployeeRepository.getInstance();
        RoleRepository roleRepository = RoleRepository.getInstance();
        ContractsRepository contractsRepository = ContractsRepository.getInstance();
        WeeklyPreferencesRepository weeklyPreferencesRepository = WeeklyPreferencesRepository.getInstance();
        BranchRepository branchRepository = BranchRepository.getInstance();

        // יצירת ה-ManagerController עם הרפוזיטוריים הנדרשים
        this.managerController = new ManagerController(
                employeeRepository,
                roleRepository,
                contractsRepository,
                weeklyPreferencesRepository,
                branchRepository
        );
    }

    public void addEmployee(String id, String name, String phoneNumber, String branchId, Set<Role> roles,EmployeeContract contract, String bankDetails, boolean isManager, String password) throws SQLException {
        managerController.addEmployee(id, name, phoneNumber, branchId, roles,contract, bankDetails, isManager, password);
    }

    public ManagerController_Status deleteEmployee(String id) {
        return managerController.deleteEmployee(id);
    }

    public ManagerController_Status addRoleToEmployee(String employeeId, Role newRole) throws SQLException {
        return managerController.addRoleToEmployee(employeeId, newRole);
    }

    public Employee getEmployeeById(String id) {
        return managerController.getEmployeeById(id);
    }

    public List<Employee> getAllEmployees() throws SQLException {
        return managerController.getAllEmployees();
    }

    public EmployeeContract createContract(String employeeId, LocalDate startDate, int freeDays, int sicknessDays, int monthlyWorkHours, String socialContributions, String advancedStudyFund, int salary) {
        return managerController.createContract(employeeId, startDate, freeDays, sicknessDays, monthlyWorkHours, socialContributions, advancedStudyFund, salary);
    }

    public ManagerController_Status deleteContract(String employeeId) {
        return managerController.deleteContract(employeeId);
    }

    public EmployeeContract getContractByEmployeeId(String employeeId) {
        return managerController.getContractByEmployeeId(employeeId);
    }

    public ManagerController_Status archiveContract(String employeeId) {
        return managerController.archiveContract(employeeId);
    }

    public ManagerController_Status updateEmployeeField(String employeeId, String fieldName, String newValue) {
        return managerController.updateEmployeeField(employeeId, fieldName, newValue);
    }

    public List<Employee> getAllEmployeesByRole(String RoleName) throws SQLException {
        return managerController.getAllEmployeesByRole(RoleName);
    }

    public List<Employee> getEmployeesByAvailability(LocalDate date, String shiftType) throws SQLException {
        return managerController.getEmployeesByAvailability(date, shiftType);
    }

    public void addRoleToTheSystem (Role newRole) {
        managerController.addRoleToTheSystem(newRole);
    }

    public List<Role> getAllRoles () {
        return managerController.getAllRoles();
    }

    public void addBranchToTheSystem (Branch newBranch) {
        managerController.addBranchToTheSystem(newBranch);
    }

    public List<Branch> getAllBranches () {
        return managerController.getAllBranches();
    }

    public void addDriverLicense(String id, String licenseType) throws SQLException {
        managerController.addDriverLicense(id, licenseType);
    }
}
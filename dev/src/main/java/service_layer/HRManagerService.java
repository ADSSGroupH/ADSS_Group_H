package ServiceLayer;

import DomainLayer.*;
import DomainLayer.Repositories.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HRManagerService {

    private ManagerController managerController;

    public HRManagerService() {
        // יצירת מופעי הרפוזיטוריים
        EmployeeRepository employeeRepository = EmployeeRepository.getInstance();
        RoleRepository roleRepository = RoleRepository.getInstance();
        ContractsRepository contractsRepository = ContractsRepository.getInstance();
        WeeklyPreferencesRepository weeklyPreferencesRepository = WeeklyPreferencesRepository.getInstance();

        // יצירת ה-ManagerController עם הרפוזיטוריים הנדרשים
        this.managerController = new ManagerController(
                employeeRepository,
                roleRepository,
                contractsRepository,
                weeklyPreferencesRepository
        );
    }

    public void addEmployee(String id, String name, String phoneNumber, Branch branch, Set<Role> roles, String bankDetails, boolean isManager, String password) {
        managerController.addEmployee(id, name, phoneNumber, branch, roles, bankDetails, isManager, password);
    }

    public ManagerController_Status deleteEmployee(String id) {
        return managerController.deleteEmployee(id);
    }

    public ManagerController_Status addRoleToEmployee(String employeeId, Role newRole) {
        return managerController.addRoleToEmployee(employeeId, newRole);
    }

    public Employee getEmployeeById(String id) {
        return managerController.getEmployeeById(id);
    }

    public List<Employee> getAllEmployees() {
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

    public List<Employee> getAllEmployeesByRole(String RoleName) {
        return managerController.getAllEmployeesByRole(RoleName);
    }

    public List<Employee> getEmployeesByAvailability(LocalDate date, String shiftType) {
        return managerController.getEmployeesByAvailability(date, shiftType);
    }
}
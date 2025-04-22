package ServiceLayer;

import DomainLayer.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HRManagerService {

    ManagerController managerController = new ManagerController();

    public void addEmployee(String id, String name, String phoneNumber, Branch branch, Set<Role> roles, String bankDetails, boolean isManager, String password ) {
        managerController.addEmployee(id,name,phoneNumber,branch,roles,bankDetails,isManager,password);
    }

    public void deleteEmployee(String id) {
        managerController.deleteEmployee(id);
    }

    public void addRoleToEmployee(String employeeId, Role newRole) {
        managerController.addRoleToEmployee(employeeId,newRole);
    }

    public Employee getEmployeeById(String id) {
        return managerController.getEmployeeById(id);
    }

    public List<Employee> getAllEmployees() {
        return managerController.getAllEmployees();
    }

    public void createContract(String employeeId, LocalDate startDate, int freeDays, int sicknessDays, int monthlyWorkHours, String socialContributions, String advancedStudyFund, int salary) {
        managerController.createContract(employeeId,startDate,freeDays,sicknessDays,monthlyWorkHours,socialContributions,advancedStudyFund,salary);

    }


    public void deleteContract(String employeeId) {
        managerController.deleteContract(employeeId);
    }

    public EmployeeContract getContractByEmployeeId(String employeeId) {
        return managerController.getContractByEmployeeId(employeeId);
    }

    public void archiveContract(String employeeId) {
        managerController.archiveContract(employeeId);
    }


    public void updateEmployeeField(String employeeId, String fieldName, String newValue) {
        managerController.updateEmployeeField(employeeId,fieldName,newValue);
    }

    public List <Employee> getAllEmployeesByRole(String RoleName) {
        return managerController.getAllEmployeesByRole(RoleName);
    }

    public List<Employee> getEmployeesByAvailability(LocalDate date, String shiftType) {
        return managerController.getEmployeesByAvailability(date,shiftType);
    }
}

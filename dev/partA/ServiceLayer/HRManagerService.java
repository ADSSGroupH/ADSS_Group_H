import java.util.*;
import java.time.LocalDate;

public class HRManagerService {

    public void addEmployee(String id, String name, String phoneNumber, Branch branch, Set<Role> roles, String bankDetails, boolean isManager, String password ) {
        Employee newEmployee = new Employee(id, name, phoneNumber, branch, roles, null, bankDetails, false, null, isManager, password);

        DataStore.employees.add(newEmployee);
        branch.UpdateEmployees(newEmployee);
        System.out.println("Employee added successfully.");
    }

    public void deleteEmployee(String id) {
        for (Employee emp : DataStore.employees) {
            if (emp.getId().equals(id)) {
                emp.setArchived(true);
                emp.setArchivedAt(LocalDate.now().toString());
                System.out.println("Employee has been marked as archived.");
                return;
            }
        }
        System.out.println("No employee found with the given ID.");
    }

    public void addRoleToEmployee(String employeeId, Role newRole) {
        for (Employee emp : DataStore.employees) {
            if (emp.getId().equals(employeeId)) {
                emp.getRoles().add(newRole);
                System.out.println("Role added to the employee.");
                return;
            }
        }
        System.out.println("No employee found with the given ID.");
    }

    public Employee getEmployeeById(String id) {
        for (Employee emp : DataStore.employees) {
            if (emp.getId().equals(id)) {
                return emp;
            }
        }
        return null;
    }

    public List<Employee> getAllEmployees() {
        return DataStore.employees;
    }

    public void createContract(String employeeId, String startDate, int freeDays, int sicknessDays,
                               int monthlyWorkHours, String socialContributions,
                               String advancedStudyFund, int salary) {
        Employee emp = getEmployeeById(employeeId);
        if (emp == null) {
            System.out.println("No employee found with the given ID.");
            return;
        }

        // אם יש חוזה פעיל - לא נזרוק אותו, נארכב אותו
        if (emp.getContract() != null) {
            EmployeeContract oldContract = emp.getContract();
            oldContract.setArchived(true);
            oldContract.setArchivedAt(LocalDate.now().toString());
        }

        // יצירת חוזה חדש
        EmployeeContract newContract = new EmployeeContract(emp.getId(), startDate, freeDays, sicknessDays,
                monthlyWorkHours, socialContributions, advancedStudyFund, salary, null, false);

        emp.setContract(newContract); // מעדכנים את החוזה הפעיל
        DataStore.contracts.add(newContract); // שומרים את כל החוזים בארכיון כללי

        System.out.println("New contract created and set as active.");
    }


    public void deleteContract(String employeeId) {
        Employee emp = getEmployeeById(employeeId);
        if (emp == null || emp.getContract() == null) {
            System.out.println("Employee or contract not found.");
            return;
        }

        DataStore.contracts.remove(emp.getContract());
        emp.setContract(null);

        System.out.println("Contract deleted from employee.");
    }

    public EmployeeContract getContractByEmployeeId(String employeeId) {
        Employee emp = getEmployeeById(employeeId);
        if (emp != null) {
            return emp.getContract();
        }
        return null;
    }

    public void archiveContract(String employeeId) {
        Employee emp = getEmployeeById(employeeId);

        if (emp == null) {
            System.out.println("Employee not found.");
            return;
        }

        EmployeeContract contract = emp.getContract();
        if (contract == null) {
            System.out.println("No active contract found for employee " + emp.getName() + ".");
            return;
        }

        contract.setArchived(true);
        contract.setArchivedAt(LocalDate.now().toString());
        emp.setContract(null); // מסירים את הקישור מהעובד

        System.out.println("Contract archived successfully.");
    }


    public void updateEmployeeField(String employeeId, String fieldName, String newValue) {
        Employee emp = getEmployeeById(employeeId);

        if (emp == null) {
            System.out.println("Employee not found.");
            return;
        }

        switch (fieldName.toLowerCase()) {
            case "name":
                emp.setName(newValue);
                System.out.println("Name updated successfully.");
                break;

            case "phonenumber":
                emp.setPhoneNumber(newValue);
                System.out.println("Phone number updated successfully.");
                break;

            case "bankdetails":
                emp.setBankDetails(newValue);
                System.out.println("Bank details updated successfully.");
                break;

            default:
                System.out.println("Invalid field name. Allowed: name, phoneNumber, bankDetails.");
        }
    }
    public List <Employee> getAllEmployeesByRole(String RoleName) {
        List <Employee> result= new ArrayList<>();
        for (Role role : DataStore.roles){
            if (role.getName().equals(RoleName)){ //Role exists!
                for (Employee employee : DataStore.employees){
                    if (employee.getRoles().contains(role)){
                        result.add(employee);
                    }
                }
                if (result.isEmpty()){
                    System.out.println("There are no workers qualified for this role");
                }

                return result;

            }
        }
        System.out.println("This Role Doesn't exist");
        return null;
    }



}

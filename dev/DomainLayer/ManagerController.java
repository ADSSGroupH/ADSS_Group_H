package DomainLayer;

import java.util.*;
import java.time.LocalDate;



public class ManagerController {

    public void addEmployee(String id, String name, String phoneNumber, Branch branch, Set<Role> roles, String bankDetails, boolean isManager, String password ) {
        Employee newEmployee = new Employee(id, name, phoneNumber, branch, roles, null, bankDetails, false, null, isManager, password);

        DAO.employees.add(newEmployee);
        branch.UpdateEmployees(newEmployee);
    }

    public ManagerController_Status deleteEmployee(String id) {
        for (Employee emp : DAO.employees) {
            if (emp.getId().equals(id)) {
                emp.setArchived(true);
                emp.setArchivedAt(LocalDate.now());
                return ManagerController_Status.EmployeeIsArchived;
            }
        }
        return ManagerController_Status.EmployeeNotFound;
    }

    public ManagerController_Status addRoleToEmployee(String employeeId, Role newRole) {
        for (Employee emp : DAO.employees) {
            if (emp.getId().equals(employeeId)) {
                Set<Role> curr_roles = emp.getRoles();
                curr_roles.add(newRole);
                emp.setRoles(curr_roles);
                return ManagerController_Status.RoleAddedToEmployee;
            }
        }
        return ManagerController_Status.EmployeeNotFound;
    }

    public Employee getEmployeeById(String id) {
        for (Employee emp : DAO.employees) {
            if (emp.getId().trim().equals(id.trim())) {
                return emp;
            }
        }
        return null;
    }

    public List<Employee> getAllEmployees() {
        return DAO.employees;
    }

    public EmployeeContract createContract(String employeeId, LocalDate startDate, int freeDays, int sicknessDays, int monthlyWorkHours, String socialContributions, String advancedStudyFund, int salary) {
        Employee emp = getEmployeeById(employeeId);
        if (emp == null) {
            return null;
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
        DAO.contracts.add(newContract); // שומרים את כל החוזים בארכיון כללי
        return newContract;
    }


    public ManagerController_Status deleteContract(String employeeId) {
        Employee emp = getEmployeeById(employeeId);
        if (emp == null || emp.getContract() == null) {
            return ManagerController_Status.EmployeeOrContractNotFound;
        }
        emp.getContract().setArchived(true); //making the contract not active.
        emp.getContract().setArchivedAt(LocalDate.now().toString());
        emp.setContract(null);


        return ManagerController_Status.ContractDeleted;
    }

    public EmployeeContract getContractByEmployeeId(String employeeId) {
        Employee emp = getEmployeeById(employeeId);
        if (emp != null) {
            return emp.getContract();
        }
        return null;
    }

    public ManagerController_Status archiveContract(String employeeId) {
        Employee emp = getEmployeeById(employeeId);

        if (emp == null) {
            return ManagerController_Status.EmployeeNotFound;
        }

        EmployeeContract contract = emp.getContract();
        if (contract == null) {
            return ManagerController_Status.NoActiveContract;
        }

        contract.setArchived(true);
        contract.setArchivedAt(LocalDate.now().toString());
        emp.setContract(null); // מסירים את הקישור מהעובד

        return ManagerController_Status.ContractArchived;
    }


    public ManagerController_Status updateEmployeeField(String employeeId, String fieldName, String newValue) {
        Employee emp = getEmployeeById(employeeId);

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
                    Role matched = null;
                    for (Role r : DAO.roles) {
                        if (r.getName().equalsIgnoreCase(roleName.trim())) {
                            matched = r;
                            break;
                        }
                    }

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
        return ManagerController_Status.Updated;
    }
    public List <Employee> getAllEmployeesByRole(String RoleName) {
        List <Employee> result= new ArrayList<>();
        for (Role role : DAO.roles){
            if (role.getName().equalsIgnoreCase(RoleName.trim())){ //Role exists!
                for (Employee employee : DAO.employees) {
                    if (employee.getRoles().contains(role)){
                        result.add(employee);
                    }
                }

                return result;

            }
        }

        return null;
    }

    public List<Employee> getEmployeesByAvailability(LocalDate date, String shiftType) {
        List<Employee> availableEmployees = new ArrayList<>();

        for (Map.Entry<String, List<Shift>> entry : DAO.WeeklyPreferneces.entrySet()) {
            String employeeId = entry.getKey();
            List<Shift> preferredShifts = entry.getValue();

            for (Shift shift : preferredShifts) {
                if (shift.getDate().equals(date.toString()) && shift.getType().equalsIgnoreCase(shiftType)) {
                    Employee emp = getEmployeeById(employeeId);
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

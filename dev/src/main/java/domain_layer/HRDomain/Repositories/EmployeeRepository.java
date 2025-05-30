package DomainLayer.Repositories;

import DomainLayer.*;
import ServiceLayer.HRManagerService;

import java.time.LocalDate;
import java.util.*;

public class EmployeeRepository {
    private static EmployeeRepository instance;

    private Map<String, Employee> employees;
    private DAO dao;

    private EmployeeRepository() {
        employees = new HashMap<>();
        dao = new DAO();
        loadEmployeesFromDAO();
    }

    public static EmployeeRepository getInstance() {
        if (instance == null) {
            synchronized (EmployeeRepository.class) {
                if (instance == null) {
                    instance = new EmployeeRepository();
                }
            }
        }
        return instance;
    }

    private void loadEmployeesFromDAO() {
        for (Employee emp : dao.employees) {
            employees.put(emp.getId(), emp);
        }
    }

    public void addEmployee(Employee newEmployee) {
        if (!employees.containsKey(newEmployee.getId())) {
            employees.put(newEmployee.getId(), newEmployee);
            dao.employees.add(newEmployee); // סנכרון ל-DAO
        }
    }

    // פונקציית עדכון לעובד קיים - חשוב מאוד להוסיף!
    public void updateEmployee(Employee updatedEmployee) {
        if (updatedEmployee == null || updatedEmployee.getId() == null) return;

        if (employees.containsKey(updatedEmployee.getId())) {
            employees.put(updatedEmployee.getId(), updatedEmployee);
            // עדכון ב-DAO: מחליף את האובייקט הישן ברשימה
            for (int i = 0; i < dao.employees.size(); i++) {
                if (dao.employees.get(i).getId().equals(updatedEmployee.getId())) {
                    dao.employees.set(i, updatedEmployee);
                    break;
                }
            }
        }
    }

    public Employee getEmployeeById(String id) {
        if (employees.containsKey(id)) {
            return employees.get(id);
        }
        // טען מה-DAO במקרה והקאש לא מעודכן
        for (Employee emp : dao.employees) {
            if (emp.getId().equals(id)) {
                employees.put(id, emp);
                return emp;
            }
        }
        return null;
    }

    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employees.values());
    }

    // מחיקת עובד ע"י סימון ארכיון
    public void deleteEmployee(String id) {
        Employee emp = employees.get(id);
        if (emp != null) {
            emp.setArchived(true);
            emp.setArchivedAt(LocalDate.now());
            updateEmployee(emp); // עדכון ב-DAO
        }
    }

    // הוספת תפקיד לעובד
    public void addRoleToEmployee(String employeeId, Role newRole) {
        Employee emp = getEmployeeById(employeeId);
        if (emp != null) {
            Set<Role> roles = emp.getRoles();
            if (roles == null) {
                roles = new HashSet<>();
            }
            roles.add(newRole);
            emp.setRoles(roles);
            updateEmployee(emp); // עדכון ב-DAO
        }
    }

    // מחזיר את כל העובדים במשמרת לפי ID
    public List<Employee> getAllEmployeesInShift(String shiftId) {
        List<Shift> allShifts = DAO.shifts;
        List<Employee> employeesInShift = new ArrayList<>();

        for (Shift shift : allShifts) {
            if (shift.getId().equals(shiftId)) {
                for (ShiftAssignment assignment : shift.getAssignments()) {
                    employeesInShift.add(assignment.getEmployee());
                }
                return employeesInShift;
            }
        }
        return Collections.emptyList();
    }

    // מחזיר רשימות עובדים זמינים ובלתי זמינים לתפקיד במשמרת
    public List<List<Employee>> findAvailableForShift(Shift targetShift, Role requiredRole) {
        HRManagerService managerService = new HRManagerService();
        List<Employee> qualifiedEmployees = managerService.getAllEmployeesByRole(requiredRole.getName());

        if (qualifiedEmployees == null || qualifiedEmployees.isEmpty()) {
            return null;
        }

        List<Employee> available = new ArrayList<>();
        List<Employee> unavailable = new ArrayList<>();
        List<List<Employee>> result = new ArrayList<>();

        for (Employee employee : qualifiedEmployees) {
            boolean alreadyAssigned = targetShift.getAssignments().stream()
                    .anyMatch(assignment -> assignment.getEmployee().getId().equals(employee.getId()));
            if (alreadyAssigned) continue;

            boolean isAvailable = DAO.WeeklyPreferneces.containsKey(employee.getId()) &&
                    DAO.WeeklyPreferneces.get(employee.getId()).contains(targetShift);

            if (isAvailable) {
                available.add(employee);
            } else {
                unavailable.add(employee);
            }
        }

        List<Employee> allOptions = new ArrayList<>();
        allOptions.addAll(available);
        allOptions.addAll(unavailable);

        result.add(allOptions);  // כל האפשרויות
        result.add(available);   // זמינים
        result.add(unavailable); // לא זמינים

        return result;
    }

    // מחזיר את כל השיבוצים השבועיים של עובד
    public List<ShiftAssignment> getWeeklyAssignmentsForEmployee(String employeeId) {
        List<ShiftAssignment> result = new ArrayList<>();

        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.with(java.time.DayOfWeek.SUNDAY);
        LocalDate weekEnd = weekStart.plusDays(6);

        for (ShiftAssignment assignment : DAO.assignments) {
            Shift shift = assignment.getShift();
            if (shift == null) continue;

            LocalDate shiftDate = shift.getDate();
            if (!shiftDate.isBefore(weekStart) && !shiftDate.isAfter(weekEnd)) {
                if (assignment.getEmployee().getId().equals(employeeId)) {
                    result.add(assignment);
                }
            }
        }
        return result;
    }

    // בדיקה אם עובד כשיר לתפקיד
    public boolean findQualified(Employee employee, Role role) {
        if (employee == null || role == null) return false;
        if (employee.getRoles() == null) return false;

        return employee.getRoles().stream()
                .anyMatch(r -> r.getId().equals(role.getId()));
    }
}

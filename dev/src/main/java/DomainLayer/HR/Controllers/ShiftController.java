package DomainLayer.HR.Controllers;

import DomainLayer.HR.Employee;
import DomainLayer.HR.Enums.Shift_Status;
import DomainLayer.HR.Repositories.*;
import DomainLayer.HR.Role;
import DomainLayer.HR.Shift;
import DomainLayer.HR.ShiftAssignment;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class ShiftController {

    private ShiftRepository shiftRepository;
    private RoleRepository roleRepository;
    private AssignmentRepository assignmentRepository;
    private WeeklyPreferencesRepository weeklyPreferencesRepository;
    private EmployeeRepository employeeRepository;

    public ShiftController() {
        this.shiftRepository = ShiftRepository.getInstance();
        this.roleRepository = RoleRepository.getInstance();
        this.assignmentRepository = AssignmentRepository.getInstance();
        this.weeklyPreferencesRepository = WeeklyPreferencesRepository.getInstance();
        this.employeeRepository = EmployeeRepository.getInstance();
    }

    public Shift createShift(String id, LocalDate date, String startTime, String endTime, String type, Employee shiftManager, List<Role> requiredRoles, List<ShiftAssignment> assignments) throws Exception {
        // בדוק אם תפקיד shift manager כבר קיים
        Role shift_Manager = roleRepository.getAllRoles().stream()
                .filter(r -> r.getName().equalsIgnoreCase("shift manager"))
                .findFirst()
                .orElse(null);

        // אם לא קיים – צור חדש עם ID קבוע
        if (shift_Manager == null) {
            shift_Manager = new Role("1", "shift manager");
            roleRepository.addRole(shift_Manager);
        }

        // הוסף לרשימת התפקידים הנדרשים אם לא קיים שם
        if (requiredRoles.stream().noneMatch(r -> r.getName().equalsIgnoreCase("shift manager"))) {
            requiredRoles.add(shift_Manager);
        }

        Shift newShift = new Shift(id, date, startTime, endTime, type, shiftManager, requiredRoles, assignments, LocalDate.now());
        shiftRepository.addShift(newShift);
        return newShift;
    }

    public Shift_Status updateShiftField(String shiftId, String fieldName, Object newValue) {
        Shift shift = getShiftById(shiftId);

        if (shift == null) {
            return Shift_Status.SHIFT_NOT_FOUND;
        }

        switch (fieldName.toLowerCase()) {
            case "shiftmanager":
                if (newValue instanceof Employee) {
                    shift.setShiftManager((Employee) newValue);
                    shiftRepository.updateShift(shift);
                    return Shift_Status.SUCCESS;
                }
                return Shift_Status.INVALID_VALUE_TYPE;

            case "assignments":
                if (newValue instanceof List<?>) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<ShiftAssignment> assignments = (List<ShiftAssignment>) newValue;
                        shift.setAssignments(assignments);
                        shiftRepository.updateShift(shift);
                        return Shift_Status.SUCCESS;
                    } catch (ClassCastException e) {
                        return Shift_Status.INVALID_LIST_TYPE;
                    }
                }
                return Shift_Status.INVALID_VALUE_TYPE;

            case "requiredroles":
                if (newValue instanceof List<?>) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<Role> roles = (List<Role>) newValue;
                        shift.setRequiredRoles(roles);
                        shiftRepository.updateShift(shift);
                        return Shift_Status.SUCCESS;
                    } catch (ClassCastException e) {
                        return Shift_Status.INVALID_LIST_TYPE;
                    }
                }
                return Shift_Status.INVALID_VALUE_TYPE;

            default:
                return Shift_Status.INVALID_FIELD;
        }
    }

    public void archiveShift(Shift shift) throws Exception {
        shift.setArchived(true);
        shift.setArchivedAt(LocalDate.now());
        shiftRepository.updateShift(shift);
    }

    public boolean deleteShift(String shiftId) {
        Shift shift = shiftRepository.getShift(shiftId);
        if (shift != null) {
            if (!(shift.getAssignments().isEmpty()) ) {
                return false; // אי אפשר למחוק משמרת עם עובדים מוקצים
            } else {
                shiftRepository.removeShift(shiftId); // מחיקה פיזית
                return true;
            }
        }
        return false;
    }


    // searching a shift by id
    public Shift getShiftById(String shiftId) {
        return shiftRepository.getShift(shiftId);
    }

    public List<Shift> getAllShiftsForThisWeek() throws SQLException {
        List<Shift> allShifts = new ArrayList<>();

        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.with(java.time.temporal.TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY));
        LocalDate weekEnd = weekStart.plusDays(6);

        for (Shift shift : shiftRepository.getAllShifts()) {
            if (shift.isArchived()) continue;

            try {
                LocalDate shiftDate = shift.getDate();
                if (!shiftDate.isBefore(weekStart) && !shiftDate.isAfter(weekEnd)) {
                    allShifts.add(shift);
                }
            } catch (Exception e) {
                // לא מדפיסים כאן – נוכל לדווח על שגיאות בפונקציה נפרדת בעתיד אם נרצה
            }
        }

        return allShifts;
    }

    public List<ShiftAssignment> getAllAssignmentsForThisWeek() {
        List<ShiftAssignment> weeklyAssignments = new ArrayList<>();

        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.with(java.time.DayOfWeek.SUNDAY);
        LocalDate weekEnd = weekStart.plusDays(6);

        for (ShiftAssignment assignment : assignmentRepository.getAllAssignments()) {
            Shift shift = shiftRepository.getShift(assignment.getShiftId());
            if (shift == null) continue;

            try {
                LocalDate shiftDate = shift.getDate();
                if (!shiftDate.isBefore(weekStart) && !shiftDate.isAfter(weekEnd)) {
                    weeklyAssignments.add(assignment);
                }
            } catch (Exception e) {
                return null;
            }
        }

        return weeklyAssignments;
    }

    public List<Shift> getShiftsByDate(String date) throws SQLException {
        List<Shift> result = new ArrayList<>();

        for (Shift shift : shiftRepository.getAllShifts()) {
            if (!shift.isArchived() && shift.getDate().equals(date)) {
                result.add(shift);
            }
        }

        return result;
    }

    // פונקציה לשליפת כל העובדים המוקצים למשמרת לפי ה-ID שלה
    public List<Employee> getAllTheEmployeesInAShift(String shiftId) throws SQLException {
        List<Shift> allShifts = this.getAllShiftsForThisWeek();
        List<Employee> employeesInShift = new ArrayList<>();

        // מחפשים את המשמרת לפי ה-ID שלה
        for (Shift shift : allShifts) {
            if (shift.getId().equals(shiftId)) {
                // עבור כל ShiftAssignment, נוסיף את העובד לרשימה
                for (ShiftAssignment assignment : shift.getAssignments()) {
                    employeesInShift.add(assignment.getEmployee());
                }
                return employeesInShift;
            }
        }
        return null;  // אם לא מצאנו את המשמרת, מחזירים null
    }

    public List<ShiftAssignment> getWeeklyAssignmentsForEmployee(String employeeId) {
        List<ShiftAssignment> result = new ArrayList<>();
        if (this.getAllAssignmentsForThisWeek() == null) {
            return null;
        }

        for (ShiftAssignment assignment : this.getAllAssignmentsForThisWeek()) {
            if (assignment.getEmployee().getId().equals(employeeId)) {
                result.add(assignment);
            }
        }
        return result;
    }

    // מציג את כל השיבוצים של השבוע לכל העובדים
    public List<String> MakeWeeklyAssignmentReport() throws SQLException {
        Map<String, Map<String, List<Shift>>> groupedShifts = new TreeMap<>();

        for (Shift shift : shiftRepository.getAllShifts()) {
            String date = String.valueOf(shift.getDate());
            String type = shift.getType();

            groupedShifts.putIfAbsent(date, new TreeMap<>());
            groupedShifts.get(date).putIfAbsent(type, new ArrayList<>());
            groupedShifts.get(date).get(type).add(shift);
        }

        List<String> report = new ArrayList<>();
        if (groupedShifts.isEmpty()) {
            report.add("Weekly Assignment Report: No shifts found for this week.");
            return report;
        }

        report.add("Weekly Assignment Report (All Shifts):");

        for (String date : groupedShifts.keySet()) {
            report.add("");
            report.add("Date: " + date);
            Map<String, List<Shift>> shiftsByType = groupedShifts.get(date);

            List<String> orderedTypes = new ArrayList<>();
            if (shiftsByType.containsKey("Morning")) orderedTypes.add("Morning");
            if (shiftsByType.containsKey("Evening")) orderedTypes.add("Evening");

            for (String type : orderedTypes) {
                report.add("  Shift Type: " + type);

                for (Shift shift : shiftsByType.get(type)) {
                    if (shift.getShiftManager() != null) {
                        report.add("     Shift Manager: " + shift.getShiftManager().getName());
                    } else {
                        report.add("     Shift Manager: No assignment");
                        report.add("     [Warning] No shift manager assigned to shift ID '" + shift.getId() + "' on " + shift.getDate() + " (" + shift.getType() + ")");
                    }

                    List<Role> requiredRoles = shift.getRequiredRoles();
                    List<ShiftAssignment> assignments = shift.getAssignments();

                    for (Role role : requiredRoles) {
                        if (role.getName().equalsIgnoreCase("Shift Manager")) continue;

                        boolean assigned = false;
                        for (ShiftAssignment assignment : assignments) {
                            if (assignment.getRole().getId().equals(role.getId())) {
                                report.add("     " + role.getName() + ": " + assignment.getEmployee().getName());
                                assigned = true;
                                break;
                            }
                        }
                        if (!assigned) {
                            report.add("     " + role.getName() + ": No assignment");
                        }
                    }
                }
            }
        }

        return report;
    }

    public ShiftAssignment CheckIfRoleIsFilled(Shift shift, Role role) throws SQLException {
        // בדיקת תקינות פרמטרים
        if (shift == null || role == null || shift.getId() == null || role.getId() == null) {
            return null;
        }

        // שימוש ב-repository החדש
        AssignmentRepository assignmentRepo = AssignmentRepository.getInstance();
        List<ShiftAssignment> assignments = assignmentRepo.findByShiftAndRole(shift.getId(), role.getId());

        // אם יש משימות, החזר את הראשונה (כיון שתפקיד צריך להיות ייחודי במשמרת)
        if (!assignments.isEmpty()) {
            return assignments.get(0);
        }

        return null; // התפקיד לא מלא במשמרת הזו
    }

    public List<List<Employee>> AvailableAndUnavailableEmpForRoleInShift(Shift targetShift, Role requiredRole) { //gets all the employees that submitted this shift and are qualified for the given role
        return employeeRepository.findAvailableAndUnavailableForShift(targetShift, requiredRole);
    }

    public void createShiftAssignment(Shift shift, Role requiredRole, Employee SelectedEmployee) throws SQLException { //the assignment is for one employee to one role
        List<ShiftAssignment> newAssignments = new ArrayList<>(shift.getAssignments());
        ShiftAssignment assignment = new ShiftAssignment(SelectedEmployee, shift.getId(), requiredRole, null);
        newAssignments.add(assignment);
        assignmentRepository.saveAssignment(assignment);

        if (requiredRole.getId().equals("1")) {
            shift.setShiftManager(SelectedEmployee);
        }

        shift.setAssignments(newAssignments);
        shiftRepository.updateShift(shift);
    }

    public List<Shift> getShiftsBetween(LocalDate from, LocalDate to) throws SQLException {
        List<Shift> result = new ArrayList<>();
        for (Shift shift : shiftRepository.getAllShifts()) {
            LocalDate shiftDate = shift.getDate();
            if ((shiftDate.isEqual(from) || shiftDate.isAfter(from)) &&
                    (shiftDate.isEqual(to) || shiftDate.isBefore(to))) {
                result.add(shift);
            }
        }
        return result;
    }

    public Shift_Status DeleteShiftAssignment(String shiftId, String employeeIdToDelete) {
        Shift shift = shiftRepository.getShift(shiftId);
        if (shift != null) {
            Iterator<ShiftAssignment> iterator = shift.getAssignments().iterator();
            while (iterator.hasNext()) {
                ShiftAssignment assignment = iterator.next();
                if (assignment.getEmployee().getId().equals(employeeIdToDelete)) {
                    iterator.remove();
                    assignment.setArchived(true);
                    assignment.setArchiveDate(LocalDate.now());
                    assignmentRepository.updateAssignment(assignment);

                    if (shift.getShiftManager() != null &&
                            shift.getShiftManager().getId().equals(employeeIdToDelete)) {
                        shift.setShiftManager(null);
                        shiftRepository.updateShift(shift);
                        return Shift_Status.SHIFT_MANAGER_REMOVED;
                    }

                    shiftRepository.updateShift(shift);
                    return Shift_Status.SUCCESS;
                }
            }
            return Shift_Status.EMPLOYEE_NOT_FOUND;
        }
        return Shift_Status.SHIFT_NOT_FOUND;
    }
    public List<Shift> getAllShifts() throws SQLException {
        return shiftRepository.getAllShifts();
    }

    public List<ShiftAssignment> getAllAssignments() {
        return assignmentRepository.getAllAssignments();
    }
}
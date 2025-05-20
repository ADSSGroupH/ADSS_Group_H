package DomainLayer;

import ServiceLayer.HRManagerService;

import java.time.LocalDate;
import java.util.*;

public class ShiftController {

    public Shift createShift(String id, LocalDate date, String startTime, String endTime, String type, Employee shiftManager, List<Role> requiredRoles, List<ShiftAssignment> assignments) {
        // בדוק אם תפקיד shift manager כבר קיים
        Role shift_Manager = DAO.roles.stream()
                .filter(r -> r.getName().equalsIgnoreCase("shift manager"))
                .findFirst()
                .orElse(null);

        // אם לא קיים – צור חדש עם ID קבוע
        if (shift_Manager == null) {
            shift_Manager = new Role("1", "shift manager");
            DAO.roles.add(shift_Manager);
        }

        // הוסף לרשימת התפקידים הנדרשים אם לא קיים שם
        if (requiredRoles.stream().noneMatch(r -> r.getName().equalsIgnoreCase("shift manager"))) {
            requiredRoles.add(shift_Manager);
        }

        Shift newShift = new Shift(id, date, startTime, endTime, type, shiftManager, requiredRoles, assignments, LocalDate.now());
        this.archiveShift(newShift);
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
                    return Shift_Status.SUCCESS;
                }
                return Shift_Status.INVALID_VALUE_TYPE;

            case "assignments":
                if (newValue instanceof List<?>) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<ShiftAssignment> assignments = (List<ShiftAssignment>) newValue;
                        shift.setAssignments(assignments);
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



    public void archiveShift(Shift shift) {
        DAO.shifts.add(shift);  // adding the shift to the DataStore
        shift.setArchived(true);
        shift.setArchivedAt(LocalDate.now());
    }


    public boolean deleteShift(String shiftId) {
        Iterator<Shift> iterator = DAO.shifts.iterator();
        while (iterator.hasNext()) {
            Shift shift = iterator.next();
            if (shift.getId().equals(shiftId)) {
                if (!shift.getAssignments().isEmpty()) {
                    return false;
                } else {
                    shift.setArchived(true);  // shift is not in use anymore so it's archived
                    shift.setArchivedAt(LocalDate.now());
                    return true;
                }

            }
        }
        return false;  // we didn't find a matching shift.
    }

    // searching a shift by id
    public Shift getShiftById(String shiftId) {
        for (Shift shift : DAO.shifts) {
            if (shift.getId().equals(shiftId)) {
                return shift;
            }
        }
        return null;  // didn't find a matching shift
    }


    public List<Shift> getAllShiftsForThisWeek() {
        List<Shift> allShifts = new ArrayList<>();

        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.with(java.time.temporal.TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY));
        LocalDate weekEnd = weekStart.plusDays(6);

        for (Shift shift : DAO.shifts) {
            if (!shift.isArchived()) continue;

            try {
                LocalDate shiftDate = shift.getDate();  // assuming yyyy-MM-dd format
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

        for (ShiftAssignment assignment : DAO.assignments) {
            Shift shift = assignment.getShift();
            if (shift == null) continue;

            try {
                LocalDate shiftDate = shift.getDate(); // assuming "yyyy-MM-dd"
                if (!shiftDate.isBefore(weekStart) && !shiftDate.isAfter(weekEnd)) {
                    weeklyAssignments.add(assignment);
                }
            } catch (Exception e) {
                return null;

            }
        }

        return weeklyAssignments;
    }


    public List<Shift> getShiftsByDate(String date) {
        List<Shift> result = new ArrayList<>();

        for (Shift shift : DAO.shifts) {
            if (!shift.isArchived() && shift.getDate().equals(date)) {
                result.add(shift);
            }
        }

        return result;
    }

    // פונקציה לשליפת כל העובדים המוקצים למשמרת לפי ה-ID שלה
    public List<Employee> getAllTheEmployeesInAShift(String shiftId) {
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

    public List<ShiftAssignment> getWeeklyAssignmentsForEmployee (String employeeId) {
        List<ShiftAssignment> result = new ArrayList<>();
        if (this.getAllAssignmentsForThisWeek() == null){
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
    public List<String> MakeWeeklyAssignmentReport() {
        Map<String, Map<String, List<Shift>>> groupedShifts = new TreeMap<>();

        for (Shift shift : DAO.shifts) {
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



    public ShiftAssignment CheckIfRoleIsFilled(Shift Shift, Role role) { //returns the assignment if the role is already filled
            for (ShiftAssignment existing : Shift.getAssignments()) {
                if (existing.getRole().getName().equalsIgnoreCase(role.getName())) {
                    return existing;
                }
            }
            return null;
    }

    public List<List<Employee>> AvailableAndUnavailableEmpForRoleInShift(Shift targetShift, Role requiredRole) { //gets all the employees that submitted this shift and are qualified for the given role
            HRManagerService managerService = new HRManagerService();
            List<Employee> qualifiedEmployees = managerService.getAllEmployeesByRole(requiredRole.getName());

            if (qualifiedEmployees == null || qualifiedEmployees.isEmpty()) {
                return null;
            }

            List<Employee> available = new ArrayList<>();
            List<Employee> unavailable = new ArrayList<>();
            List<List<Employee>> result = new ArrayList<>(); //first element will be allOptions, second will be available and the third will be unavailable.

            for (Employee employee : qualifiedEmployees) {
                boolean alreadyAssigned = false;
                for (ShiftAssignment assignment : targetShift.getAssignments()) {
                    if (assignment.getEmployee().getId().equals(employee.getId())) {
                        alreadyAssigned = true;
                        break; //irrelevant so he won't be in the list at all
                    }
                }
                if (alreadyAssigned)
                    continue;

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

            result.add(allOptions);
            result.add(available);
            result.add(unavailable);
            return result;

    }


    public void createShiftAssignment(Shift shift, Role requiredRole, Employee SelectedEmployee) { //the assignment is for one employee to *one* role

        List<ShiftAssignment> newAssignments = new ArrayList<>(shift.getAssignments());
            ShiftAssignment assignment = new ShiftAssignment(SelectedEmployee, shift, requiredRole, null);
            newAssignments.add(assignment);
            DAO.assignments.add(assignment);

            if (requiredRole.getId().equals("1")) {
                shift.setShiftManager(SelectedEmployee);
            }

        shift.setAssignments(newAssignments);

    }



    public List<Shift> getShiftsBetween(LocalDate from, LocalDate to) {
        List<Shift> result = new ArrayList<>();
        for (Shift shift : DAO.shifts) {
            LocalDate shiftDate = shift.getDate();
            if ((shiftDate.isEqual(from) || shiftDate.isAfter(from)) &&
                    (shiftDate.isEqual(to) || shiftDate.isBefore(to))) {
                result.add(shift);
            }
        }
        return result;
    }
    public Shift_Status DeleteShiftAssignment(String shiftId, String employeeIdToDelete) {
        for (Shift shift : DAO.shifts) {
            if (shift.getId().equals(shiftId)) {
                Iterator<ShiftAssignment> iterator = shift.getAssignments().iterator();
                while (iterator.hasNext()) {
                    ShiftAssignment assignment = iterator.next();
                    if (assignment.getEmployee().getId().equals(employeeIdToDelete)) {
                        iterator.remove();
                        assignment.setArchived(true);
                        assignment.setArchiveDate(LocalDate.now());

                        if (shift.getShiftManager() != null &&
                                shift.getShiftManager().getId().equals(employeeIdToDelete)) {
                            shift.setShiftManager(null);
                            return Shift_Status.SHIFT_MANAGER_REMOVED;
                        }

                        return Shift_Status.SUCCESS;
                    }
                }
                return Shift_Status.EMPLOYEE_NOT_FOUND;
            }
        }
        return Shift_Status.SHIFT_NOT_FOUND;
    }

}

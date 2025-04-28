package DomainLayer;

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

//    public Shift createShift(String id, LocalDate date, String startTime, String endTime, String type, Employee shiftManager, List<Role> requiredRoles, List<ShiftAssignment> assignments) {
//        // creating the shift
//        Role shift_Manager = new Role("1", "shift manager"); //make sure there is always a shift manager in a shift
//        requiredRoles.add(shift_Manager);
//        if (!DAO.roles.contains(shift_Manager)) {
//            DAO.roles.add(shift_Manager);
//        }
//        Shift newShift = new Shift(id, date, startTime, endTime, type, shiftManager, requiredRoles, assignments,LocalDate.now());
//
//        // adding the shift to the archive (data store)
//        this.archiveShift(newShift);
//
//
//        return newShift;
//    }

    public void updateShiftField(String shiftId, String fieldName, Object newValue) {
        Shift shift = getShiftById(shiftId);

        if (shift == null) {
            System.out.println("Shift not found.");
            return;
        }

        switch (fieldName.toLowerCase()) {
            case "shiftmanager":
                if (newValue instanceof Employee) {
                    shift.setShiftManager((Employee) newValue);
                    System.out.println("Shift manager updated successfully.");
                } else {
                    System.out.println("Invalid value. Expected an Employee.");
                }
                break;

            case "assignments":
                if (newValue instanceof List<?>) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<ShiftAssignment> assignments = (List<ShiftAssignment>) newValue;
                        shift.setAssignments(assignments);
                        System.out.println("Assignments updated successfully.");
                    } catch (ClassCastException e) {
                        System.out.println("Invalid assignments list.");
                    }
                } else {
                    System.out.println("Invalid value. Expected a list of ShiftAssignment.");
                }
                break;

            case "requiredroles":
                if (newValue instanceof List<?>) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<Role> roles = (List<Role>) newValue;
                        shift.setRequiredRoles(roles);
                        System.out.println("Required roles updated successfully.");
                    } catch (ClassCastException e) {
                        System.out.println("Invalid roles list.");
                    }
                } else {
                    System.out.println("Invalid value. Expected a list of Role.");
                }
                break;

            default:
                System.out.println("Invalid field name. Allowed: shiftManager, assignments, requiredRoles.");
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
                    System.out.println("you can't delete a shift if there are employees assigned to it!");
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

        // לחשב את יום ראשון הבא (אפילו אם היום ראשון – נלך לראשון הבא)
        LocalDate weekStart = today.with(java.time.temporal.TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY));
        LocalDate weekEnd = weekStart.plusDays(6);

        System.out.println("Checking shifts between: " + weekStart + " to " + weekEnd);

        for (Shift shift : DAO.shifts) {
            if (!shift.isArchived()) continue;

            try {
                LocalDate shiftDate = shift.getDate();  // assuming yyyy-MM-dd format
                if (!shiftDate.isBefore(weekStart) && !shiftDate.isAfter(weekEnd)) {
                    allShifts.add(shift);
                }
            } catch (Exception e) {
                System.out.println("Invalid date format in shift: " + shift.getDate());
            }
        }

        if (allShifts.isEmpty()) {
            System.out.println("No shifts scheduled between " + weekStart + " and " + weekEnd);
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
                System.out.println("Invalid date format in shift: " + shift.getDate());
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

        for (ShiftAssignment assignment : this.getAllAssignmentsForThisWeek()) {
            if (assignment.getEmployee().getId().equals(employeeId)) {
                result.add(assignment);
            }
        }

        if (result.isEmpty()) {
            System.out.println("No assignments found for employee ID: " + employeeId);
            return null;
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

        if (groupedShifts.isEmpty()) {
            System.out.println("Weekly Assignment Report: No shifts found for this week.");
            return null;
        }

        List<String> report = new ArrayList<>();
        report.add("Weekly Assignment Report (All Shifts):  ");

        for (String date : groupedShifts.keySet()) {
            report.add("");
            report.add("Date: " + date + " ");
            Map<String, List<Shift>> shiftsByType = groupedShifts.get(date);

            // סדר ידני - קודם Morning ואז Evening
            List<String> orderedTypes = new ArrayList<>();
            if (shiftsByType.containsKey("Morning")) orderedTypes.add("Morning");
            if (shiftsByType.containsKey("Evening")) orderedTypes.add("Evening");

            for (String type : orderedTypes) {
                report.add("  Shift Type: " + type);

                for (Shift shift : shiftsByType.get(type)) {
                    // הצגת מנהל המשמרת אם קיים
                    if (shift.getShiftManager() != null) {
                        report.add("     Shift Manager: " + shift.getShiftManager().getName());
                    } else {
                        report.add("     Shift Manager: No assignment");
                    }

                    List<Role> requiredRoles = shift.getRequiredRoles();
                    List<ShiftAssignment> assignments = shift.getAssignments();

                    for (Role role : requiredRoles) {
                        if (role.getName().equalsIgnoreCase("Shift Manager")) continue;

                        boolean assigned = false;
                        for (ShiftAssignment assignment : assignments) {
                            if (assignment.getRole().getId().equals(role.getId())) {
                                String employeeName = assignment.getEmployee().getName();
                                report.add("     " + role.getName() + ": " + employeeName);
                                assigned = true;
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


    public void createShiftAssignment(String shiftID) {
        Shift targetShift = null;

        for (Shift shift : DAO.shifts) {
            if (shift.getId().equals(shiftID)) {
                targetShift = shift;
                break;
            }
        }

        if (targetShift == null) {
            System.out.println("This shift does not exist in the system!");
            return;
        }

        ManagerController managerService = new ManagerController();
        Scanner scanner = new Scanner(System.in);
        List<ShiftAssignment> newAssignments = new ArrayList<>(targetShift.getAssignments());

        List<Role> sortedRoles = targetShift.getRequiredRoles();
        sortedRoles.sort(Comparator.comparing(Role::getId)); // shift manager first

        for (Role requiredRole : sortedRoles) {
            System.out.println("\n--- Role: " + requiredRole.getName() + " ---");

            boolean alreadyFilled = false;
            for (ShiftAssignment existing : targetShift.getAssignments()) {
                if (existing.getRole().getName().equalsIgnoreCase(requiredRole.getName())) {
                    System.out.println("Role \"" + requiredRole.getName() + "\" is already assigned to " + existing.getEmployee().getName());
                    alreadyFilled = true;
                    break;
                }
            }
            if (alreadyFilled) continue;

            List<Employee> qualifiedEmployees = managerService.getAllEmployeesByRole(requiredRole.getName());

            if (qualifiedEmployees == null || qualifiedEmployees.isEmpty()) {
                continue;
            }

            List<Employee> available = new ArrayList<>();
            List<Employee> unavailable = new ArrayList<>();

            for (Employee employee : qualifiedEmployees) {
                boolean alreadyAssigned = false;
                for (ShiftAssignment assignment : targetShift.getAssignments()) {
                    if (assignment.getEmployee().getId().equals(employee.getId())) {
                        alreadyAssigned = true;
                        break;
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

            for (int i = 0; i < allOptions.size(); i++) {
                Employee emp = allOptions.get(i);
                boolean isAvailable = available.contains(emp);
                String status = isAvailable ? "" : " [Not Available]";
                System.out.println((i + 1) + ". " + emp.getName() + " (ID: " + emp.getId() + ")" + status);
            }

            if (allOptions.isEmpty()) {
                System.out.println("No employees available for this role.");
                continue;
            }

            System.out.print("Choose an employee number (from list above, not ID. If you don't want to assign, press 0): ");
            int choice;
            try {
                choice = Integer.parseInt(scanner.next());
                if (choice < 0 || choice > allOptions.size()) {
                    System.out.println("Invalid number.");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number only.");
                continue;
            }
            if (choice == 0) {
                break;
            }

            Employee selected = allOptions.get(choice - 1);

            // בדיקת כשירות של העובד לתפקיד
            if (!selected.getRoles().contains(requiredRole)) {
                System.out.println("This employee is NOT qualified for the role: " + requiredRole.getName());
                continue;
            }

            if (unavailable.contains(selected)) {
                System.out.print("This employee is not available. Do you still want to assign? (yes/no): ");
                String confirm = scanner.next();
                if (!confirm.equalsIgnoreCase("yes")) {
                    System.out.println("Skipping assignment for this role.");
                    continue;
                }
            }

            ShiftAssignment assignment = new ShiftAssignment(selected, targetShift, requiredRole, null);
            newAssignments.add(assignment);
            DAO.assignments.add(assignment);
            System.out.println("Assigned " + selected.getName() + " to role: " + requiredRole.getName());

            if (requiredRole.getId().equals("1")) {
                targetShift.setShiftManager(selected);
            }
        }

        targetShift.setAssignments(newAssignments);

        if (targetShift.getAssignments().size() == targetShift.getRequiredRoles().size()) {
            System.out.println("\nAll assignments completed for shift " + shiftID);
        }
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
    public void DeleteShiftAssignment(String shiftId, String employeeIdToDelete) {
        for (Shift shift : DAO.shifts) {
            if (shift.getId().equals(shiftId)) {
                Iterator<ShiftAssignment> iterator = shift.getAssignments().iterator();
                while (iterator.hasNext()) {
                    ShiftAssignment assignment = iterator.next();
                    if (assignment.getEmployee().getId().equals(employeeIdToDelete)) {
                        // Step 1: Remove from assignments
                        iterator.remove();
                        assignment.setArchived(true);
                        assignment.setArchiveDate(LocalDate.now());

                        // Step 2: If the removed employee was the shift manager - set it to null
                        if (shift.getShiftManager() != null &&
                                shift.getShiftManager().getId().equals(employeeIdToDelete)) {
                            shift.setShiftManager(null);
                            System.out.println("The shift manager removed from the shift!");
                        }

                        System.out.printf("%s's shift assignment was successfully cancelled. Please notice that there are not enough employees in this shift!\n",
                                assignment.getEmployee().getName());
                        return;
                    }
                }
                // Step 4: Employee wasn't assigned to this shift
                System.out.println("This employee is not assigned to the given shift!");
                return;
            }
        }
        // Step 5: Shift not found
        System.out.println("This given shift does not exist!");
    }

}

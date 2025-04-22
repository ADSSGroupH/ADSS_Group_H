package DomainLayer;

import java.time.LocalDate;
import java.util.*;
import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjusters;


public class ShiftController {

    public Shift createShift(String id, LocalDate date, String startTime, String endTime, String type, Employee shiftManager, List<Role> requiredRoles, List<ShiftAssignment> assignments) {
        // creating the shift

        Shift newShift = new Shift(id, date, startTime, endTime, type, shiftManager, requiredRoles, assignments,LocalDate.now());

        // adding the shift to the archive (data store)
        this.archiveShift(newShift);


        return newShift;
    }

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
            if (shift == null || shift.isArchived()) continue;

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
        Map<String, Map<String, List<ShiftAssignment>>> grouped = new TreeMap<>();

        for (ShiftAssignment assignment : DAO.assignments) {
            LocalDate date = assignment.getShift().getDate();
            String type = assignment.getShift().getType();

            grouped.putIfAbsent(String.valueOf(date), new TreeMap<>());
            grouped.get(date).putIfAbsent(type, new ArrayList<>());
            grouped.get(date).get(type).add(assignment);
        }

        if (grouped.isEmpty()) {
            System.out.println("Weekly Assignment Report: No assignments found for this week.");
            return null;
        }

        List <String> ListOfSentences = new ArrayList<>();

        ListOfSentences.add("Weekly Assignment Report (All Employees):");
        for (String date : grouped.keySet()) {
            ListOfSentences.add("Date: " + date);
            Map<String, List<ShiftAssignment>> shiftsByType = grouped.get(date);

            for (String type : shiftsByType.keySet()) {
                ListOfSentences.add("  Shift: " + type);
                for (ShiftAssignment assignment : shiftsByType.get(type)) {
                    String roleName = assignment.getRole() != null ? assignment.getRole().getName() : "No role assigned";
                    String employeeName = assignment.getEmployee().getName();
                    ListOfSentences.add("    - Employee: " + employeeName + " | Role: " + roleName);
                }
            }
        }
        return ListOfSentences;
    }
    public void createShiftAssignment(String shiftID) {
        Shift targetShift = null;

        // first find the shift
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

        List<ShiftAssignment> newAssignments = new ArrayList<>();
        ManagerController managerService = new ManagerController();

        // go through all the required roles for this shift
        for (Role requiredRole : targetShift.getRequiredRoles()) {
            List<Employee> qualifiedEmployees = managerService.getAllEmployeesByRole(requiredRole.getName());
            boolean foundMatch = false;

            // now we need to check if these workers are available for this shift in order to make the assignment.
            for (Employee employee : qualifiedEmployees) {
                //first check if there is a shift manager available
                if (requiredRole.getId().equals("1")){ //shift manager id
                    if (qualifiedEmployees.isEmpty()){
                        System.out.println("There are no employees qualified as shift manager. shift can't be created!");
                        return;
                    }
                }
                // checking the employee is not already assigned to this shift with a different role:
                boolean alreadyAssigned = false;
                for (ShiftAssignment assignment : targetShift.getAssignments()) {
                    if (assignment.getEmployee().getId().equals(employee.getId())) {
                        alreadyAssigned = true;
                        break;
                    }
                }

                if (alreadyAssigned) {
                    continue;
                }

                // if the worker submitted this shift
                boolean isAvailable = DAO.WeeklyPreferneces.containsKey(employee.getId()) &&
                        DAO.WeeklyPreferneces.get(employee.getId()).contains(targetShift);

                if (!isAvailable) {
                    System.out.println("This employee will be assigned to this shift, but he isn't available!");
                }

                // create the assignment
                if (requiredRole.getId().equals("1")){
                    targetShift.setShiftManager(employee);
                }
                ShiftAssignment assignment = new ShiftAssignment(employee, targetShift, requiredRole, LocalDate.now());
                newAssignments.add(assignment); //using the array to update the *array* of assignments in the shift
                DAO.assignments.add(assignment);
                targetShift.setAssignments(newAssignments);
                foundMatch = true;
                break; // stop looking once we assign someone to this role
            }

            // if no employee matched this role
            if (!foundMatch) {
                System.out.printf("There is no matching employee for the role : %S in this shift", requiredRole.getName());
            }
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
        // Step 1: Find the shift
        for (Shift shift : DAO.shifts) {
            if (shift.getId().equals(shiftId)) {
                // Step 2: Use iterator to safely remove the assignment
                Iterator<ShiftAssignment> iterator = shift.getAssignments().iterator();
                while (iterator.hasNext()) {
                    ShiftAssignment assignment = iterator.next();
                    if (assignment.getEmployee().getId().equals(employeeIdToDelete)) {
                        // Step 3: Remove assignment from both shift and DataStore
                        iterator.remove();
                        assignment.setArchived(true);
                        assignment.setArchiveDate(LocalDate.now());
                        System.out.printf("%s's shift assignment was successfully cancelled. Please notice that there are not enough employees in this shift!\n",
                                assignment.getEmployee().getName());
                        return; // Done!
                    }
                }
                // Step 4: If we got here, employee wasn't assigned to this shift
                System.out.println("This employee is not assigned to the given shift!");
                return;
            }
        }
        // Step 5: If we got here, shift wasn't found
        System.out.println("This given shift does not exist!");
    }




}



import java.time.LocalDate;
import java.util.*;

public class ShiftService {

    public Shift createShift(String id, String date, String startTime, String endTime, String type, Employee shiftManager, List<Role> requiredRoles, List<ShiftAssignment> assignments) {
        // creating the shift
        Shift newShift = new Shift(id, date, startTime, endTime, type, shiftManager, requiredRoles, assignments);

        // adding the shift to the archive (data store)
        this.archiveShift(newShift, date);


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


    public void archiveShift(Shift shift, String date_of_being_archived) {
        DataStore.shifts.add(shift);  // adding the shift to the DataStore
        shift.setArchived(true);
        shift.setArchivedAt(date_of_being_archived);
    }


    public boolean deleteShift(String shiftId) {
        Iterator<Shift> iterator = DataStore.shifts.iterator();
        while (iterator.hasNext()) {
            Shift shift = iterator.next();
            if (shift.getId().equals(shiftId)) {
                iterator.remove();  // deleting the shift from the memory.
                return true;
            }
        }
        return false;  // we didn't find a matching shift.
    }

    // searching a shift by id
    public Shift getShiftById(String shiftId) {
        for (Shift shift : DataStore.shifts) {
            if (shift.getId().equals(shiftId)) {
                return shift;
            }
        }
        return null;  // didn't find a matching shift
    }


    public List<Shift> getAllShifts() {
        return new ArrayList<>(DataStore.shifts);  // returning all the shifts that happened in the last 7 years in a list.
    }


    public List<Shift> getAllShiftsForThisWeek() {
        List<Shift> allShifts = new ArrayList<>();

        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.with(java.time.DayOfWeek.SUNDAY);
        LocalDate weekEnd = weekStart.plusDays(6);

        for (Shift shift : DataStore.shifts) {
            if (shift.isArchived()) continue;

            try {
                LocalDate shiftDate = LocalDate.parse(shift.getDate());  // assuming yyyy-MM-dd format
                if (!shiftDate.isBefore(weekStart) && !shiftDate.isAfter(weekEnd)) {
                    allShifts.add(shift);
                }
            } catch (Exception e) {
                System.out.println("Invalid date format in shift: " + shift.getDate());
            }
        }

        return allShifts;
    }
    public List<ShiftAssignment> getAllAssignmentsForThisWeek() {
        List<ShiftAssignment> weeklyAssignments = new ArrayList<>();

        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.with(java.time.DayOfWeek.SUNDAY);
        LocalDate weekEnd = weekStart.plusDays(6);

        for (ShiftAssignment assignment : DataStore.assignments) {
            Shift shift = assignment.getShift();
            if (shift == null || shift.isArchived()) continue;

            try {
                LocalDate shiftDate = LocalDate.parse(shift.getDate()); // assuming "yyyy-MM-dd"
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

        for (Shift shift : DataStore.shifts) {
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

        for (ShiftAssignment assignment : DataStore.assignments) {
            String date = assignment.getShift().getDate();
            String type = assignment.getShift().getType();

            grouped.putIfAbsent(date, new TreeMap<>());
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



}



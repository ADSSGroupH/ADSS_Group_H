import java.util.*;
import java.time.LocalDate;

public class EmployeeService {

    // מגיש העדפות לשיבוץ השבועי לפי משמרות קיימות
    public void submitWeeklyShiftPreferences(String employeeId) {
        this.sendEmployeesAssignments(employeeId);
    }

    // מציג את כל השיבוצים של השבוע לכל העובדים
    public void printWeeklyAssignmentReport() {
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
            return;
        }

        System.out.println("Weekly Assignment Report (All Employees):");
        for (String date : grouped.keySet()) {
            System.out.println("Date: " + date);
            Map<String, List<ShiftAssignment>> shiftsByType = grouped.get(date);

            for (String type : shiftsByType.keySet()) {
                System.out.println("  Shift: " + type);
                for (ShiftAssignment assignment : shiftsByType.get(type)) {
                    String roleName = assignment.getRole() != null ? assignment.getRole().getName() : "No role assigned";
                    String employeeName = assignment.getEmployee().getName();
                    System.out.println("    - Employee: " + employeeName + " | Role: " + roleName);
                }
            }
        }
    }

    // מציג את השיבוצים של השבוע לעובד מסוים
    public void printWeeklyAssignmentsForEmployee(String employeeId) {
        List<ShiftAssignment> result = new ArrayList<>();

        for (ShiftAssignment assignment : DataStore.assignments) {
            if (assignment.getEmployee().getId().equals(employeeId)) {
                result.add(assignment);
            }
        }

        if (result.isEmpty()) {
            System.out.println("No assignments found for employee ID: " + employeeId);
            return;
        }

        System.out.println("Weekly Assignments for Employee ID: " + employeeId);
        for (ShiftAssignment assignment : result) {
            Shift shift = assignment.getShift();
            String date = shift.getDate();
            String type = shift.getType();
            String roleName = assignment.getRole() != null ? assignment.getRole().getName() : "No role assigned";
            System.out.println("- Date: " + date + " | Shift: " + type + " | Role: " + roleName);
        }
    }

    // שולף את בקשות ההחלפה של העובד
    public List<ShiftSwapRequest> getEmployeeSwapRequests(String employeeId) {
        List<ShiftSwapRequest> requests = new ArrayList<>();
        for (ShiftSwapRequest request : DataStore.swapRequests) {
            if (request.getRequester().getId().equals(employeeId)) {
                requests.add(request);
            }
        }
        return requests;
    }

    // מגיש בקשת החלפת משמרת
    public ShiftSwapRequest submitSwapRequest(Employee requester, Shift fromShift, Shift toShift) {
        String requestId = UUID.randomUUID().toString();
        String date = LocalDate.now().toString();
        ShiftSwapRequestService service = new ShiftSwapRequestService();
        return service.createRequest(requestId, requester, fromShift, toShift, date);
    }
    public void sendEmployeesAssignments(String EmployeeId) { // the employee gives a map of date and type of shift
        ShiftService shiftService = new ShiftService();
        List<Shift> allShifts = shiftService.getAllShiftsForThisWeek();
        Scanner scanner = new Scanner(System.in);
        List<Shift> selectedShifts = new ArrayList<>();

        // Display all existing shifts
        System.out.println("Available Shifts:");
        for (Shift shift : allShifts) {
            System.out.println("ID: " + shift.getId() + ", Date: " + shift.getDate() + ", Type: " + shift.getType());
        }

        System.out.println("Enter the IDs of the shifts you want to select (separated by commas):");
        String input = scanner.nextLine();

        // Split the IDs
        String[] shiftIds = input.split(",");

        // Find the shifts by ID
        for (String id : shiftIds) {
            String trimmedId = id.trim();
            for (Shift shift : allShifts) {
                if (shift.getId().equals(trimmedId)) {
                    selectedShifts.add(shift);
                    break;
                }
            }
        }

        DataStore.WeeklyPreferneces.put(EmployeeId, selectedShifts);
    }
}

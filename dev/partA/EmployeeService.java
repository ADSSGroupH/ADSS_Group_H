import java.util.*;
import java.time.LocalDate;

public class EmployeeService {

    // מגיש העדפות לשיבוץ השבועי לפי משמרות קיימות
    public void submitWeeklyShiftPreferences(String employeeId) {
        this.sendEmployeesAssignments(employeeId);
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

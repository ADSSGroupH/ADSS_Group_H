import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.time.LocalDate;

public class EmployeeService {


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


    public void submitWeeklyShiftPreferences(String employeeId) {
        ShiftService shiftService = new ShiftService();

        // קבע את היום הנוכחי
        LocalDate today = LocalDate.now();
        DayOfWeek todayDay = today.getDayOfWeek();

        // אם היום שישי או שבת - ההעדפות הן לשבוע שאחר לשבוע הבא
        int weeksToAdd = (todayDay.getValue() >= DayOfWeek.FRIDAY.getValue()) ? 2 : 1;

        // חשב את יום ראשון של השבוע הרצוי
        LocalDate targetWeekStart = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).plusWeeks(weeksToAdd - 1);
        LocalDate targetWeekEnd = targetWeekStart.plusDays(6); // שבת

        // שלוף את כל המשמרות בטווח הרצוי
        List<Shift> allShifts = shiftService.getShiftsBetween(targetWeekStart, targetWeekEnd);

        Scanner scanner = new Scanner(System.in);
        List<Shift> selectedShifts = new ArrayList<>();

        System.out.println("Available Shifts for the week: " + targetWeekStart + " to " + targetWeekEnd);
        for (Shift shift : allShifts) {
            System.out.println("ID: " + shift.getId() + ", Date: " + shift.getDate() + ", Type: " + shift.getType());
        }

        System.out.println("Enter the IDs of the shifts you want to select (separated by commas):");
        String input = scanner.nextLine();
        String[] shiftIds = input.split(",");

        for (String id : shiftIds) {
            String trimmedId = id.trim();
            for (Shift shift : allShifts) {
                if (shift.getId().equals(trimmedId)) {
                    selectedShifts.add(shift);
                    break;
                }
            }
        }

        // save the employee's submissions
        DataStore.WeeklyPreferneces.put(employeeId, selectedShifts);
    }

}

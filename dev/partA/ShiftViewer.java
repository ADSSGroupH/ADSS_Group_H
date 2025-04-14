import java.util.List;
import java.util.Scanner;

public class ShiftViewer {

    private Employee watchingEmployee;
    private List<Shift> weeklyShifts;

    private ShiftService shiftService;
    private Scanner scanner;

    public ShiftViewer(Employee employee) {
        this.watchingEmployee = employee;
        this.shiftService = new ShiftService();
        this.scanner = new Scanner(System.in);
        this.weeklyShifts = shiftService.getAllShiftsForThisWeek();
    }

    public void displayMenu() {
        while (true) {
            System.out.println("\n--- צפייה בלוח המשמרות השבועי ---");
            System.out.println("1. הצג את כל משמרות השבוע");
            System.out.println("2. סנן לפי תאריך");
            System.out.println("3. חפש משמרות לפי עובד");
            System.out.println("4. חזרה לתפריט הראשי");
            System.out.print("בחר אפשרות: ");

            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    displayAllShiftsThisWeek();
                    break;
                case "2":
                    filterShiftsByDate();
                    break;
                case "3":
                    filterShiftsByEmployee();
                    break;
                case "4":
                    return;
                default:
                    System.out.println("בחירה לא חוקית, נסה שוב.");
            }
        }
    }

    private void displayAllShiftsThisWeek() {
        weeklyShifts = shiftService.getAllShiftsForThisWeek();

        if (weeklyShifts.isEmpty()) {
            System.out.println("אין משמרות לשבוע זה.");
            return;
        }

        System.out.println("\nכל משמרות השבוע:");
        for (Shift shift : weeklyShifts) {
            System.out.println("תאריך: " + shift.getDate() +
                    ", שעת התחלה: " + shift.getStartTime() +
                    ", שעת סיום: " + shift.getEndTime() +
                    ", סוג משמרת: " + shift.getType() +
                    ", מנהל משמרת: " + (shift.getShiftManager() != null ? shift.getShiftManager().getName() : "ללא"));
        }
    }

    private void filterShiftsByDate() {
        System.out.print("הכנס תאריך (YYYY-MM-DD): ");
        String date = scanner.nextLine();

        List<Shift> filtered = shiftService.getShiftsByDate(date);
        if (filtered.isEmpty()) {
            System.out.println("לא נמצאו משמרות בתאריך זה.");
        } else {
            System.out.println("משמרות בתאריך " + date + ":");
            for (Shift shift : filtered) {
                System.out.println("שעת התחלה: " + shift.getStartTime() +
                        ", שעת סיום: " + shift.getEndTime() +
                        ", סוג: " + shift.getType());
            }
        }
    }

    private void filterShiftsByEmployee() {
        System.out.print("הכנס ID של עובד: ");
        String employeeId = scanner.nextLine();

        List<Shift> employeeShifts = shiftService.getShiftsForEmployeeThisWeek(employeeId);
        if (employeeShifts.isEmpty()) {
            System.out.println("אין משמרות לעובד זה בשבוע הנוכחי.");
        } else {
            System.out.println("משמרות לעובד:");
            for (Shift shift : employeeShifts) {
                System.out.println("תאריך: " + shift.getDate() +
                        ", שעת התחלה: " + shift.getStartTime() +
                        ", שעת סיום: " + shift.getEndTime() +
                        ", סוג: " + shift.getType());
            }
        }
    }
}

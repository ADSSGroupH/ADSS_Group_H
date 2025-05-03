package DomainLayer;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DAO {

    public static HRManager hrManager;
    public static List<Employee> employees = new ArrayList<>();
    public static List<Branch> branches = new ArrayList<>();
    public static List<Shift> shifts = new ArrayList<>();
    public static List<ShiftAssignment> assignments = new ArrayList<>();
    public static List<ShiftSwapRequest> swapRequests = new ArrayList<>();
    public static List<EmployeeContract> contracts = new ArrayList<>();
    public static List<Role> roles = new ArrayList<>();
    public static Map<String, List<Shift>> WeeklyPreferneces = new HashMap<>();

    // ניקוי נתונים
    public static void clearAll() {
        hrManager = null;
        employees.clear();
        branches.clear();
        shifts.clear();
        assignments.clear();
        swapRequests.clear();
        contracts.clear();
        roles.clear();
    }



    // פונקציה למחיקת אובייקטים ישנים יותר מ-7 שנים
    public static void clearOldData() {


        // delete shifts that are older than 7 years
        Iterator<Shift> shiftIterator = shifts.iterator();
        while (shiftIterator.hasNext()) {
            Shift shift = shiftIterator.next();
            LocalDate SevenYearsAgoDate = LocalDate.now();
            if (shift.getArchivedAt().isBefore(SevenYearsAgoDate)) {
                shiftIterator.remove();
            }
        }

        // delete shift assignments that are older than 7 years
        Iterator<ShiftAssignment> assignmentIterator = assignments.iterator();
        while (assignmentIterator.hasNext()) {
            ShiftAssignment assignment = assignmentIterator.next();
            LocalDate SevenYearsAgoDate = LocalDate.now();
            if (assignment.getArchiveDate().isBefore(SevenYearsAgoDate)) {
                assignmentIterator.remove();
            }
        }

        // delete shift swap requests that are older than 7 years
        Iterator<ShiftSwapRequest> swapRequestIterator = swapRequests.iterator();
        while (swapRequestIterator.hasNext()) {
            ShiftSwapRequest swapRequest = swapRequestIterator.next();
            LocalDate SevenYearsAgoDate = LocalDate.now();
            if (swapRequest.getArchivedAt().isBefore(SevenYearsAgoDate)) {
                swapRequestIterator.remove();
            }
        }
        
    }
    public void startDailyClearTask() { //cleaned the datastore daily
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        // מבצע את הפונקציה כל יום
        scheduler.scheduleAtFixedRate(() -> {clearOldData();  // מנקה את הנתונים הישנים
        }, 0, 1, TimeUnit.DAYS);  // כל יום
    }

}

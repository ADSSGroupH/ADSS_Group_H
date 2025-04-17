package DomainLayer;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DataStore {

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
    // פונקציה לחישוב הזמן עד יום חמישי הבא
    public long getTimeUntilNextThursday() {
        Calendar now = Calendar.getInstance();
        int dayOfWeek = now.get(Calendar.DAY_OF_WEEK);
        long timeUntilThursday = 0;

        // אם היום יום חמישי, התזמון יהיה מיידי
        if (dayOfWeek == Calendar.THURSDAY) {
            timeUntilThursday = 0;
        } else {
            // אם לא, חישוב הזמן עד יום חמישי הבא
            int daysUntilThursday = (Calendar.THURSDAY - dayOfWeek + 7) % 7;
            timeUntilThursday = daysUntilThursday * 24L * 60L * 60L * 1000L;  // הזמן בשעות, דקות, שניות ומילישניות
        }

        return timeUntilThursday;
    }

    public void startWeeklyClearTask() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        // זמן עד יום חמישי הבא
        long initialDelay = getTimeUntilNextThursday();

        // מבצע את הפונקציה כל חמישי
        scheduler.scheduleAtFixedRate(() -> {
            WeeklyPreferneces.clear();  // מנקה את העדפות המשמרות
        }, initialDelay, TimeUnit.DAYS.toMillis(7), TimeUnit.MILLISECONDS);
    }
}
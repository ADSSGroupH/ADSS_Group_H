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

//            // למחוק עובדים ישנים יותר מ-7 שנים
//            Iterator<Employee> employeeIterator = employees.iterator();
//            while (employeeIterator.hasNext()) {
//                Employee employee = employeeIterator.next();
//                if (new Date().getTime() - employee.getCreationDate().getTime() > sevenYearsInMillis) {
//                    employeeIterator.remove();
//                }
//            }

//            // למחוק סניפים ישנים יותר מ-7 שנים
//            Iterator<Branch> branchIterator = branches.iterator();
//            while (branchIterator.hasNext()) {
//                Branch branch = branchIterator.next();
//                if (new Date().getTime() - branch.getCreationDate().getTime() > sevenYearsInMillis) {
//                    branchIterator.remove();
//                }
//            }

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

//            // למחוק חוזים ישנים יותר מ-7 שנים
//            Iterator<EmployeeContract> contractIterator = contracts.iterator();
//            while (contractIterator.hasNext()) {
//                EmployeeContract contract = contractIterator.next();
//                if (new Date().getTime() - contract.getCreationDate().getTime() > sevenYearsInMillis) {
//                    contractIterator.remove();
//                }
//            }

//            // למחוק תפקידים ישנים יותר מ-7 שנים
//            Iterator<Role> roleIterator = roles.iterator();
//            while (roleIterator.hasNext()) {
//                Role role = roleIterator.next();
//                if (new Date().getTime() - role.getCreationDate().getTime() > sevenYearsInMillis) {
//                    roleIterator.remove();
//                }
//            }
    }
    public void startDailyClearTask() { //cleaned the datastore daily
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        // מבצע את הפונקציה כל יום
        scheduler.scheduleAtFixedRate(() -> {clearOldData();  // מנקה את הנתונים הישנים
        }, 0, 1, TimeUnit.DAYS);  // כל יום
    }

}


import java.util.*;

public class DataStore {

    public static HRManager hrManager;
    public static List<Employee> employees = new ArrayList<>();
    public static List<Branch> branches = new ArrayList<>();
    public static List<Shift> shifts = new ArrayList<>();
    public static List<ShiftAssignment> assignments = new ArrayList<>();
    public static List<ShiftSwapRequest> swapRequests = new ArrayList<>();
    public static List<EmployeeContract> contracts = new ArrayList<>();
    public static List<Role> roles = new ArrayList<>();

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
}
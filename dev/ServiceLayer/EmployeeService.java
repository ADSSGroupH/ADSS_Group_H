package ServiceLayer;

import DomainLayer.*;

import java.util.List;


public class EmployeeService {

    EmployeeController employeeController = new EmployeeController();

    public List<ShiftSwapRequest> getEmployeeSwapRequests(String employeeId) {
        return employeeController.getEmployeeSwapRequests(employeeId);
    }


    public ShiftSwapRequest submitSwapRequest(Employee requester, Shift fromShift, Shift toShift) {
        return employeeController.submitSwapRequest(requester,fromShift,toShift);
    }


    public void submitWeeklyShiftPreferences(String employeeId) {
        employeeController.submitWeeklyShiftPreferences(employeeId);
    }


}

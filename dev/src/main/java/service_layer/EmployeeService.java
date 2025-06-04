package ServiceLayer;

import DomainLayer.HR.Controllers.EmployeeController;
import DomainLayer.HR.Employee;
import DomainLayer.HR.Shift;
import DomainLayer.HR.ShiftSwapRequest;

import java.util.List;


public class EmployeeService {

    EmployeeController employeeController = new EmployeeController();

    public List<ShiftSwapRequest> getEmployeeSwapRequests(String employeeId) {
        return employeeController.getEmployeeSwapRequests(employeeId);
    }


    public ShiftSwapRequest submitSwapRequest(Employee requester, Shift fromShift, Shift toShift) {
        return employeeController.submitSwapRequest(requester,fromShift,toShift);
    }

    public List<Shift> getEligibleShiftsForNextWeek(String employeeId) throws Exception {
        return employeeController.getEligibleShiftsForNextWeek(employeeId);
    }

    public void submitSelectedShifts(String employeeId, List<Shift> selectedShifts, String status) {
        employeeController.submitSelectedShifts(employeeId, selectedShifts, status);
    }

    public List<DomainLayer.HR.ShiftAssignment> getAssignmentsForEmployee(String employeeId) {
        return employeeController.getAssignmentsForEmployee(employeeId);
    }
}
package ServiceLayer;

import DomainLayer.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

public class ShiftService {

    ShiftController shiftController = new ShiftController();

    public Shift createShift(String id, LocalDate date, String startTime, String endTime, String type, Employee shiftManager, List<Role> requiredRoles, List<ShiftAssignment> assignments) {
        return shiftController.createShift(id,date,startTime,endTime,type,shiftManager,requiredRoles,assignments);
    }

    public void updateShiftField(String shiftId, String fieldName, Object newValue) {
        shiftController.updateShiftField(shiftId,fieldName,newValue);
    }


    public void archiveShift(Shift shift) {
        shiftController.archiveShift(shift);
    }


    public boolean deleteShift(String shiftId) {
        return shiftController.deleteShift(shiftId);
    }


    public Shift getShiftById(String shiftId) {
        return shiftController.getShiftById(shiftId);
    }


    public List<Shift> getAllShiftsForThisWeek() {
        return shiftController.getAllShiftsForThisWeek();
    }

    public List<ShiftAssignment> getAllAssignmentsForThisWeek() {
        return shiftController.getAllAssignmentsForThisWeek();
    }


    public List<Shift> getShiftsByDate(String date) {
        return shiftController.getShiftsByDate(date);
    }


    public List<Employee> getAllTheEmployeesInAShift(String shiftId) {
        return shiftController.getAllTheEmployeesInAShift(shiftId);
    }

    public List<ShiftAssignment> getWeeklyAssignmentsForEmployee (String employeeId) {
        return shiftController.getWeeklyAssignmentsForEmployee(employeeId);
    }


    public List<String> MakeWeeklyAssignmentReport() {
        return shiftController.MakeWeeklyAssignmentReport();
    }

    public void createShiftAssignment(String shiftID) {
        shiftController.createShiftAssignment(shiftID);
    }


    public List<Shift> getShiftsBetween(LocalDate from, LocalDate to) {
        return shiftController.getShiftsBetween(from,to);
    }

    public void DeleteShiftAssignment(String shiftId, String employeeIdToDelete) {
        shiftController.DeleteShiftAssignment(shiftId,employeeIdToDelete);
    }

}

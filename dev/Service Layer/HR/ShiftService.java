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

    public Shift_Status updateShiftField(String shiftId, String fieldName, Object newValue) {
        return shiftController.updateShiftField(shiftId,fieldName,newValue);
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

    public ShiftAssignment CheckIfRoleIsFilled(Shift Shift, Role role){
        return shiftController.CheckIfRoleIsFilled(Shift,role);
    }

    public List<List<Employee>> AvailableAndUnavailableEmpForRoleInShift(Shift targetShift, Role requiredRole){
        return shiftController.AvailableAndUnavailableEmpForRoleInShift(targetShift,requiredRole);
    }

    public void createShiftAssignment(Shift shift, Role requiredRole, Employee SelectedEmployee) {
        shiftController.createShiftAssignment(shift,requiredRole,SelectedEmployee);
    }


    public List<Shift> getShiftsBetween(LocalDate from, LocalDate to) {
        return shiftController.getShiftsBetween(from,to);
    }

    public Shift_Status DeleteShiftAssignment(String shiftId, String employeeIdToDelete) {
        return shiftController.DeleteShiftAssignment(shiftId,employeeIdToDelete);
    }

}

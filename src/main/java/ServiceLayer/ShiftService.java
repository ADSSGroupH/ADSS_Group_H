package ServiceLayer;

import DomainLayer.HR.Controllers.ShiftController;
import DomainLayer.HR.Employee;
import DomainLayer.HR.Enums.Shift_Status;
import DomainLayer.HR.Role;
import DomainLayer.HR.Shift;
import DomainLayer.HR.ShiftAssignment;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class ShiftService {

    ShiftController shiftController = new ShiftController();

    public Shift createShift(String id, LocalDate date, String startTime, String endTime, String type, Employee shiftManager, List<Role> requiredRoles, List<ShiftAssignment> assignments) throws Exception {
        return shiftController.createShift(id,date,startTime,endTime,type,shiftManager,requiredRoles,assignments);
    }

    public Shift_Status updateShiftField(String shiftId, String fieldName, Object newValue) {
        return shiftController.updateShiftField(shiftId,fieldName,newValue);
    }


    public void archiveShift(Shift shift) throws Exception {
        shiftController.archiveShift(shift);
    }


    public boolean deleteShift(String shiftId) {
        return shiftController.deleteShift(shiftId);
    }


    public Shift getShiftById(String shiftId) {
        return shiftController.getShiftById(shiftId);
    }


    public List<Shift> getAllShiftsForThisWeek() throws SQLException {
        return shiftController.getAllShiftsForThisWeek();
    }

    public List<ShiftAssignment> getAllAssignmentsForThisWeek() {
        return shiftController.getAllAssignmentsForThisWeek();
    }


    public List<Shift> getShiftsByDate(String date) throws SQLException {
        return shiftController.getShiftsByDate(date);
    }


    public List<Employee> getAllTheEmployeesInAShift(String shiftId) throws SQLException {
        return shiftController.getAllTheEmployeesInAShift(shiftId);
    }

    public List<ShiftAssignment> getWeeklyAssignmentsForEmployee (String employeeId) {
        return shiftController.getWeeklyAssignmentsForEmployee(employeeId);
    }


    public List<String> MakeWeeklyAssignmentReport() throws SQLException {
        return shiftController.MakeWeeklyAssignmentReport();
    }

    public ShiftAssignment CheckIfRoleIsFilled(Shift Shift, Role role) throws SQLException {
        return shiftController.CheckIfRoleIsFilled(Shift,role);
    }

    public List<List<Employee>> AvailableAndUnavailableEmpForRoleInShift(Shift targetShift, Role requiredRole){
        return shiftController.AvailableAndUnavailableEmpForRoleInShift(targetShift,requiredRole);
    }

    public void createShiftAssignment(Shift shift, Role requiredRole, Employee SelectedEmployee) throws SQLException {
        shiftController.createShiftAssignment(shift,requiredRole,SelectedEmployee);
    }


    public List<Shift> getShiftsBetween(LocalDate from, LocalDate to) throws SQLException {
        return shiftController.getShiftsBetween(from,to);
    }

    public Shift_Status DeleteShiftAssignment(String shiftId, String employeeIdToDelete) {
        return shiftController.DeleteShiftAssignment(shiftId,employeeIdToDelete);
    }
    public List<Shift> getAllShifts() throws SQLException {
        return shiftController.getAllShifts();
    }

    public List<ShiftAssignment> getAllAssignments() {
        return shiftController.getAllAssignments();
    }

}
package Dal.HR;

import DTO.HR.ShiftAssignmentDTO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AssignmentDAO {
    Optional<ShiftAssignmentDTO> findById(String id) throws SQLException;
    List<ShiftAssignmentDTO> findAll() throws SQLException;
    List<ShiftAssignmentDTO> findActiveAssignments() throws SQLException;
    List<ShiftAssignmentDTO> findByShift(String shiftId) throws SQLException;
    List<ShiftAssignmentDTO> findByEmployee(String employeeId) throws SQLException;
    List<ShiftAssignmentDTO> findByRole(String roleId) throws SQLException;
    List<ShiftAssignmentDTO> findByShiftAndRole(String shiftId, String roleId) throws SQLException;
    List<ShiftAssignmentDTO> findByDate(LocalDate date) throws SQLException;
    List<ShiftAssignmentDTO> findByDateRange(LocalDate startDate, LocalDate endDate) throws SQLException;
    List<ShiftAssignmentDTO> findByEmployeeAndDateRange(String employeeId, LocalDate startDate, LocalDate endDate) throws SQLException;
    void save(ShiftAssignmentDTO assignment) throws SQLException;
    void update(ShiftAssignmentDTO assignment) throws SQLException;
    void delete(String id) throws SQLException;
    boolean exists(String id) throws SQLException;
    int getAssignmentCount() throws SQLException;
    int getActiveAssignmentCount() throws SQLException;
    boolean existsAssignmentForShiftAndRole(String shiftId, String roleId) throws SQLException;
    boolean existsAssignmentForEmployeeOnDate(String employeeId, LocalDate date) throws SQLException;
}
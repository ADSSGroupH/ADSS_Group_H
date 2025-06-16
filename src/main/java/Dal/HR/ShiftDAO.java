package Dal.HR;

import DTO.HR.ShiftDTO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ShiftDAO {
    Optional<ShiftDTO> findById(String id) throws SQLException;
    List<ShiftDTO> findAll() throws SQLException;
    List<ShiftDTO> findActiveShifts() throws SQLException;
    List<ShiftDTO> findByDate(LocalDate date) throws SQLException;
    //List<ShiftDTO> findByBranch(String branchId) throws SQLException;
    List<ShiftDTO> findByManager(String managerId) throws SQLException;
    List<ShiftDTO> findByDateRange(LocalDate startDate, LocalDate endDate) throws SQLException;
    List<ShiftDTO> findByType(String type) throws SQLException;
    void save(ShiftDTO shift) throws SQLException;
    void update(ShiftDTO shift) throws SQLException;
    void delete(String id) throws SQLException;
    boolean exists(String id) throws SQLException;
    int getShiftCount() throws SQLException;
    boolean existsShiftOnDate(LocalDate date) throws SQLException;

    ShiftDTO findByDateAndTime(String date, String startTime) throws SQLException;
}
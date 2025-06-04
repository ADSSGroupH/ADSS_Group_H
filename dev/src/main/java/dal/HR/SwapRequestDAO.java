package Dal.HR;

import DTO.HR.ShiftSwapRequestDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface SwapRequestDAO {
    Optional<ShiftSwapRequestDTO> findById(String id) throws SQLException;
    List<ShiftSwapRequestDTO> findAll() throws SQLException;
    // מחפשים לפי סטטוס במחרוזת (לא enum)
    List<ShiftSwapRequestDTO> findByStatus(String status) throws SQLException;
    // מחפשים לפי טווח תאריכים במחרוזות (פורמט yyyy-MM-dd)
    List<ShiftSwapRequestDTO> findByDateRange(String startDate, String endDate) throws SQLException;
    List<ShiftSwapRequestDTO> findByRequestor(String requestorId) throws SQLException;
    List<ShiftSwapRequestDTO> findByShift(String shiftId) throws SQLException;
    // מחפש בקשות במצב Pending - משתמש במחרוזת "Pending" או שווה ערך
    List<ShiftSwapRequestDTO> findPendingRequests() throws SQLException;
    // מחפש בקשות פעילות (שאינן ארכיב)
    List<ShiftSwapRequestDTO> findActiveRequests() throws SQLException;
    void save(ShiftSwapRequestDTO request) throws SQLException;
    void update(ShiftSwapRequestDTO request) throws SQLException;
    void delete(String id) throws SQLException;
    boolean exists(String id) throws SQLException;
    int getRequestCount() throws SQLException;

    // ספירת בקשות לפי סטטוס במחרוזת
    int getRequestCountByStatus(String status) throws SQLException;
}

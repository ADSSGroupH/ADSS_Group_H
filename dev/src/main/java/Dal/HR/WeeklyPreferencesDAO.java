package Dal.HR;

import DTO.HR.WeeklyPreferencesDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface WeeklyPreferencesDAO {
    Optional<WeeklyPreferencesDTO> findByEmployeeAndWeek(String employeeId, String weekStartDate) throws SQLException;
    List<WeeklyPreferencesDTO> findByEmployee(String employeeId) throws SQLException;
    List<WeeklyPreferencesDTO> findByWeek(String weekStartDate) throws SQLException;
    List<WeeklyPreferencesDTO> findByStatus(String status) throws SQLException;
    List<WeeklyPreferencesDTO> findAll() throws SQLException;
    void save(WeeklyPreferencesDTO preferences) throws SQLException;
    void update(WeeklyPreferencesDTO preferences) throws SQLException;
    void delete(String employeeId, String weekStartDate) throws SQLException;
    boolean exists(String employeeId, String weekStartDate) throws SQLException;
}

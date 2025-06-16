package Dal.Transportation;

import DTO.Transportation.driverDTO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface DriverDAO {
    Optional<driverDTO> findById(String id) throws SQLException;
    List<driverDTO> findAll() throws SQLException;
    void save(driverDTO driver) throws SQLException;
    void update(driverDTO driver) throws SQLException;
    void delete(String id) throws SQLException;
    boolean exists(String id) throws SQLException;
    String getLicenseByDriverId(String id) throws SQLException;
}

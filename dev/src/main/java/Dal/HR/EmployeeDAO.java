package Dal.HR;


import DTO.HR.EmployeeDTO;
import DTO.Transportation.driverDTO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface EmployeeDAO {
    Optional<EmployeeDTO> findById(String id) throws SQLException;
    List<EmployeeDTO> findAll() throws SQLException;
    List<EmployeeDTO> findActiveEmployees() throws SQLException;
    List<EmployeeDTO> findByBranch(String branchId) throws SQLException;
    List<EmployeeDTO> findManagers() throws SQLException;
    Optional<EmployeeDTO> findByIdAndPassword(String id, String password) throws SQLException;
    List<EmployeeDTO> findQualified(String branchId, String roleId) throws SQLException;
    void save(EmployeeDTO employee) throws SQLException;

    void saveIfDriver(EmployeeDTO driver) throws SQLException;

    void addDriverLicense(String id, String licenseType) throws SQLException;

    String getLicenseByDriverId(String id) throws SQLException;


    void update(EmployeeDTO employee) throws SQLException;
    void delete(String id) throws SQLException;
    boolean exists(String id) throws SQLException;
    int getEmployeeCount() throws SQLException;
    public boolean isDriverAvailable(String driverId, LocalDate date, LocalTime startTime) throws SQLException;
}
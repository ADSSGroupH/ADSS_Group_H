package Dal.HR;

import DTO.HR.EmployeeContractDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ContractDAO {
    Optional<EmployeeContractDTO> findById(String id) throws SQLException;
    Optional<EmployeeContractDTO> findByEmployeeId(String employeeId) throws SQLException;
    List<EmployeeContractDTO> findAll() throws SQLException;
    List<EmployeeContractDTO> findActiveContracts() throws SQLException;

    // במקום LocalDate - String (פורמט yyyy-MM-dd)
    List<EmployeeContractDTO> findByStartDate(String startDate) throws SQLException;

    // תאריכים בטווח - שני פרמטרים מסוג String (פורמט yyyy-MM-dd)
    List<EmployeeContractDTO> findByDateRange(String startDate, String endDate) throws SQLException;

    void save(EmployeeContractDTO contract) throws SQLException;
    void update(EmployeeContractDTO contract) throws SQLException;
    void delete(String id) throws SQLException;
    void deleteByEmployeeId(String employeeId) throws SQLException;
    boolean exists(String id) throws SQLException;
    boolean existsByEmployeeId(String employeeId) throws SQLException;
    int getContractCount() throws SQLException;
}

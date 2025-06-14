package Dal.HR;

import DTO.HR.BranchDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface BranchDAO {
    Optional<BranchDTO> findById(String id) throws SQLException;
    List<BranchDTO> findAll() throws SQLException;
    List<BranchDTO> findByName(String name) throws SQLException;
    List<BranchDTO> findByAddress(String address) throws SQLException;
    void save(BranchDTO branch) throws SQLException;
    void update(BranchDTO branch) throws SQLException;
    void delete(String id) throws SQLException;
    boolean exists(String id) throws SQLException;
    int getBranchCount() throws SQLException;
}
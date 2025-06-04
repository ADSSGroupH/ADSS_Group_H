package Dal.HR;

import DTO.HR.RoleDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface RoleDAO {
    Optional<RoleDTO> findById(String id) throws SQLException;
    List<RoleDTO> findAll() throws SQLException;
    List<RoleDTO> findActiveRoles() throws SQLException;
    Optional<RoleDTO> findByName(String name) throws SQLException;
    void save(RoleDTO role) throws SQLException;
    void update(RoleDTO role) throws SQLException;
    void delete(String id) throws SQLException;
    boolean exists(String id) throws SQLException;
    boolean existsByName(String name) throws SQLException;
    int getRoleCount() throws SQLException;
}
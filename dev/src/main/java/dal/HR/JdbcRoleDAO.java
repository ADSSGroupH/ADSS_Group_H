package Dal.HR;

import DTO.HR.RoleDTO;
import database.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcRoleDAO implements RoleDAO {

    @Override
    public Optional<RoleDTO> findById(String id) throws SQLException {
        String sql = "SELECT * FROM roles WHERE id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    RoleDTO role = mapResultSetToRoleDTO(rs);
                    return Optional.of(role);
                }
                return Optional.empty();
            }
        }
    }

    @Override
    public List<RoleDTO> findAll() throws SQLException {
        List<RoleDTO> roles = new ArrayList<>();
        String sql = "SELECT * FROM roles ORDER BY id";

        try (PreparedStatement stmt = Database.getConnection().prepareStatement(sql)){
            try(ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    RoleDTO role = mapResultSetToRoleDTO(rs);
                    roles.add(role);
                }
            }

            return roles;
        }

    }

    @Override
    public List<RoleDTO> findActiveRoles() throws SQLException {
        List<RoleDTO> roles = new ArrayList<>();
        String sql = "SELECT * FROM roles WHERE is_archived = false ORDER BY name";

        try (Statement stmt = Database.getConnection().createStatement()){
            try(ResultSet rs = stmt.executeQuery(sql)){
                while (rs.next()) {
                    RoleDTO role = mapResultSetToRoleDTO(rs);
                    roles.add(role);
                }
            }
            return roles;
            }
        }



    @Override
    public Optional<RoleDTO> findByName(String name) throws SQLException {
        String sql = "SELECT * FROM roles WHERE LOWER(name) = LOWER(?) AND is_archived = false";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)){
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    RoleDTO role = mapResultSetToRoleDTO(rs);
                    return Optional.of(role);
                }
                return Optional.empty();
            }
        }
    }

    @Override
    public void save(RoleDTO role) throws SQLException {
        String sql = """
            INSERT INTO roles (id, name, is_archived) 
            VALUES (?, ?, ?)
        """;

        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, role.getId());
            ps.setString(2, role.getName());
            ps.setBoolean(3, false); // תפקיד חדש לא יהיה מארכב
            ps.executeUpdate();
        }
    }

    @Override
    public void update(RoleDTO role) throws SQLException {
        String sql = """
            UPDATE roles SET name = ?, updated_at = CURRENT_TIMESTAMP 
            WHERE id = ?
        """;

        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, role.getName());
            ps.setString(2, role.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(String id) throws SQLException {
        String sql = """
            UPDATE roles SET is_archived = true, archived_at = CURRENT_TIMESTAMP 
            WHERE id = ?
        """;
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)){
            ps.setString(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public boolean exists(String id) throws SQLException {
        String sql = "SELECT 1 FROM roles WHERE id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public boolean existsByName(String name) throws SQLException {
        String sql = "SELECT 1 FROM roles WHERE LOWER(name) = LOWER(?) AND is_archived = false";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public int getRoleCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM roles WHERE is_archived = false";
        try (Statement stmt = Database.getConnection().createStatement()){
            try(ResultSet rs = stmt.executeQuery(sql)) {

                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            return 0;
        }

    }

    private RoleDTO mapResultSetToRoleDTO(ResultSet rs) throws SQLException {
        RoleDTO role = new RoleDTO();
        role.setId(rs.getString("id"));
        role.setName(rs.getString("name"));
        return role;
    }
}

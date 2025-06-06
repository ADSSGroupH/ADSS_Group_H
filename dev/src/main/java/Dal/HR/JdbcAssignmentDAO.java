package Dal.HR;

import DTO.HR.ShiftAssignmentDTO;
import database.Database;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcAssignmentDAO implements AssignmentDAO {

    private ShiftAssignmentDTO mapResultSetToDTO(ResultSet rs) throws SQLException {
        ShiftAssignmentDTO dto = new ShiftAssignmentDTO();
        dto.setId(rs.getString("id"));
        dto.setEmployeeId(rs.getString("employee_id"));
        dto.setShiftId(rs.getString("shift_id"));
        dto.setRoleId(rs.getString("role_id"));
        dto.setArchived(rs.getBoolean("is_archived"));

        long archivedAtMillis = rs.getLong("archived_at");
        if (!rs.wasNull()) {
            LocalDate archiveDate = Instant.ofEpochMilli(archivedAtMillis)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            dto.setArchiveDate(archiveDate.toString());
        } else {
            dto.setArchiveDate(null);
        }

        return dto;
    }


    @Override
    public Optional<ShiftAssignmentDTO> findById(String id) throws SQLException {
        String sql = "SELECT * FROM shift_assignments WHERE id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToDTO(rs));
                }
                return Optional.empty();
            }
        }
    }

    @Override
    public List<ShiftAssignmentDTO> findAll() throws SQLException {
        List<ShiftAssignmentDTO> assignments = new ArrayList<>();
        String sql = "SELECT * FROM shift_assignments";

        try (Statement stmt = Database.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                assignments.add(mapResultSetToDTO(rs));
            }
        }
        return assignments;
    }

    @Override
    public List<ShiftAssignmentDTO> findActiveAssignments() throws SQLException {
        List<ShiftAssignmentDTO> assignments = new ArrayList<>();
        String sql = "SELECT * FROM shift_assignments WHERE is_archived = false";

        try (Statement stmt = Database.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                assignments.add(mapResultSetToDTO(rs));
            }
        }
        return assignments;
    }

    @Override
    public List<ShiftAssignmentDTO> findByShift(String shiftId) throws SQLException {
        List<ShiftAssignmentDTO> assignments = new ArrayList<>();
        String sql = "SELECT * FROM shift_assignments WHERE shift_id = ? AND is_archived = false";

        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, shiftId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    assignments.add(mapResultSetToDTO(rs));
                }
            }
        }
        return assignments;
    }

    @Override
    public List<ShiftAssignmentDTO> findByEmployee(String employeeId) throws SQLException {
        List<ShiftAssignmentDTO> assignments = new ArrayList<>();
        String sql = "SELECT * FROM shift_assignments WHERE employee_id = ? AND is_archived = false";

        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, employeeId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    assignments.add(mapResultSetToDTO(rs));
                }
            }
        }
        return assignments;
    }

    @Override
    public List<ShiftAssignmentDTO> findByRole(String roleId) throws SQLException {
        List<ShiftAssignmentDTO> assignments = new ArrayList<>();
        String sql = "SELECT * FROM shift_assignments WHERE role_id = ? AND is_archived = false";

        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, roleId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    assignments.add(mapResultSetToDTO(rs));
                }
            }
        }
        return assignments;
    }

    @Override
    public List<ShiftAssignmentDTO> findByShiftAndRole(String shiftId, String roleId) throws SQLException {
        List<ShiftAssignmentDTO> assignments = new ArrayList<>();
        String sql = "SELECT * FROM shift_assignments WHERE shift_id = ? AND role_id = ? AND is_archived = false";

        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, shiftId);
            ps.setString(2, roleId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    assignments.add(mapResultSetToDTO(rs));
                }
            }
        }
        return assignments;
    }

    @Override
    public List<ShiftAssignmentDTO> findByDate(LocalDate date) throws SQLException {
        List<ShiftAssignmentDTO> assignments = new ArrayList<>();
        String sql = """
            SELECT sa.* FROM shift_assignments sa 
            JOIN shifts s ON sa.shift_id = s.id 
            WHERE s.date = ? AND sa.is_archived = false AND s.is_archived = false
        """;

        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    assignments.add(mapResultSetToDTO(rs));
                }
            }
        }
        return assignments;
    }

    @Override
    public List<ShiftAssignmentDTO> findByDateRange(LocalDate startDate, LocalDate endDate) throws SQLException {
        List<ShiftAssignmentDTO> assignments = new ArrayList<>();
        String sql = """
            SELECT sa.* FROM shift_assignments sa 
            JOIN shifts s ON sa.shift_id = s.id 
            WHERE s.date BETWEEN ? AND ? AND sa.is_archived = false AND s.is_archived = false 
            ORDER BY s.date, s.start_time
        """;

        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(startDate));
            ps.setDate(2, java.sql.Date.valueOf(endDate));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    assignments.add(mapResultSetToDTO(rs));
                }
            }
        }
        return assignments;
    }

    @Override
    public List<ShiftAssignmentDTO> findByEmployeeAndDateRange(String employeeId, LocalDate startDate, LocalDate endDate) throws SQLException {
        List<ShiftAssignmentDTO> assignments = new ArrayList<>();
        String sql = """
            SELECT sa.* FROM shift_assignments sa 
            JOIN shifts s ON sa.shift_id = s.id 
            WHERE sa.employee_id = ? AND s.date BETWEEN ? AND ? 
            AND sa.is_archived = false AND s.is_archived = false 
            ORDER BY s.date, s.start_time
        """;

        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, employeeId);
            ps.setDate(2, java.sql.Date.valueOf(startDate));
            ps.setDate(3, java.sql.Date.valueOf(endDate));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    assignments.add(mapResultSetToDTO(rs));
                }
            }
        }
        return assignments;
    }

    @Override
    public void save(ShiftAssignmentDTO assignment) throws SQLException {
        String sql = """
        INSERT INTO shift_assignments (id, employee_id, shift_id, role_id, is_archived, archived_at) 
        VALUES (?, ?, ?, ?, ?, ?)
    """;

        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, assignment.getId());
            ps.setString(2, assignment.getEmployeeId());
            ps.setString(3, assignment.getShiftId());
            ps.setString(4, assignment.getRoleId());
            ps.setBoolean(5, assignment.isArchived());
            ps.setDate(6, assignment.getArchiveDate() != null ? java.sql.Date.valueOf(assignment.getArchiveDate()) : null);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void update(ShiftAssignmentDTO assignment) throws SQLException {
        String sql = """
        UPDATE shift_assignments 
        SET employee_id = ?, shift_id = ?, role_id = ?, is_archived = ?, archived_at = ?
        WHERE id = ?
    """;

        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, assignment.getEmployeeId());
            ps.setString(2, assignment.getShiftId());
            ps.setString(3, assignment.getRoleId());
            ps.setBoolean(4, assignment.isArchived());
            ps.setDate(5, assignment.getArchiveDate() != null ? java.sql.Date.valueOf(assignment.getArchiveDate()) : null);
            ps.setString(6, assignment.getId());

            ps.executeUpdate();
        }
    }

    @Override
    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM shift_assignments WHERE id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public boolean exists(String id) throws SQLException {
        String sql = "SELECT 1 FROM shift_assignments WHERE id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public int getAssignmentCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM shift_assignments";
        try (Statement stmt = Database.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    @Override
    public int getActiveAssignmentCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM shift_assignments WHERE is_archived = false";
        try (Statement stmt = Database.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    @Override
    public boolean existsAssignmentForShiftAndRole(String shiftId, String roleId) throws SQLException {
        String sql = "SELECT 1 FROM shift_assignments WHERE shift_id = ? AND role_id = ? AND is_archived = false";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, shiftId);
            ps.setString(2, roleId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public boolean existsAssignmentForEmployeeOnDate(String employeeId, LocalDate date) throws SQLException {
        String sql = """
            SELECT 1 FROM shift_assignments sa 
            JOIN shifts s ON sa.shift_id = s.id 
            WHERE sa.employee_id = ? AND s.date = ? 
            AND sa.is_archived = false AND s.is_archived = false
        """;
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, employeeId);
            ps.setDate(2, java.sql.Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
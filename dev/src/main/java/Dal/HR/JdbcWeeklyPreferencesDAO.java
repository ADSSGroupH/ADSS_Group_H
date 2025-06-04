package Dal.HR;

import DTO.HR.WeeklyPreferencesDTO;
import database.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcWeeklyPreferencesDAO implements WeeklyPreferencesDAO {

    @Override
    public Optional<WeeklyPreferencesDTO> findByEmployeeAndWeek(String employeeId, String weekStartDate) throws SQLException {
        String sql = """
            SELECT employee_id, preferred_shift_ids_csv, week_start_date, created_at, 
                   last_modified, status, notes, employee_id_simple
            FROM weekly_preferences 
            WHERE employee_id = ? AND week_start_date = ?
        """;

        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, employeeId);
            ps.setString(2, weekStartDate);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    WeeklyPreferencesDTO preferences = mapResultSetToDTO(rs);
                    return Optional.of(preferences);
                }
                return Optional.empty();
            }
        }
    }

    @Override
    public List<WeeklyPreferencesDTO> findByEmployee(String employeeId) throws SQLException {
        List<WeeklyPreferencesDTO> preferences = new ArrayList<>();
        String sql = """
            SELECT employee_id, preferred_shift_ids_csv, week_start_date, created_at, 
                   last_modified, status, notes, employee_id_simple
            FROM weekly_preferences 
            WHERE employee_id = ?
            ORDER BY week_start_date DESC
        """;

        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, employeeId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    WeeklyPreferencesDTO preference = mapResultSetToDTO(rs);
                    preferences.add(preference);
                }
            }
        }
        return preferences;
    }

    @Override
    public List<WeeklyPreferencesDTO> findByWeek(String weekStartDate) throws SQLException {
        List<WeeklyPreferencesDTO> preferences = new ArrayList<>();
        String sql = """
            SELECT employee_id, preferred_shift_ids_csv, week_start_date, created_at, 
                   last_modified, status, notes, employee_id_simple
            FROM weekly_preferences 
            WHERE week_start_date = ?
        """;

        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, weekStartDate);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    WeeklyPreferencesDTO preference = mapResultSetToDTO(rs);
                    preferences.add(preference);
                }
            }
        }
        return preferences;
    }

    @Override
    public List<WeeklyPreferencesDTO> findByStatus(String status) throws SQLException {
        List<WeeklyPreferencesDTO> preferences = new ArrayList<>();
        String sql = """
            SELECT employee_id, preferred_shift_ids_csv, week_start_date, created_at, 
                   last_modified, status, notes, employee_id_simple
            FROM weekly_preferences 
            WHERE status = ?
            ORDER BY last_modified DESC
        """;

        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, status);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    WeeklyPreferencesDTO preference = mapResultSetToDTO(rs);
                    preferences.add(preference);
                }
            }
        }
        return preferences;
    }

    @Override
    public List<WeeklyPreferencesDTO> findAll() throws SQLException {
        List<WeeklyPreferencesDTO> preferences = new ArrayList<>();
        String sql = """
            SELECT employee_id, preferred_shift_ids_csv, week_start_date, created_at, 
                   last_modified, status, notes, employee_id_simple
            FROM weekly_preferences 
            ORDER BY last_modified DESC
        """;

        try (Statement stmt = Database.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                WeeklyPreferencesDTO preference = mapResultSetToDTO(rs);
                preferences.add(preference);
            }
        }
        return preferences;
    }

    @Override
    public void save(WeeklyPreferencesDTO preferences) throws SQLException {
        String sql = """
            INSERT INTO weekly_preferences 
            (employee_id, preferred_shift_ids_csv, week_start_date, created_at, 
             last_modified, status, notes, employee_id_simple) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, preferences.getEmployeeId());
            ps.setString(2, convertShiftIdsToCSV(preferences.getPreferredShiftIds()));
            ps.setString(3, preferences.getWeekStartDate());
            ps.setString(4, preferences.getCreatedAt());
            ps.setString(5, preferences.getLastModified());
            ps.setString(6, preferences.getStatus());
            ps.setString(7, preferences.getNotes());
            ps.setString(8, preferences.getEmployeeIdSimple());
            ps.executeUpdate();
        }
    }

    @Override
    public void update(WeeklyPreferencesDTO preferences) throws SQLException {
        String sql = """
            UPDATE weekly_preferences 
            SET preferred_shift_ids_csv = ?, last_modified = ?, status = ?, 
                notes = ?, employee_id_simple = ?
            WHERE employee_id = ? AND week_start_date = ?
        """;

        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, convertShiftIdsToCSV(preferences.getPreferredShiftIds()));
            ps.setString(2, preferences.getLastModified());
            ps.setString(3, preferences.getStatus());
            ps.setString(4, preferences.getNotes());
            ps.setString(5, preferences.getEmployeeIdSimple());
            ps.setString(6, preferences.getEmployeeId());
            ps.setString(7, preferences.getWeekStartDate());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(String employeeId, String weekStartDate) throws SQLException {
        String sql = "DELETE FROM weekly_preferences WHERE employee_id = ? AND week_start_date = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, employeeId);
            ps.setString(2, weekStartDate);
            ps.executeUpdate();
        }
    }

    @Override
    public boolean exists(String employeeId, String weekStartDate) throws SQLException {
        String sql = "SELECT 1 FROM weekly_preferences WHERE employee_id = ? AND week_start_date = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, employeeId);
            ps.setString(2, weekStartDate);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private WeeklyPreferencesDTO mapResultSetToDTO(ResultSet rs) throws SQLException {
        WeeklyPreferencesDTO dto = new WeeklyPreferencesDTO();
        dto.setEmployeeId(rs.getString("employee_id"));
        dto.setWeekStartDate(rs.getString("week_start_date"));
        dto.setStatus(rs.getString("status"));
        dto.setCreatedAt(rs.getString("created_at"));
        dto.setLastModified(rs.getString("last_modified"));
        dto.setNotes(rs.getString("notes"));
        dto.setEmployeeIdSimple(rs.getString("employee_id_simple"));

        // המרת CSV לרשימה
        String shiftIds = rs.getString("preferred_shift_ids_csv");
        if (shiftIds != null && !shiftIds.trim().isEmpty()) {
            List<String> shiftIdList = List.of(shiftIds.split(","));
            // הסרת רווחים מיותרים
            shiftIdList = shiftIdList.stream()
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .toList();
            dto.setPreferredShiftIds(shiftIdList);
        } else {
            dto.setPreferredShiftIds(new ArrayList<>());
        }

        return dto;
    }

    private String convertShiftIdsToCSV(List<String> shiftIds) {
        if (shiftIds == null || shiftIds.isEmpty()) {
            return null;
        }
        return String.join(",", shiftIds);
    }
}
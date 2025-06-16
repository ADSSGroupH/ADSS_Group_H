package Dal.HR;

import DTO.HR.ShiftSwapRequestDTO;
import database.Database;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcSwapRequestDAO implements SwapRequestDAO {

    @Override
    public Optional<ShiftSwapRequestDTO> findById(String id) throws SQLException {
        String sql = "SELECT * FROM shift_swap_requests WHERE id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToSwapRequestDTO(rs));
                }
                return Optional.empty();
            }
        }
    }

    @Override
    public List<ShiftSwapRequestDTO> findAll() throws SQLException {
        List<ShiftSwapRequestDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM shift_swap_requests";
        try (Statement stmt = Database.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToSwapRequestDTO(rs));
            }
        }
        return list;
    }

    @Override
    public List<ShiftSwapRequestDTO> findByStatus(String status) throws SQLException {
        List<ShiftSwapRequestDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM shift_swap_requests WHERE status = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToSwapRequestDTO(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<ShiftSwapRequestDTO> findByDateRange(String startDate, String endDate) throws SQLException {
        List<ShiftSwapRequestDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM shift_swap_requests WHERE date >= ? AND date <= ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(startDate));
            ps.setDate(2, Date.valueOf(endDate));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToSwapRequestDTO(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<ShiftSwapRequestDTO> findByRequestor(String requestorId) throws SQLException {
        List<ShiftSwapRequestDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM shift_swap_requests WHERE requestor_id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, requestorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToSwapRequestDTO(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<ShiftSwapRequestDTO> findByShift(String shiftId) throws SQLException {
        List<ShiftSwapRequestDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM shift_swap_requests WHERE from_shift_id = ? OR to_shift_id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, shiftId);
            ps.setString(2, shiftId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToSwapRequestDTO(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<ShiftSwapRequestDTO> findPendingRequests() throws SQLException {
        // נניח שסטטוס "Pending" כתוב כך במערכת
        return findByStatus("Pending");
    }

    @Override
    public List<ShiftSwapRequestDTO> findActiveRequests() throws SQLException {
        List<ShiftSwapRequestDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM shift_swap_requests WHERE is_archived = false";
        try (Statement stmt = Database.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToSwapRequestDTO(rs));
            }
        }
        return list;
    }

    @Override
    public void save(ShiftSwapRequestDTO request) throws SQLException {
        String sql = """
            INSERT INTO shift_swap_requests (id, requestor_id, from_shift_id, to_shift_id, status, date, is_archived, archived_at) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, request.getId());
            ps.setString(2, request.getRequestorId());
            ps.setString(3, request.getFromShiftId());
            ps.setString(4, request.getToShiftId());
            ps.setString(5, request.getStatus());
            ps.setDate(6, request.getDate() != null ? Date.valueOf(request.getDate()) : null);
            ps.setBoolean(7, request.isArchived());
            ps.setDate(8, request.getArchivedAt() != null ? Date.valueOf(request.getArchivedAt()) : null);

            ps.executeUpdate();
        }
    }

    @Override
    public void update(ShiftSwapRequestDTO request) throws SQLException {
        String sql = """
            UPDATE shift_swap_requests SET requestor_id = ?, from_shift_id = ?, to_shift_id = ?, 
            status = ?, date = ?, is_archived = ?, archived_at = ? WHERE id = ?
        """;

        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, request.getRequestorId());
            ps.setString(2, request.getFromShiftId());
            ps.setString(3, request.getToShiftId());
            ps.setString(4, request.getStatus());
            ps.setDate(5, request.getDate() != null ? Date.valueOf(request.getDate()) : null);
            ps.setBoolean(6, request.isArchived());
            ps.setDate(7, request.getArchivedAt() != null ? Date.valueOf(request.getArchivedAt()) : null);
            ps.setString(8, request.getId());

            ps.executeUpdate();
        }
    }

    @Override
    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM shift_swap_requests WHERE id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public boolean exists(String id) throws SQLException {
        String sql = "SELECT 1 FROM shift_swap_requests WHERE id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public int getRequestCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM shift_swap_requests";
        try (Statement stmt = Database.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    @Override
    public int getRequestCountByStatus(String status) throws SQLException {
        String sql = "SELECT COUNT(*) FROM shift_swap_requests WHERE status = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    private ShiftSwapRequestDTO mapResultSetToSwapRequestDTO(ResultSet rs) throws SQLException {
        ShiftSwapRequestDTO request = new ShiftSwapRequestDTO();
        request.setId(rs.getString("id"));
        request.setRequestorId(rs.getString("requestor_id"));
        request.setFromShiftId(rs.getString("from_shift_id"));
        request.setToShiftId(rs.getString("to_shift_id"));
        request.setStatus(rs.getString("status"));

        // המרת שדה date (long millis -> LocalDate)
        long dateMillis = rs.getLong("date");
        if (!rs.wasNull()) {
            LocalDate date = Instant.ofEpochMilli(dateMillis)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            request.setDate(date.toString());
        } else {
            request.setDate(null);
        }

        request.setArchived(rs.getBoolean("is_archived"));

        // המרת שדה archived_at
        long archivedAtMillis = rs.getLong("archived_at");
        if (!rs.wasNull()) {
            LocalDate archivedAt = Instant.ofEpochMilli(archivedAtMillis)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            request.setArchivedAt(archivedAt.toString());
        } else {
            request.setArchivedAt(null);
        }

        return request;
    }

}

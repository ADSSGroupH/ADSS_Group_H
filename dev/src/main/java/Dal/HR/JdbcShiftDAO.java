package Dal.HR;

import DTO.HR.ShiftDTO;
import database.Database;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcShiftDAO implements ShiftDAO {

    private static final String SQL_FIND_BY_ID = "SELECT * FROM shifts WHERE id = ?";
    private static final String SQL_FIND_ALL = "SELECT * FROM shifts";
    private static final String SQL_FIND_ACTIVE = "SELECT * FROM shifts WHERE active = true";
    private static final String SQL_FIND_BY_DATE = "SELECT * FROM shifts WHERE DATE(start_time) = ?";
    //private static final String SQL_FIND_BY_BRANCH = "SELECT * FROM shifts WHERE branch_id = ?";
    private static final String SQL_FIND_BY_MANAGER = "SELECT * FROM shifts WHERE manager_id = ?";
    private static final String SQL_FIND_BY_DATE_RANGE = "SELECT * FROM shifts WHERE DATE(start_time) BETWEEN ? AND ?";
    private static final String SQL_FIND_BY_TYPE = "SELECT * FROM shifts WHERE role = ?";
    private static final String SQL_INSERT = "INSERT INTO shifts (id,date, start_time, end_time, type, required_roles_csv,assignments_csv,shift_manager_id,archived_at,is_archived) VALUES (?, ?, ?, ?, ?, ?, ?,?,?,?)";
    private static final String SQL_UPDATE = "UPDATE shifts SET date = ? , start_time = ?, end_time = ?,type = ?, required_roles_csv = ?,assignments_csv = ? ,shift_manager_id = ?,archived_at = ?,is_archived = ?  WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM shifts WHERE id = ?";
    private static final String SQL_EXISTS = "SELECT 1 FROM shifts WHERE id = ?";
    private static final String SQL_COUNT = "SELECT COUNT(*) FROM shifts";
    private static final String SQL_EXISTS_ON_DATE = "SELECT 1 FROM shifts WHERE DATE(start_time) = ? LIMIT 1";
    private static final String SQL_FIND_BY_DATE_AND_TIME = "SELECT * FROM shifts WHERE date = ? AND start_time = ?";


    @Override
    public Optional<ShiftDTO> findById(String id) throws SQLException {
        try (PreparedStatement ps = Database.getConnection().prepareStatement(SQL_FIND_BY_ID)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToShiftDTO(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<ShiftDTO> findAll() throws SQLException {
        List<ShiftDTO> list = new ArrayList<>();
        try (PreparedStatement ps = Database.getConnection().prepareStatement(SQL_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToShiftDTO(rs));
            }
        }
        return list;
    }

    @Override
    public List<ShiftDTO> findActiveShifts() throws SQLException {
        List<ShiftDTO> list = new ArrayList<>();
        try (PreparedStatement ps = Database.getConnection().prepareStatement(SQL_FIND_ACTIVE);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToShiftDTO(rs));
            }
        }
        return list;
    }

    @Override
    public List<ShiftDTO> findByDate(LocalDate date) throws SQLException {
        List<ShiftDTO> list = new ArrayList<>();
        try (PreparedStatement ps = Database.getConnection().prepareStatement(SQL_FIND_BY_DATE)) {
            ps.setDate(1, Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToShiftDTO(rs));
                }
            }
        }
        return list;
    }


    @Override
    public List<ShiftDTO> findByManager(String managerId) throws SQLException {
        List<ShiftDTO> list = new ArrayList<>();
        try (PreparedStatement ps = Database.getConnection().prepareStatement(SQL_FIND_BY_MANAGER)) {
            ps.setString(1, managerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToShiftDTO(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<ShiftDTO> findByDateRange(LocalDate startDate, LocalDate endDate) throws SQLException {
        List<ShiftDTO> list = new ArrayList<>();
        try (PreparedStatement ps = Database.getConnection().prepareStatement(SQL_FIND_BY_DATE_RANGE)) {
            ps.setDate(1, Date.valueOf(startDate));
            ps.setDate(2, Date.valueOf(endDate));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToShiftDTO(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<ShiftDTO> findByType(String type) throws SQLException {
        List<ShiftDTO> list = new ArrayList<>();
        try (PreparedStatement ps = Database.getConnection().prepareStatement(SQL_FIND_BY_TYPE)) {
            ps.setString(1, type);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToShiftDTO(rs));
                }
            }
        }
        return list;
    }

    @Override
    public void save(ShiftDTO shift) throws SQLException {
        try (PreparedStatement ps = Database.getConnection().prepareStatement(SQL_INSERT)) {

            ps.setString(1, shift.getId());
            ps.setString(2, shift.getDate());
            ps.setString(3, shift.getStartTime());
            ps.setString(4, shift.getEndTime());
            ps.setString(5, shift.getType());
            ps.setString(6, shift.getRequiredRolesCsv());
            ps.setString(7, shift.getAssignmentsCsv());
            ps.setString(8, shift.getShiftManagerId());
            ps.setString(9, shift.getArchivedAt());
            ps.setBoolean(10, shift.isArchived());
            ps.executeUpdate();
        }
    }


    @Override
    public void update(ShiftDTO shift) throws SQLException {
        try (PreparedStatement ps = Database.getConnection().prepareStatement(SQL_UPDATE)) {

            ps.setString(1, shift.getDate());
            ps.setString(2, shift.getStartTime());
            ps.setString(3, shift.getEndTime());
            ps.setString(4, shift.getType());
            ps.setString(5, shift.getRequiredRolesCsv());
            ps.setString(6, shift.getAssignmentsCsv());
            ps.setString(7, shift.getShiftManagerId());
            ps.setString(8, shift.getArchivedAt());
            ps.setBoolean(9, shift.isArchived());
            ps.setString(10, shift.getId());
            ps.executeUpdate();

            ps.executeUpdate();
        }
    }

    @Override
    public void delete(String id) throws SQLException {
        try (PreparedStatement ps = Database.getConnection().prepareStatement(SQL_DELETE)) {
            ps.setString(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public boolean exists(String id) throws SQLException {
        try (PreparedStatement ps = Database.getConnection().prepareStatement(SQL_EXISTS)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public int getShiftCount() throws SQLException {
        try (PreparedStatement ps = Database.getConnection().prepareStatement(SQL_COUNT);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }

    @Override
    public boolean existsShiftOnDate(LocalDate date) throws SQLException {
        try (PreparedStatement ps = Database.getConnection().prepareStatement(SQL_EXISTS_ON_DATE)) {
            ps.setDate(1, Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private ShiftDTO mapResultSetToShiftDTO(ResultSet rs) throws SQLException {
        ShiftDTO shift = new ShiftDTO();
        shift.setId(rs.getString("id"));

        String startTimeStr = rs.getString("start_time");
        String endTimeStr = rs.getString("end_time");

        shift.setStartTime(startTimeStr);
        shift.setEndTime(endTimeStr);

        shift.setRequiredRolesCsv(rs.getString("required_roles_csv"));
        shift.setAssignmentsCsv(rs.getString("assignments_csv"));
        shift.setShiftManagerId(rs.getString("shift_manager_id"));

        shift.setArchived(rs.getInt("is_archived") == 1);
        shift.setDate(rs.getString("date"));
        shift.setType(rs.getString("type"));

        return shift;
    }

    @Override
    public ShiftDTO findByDateAndTime(String date, String startTime) throws SQLException {
        String sql = "SELECT * FROM shifts WHERE date = ? AND ? >= start_time AND ? < end_time";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, date);
            ps.setString(2, startTime);
            ps.setString(3, startTime);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToShiftDTO(rs);
                }
            }
        }
        return null; // אם לא נמצא שום רשומה
    }







}

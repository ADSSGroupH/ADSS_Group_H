package Dal.HR;

import DTO.HR.BranchDTO;
import database.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcBranchDAO implements BranchDAO {
    private static final String SELECT_BY_ID = "SELECT * FROM branches WHERE id = ?";
    private static final String SELECT_ALL = "SELECT * FROM branches";
    private static final String SELECT_BY_NAME = "SELECT * FROM branches WHERE name LIKE ?";
    private static final String SELECT_BY_ADDRESS = "SELECT * FROM branches WHERE address LIKE ?";
    private static final String INSERT = "INSERT INTO branches (id, name, address) VALUES (?, ?, ?)";
    private static final String UPDATE = "UPDATE branches SET name = ?, address = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM branches WHERE id = ?";
    private static final String EXISTS = "SELECT COUNT(*) FROM branches WHERE id = ?";
    private static final String COUNT = "SELECT COUNT(*) FROM branches";
    private static final String SELECT_EMPLOYEE_IDS_BY_BRANCH = "SELECT id FROM employees WHERE branch_id = ?";

    @Override
    public Optional<BranchDTO> findById(String id) throws SQLException {
        try (PreparedStatement stmt = Database.getConnection().prepareStatement(SELECT_BY_ID)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                BranchDTO branch = mapResultSetToBranchDTO(rs);
                String employeeIds = getEmployeeIdsByBranchId(id); // ← העברת conn
                branch.setEmployeeIds(employeeIds);
                return Optional.of(branch);
            }
            return Optional.empty();
        }
    }


    @Override
    public List<BranchDTO> findAll() throws SQLException {
        List<BranchDTO> branches = new ArrayList<>();

        try (PreparedStatement stmt = Database.getConnection().prepareStatement(SELECT_ALL)){
             try(ResultSet rs = stmt.executeQuery()) {

                 while (rs.next()) {
                     BranchDTO branch = mapResultSetToBranchDTO(rs);
                     String employeeIds = getEmployeeIdsByBranchId(branch.getId());
                     branch.setEmployeeIds(employeeIds);
                     branches.add(branch);
                 }
             }
        }

        return branches;
    }


    @Override
    public List<BranchDTO> findByName(String name) throws SQLException {
        List<BranchDTO> branches = new ArrayList<>();
        try (PreparedStatement stmt = Database.getConnection().prepareStatement(SELECT_BY_NAME)) {

            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                BranchDTO branch = mapResultSetToBranchDTO(rs);
                String employeeIds = getEmployeeIdsByBranchId(branch.getId()); // ← העברת conn
                branch.setEmployeeIds(employeeIds);
                branches.add(branch);
            }
        }
        return branches;
    }


    @Override
    public List<BranchDTO> findByAddress(String address) throws SQLException {
        List<BranchDTO> branches = new ArrayList<>();
        try (PreparedStatement stmt = Database.getConnection().prepareStatement(SELECT_BY_ADDRESS)) {

            stmt.setString(1, "%" + address + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                BranchDTO branch = mapResultSetToBranchDTO(rs);
                String employeeIds = getEmployeeIdsByBranchId(branch.getId()); // ← העברת conn
                branch.setEmployeeIds(employeeIds);
                branches.add(branch);
            }
        }
        return branches;
    }


    @Override
    public void save(BranchDTO branch) throws SQLException {
        try (PreparedStatement stmt = Database.getConnection().prepareStatement(INSERT)) {

            stmt.setString(1, branch.getId());
            stmt.setString(2, branch.getName());
            stmt.setString(3, branch.getAddress());
            stmt.executeUpdate();
        }
    }

    @Override
    public void update(BranchDTO branch) throws SQLException {
        try (PreparedStatement stmt = Database.getConnection().prepareStatement(UPDATE)) {

            stmt.setString(1, branch.getName());
            stmt.setString(2, branch.getAddress());
            stmt.setString(3, branch.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(String id) throws SQLException {
        try (PreparedStatement stmt = Database.getConnection().prepareStatement(DELETE)) {

            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public boolean exists(String id) throws SQLException {
        try (PreparedStatement stmt = Database.getConnection().prepareStatement(EXISTS)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }

    @Override
    public int getBranchCount() throws SQLException {
        try (PreparedStatement stmt = Database.getConnection().prepareStatement(COUNT);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }

    private BranchDTO mapResultSetToBranchDTO(ResultSet rs) throws SQLException {
        return new BranchDTO(
                rs.getString("id"),
                rs.getString("name"),
                rs.getString("address"),
                null // employeeIds יוגדר בנפרד
        );
    }

    private String getEmployeeIdsByBranchId(String branchId) throws SQLException {
        StringBuilder sb = new StringBuilder();

        try (PreparedStatement stmt = Database.getConnection().prepareStatement(SELECT_EMPLOYEE_IDS_BY_BRANCH)) {
            stmt.setString(1, branchId);
            try (ResultSet rs = stmt.executeQuery()) {
                boolean first = true;
                while (rs.next()) {
                    if (!first) sb.append(",");
                    sb.append(rs.getString("id"));
                    first = false;
                }
            }
        }

        return sb.toString();
    }

}

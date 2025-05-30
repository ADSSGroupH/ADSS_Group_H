package dal.transportation_dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import database.Database;
import dto_folder.LicenseType;
import dto_folder.TruckDTO;

public class JdbcTruckDAO implements TruckDAO {

        @Override
    public Optional<TruckDTO> findTruckById(String plateNumber) throws SQLException {
        String sql = "SELECT plateNumber, model, netWeight, maxWeight, licenseType  FROM trucks WHERE plateNumber = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, plateNumber);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next()
                        ? Optional.of(new TruckDTO(rs.getString("plateNumber"), rs.getString("model"),
                                rs.getInt("netWeight"), rs.getInt("maxWeight"),
                                LicenseType.valueOf(rs.getString("licenseType"))))
                        : Optional.empty();
            }
        }
    }

    @Override
    public TruckDTO save(TruckDTO truck) throws SQLException {
           String sql = "INSERT INTO trucks (plateNumber, model, netWeight, maxWeight, licenseType) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
                ps.setString(1, truck.getPlateNumber());
                ps.setString(2, truck.getModel());
                ps.setInt(3, truck.getNetWeight());
                ps.setInt(4, truck.getMaxWeight());
                ps.setString(5, truck.getLicenseType().name());
                ps.executeUpdate();
                return truck;
        }
    }

    public void delete(String plateNumber) throws SQLException {
        String sql = "DELETE FROM trucks WHERE plateNumber = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, plateNumber);
            ps.executeUpdate();
        }
    }

}
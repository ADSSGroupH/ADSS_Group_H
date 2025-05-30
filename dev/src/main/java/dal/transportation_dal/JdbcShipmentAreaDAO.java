package dal.transportation_dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import database.Database;
import dto_folder.ShipmentAreaDTO;

public class JdbcShipmentAreaDAO implements ShipmentAreaDAO {

        @Override
    public Optional<ShipmentAreaDTO> findById(int id) throws SQLException {
        String sql = "SELECT id, name FROM shipmentAreas WHERE id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next()
                        ? Optional.of(new ShipmentAreaDTO(rs.getInt("id"), rs.getString("name")))
                        : Optional.empty();
            }
        }
    }
         @Override
        public void save(ShipmentAreaDTO shipmentArea) throws SQLException {
           String sql = "INSERT INTO shipmentAreas (id, name) VALUES (?, ?)";
            try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
                ps.setInt(1, shipmentArea.getId());
                ps.setString(2, shipmentArea.getName());

                ps.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM shipmentAreas WHERE id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }


}

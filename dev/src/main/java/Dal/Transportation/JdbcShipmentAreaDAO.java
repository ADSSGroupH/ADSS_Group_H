package Dal.Transportation;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import DTO.Transportation.ShipmentAreaDTO;
import database.Database;

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

    public List<ShipmentAreaDTO> findAll() throws SQLException{
        String sql = "SELECT id, name FROM shipmentAreas";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<ShipmentAreaDTO> shipmentAreas = new java.util.ArrayList<>();
            while (rs.next()) {
                shipmentAreas.add(new ShipmentAreaDTO(rs.getInt("id"), rs.getString("name")));
            }
            return shipmentAreas;
        }
    }


}
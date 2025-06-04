package Dal.transportation_dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import DTO.TransportationDTO;
import database.Database;
import database.Database;


public class JdbcTransportationDAO implements TransportationDAO {

    @Override
    public Optional<TransportationDTO> getTransportationById(int id) throws SQLException{
        String sql = "SELECT id, date, departureTime, arrivalTime, truckPlateNumber, driverName, succeeded, originName, originShipmentAreaId, accident  FROM transportations WHERE id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next()
                        ? Optional.of(new TransportationDTO(rs.getInt(id), rs.getString("date"),
                                rs.getString("departureTime"), rs.getString("arrivalTime"),
                                rs.getString("truckPlateNumber"), rs.getString("driverName"),
                                rs.getBoolean("succeeded"), rs.getString("originName"),
                                rs.getInt("originShipmentAreaId"), rs.getString("accident")))
                        : Optional.empty();
            }
        }
    }

    @Override
    public List<TransportationDTO> getAllTransportations() throws SQLException{
        String sql = "SELECT id, date, departureTime, arrivalTime, truckPlateNumber, driverName, succeeded, originName, originShipmentAreaId, accident FROM transportations";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<TransportationDTO> transportations = new ArrayList<>();
            while (rs.next()) {
                transportations.add(new TransportationDTO(rs.getInt("id"), rs.getString("date"),
                        rs.getString("departureTime"), rs.getString("arrivalTime"),
                        rs.getString("truckPlateNumber"), rs.getString("driverName"),
                        rs.getBoolean("succeeded"), rs.getString("originName"),
                        rs.getInt("originShipmentAreaId"), rs.getString("accident")));
            }
            return transportations;
        }
    }

    @Override
    public void saveTransportation(TransportationDTO transportation) throws SQLException {
        String sql = "INSERT INTO transportations (id, date, departureTime, arrivalTime, truckPlateNumber, driverName, succeeded, originName, originShipmentAreaId, accident) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, transportation.getId());
            ps.setString(2, transportation.getDate());
            ps.setString(3, transportation.getDepartureTime());
            ps.setString(4, transportation.getArrivalTime());
            ps.setString(5, transportation.getTruckPlateNumber());
            ps.setString(6, transportation.getDriverName());
            ps.setBoolean(7, transportation.isSucceeded());
            ps.setString(8, transportation.getOriginName());
            ps.setInt(9, transportation.getOriginShipmentAreaId());
            ps.setString(10, transportation.getAccident());
            ps.executeUpdate();
        }
    }

    @Override
    public void deleteTransportation(int id) throws SQLException {
        String sql = "DELETE FROM transportations WHERE id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public List<TransportationDTO> getAllTransportationsByDriverName(String driverName) throws SQLException{
        String sql = "SELECT id, date, departureTime, arrivalTime, truckPlateNumber, driverName, succeeded, originName, originShipmentAreaId, accident FROM transportations WHERE driverName = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, driverName);
            try (ResultSet rs = ps.executeQuery()) {
                List<TransportationDTO> transportations = new ArrayList<>();
                while (rs.next()) {
                    transportations.add(new TransportationDTO(rs.getInt("id"), rs.getString("date"),
                            rs.getString("departureTime"), rs.getString("arrivalTime"),
                            rs.getString("truckPlateNumber"), rs.getString("driverName"),
                            rs.getBoolean("succeeded"), rs.getString("originName"),
                            rs.getInt("originShipmentAreaId"), rs.getString("accident")));
                }
                return transportations;
            }
        }
    }

    @Override
    public List<TransportationDTO> getAllTransportationsByTruckPlateNumber(String truckPlateNumber) throws SQLException{
        String sql = "SELECT id, date, departureTime, arrivalTime, truckPlateNumber, driverName, succeeded, originName, originShipmentAreaId, accident FROM transportations WHERE truckPlateNumber = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, truckPlateNumber);
            try (ResultSet rs = ps.executeQuery()) {
                List<TransportationDTO> transportations = new ArrayList<>();
                while (rs.next()) {
                    transportations.add(new TransportationDTO(rs.getInt("id"), rs.getString("date"),
                            rs.getString("departureTime"), rs.getString("arrivalTime"),
                            rs.getString("truckPlateNumber"), rs.getString("driverName"),
                            rs.getBoolean("succeeded"), rs.getString("originName"),
                            rs.getInt("originShipmentAreaId"), rs.getString("accident")));
                }
                return transportations;
            }
        }
    }
}

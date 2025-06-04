package Dal.transportation_dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import database.Database;
import database.Database;

public class JdbcSiteDAO implements SiteDAO {

        @Override
    public Optional<SiteDTO> findSite(String name, int shipmentAreaId) throws SQLException {
        String sql = "SELECT name, shipmentAreaId, address, phoneNumber, contactPersonName  FROM sites WHERE name = ? and shipmentAreaId = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setInt(2, shipmentAreaId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next()
                        ? Optional.of(new SiteDTO(rs.getString("name"), rs.getInt("shipmentAreaId"),
                                rs.getString("address"), rs.getString("phoneNumber"),
                                rs.getString("contactPersonName")
                                ))
                        : Optional.empty();
            }
        }
    }

    @Override
    public void save(SiteDTO site) throws SQLException {
           String sql = "INSERT INTO sites (name, shipmentAreaId, address, phoneNumber, contactPersonName) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
                ps.setString(1, site.getName());
                ps.setInt(2, site.getShipmentAreaId());
                ps.setString(3, site.getAddress());
                ps.setString(4, site.getPhoneNumber());
                ps.setString(5, site.getContactPersonName());
                ps.executeUpdate();
        }
    }
    @Override
    public void delete(String name, int shipmentAreaId) throws SQLException {
        String sql = "DELETE FROM sites WHERE plateNumber = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setInt(2, shipmentAreaId);
            ps.executeUpdate();
        }
    }

    @Override
    public List<SiteDTO> findAllSitesByShipmentAreaId(int shipmentAreaId) throws SQLException {
        String sql = "SELECT name, shipmentAreaId, address, phoneNumber, contactPersonName FROM sites WHERE shipmentAreaId = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, shipmentAreaId);
            try (ResultSet rs = ps.executeQuery()) {
                List<SiteDTO> sites = new ArrayList<>();
                while (rs.next()) {
                    sites.add(new SiteDTO(rs.getString("name"), rs.getInt("shipmentAreaId"),
                            rs.getString("address"), rs.getString("phoneNumber"),
                            rs.getString("contactPersonName")));
                }
                return sites;
            }
        }
    }
    public void deleteAllSitesByShipmentAreaId(int shipmentAreaId) throws SQLException {
        String sql = "DELETE FROM sites WHERE shipmentAreaId = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, shipmentAreaId);
            ps.executeUpdate();
        }
    }

}

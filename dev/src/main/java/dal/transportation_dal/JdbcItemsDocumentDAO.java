package Dal.transportation_dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import database.Database;
import database.Database;

public class JdbcItemsDocumentDAO implements ItemsDocumentDAO {

    @Override
    public Optional<ItemsDocumentDTO> getItemsDocument(int id) throws SQLException{
        String sql = "SELECT id, transportationId, shipmentAreaId, destinationName, arrivalTime  FROM ItemsDocuments WHERE id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next()
                        ? Optional.of(new ItemsDocumentDTO(rs.getInt("id"), rs.getInt("transportationId"),
                                rs.getInt("shipmentAreaId"), rs.getString("destinationName"),
                                rs.getString("arrivalTime")
                                ))
                        : Optional.empty();
            }
        }
    }

    @Override
    public void saveItemsDocument(ItemsDocumentDTO itemsDocument) throws SQLException{
        String sql = "INSERT INTO ItemsDocuments (id, transportationId, shipmentAreaId, destinationName, arrivalTime) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, itemsDocument.getId());
            ps.setInt(2, itemsDocument.getTransportationId());
            ps.setInt(3, itemsDocument.getShipmentAreaId());
            ps.setString(4, itemsDocument.getDestinationName());
            ps.setObject(5, itemsDocument.getArrivalTime());
            ps.executeUpdate();
        }
    }

    @Override
    public void deleteItemsDocument(int id) throws SQLException {
        String sql = "DELETE FROM ItemsDocuments WHERE id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public List<ItemsDocumentDTO> getAllTransportationItemsDocuments(int transportationId) throws SQLException{
        String sql = "SELECT id, transportationId, shipmentAreaId, destinationName, arrivalTime FROM ItemsDocuments WHERE transportationId = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, transportationId);
            try (ResultSet rs = ps.executeQuery()) {
                List<ItemsDocumentDTO> itemsDocuments = new java.util.ArrayList<>();
                while (rs.next()) {
                    itemsDocuments.add(new ItemsDocumentDTO(rs.getInt("id"), rs.getInt("transportationId"),
                            rs.getInt("shipmentAreaId"), rs.getString("destinationName"),
                            rs.getString("arrivalTime")));
                }
                return itemsDocuments;
            }
        }
    }

    @Override
   public List<Integer> getAllShipmentAreaIdsByTransportationId(int transportationId) throws SQLException{
        String sql = "SELECT shipmentAreaId FROM ItemsDocuments WHERE transportationId = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, transportationId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Integer> shipmentAreaIds = new java.util.ArrayList<>();
                while (rs.next()) {
                    shipmentAreaIds.add(rs.getInt("shipmentAreaId"));
                }
                return shipmentAreaIds;
            }
        }
   }

    @Override
    public void deleteAllItemsDocumentsByTransportationId(int transportationId) throws SQLException {
        String sql = "DELETE FROM ItemsDocuments WHERE transportationId = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, transportationId);
            ps.executeUpdate();
        }
    }

    @Override
    public List<Integer> getAllItemDocumentIdByTransportationId(int transportationId) throws SQLException{
        String sql = "SELECT id FROM ItemsDocuments WHERE transportationId = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, transportationId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Integer> itemDocumentIds = new java.util.ArrayList<>();
                while (rs.next()) {
                    itemDocumentIds.add(rs.getInt("id"));
                }
                return itemDocumentIds;
            }
        }
    }
}

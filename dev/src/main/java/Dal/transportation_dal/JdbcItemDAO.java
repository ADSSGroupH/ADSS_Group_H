package Dal.transportation_dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DTO.ItemDTO;
import database.Database;

public class JdbcItemDAO implements ItemDAO {

    @Override
        public List<ItemDTO> findItems(int ItemsDocumentID) throws SQLException {
        String sql = "SELECT itemsDocumentId, itemId, name, weight, quantity FROM items WHERE ItemsDocumentID = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, ItemsDocumentID);
            try (ResultSet rs = ps.executeQuery()) {
                List<ItemDTO> items = new ArrayList<>();
                while (rs.next()) {
                    items.add(new ItemDTO(rs.getInt("itemsDocumentId"), rs.getInt("itemId"),
                            rs.getString("name"), rs.getInt("weight"),
                            rs.getInt("quantity")));
                }
                return items;
            }
        }
    }

    @Override
    public void deleteallItemsByItemsDocumentID(int ItemsDocumentID) throws SQLException {
        String sql = "DELETE FROM items WHERE ItemsDocumentID = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, ItemsDocumentID);
            ps.executeUpdate();
        }
    }

    @Override
    public void saveItem(ItemDTO item) throws SQLException {
        String sql = "INSERT INTO items (itemsDocumentId, itemId, name, weight, quantity) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, item.getItemsDocumentId());
            ps.setInt(2, item.getItemId());
            ps.setString(3, item.getName());
            ps.setInt(4, item.getWeight());
            ps.setInt(5, item.getQuantity());
            ps.executeUpdate();
        }
    }

    @Override
    public void deleteItem(int id) throws SQLException {
        String sql = "DELETE FROM items WHERE itemId = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public List<ItemDTO> getAllItemsByItemsDocumentId(int itemsDocumentId) throws SQLException{
        String sql = "SELECT itemsDocumentId, itemId, name, weight, quantity FROM items WHERE itemsDocumentId = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, itemsDocumentId);
            try (ResultSet rs = ps.executeQuery()) {
                List<ItemDTO> items = new ArrayList<>();
                while (rs.next()) {
                    items.add(new ItemDTO(rs.getInt("itemsDocumentId"), rs.getInt("itemId"),
                            rs.getString("name"), rs.getInt("weight"),
                            rs.getInt("quantity")));
                }
                return items;
            }
        }
    }
}


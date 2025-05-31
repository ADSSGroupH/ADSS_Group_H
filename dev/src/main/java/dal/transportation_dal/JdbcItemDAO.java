package dal.transportation_dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.Database;
import dto_folder.ItemDTO;

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
}

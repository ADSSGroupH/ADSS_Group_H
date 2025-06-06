package Dal.Transportation;

import DTO.Transportation.ItemDTO;

import java.sql.SQLException;
import java.util.List;

public interface  ItemDAO {
    public List<ItemDTO> findItems(int ItemsDocumentID) throws SQLException;
    public void deleteallItemsByItemsDocumentID(int ItemsDocumentID) throws SQLException;
    public void saveItem(ItemDTO item) throws SQLException;
    public void deleteItem(int id) throws SQLException;
    public List<ItemDTO> getAllItemsByItemsDocumentId(int itemsDocumentId) throws SQLException;
}

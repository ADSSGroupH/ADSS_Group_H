package dal.transportation_dal;

import java.sql.SQLException;
import java.util.List;

import dto_folder.ItemDTO;

public interface  ItemDAO {
    public List<ItemDTO> findItems(int ItemsDocumentID) throws SQLException;
    public void deleteallItemsByItemsDocumentID(int ItemsDocumentID) throws SQLException;
}

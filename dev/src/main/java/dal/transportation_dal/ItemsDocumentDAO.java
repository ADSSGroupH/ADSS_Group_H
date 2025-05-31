package dal.transportation_dal;

import java.sql.SQLException;
import java.util.Optional;

import dto_folder.ItemsDocumentDTO;

public interface ItemsDocumentDAO {
    public Optional<ItemsDocumentDTO> getItemsDocument(int id) throws SQLException;
    public void saveItemsDocument(ItemsDocumentDTO itemsDocument) throws SQLException;
    public void deleteItemsDocument(int id) throws SQLException;
}

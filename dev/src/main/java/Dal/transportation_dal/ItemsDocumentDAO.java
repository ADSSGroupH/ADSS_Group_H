package Dal.transportation_dal;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import DTO.ItemsDocumentDTO;

public interface ItemsDocumentDAO {
    public Optional<ItemsDocumentDTO> getItemsDocument(int id) throws SQLException;
    public void saveItemsDocument(ItemsDocumentDTO itemsDocument) throws SQLException;
    public void deleteItemsDocument(int id) throws SQLException;
    public List<ItemsDocumentDTO> getAllTransportationItemsDocuments(int transportationId) throws SQLException;
    public List<Integer> getAllShipmentAreaIdsByTransportationId(int transportationId) throws SQLException;
    public void deleteAllItemsDocumentsByTransportationId(int transportationId) throws SQLException;
    public List<Integer> getAllItemDocumentIdByTransportationId(int transportationId) throws SQLException;
}

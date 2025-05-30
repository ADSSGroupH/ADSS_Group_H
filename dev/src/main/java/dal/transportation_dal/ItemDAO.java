package dal.transportation_dal;

import dto_folder.ItemDTO;
import java.util.List;

public interface  ItemDAO {
    public List<ItemDTO> findItems(int ItemsDocumentID);
}

package dal;

import dto_folder.TruckDTO;

public interface TruckDAO {
    public TruckDTO findTruckById(int id);
}

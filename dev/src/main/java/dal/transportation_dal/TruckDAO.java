package dal.transportation_dal;

import java.sql.SQLException;
import java.util.Optional;

import dto_folder.TruckDTO;

public interface TruckDAO {
    public Optional<TruckDTO> findTruckById(String plateNumber) throws SQLException;
    public TruckDTO save(TruckDTO user) throws SQLException;
    public void delete(String plateNumber) throws SQLException;
}

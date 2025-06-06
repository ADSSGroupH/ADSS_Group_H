package Dal.Transportation;

import DTO.Transportation.TruckDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


public interface TruckDAO {
    public Optional<TruckDTO> findTruckById(String plateNumber) throws SQLException;
    public void save(TruckDTO user) throws SQLException;
    public void delete(String plateNumber) throws SQLException;
    public List<TruckDTO> getAllTrucks() throws SQLException;
}

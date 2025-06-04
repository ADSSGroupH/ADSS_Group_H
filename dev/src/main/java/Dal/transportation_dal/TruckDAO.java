package Dal.transportation_dal;

import java.sql.SQLException;
import java.util.Optional;

import DTO.TruckDTO;

public interface TruckDAO {
    public Optional<TruckDTO> findTruckById(String plateNumber) throws SQLException;
    public void save(TruckDTO user) throws SQLException;
    public void delete(String plateNumber) throws SQLException;
}

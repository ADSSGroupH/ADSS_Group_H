package Dal.transportation_dal;

import java.sql.SQLException;
import java.util.Optional;

import DTO.ShipmentAreaDTO;

public interface ShipmentAreaDAO {
    public Optional<ShipmentAreaDTO> findById(int id) throws SQLException;
    public void save(ShipmentAreaDTO shipmentArea) throws SQLException;
    public void delete(int id) throws SQLException;
}

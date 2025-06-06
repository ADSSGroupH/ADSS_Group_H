package Dal.Transportation;


import DTO.Transportation.ShipmentAreaDTO;

import java.sql.SQLException;
import java.util.Optional;



public interface ShipmentAreaDAO {
    public Optional<ShipmentAreaDTO> findById(int id) throws SQLException;
    public void save(ShipmentAreaDTO shipmentArea) throws SQLException;
    public void delete(int id) throws SQLException;
}

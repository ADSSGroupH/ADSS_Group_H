package Dal.Transportation;


import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import DTO.Transportation.ShipmentAreaDTO;



public interface ShipmentAreaDAO {
    public Optional<ShipmentAreaDTO> findById(int id) throws SQLException;
    public void save(ShipmentAreaDTO shipmentArea) throws SQLException;
    public void delete(int id) throws SQLException;
    public List<ShipmentAreaDTO> findAll() throws SQLException;
}

package Dal.Transportation;


import DTO.Transportation.TransportationDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


public interface TransportationDAO {
    public Optional<TransportationDTO> getTransportationById(int id) throws SQLException;
    public List<TransportationDTO> getAllTransportations() throws SQLException;
    public void saveTransportation(TransportationDTO transportation) throws SQLException;
    public void deleteTransportation(int id) throws SQLException;
    public List<TransportationDTO> getAllTransportationsByDriverName(String driverName) throws SQLException;
    public List<TransportationDTO> getAllTransportationsByTruckPlateNumber(String truckPlateNumber) throws SQLException;
}

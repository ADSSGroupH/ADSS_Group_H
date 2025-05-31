package dal.transportation_dal;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import dto_folder.TransportationDTO;

public interface TransportationDAO {
    public Optional<TransportationDTO> getTransportationById(int id) throws SQLException;
    public List<TransportationDTO> getAllTransportations() throws SQLException;
    public void saveTransportation(TransportationDTO transportation) throws SQLException;
    public void deleteTransportation(int id) throws SQLException;
}

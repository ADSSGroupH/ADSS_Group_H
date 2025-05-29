package dal;

import java.util.List;

import dto_folder.TransportationDTO;

public interface TransportationDAO {
    public TransportationDTO getTransportationById(int id);
    public List<TransportationDTO> getAllTransportations();
}
package Dal.Transportation;

import DTO.Transportation.SiteDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface  SiteDAO {
    public Optional<SiteDTO> findSite(String name, int shipmentAreaId) throws SQLException;
    public void save(SiteDTO site) throws SQLException;
    public void delete(String name, int shipmentAreaId) throws SQLException;
    public List<SiteDTO> findAllSitesByShipmentAreaId(int shipmentAreaId) throws SQLException;
    public void deleteAllSitesByShipmentAreaId(int shipmentAreaId) throws SQLException;
}
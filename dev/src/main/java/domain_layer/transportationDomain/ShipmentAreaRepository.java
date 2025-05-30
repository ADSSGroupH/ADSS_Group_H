package domain_layer.transportationDomain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import dal.transportation_dal.JdbcShipmentAreaDAO;
import dal.transportation_dal.JdbcSiteDAO;
import dto_folder.ShipmentAreaDTO;
import dto_folder.SiteDTO;

public class ShipmentAreaRepository {
    private Map<Integer, ShipmentArea> shipmentAreaMap;
    private JdbcSiteDAO jdbcSiteDAO;
    private JdbcShipmentAreaDAO jdbcShipmentAreaDAO;
    
    public ShipmentAreaRepository() {
        this.shipmentAreaMap = new HashMap<>();
        this.jdbcSiteDAO = new JdbcSiteDAO();
        this.jdbcShipmentAreaDAO = new JdbcShipmentAreaDAO();
    }
    public void addShipmentArea(ShipmentArea shipmentArea) {

        shipmentAreaMap.put(shipmentArea.getId(), shipmentArea);
        try {
            jdbcShipmentAreaDAO.save(shipmentArea.toDTO());
            for (Site site : shipmentArea.getSites()) {
                jdbcSiteDAO.save(site.toDTO());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public ShipmentArea getShipmentArea(int id) {
        if (shipmentAreaMap.containsKey(id)) {
            return shipmentAreaMap.get(id);
        }
        try {
            Optional<ShipmentAreaDTO> tempShipmentArea = jdbcShipmentAreaDAO.findById(id);
            if (!tempShipmentArea.isPresent()) {
                return null;
            }
            List<SiteDTO> siteDTOs = jdbcSiteDAO.findAllSitesByShipmentAreaId(id);
            ShipmentAreaDTO shipmentAreaDTO = tempShipmentArea.get();
            ShipmentArea shipmentArea = new ShipmentArea(shipmentAreaDTO, siteDTOs);
            shipmentAreaMap.put(id, shipmentArea);
            return shipmentAreaMap.get(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public void removeShipmentArea(int id) {
        shipmentAreaMap.remove(id);
    }
    public boolean shipmentAreaExists(int id) {
        return shipmentAreaMap.containsKey(id);
    }
}

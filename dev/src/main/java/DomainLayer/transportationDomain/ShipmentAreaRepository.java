package DomainLayer.transportationDomain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import DTO.ShipmentAreaDTO;
import DTO.SiteDTO;
import Dal.transportation_dal.JdbcShipmentAreaDAO;
import Dal.transportation_dal.JdbcSiteDAO;


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
            return null;
        }
    }
    public void removeShipmentArea(int id) {
        if(shipmentAreaExists(id))
            shipmentAreaMap.remove(id);
        try {
            jdbcShipmentAreaDAO.delete(id);
            jdbcSiteDAO.deleteAllSitesByShipmentAreaId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean shipmentAreaExists(int id) {
        if (shipmentAreaMap.containsKey(id)) {
            return true;
        }
        try {
        Optional<ShipmentAreaDTO> tempShipmentArea = jdbcShipmentAreaDAO.findById(id);
        return tempShipmentArea.isPresent();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addSiteToShipmentArea(int shipmentAreaId, Site site) {
        ShipmentArea shipmentArea = getShipmentArea(shipmentAreaId);
        if (shipmentArea != null) {
            shipmentArea.addSite(site);
            try {
                jdbcSiteDAO.save(site.toDTO());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void removeSiteFromShipmentArea(int shipmentAreaId, Site site) {
        ShipmentArea shipmentArea = getShipmentArea(shipmentAreaId);
        if (shipmentArea != null) {
            shipmentArea.removeSite(site);
            try {
                jdbcSiteDAO.delete(site.getName(), shipmentAreaId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean siteExistsInShipmentArea(int shipmentAreaId, String siteName) {
        ShipmentArea shipmentArea = getShipmentArea(shipmentAreaId);
        if (shipmentArea != null) {
            return shipmentArea.getSiteByName(siteName) != null;
        }
        return false;
    }

    public  boolean checkSiteExists(String siteName, List<Integer> shipmentAreaIds) {
     for (Integer id : shipmentAreaIds) {
            if (siteExistsInShipmentArea(id, siteName)) {
                return true;
            }
        }
        return false;   
    }

    public Site getSiteByName(String siteName, int shipmentAreaId) {
        ShipmentArea shipmentArea = getShipmentArea(shipmentAreaId);
        if (shipmentArea != null) {
            return shipmentArea.getSiteByName(siteName);
        }
        return null;
    }

    public List<ShipmentArea> getAllShipmentAreas() {
        return new ArrayList<>(shipmentAreaMap.values());
    }

}

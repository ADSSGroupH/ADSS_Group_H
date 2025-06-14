package DomainLayer.Transportation.Repositories;

import java.util.*;

import DTO.Transportation.ItemDTO;
import DTO.Transportation.ItemsDocumentDTO;
import DTO.Transportation.TransportationDTO;
import Dal.Transportation.JdbcItemDAO;
import Dal.Transportation.JdbcItemsDocumentDAO;
import Dal.Transportation.JdbcSiteDAO;
import Dal.Transportation.JdbcTransportationDAO;
import DomainLayer.Transportation.Item;
import DomainLayer.Transportation.ItemsDocument;
import DomainLayer.Transportation.Site;
import DomainLayer.Transportation.Transportation;


public class TransportationRepository {
    private Map<Integer, Transportation> transportationMap;
    private JdbcTransportationDAO jdbcTransportationDAO;
    private JdbcItemsDocumentDAO jdbcItemsDocumentDAO;
    private JdbcItemDAO jdbcItemDAO;
    private JdbcSiteDAO jdbcSiteDAO;

    public TransportationRepository() {
        this.transportationMap = new HashMap<>();
        this.jdbcTransportationDAO = new JdbcTransportationDAO();
        this.jdbcItemsDocumentDAO = new JdbcItemsDocumentDAO();
        this.jdbcItemDAO = new JdbcItemDAO();
        this.jdbcSiteDAO = new JdbcSiteDAO();
    }
    public void addTransportation(int id, Transportation transportation) {
        transportationMap.put(id, transportation);
        try {
            jdbcTransportationDAO.saveTransportation(transportation.toDTO());
            for (ItemsDocument itemDocument : transportation.getItemsDocument()) {
                jdbcItemsDocumentDAO.saveItemsDocument(itemDocument.toDTO(transportation.getId()));
                for (Item item : itemDocument.getItems()) {
                    jdbcItemDAO.saveItem(item.toDTO(itemDocument.getId()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Transportation getTransportation(int id) {
        if (transportationMap.containsKey(id)) {
            return transportationMap.get(id);
        }
        try {
            Optional<TransportationDTO> transportationdto = jdbcTransportationDAO.getTransportationById(id);
            if (!transportationdto.isPresent()) {
                return null;
            }

            Site origin = new Site(jdbcSiteDAO.findSite(transportationdto.get().getOriginName(),  transportationdto.get().getOriginShipmentAreaId()).get());
            List<ItemsDocumentDTO> itemsDocuments = jdbcItemsDocumentDAO.getAllTransportationItemsDocuments(id);
            List<ItemsDocument> itemsDocuments2 = new ArrayList<>();
            for (ItemsDocumentDTO itemsDocument3 : itemsDocuments) {
                Site destination = new Site(jdbcSiteDAO.findSite(itemsDocument3.getDestinationName(), itemsDocument3.getShipmentAreaId()).get());
                List<ItemDTO> itemDTOs = jdbcItemDAO.getAllItemsByItemsDocumentId(itemsDocument3.getId());
                List<Item> items = new ArrayList<>();
                for (ItemDTO itemDTO : itemDTOs) {
                    Item item = new Item(itemDTO);
                    items.add(item);
                }
                itemsDocuments2.add(new ItemsDocument(itemsDocument3, destination, items));
            }
            List<Integer> shipmentAreaIds = jdbcItemsDocumentDAO.getAllShipmentAreaIdsByTransportationId(id);
            return new Transportation(transportationdto.get(), origin, itemsDocuments2, shipmentAreaIds);
        } catch (Exception e) {
            return null;
        }
    }

    public void removeTransportation(int id) {
        if (transportationExists(id)) {
            try {
                List<Integer> itemsDocumentIds = jdbcItemsDocumentDAO.getAllItemDocumentIdByTransportationId(id);
                for (Integer itemsDocumentId : itemsDocumentIds) {
                    jdbcItemDAO.deleteallItemsByItemsDocumentID(itemsDocumentId);
                }
                jdbcItemsDocumentDAO.deleteAllItemsDocumentsByTransportationId(id);
                jdbcTransportationDAO.deleteTransportation(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (transportationMap.containsKey(id))
            transportationMap.remove(id);
    }

    public boolean transportationExists(int id) {
        if (transportationMap.containsKey(id)) {
            return true;
        }
        try {
            return jdbcTransportationDAO.getTransportationById(id).isPresent();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public String displayAllTransportations() {
        try {
            List<TransportationDTO> transportationDTOs = jdbcTransportationDAO.getAllTransportations();
            for (TransportationDTO transportationDTO : transportationDTOs) {
                Site origin = new Site(jdbcSiteDAO.findSite(transportationDTO.getOriginName(), transportationDTO.getOriginShipmentAreaId()).get());
                List<ItemsDocumentDTO> itemsDocuments = jdbcItemsDocumentDAO.getAllTransportationItemsDocuments(transportationDTO.getId());
                List<ItemsDocument> itemsDocuments2 = new ArrayList<>();
                for (ItemsDocumentDTO itemsDocument3 : itemsDocuments) {
                    Site destination = new Site(jdbcSiteDAO.findSite(itemsDocument3.getDestinationName(), itemsDocument3.getShipmentAreaId()).get());
                    List<ItemDTO> itemDTOs = jdbcItemDAO.getAllItemsByItemsDocumentId(itemsDocument3.getId());
                    List<Item> items = new ArrayList<>();
                    for (ItemDTO itemDTO : itemDTOs) {
                        Item item = new Item(itemDTO);
                        items.add(item);
                    }
                    itemsDocuments2.add(new ItemsDocument(itemsDocument3, destination, items));
                }
                List<Integer> shipmentAreaIds = jdbcItemsDocumentDAO.getAllShipmentAreaIdsByTransportationId(transportationDTO.getId());
                Transportation transportation = new Transportation(transportationDTO, origin, itemsDocuments2, shipmentAreaIds);
                transportationMap.put(transportation.getId(), transportation);
            }
        } catch (Exception e) {
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, Transportation> entry : transportationMap.entrySet()) {
            Transportation t = entry.getValue();
            sb.append("ID: ").append(t.getId()).append(", Date: ").append(t.getDate())
                    .append(", Departure Time: ").append(t.getDepartureTime())
                    .append(", Truck Plate Number: ").append(t.getTruckPlateNumber())
                    .append(", Driver Name: ").append(t.getDriverName()).append("\n");
        }
        return sb.toString();
    }

    public List<Transportation> getTransportationsByDriverName(String driverName) {
        List<Transportation> transportations = new ArrayList<>();
        try{
            List<TransportationDTO> transportationDTOs = jdbcTransportationDAO.getAllTransportationsByDriverName(driverName);
            for(TransportationDTO transportationDTO : transportationDTOs) {
                Site origin = new Site(jdbcSiteDAO.findSite(transportationDTO.getOriginName(), transportationDTO.getOriginShipmentAreaId()).get());
                List<ItemsDocumentDTO> itemsDocuments = jdbcItemsDocumentDAO.getAllTransportationItemsDocuments(transportationDTO.getId());
                List<ItemsDocument> itemsDocuments2 = new ArrayList<>();
                for (ItemsDocumentDTO itemsDocument3 : itemsDocuments) {
                    Site destination = new Site(jdbcSiteDAO.findSite(itemsDocument3.getDestinationName(), itemsDocument3.getShipmentAreaId()).get());
                    List<ItemDTO> itemDTOs = jdbcItemDAO.getAllItemsByItemsDocumentId(itemsDocument3.getId());
                    List<Item> items = new ArrayList<>();
                    for (ItemDTO itemDTO : itemDTOs) {
                        Item item = new Item(itemDTO);
                        items.add(item);
                    }
                    itemsDocuments2.add(new ItemsDocument(itemsDocument3, destination, items));
                }
                List<Integer> shipmentAreaIds = jdbcItemsDocumentDAO.getAllShipmentAreaIdsByTransportationId(transportationDTO.getId());
                transportations.add(new Transportation(transportationDTO, origin, itemsDocuments2, shipmentAreaIds));
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
        return transportations;

    }

    public List<Transportation> getTransportationsByPlateNumber(String plateNumber) {
        List<Transportation> transportations = new ArrayList<>();
        try {
            List<TransportationDTO> transportationDTOs = jdbcTransportationDAO.getAllTransportationsByTruckPlateNumber(plateNumber);
            for (TransportationDTO transportationDTO : transportationDTOs) {
                Site origin = new Site(jdbcSiteDAO.findSite(transportationDTO.getOriginName(), transportationDTO.getOriginShipmentAreaId()).get());
                List<ItemsDocumentDTO> itemsDocuments = jdbcItemsDocumentDAO.getAllTransportationItemsDocuments(transportationDTO.getId());
                List<ItemsDocument> itemsDocuments2 = new ArrayList<>();
                for (ItemsDocumentDTO itemsDocument3 : itemsDocuments) {
                    Site destination = new Site(jdbcSiteDAO.findSite(itemsDocument3.getDestinationName(), itemsDocument3.getShipmentAreaId()).get());
                    List<ItemDTO> itemDTOs = jdbcItemDAO.getAllItemsByItemsDocumentId(itemsDocument3.getId());
                    List<Item> items = new ArrayList<>();
                    for (ItemDTO itemDTO : itemDTOs) {
                        Item item = new Item(itemDTO);
                        items.add(item);
                    }
                    itemsDocuments2.add(new ItemsDocument(itemsDocument3, destination, items));
                }
                List<Integer> shipmentAreaIds = jdbcItemsDocumentDAO.getAllShipmentAreaIdsByTransportationId(transportationDTO.getId());
                transportations.add(new Transportation(transportationDTO, origin, itemsDocuments2, shipmentAreaIds));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transportations;
    }
}
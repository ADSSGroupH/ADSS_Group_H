package domain_layer.transportationDomain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import dal.transportation_dal.JdbcItemDAO;
import dal.transportation_dal.JdbcItemsDocumentDAO;
import dal.transportation_dal.JdbcTransportationDAO;
import dto_folder.ItemDTO;
import dto_folder.ItemsDocumentDTO;
import dto_folder.TransportationDTO;

public class TransportationRepository {
    private Map<Integer, Transportation> transportationMap;
    private JdbcTransportationDAO jdbcTransportationDAO;
    private JdbcItemsDocumentDAO jdbcItemsDocumentDAO;
    private JdbcItemDAO jdbcItemDAO;

    public TransportationRepository() {
        this.transportationMap = new HashMap<>();
        this.jdbcTransportationDAO = new JdbcTransportationDAO();
        this.jdbcItemsDocumentDAO = new JdbcItemsDocumentDAO();
        this.jdbcItemDAO = new JdbcItemDAO();
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
            ShipmentAreaRepository shipmentAreaRepository = new ShipmentAreaRepository();
            Site origin = shipmentAreaRepository.getSiteByName(transportationdto.get().getOriginName(), transportationdto.get().getOriginShipmentAreaId());
            List<ItemsDocumentDTO> itemsDocuments = jdbcItemsDocumentDAO.getAllTransportationItemsDocuments(id);
            List<ItemsDocument> itemsDocuments2 = new ArrayList<>();
            for (ItemsDocumentDTO itemsDocument3 : itemsDocuments) {
                Site destination = shipmentAreaRepository.getSiteByName(itemsDocument3.getDestinationName(), itemsDocument3.getShipmentAreaId());
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
            e.printStackTrace();
        return null;
    }
}

    public void removeTransportation(int id) {
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

}

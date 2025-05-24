package domain_layer.transportationDomain;

import java.util.HashMap;
import java.util.Map;

public class ShipmentAreaRepository {
    private Map<Integer, ShipmentArea> shipmentAreaMap;
    
    public ShipmentAreaRepository() {
        this.shipmentAreaMap = new HashMap<>();
    }
    public void addShipmentArea(ShipmentArea shipmentArea) {
        shipmentAreaMap.put(shipmentArea.getId(), shipmentArea);
    }
    public ShipmentArea getShipmentArea(int id) {
        return shipmentAreaMap.get(id);
    }
    public void removeShipmentArea(int id) {
        shipmentAreaMap.remove(id);
    }
    public boolean shipmentAreaExists(int id) {
        return shipmentAreaMap.containsKey(id);
    }
}

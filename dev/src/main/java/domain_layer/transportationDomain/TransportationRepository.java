package domain_layer.transportationDomain;

import java.util.HashMap;
import java.util.Map;

public class TransportationRepository {
    private Map<Integer, Transportation> transportationMap;


    public TransportationRepository() {
        this.transportationMap = new HashMap<>();
    }
    public void addTransportation(int id, Transportation transportation) {
        transportationMap.put(id, transportation);
    }
    public Transportation getTransportation(int id) {
        return transportationMap.get(id);
    }
    public void removeTransportation(int id) {
        transportationMap.remove(id);
    }
    public boolean transportationExists(int id) {
        return transportationMap.containsKey(id);
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

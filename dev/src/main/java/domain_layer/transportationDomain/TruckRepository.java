package domain_layer.transportationDomain;

import java.util.HashMap;
import java.util.Map;

public class TruckRepository {
     private Map<String, Truck> truckMap;

     public TruckRepository() {
         this.truckMap = new HashMap<>();
     }

    public void addTruck(String plateNumber, Truck truck) {
        truckMap.put(plateNumber, truck);
    }
    public Truck getTruck(String plateNumber) {
        return truckMap.get(plateNumber);
    }
    public void removeTruck(String plateNumber) {
        truckMap.remove(plateNumber);
    }
    public boolean truckExists(String plateNumber) {
        return truckMap.containsKey(plateNumber);
    }
    public String displayTrucks(){
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Truck> entry : truckMap.entrySet()) {
            Truck truck = entry.getValue();
            sb.append(truck.display()).append("\n");
        }
        return sb.toString();
    }
}

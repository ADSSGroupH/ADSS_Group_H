package domain_layer.transportationDomain;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import dal.transportation_dal.JdbcTruckDAO;
import dto_folder.TruckDTO;

public class TruckRepository {
     private Map<String, Truck> truckMap;
     private JdbcTruckDAO jdbcTruckDAO;
     

     public TruckRepository() {
         this.truckMap = new HashMap<>();
         this.jdbcTruckDAO = new JdbcTruckDAO();
     }

    public void addTruck(String plateNumber, Truck truck) {
        truckMap.put(plateNumber, truck);
        try {
            jdbcTruckDAO.save(truck.toDTO());
        } catch (Exception e) {
            e.printStackTrace();
    }
}

    public Truck getTruck(String plateNumber) {
        if (truckMap.containsKey(plateNumber)) {
            return truckMap.get(plateNumber);
        }
        try {
            Optional<TruckDTO> tempTruck = jdbcTruckDAO.findTruckById(plateNumber);
            if (!tempTruck.isPresent()) {
                return null;
            }
            TruckDTO truckDTO = tempTruck.get();
            Truck truck = new Truck(truckDTO);
            truckMap.put(plateNumber, truck);
            return truckMap.get(plateNumber);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public void removeTruck(String plateNumber) {
        if(truckMap.containsKey(plateNumber)) {
            truckMap.remove(plateNumber);
        }
        try {
            jdbcTruckDAO.delete(plateNumber);
        } catch (Exception e) {
            // do nothing
        }
        
    }
    public boolean truckExists(String plateNumber) {
        if (truckMap.containsKey(plateNumber)) {
            return true;
        }
        try {
            Optional<TruckDTO> tempTruck = jdbcTruckDAO.findTruckById(plateNumber);
            if (!tempTruck.isPresent()) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

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

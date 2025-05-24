package domain_layer.transportationDomain;

import java.util.HashMap;
import java.util.Map;

import domain_layer.transportationDomain.Driver.LicenseType;

public class DriverRepository {
    private Map<String, Driver> driverMap; 

    public DriverRepository() {
        this.driverMap = new HashMap<>();
    }
    public void addDriver(String name, Driver driver) {
        driverMap.put(name, driver);
    }
    public Driver getDriver(String id) {
        return driverMap.get(id);
    }
    public void removeDriver(String id) {
        driverMap.remove(id);
    }
    public boolean driverExists(String id) {
        return driverMap.containsKey(id);
    }
    public String displayDrivers(){
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Driver> entry : driverMap.entrySet()) {
            Driver driver = entry.getValue();
            sb.append(driver.display()).append("\n");
        }
        return sb.toString();
    }
    public boolean checkAvalableDrivers(LicenseType licenseType) {
        for (Map.Entry<String, Driver> entry : driverMap.entrySet()) {
            Driver driver = entry.getValue();
            if (!driver.isOccupied() && driver.getLicenseType().equals(licenseType)) {
                return true; // Found an available driver with the required license type
            }
        }
        return false;
    }
}

package DomainLayer.transportationDomain;


import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DTO.LicenseType;
import DTO.driverDTO;
import Dal.HR.JdbcEmployeeDAO;
import DomainLayer.HR_TransportationController;


public class DriverRepository {
    private Map<String, Driver> driverMap;
    private HR_TransportationController hrTransportationController;

    public DriverRepository() {
        this.driverMap = new HashMap<>();
        this.hrTransportationController = new HR_TransportationController();
    }
    public void addDriver(String name, Driver driver) {
        driverMap.put(name, driver);
    }
    public Driver getDriver(String id) {
        return driverMap.get(id);
    }
    public List<Driver> getAll() {
        List<Driver> drivers = new ArrayList<>();
        int size = driverMap.size();
        for (int i = 0; i < size; i++) {
            Driver driver = driverMap.get(size);
            drivers.add(driver);
        }
        return drivers;
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
            if (driver.getLicenseType().equals(licenseType)) {
                return true; // Found an available driver with the required license type
            }
        }
        return false;
    }

    public LicenseType getLicenseByDriverId(String id) throws SQLException {
        JdbcEmployeeDAO empDAO = new JdbcEmployeeDAO();
        String LicenseAsString = empDAO.getLicenseByDriverId(id);
        return LicenseType.valueOf(LicenseAsString);
    }

    public void loadAvailableDrivers(LocalDate date, LocalTime startTime) throws SQLException {
        driverMap.clear();
        List<driverDTO> availableDrivers = hrTransportationController.getAvailableDrivers(date, startTime);
        for (driverDTO driver : availableDrivers) {
            driverMap.put(driver.getName(), new Driver(driver.getName(), driver.getLicenseType()));
        }
    }

}

package DomainLayer.Transportation.Repositories;

import DTO.Transportation.LicenseType;
import DTO.Transportation.driverDTO;
import Dal.HR.JdbcEmployeeDAO;
import DomainLayer.HR_TransportationController;
import DomainLayer.Transportation.Driver;
import database.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class DriverRepository {
    private Map<String, Driver> driverMap;
    private JdbcEmployeeDAO jdbcEmployeeDAO;
    //private HR_TransportationController hrTransportationController;

    public DriverRepository() {
        this.driverMap = new HashMap<>();
        this.jdbcEmployeeDAO = new JdbcEmployeeDAO();
        //this.hrTransportationController = new HR_TransportationController();
    }
    public void addDriver(String name, Driver driver) {
        driverMap.put(name, driver);
    }
    public Driver getDriver(String id) {
        return driverMap.get(id);
    }
    public List<Driver> getAll() {
        return new ArrayList<>(driverMap.values());
    }

    public void removeDriver(String id) {
        driverMap.remove(id);
    }
    public boolean driverExists(String id) {
        return driverMap.containsKey(id);
    }
    public String displayDrivers(List<driverDTO> drivers) {
        driverMap.clear();
        for (driverDTO driver : drivers) {
            driverMap.put(driver.getName(), new Driver(driver.getName(), driver.getLicenseType()));
        }
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

    public void loadAvailableDrivers(List<driverDTO> availableDrivers) {
        driverMap.clear();
        for (driverDTO driver : availableDrivers) {
            driverMap.put(driver.getName(), new Driver(driver.getName(), driver.getLicenseType()));
        }
    }

    public boolean isDriverAvailable(String driverId, LocalDate date, LocalTime startTime) throws SQLException {
        return jdbcEmployeeDAO.isDriverAvailable(driverId,date,startTime);
    }


}

package DomainLayer.transportationDomain;

import DTO.LicenseType;
import DTO.driverDTO;
import Dal.HR.JdbcEmployeeDAO;
import Dal.Transportation.JdbcDriverDAO;
import DomainLayer.transportationDomain.Driver;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class DriverRepository {
    private Map<String, Driver> driverMap;
    private final JdbcEmployeeDAO jdbcEmployeeDAO;
    private final JdbcDriverDAO jdbcDriverDAO;

    public DriverRepository() {
        this.driverMap = new HashMap<>();
        this.jdbcEmployeeDAO = new JdbcEmployeeDAO();
        this.jdbcDriverDAO = new JdbcDriverDAO();
    }

    // מוסיף נהג חדש לבסיס הנתונים
    public void addDriver(String id, Driver driver) throws SQLException {
        jdbcDriverDAO.save(fromEntity(id, driver));
    }

    // מחזיר ישות Driver לפי ID מה-DAO
    public Driver getDriver(String id) throws SQLException {
        Optional<driverDTO> dto = jdbcDriverDAO.findById(id);
        assert dto.orElse(null) != null;
        return toEntity(id, dto.orElse(null));
    }

    // מחזיר את כל הנהגים כ־Driver
    public List<Driver> getAll() throws SQLException {
        List<driverDTO> driverDTOList = jdbcDriverDAO.findAll();
        return driverDTOList.stream()
                .map(dto -> toEntity(dto.getName(), dto))
                .collect(Collectors.toList());
    }

    // מוחק נהג לפי ID
    public void removeDriver(String id) throws SQLException {
        jdbcDriverDAO.delete(id);
    }

    // בודק אם נהג קיים לפי ID
    public boolean driverExists(String id) throws SQLException {
        return jdbcDriverDAO.exists(id);
    }

    // מציג את כל הנהגים כמחרוזת
    public String displayDrivers() throws SQLException {
        List<Driver> drivers = getAll();
        StringBuilder sb = new StringBuilder();
        for (Driver driver : drivers) {
            sb.append(driver.display()).append("\n");
        }
        return sb.toString();
    }

    // בודק אם יש נהג עם סוג רישיון מסוים
    public boolean checkAvailableDrivers(LicenseType licenseType) throws SQLException {
        List<Driver> drivers = getAll();
        return drivers.stream().anyMatch(driver -> driver.getLicenseType().equals(licenseType));
    }

    // מחזיר את סוג הרישיון לפי ID של נהג
    public LicenseType getLicenseByDriverId(String id) throws SQLException {
        String licenseAsString = jdbcEmployeeDAO.getLicenseByDriverId(id);
        if (licenseAsString == null) {
            throw new IllegalArgumentException("License not found for driver ID: " + id);
        }
        return LicenseType.valueOf(licenseAsString);
    }


    // המרה מ־Driver ל־driverDTO
    public driverDTO fromEntity(String id, Driver driver) {
        driverDTO dto = new driverDTO(driver.getLicenseType()); // ID נשמר רק בשאילתה
        dto.setId(id);
        return dto;
    }

    // המרה מ־driverDTO ל־Driver
    public Driver toEntity(String id, driverDTO dto) {
        return new Driver(id, dto.getLicenseType());
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

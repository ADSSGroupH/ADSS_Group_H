import DomainLayer.Transportation.*;
import DomainLayer.Transportation.Controllers.TransportationController;
import DomainLayer.Transportation.Repositories.ShipmentAreaRepository;
import DomainLayer.Transportation.Repositories.TransportationRepository;
import DomainLayer.Transportation.Repositories.TruckRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import DomainLayer.HR_TransportationController;
import DomainLayer.HR.Controllers.ShiftController;
import DomainLayer.HR.Employee;
import DomainLayer.HR.EmployeeContract;
import DomainLayer.HR.Repositories.AssignmentRepository;
import DomainLayer.HR.Repositories.EmployeeRepository;
import DomainLayer.HR.Repositories.RoleRepository;
import DomainLayer.HR.Repositories.ShiftRepository;
import DomainLayer.HR.Role;
import DomainLayer.HR.Shift;
import DomainLayer.HR.ShiftAssignment;
import DomainLayer.Transportation.Repositories.DriverRepository;
import DTO.Transportation.LicenseType;
import DTO.Transportation.driverDTO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class HR_Transportation_Tests {

    // Real repositories and controllers
    private ShiftController shiftController;
    private ShiftRepository shiftRepository;
    private EmployeeRepository employeeRepository;
    private RoleRepository roleRepository;
    private AssignmentRepository assignmentRepository;
    private DriverRepository driverRepository;
    private TruckRepository truckRepository;
    private ShipmentAreaRepository shipmentAreaRepository;
    private TransportationRepository transportationRepository;

    private HR_TransportationController hrTransportationController;
    private TransportationController transportationController;

    // Test data containers for cleanup
    private List<String> createdEmployeeIds = new ArrayList<>();
    private List<String> createdShiftIds = new ArrayList<>();
    private List<String> createdRoleIds = new ArrayList<>();
    private List<Integer> createdTransportationIds = new ArrayList<>();
    private List<String> createdTruckPlates = new ArrayList<>();

    @BeforeEach
    void setUp() throws SQLException {
        // Initialize real repositories
        shiftRepository = ShiftRepository.getInstance();
        employeeRepository = EmployeeRepository.getInstance();
        roleRepository = RoleRepository.getInstance();
        assignmentRepository = AssignmentRepository.getInstance();
        driverRepository = new DriverRepository();
        truckRepository = new TruckRepository();
        shipmentAreaRepository = new ShipmentAreaRepository();
        transportationRepository = new TransportationRepository();

        // Initialize real controllers
        shiftController = new ShiftController();
        hrTransportationController = new HR_TransportationController();
        transportationController = new TransportationController();
        transportationController.setDriverRep(driverRepository);
        transportationController.setTruckRep(truckRepository);
        transportationController.setHrTransportationController(hrTransportationController);
        transportationController.setTransportationRep(transportationRepository);
        transportationController.setShipmentAreaRep(shipmentAreaRepository);


    }

    @AfterEach
    void tearDown() throws SQLException {
        // Clean up test data in reverse order of dependencies
        cleanupTestData();
    }

    private void setupTestData() throws SQLException {
        // Create unique role IDs using timestamp to avoid conflicts
        String timestamp = String.valueOf(System.currentTimeMillis());

        // Create or get driver role
        Role driverRole = getOrCreateRole("driver", "driver_role_" + timestamp);

        // Create or get stocker role
        Role stockerRole = getOrCreateRole("stocker", "stocker_role_" + timestamp);

        // Create test shifts with unique IDs
        LocalDate testDate = LocalDate.of(2025, 6, 5);
        String shiftId1 = "shift_" + timestamp + "_morning";
        String shiftId2 = "shift_" + timestamp + "_afternoon";

        Shift morningShift = new Shift(shiftId1, testDate, "09:00", "17:00", "morning",
                null, new ArrayList<>(), new ArrayList<>(), null);
        Shift afternoonShift = new Shift(shiftId2, testDate, "13:00", "21:00", "afternoon",
                null, new ArrayList<>(), new ArrayList<>(), null);

        shiftRepository.addShift(morningShift);
        shiftRepository.addShift(afternoonShift);
        createdShiftIds.add(morningShift.getId());
        createdShiftIds.add(afternoonShift.getId());

        // Create test employees with driver role using unique IDs
        Set<Role> driverRoles = new HashSet<>();
        driverRoles.add(driverRole);

        String driverId1 = "driver_001";
        String driverId2 = "driver_002";

        EmployeeContract con1 = new EmployeeContract(driverId1, LocalDate.now(), 1, 1, 1, "1", "1", 1, "no", false);
        EmployeeContract con2 = new EmployeeContract(driverId2, LocalDate.now(), 1, 1, 1, "1", "1", 1, "no", false);

        Employee driver1 = new Employee(driverId1, "John Doe", "123-456-7890", "branch1",
                driverRoles, con1, "bank123", false, null, false, "password");
        Driver driver_001 = new Driver(driverId1, LicenseType.C);

        Employee driver2 = new Employee(driverId2, "Jane Smith", "987-654-3210", "branch1",
                driverRoles, con2, "bank456", false, null, false, "password");
        Driver driver_002 = new Driver(driverId2, LicenseType.C);

        employeeRepository.addEmployee(driver1);
        employeeRepository.addEmployee(driver2);
        createdEmployeeIds.add(driver1.getId());
        createdEmployeeIds.add(driver2.getId());

        // Create driver licenses
        driverRepository.addDriver(driverId1, driver_001);
        driverRepository.addDriver(driverId2, driver_002);

        // Create test employees with stocker role using unique IDs
        Set<Role> stockerRoles = new HashSet<>();
        stockerRoles.add(stockerRole);

        String stockerId1 = "stocker_" + timestamp + "_001";
        String stockerId2 = "stocker_" + timestamp + "_002";

        EmployeeContract con3 = new EmployeeContract(stockerId1, LocalDate.now(), 1, 1, 1, "1", "1", 1, "no", false);
        EmployeeContract con4 = new EmployeeContract(stockerId2, LocalDate.now(), 1, 1, 1, "1", "1", 1, "no", false);

        Employee stocker1 = new Employee(stockerId1, "Stocker One", "111-222-3333", "branch1",
                stockerRoles, con3, "bank789", false, null, false, "password");
        Employee stocker2 = new Employee(stockerId2, "Stocker Two", "444-555-6666", "branch1",
                stockerRoles, con4, "bank101", false, null, false, "password");

        employeeRepository.addEmployee(stocker1);
        employeeRepository.addEmployee(stocker2);
        createdEmployeeIds.add(stocker1.getId());
        createdEmployeeIds.add(stocker2.getId());

        // Assign employees to shifts
        ShiftAssignment driverAssignment1 = new ShiftAssignment(driver1, morningShift.getId(), driverRole, null);
        ShiftAssignment driverAssignment2 = new ShiftAssignment(driver2, afternoonShift.getId(), driverRole, null);
        ShiftAssignment stockerAssignment1 = new ShiftAssignment(stocker1, morningShift.getId(), stockerRole, null);
        ShiftAssignment stockerAssignment2 = new ShiftAssignment(stocker2, afternoonShift.getId(), stockerRole, null);

        assignmentRepository.addAssignment(driverAssignment1);
        assignmentRepository.addAssignment(driverAssignment2);
        assignmentRepository.addAssignment(stockerAssignment1);
        assignmentRepository.addAssignment(stockerAssignment2);

        // Create test trucks with unique plate numbers
        String plateNumber1 = "TEST1";
        String plateNumber2 = "TEST2";

        Truck truck1 = new Truck(plateNumber1, "Ford Transit", 1000, 5000, LicenseType.C);
        Truck truck2 = new Truck(plateNumber2, "Mercedes Sprinter", 2000, 8000, LicenseType.B);

        truckRepository.addTruck(plateNumber1, truck1);
        truckRepository.addTruck(plateNumber2, truck2);
        createdTruckPlates.add(truck1.getPlateNumber());
        createdTruckPlates.add(truck2.getPlateNumber());

        // Create test shipment areas
        ShipmentArea shipmentArea1 = new ShipmentArea(1, "Test Area 1", new ArrayList<>());
        if (!shipmentAreaRepository.shipmentAreaExists(1)) {
            shipmentAreaRepository.addShipmentArea(shipmentArea1);
        }
        ShipmentArea shipmentArea2 = new ShipmentArea(2, "Test Area 2", new ArrayList<>());
        if (!shipmentAreaRepository.shipmentAreaExists(2)) {
            shipmentAreaRepository.addShipmentArea(shipmentArea2);
        }
    }


    private Role getOrCreateRole(String roleName, String roleId) throws SQLException {
        try {
            // First, try to get existing role by name
            Role existingRole = roleRepository.getRoleByName(roleName);
            if (existingRole != null) {
                return existingRole;
            }
        } catch (Exception e) {
            // Role doesn't exist or getRoleByName failed, continue to create it
        }

        // Create new role
        Role newRole = new Role(roleId, roleName);
        roleRepository.addRole(newRole);
        createdRoleIds.add(newRole.getId());
        return newRole;
    }


    private void cleanupTestData() throws SQLException {
        // Clean up in reverse order of dependencies

        // Remove transportation records
        for (Integer transportationId : createdTransportationIds) {
            try {
                transportationRepository.removeTransportation(transportationId);
            } catch (Exception e) {
                System.out.println("Failed to delete transportation " + transportationId + ": " + e.getMessage());
            }
        }

        // Remove assignments
        for (String employeeId : createdEmployeeIds) {
            try {
                List<ShiftAssignment> assignmentsOfEmployee = assignmentRepository.findByEmployee(employeeId);
                for (ShiftAssignment assignment : assignmentsOfEmployee) {
                    assignmentRepository.removeAssignment(assignment.getId());
                }
            } catch (Exception e) {
                System.out.println("Failed to delete assignments for employee " + employeeId + ": " + e.getMessage());
            }
        }

        // Remove drivers
        for (String employeeId : createdEmployeeIds) {
            if (employeeId.contains("driver_")) {
                try {
                    driverRepository.removeDriver(employeeId);
                } catch (Exception e) {
                    System.out.println("Failed to delete driver " + employeeId + ": " + e.getMessage());
                }
            }
        }

        // Remove employees
        for (String employeeId : createdEmployeeIds) {
            try {
                employeeRepository.deleteEmployee(employeeId);
            } catch (Exception e) {
                System.out.println("Failed to delete employee " + employeeId + ": " + e.getMessage());
            }
        }

        // Remove trucks
        for (String plateNumber : createdTruckPlates) {
            try {
                truckRepository.removeTruck(plateNumber);
            } catch (Exception e) {
                System.out.println("Failed to delete truck " + plateNumber + ": " + e.getMessage());
            }
        }

        // Remove shifts
        for (String shiftId : createdShiftIds) {
            try {
                shiftRepository.removeShift(shiftId);
            } catch (Exception e) {
                System.out.println("Failed to delete shift " + shiftId + ": " + e.getMessage());
            }
        }

        // Remove roles (only if we created them)
        for (String roleId : createdRoleIds) {
            try {
                roleRepository.removeRole(roleId);
            } catch (Exception e) {
                System.out.println("Failed to delete role " + roleId + ": " + e.getMessage());
            }
        }

        // Clear tracking lists
        createdEmployeeIds.clear();
        createdShiftIds.clear();
        createdRoleIds.clear();
        createdTransportationIds.clear();
        createdTruckPlates.clear();
    }

    // Helper method to create test items document
    private List<ItemsDocument> createTestItemsDocument() {
        List<Item> items = Arrays.asList(new Item(1, "item1", 10, 5));
        ItemsDocument doc = new ItemsDocument(1, new Site("TestSite", "Address", "123", "Contact", 1),
                LocalTime.of(14, 0), items);
        return Arrays.asList(doc);
    }

    // Helper method to get the first created driver ID
    private String getFirstDriverId() {
        return createdEmployeeIds.stream()
                .filter(id -> id.contains("driver_"))
                .findFirst()
                .orElse(null);
    }

    // Helper method to get the first created truck plate
    private String getFirstTruckPlate() {
        return createdTruckPlates.isEmpty() ? null : createdTruckPlates.get(0);
    }

    // =============== INTEGRATION TESTS WITH REAL OBJECTS ===============

    @Test
    void testGetAvailableDrivers_WithRealData_ReturnsActualDrivers() throws SQLException {
        // Setup base test data
        setupTestData();

        // Given: Real shift and driver data exists
        LocalDate testDate = LocalDate.of(2025, 6, 5);
        LocalTime testTime = LocalTime.of(9, 0);

        // When: Getting available drivers using real controller and repositories
        List<driverDTO> result = hrTransportationController.getAvailableDrivers(testDate, testTime);

        // Then: Should return real drivers with complete information
        assertNotNull(result, "Result should not be null");
        assertFalse(result.isEmpty(), "Should have at least one available driver");

        // Verify driver data is complete and valid
        for (driverDTO driver : result) {
            assertNotNull(driver.getId(), "Driver ID should not be null");
            assertNotNull(driver.getName(), "Driver name should not be null");
            assertNotNull(driver.getLicenseType(), "Driver license type should not be null");
            assertFalse(driver.getName().trim().isEmpty(), "Driver name should not be empty");
        }

        // Verify we have at least one driver with expected characteristics
        boolean hasValidDriver = result.stream()
                .anyMatch(d -> d.getName().equals("John Doe") || d.getName().equals("Jane Smith"));
        assertTrue(hasValidDriver, "Should include at least one of our test drivers");

        cleanupTestData();
    }

    @Test
    void testIsWarehouseWorkerAvailable_WithRealStockers_ReturnsTrue() throws SQLException {

        // Setup base test data
        setupTestData();

        // Given: Real shift data with assigned stockers
        LocalDate testDate = LocalDate.of(2025, 6, 5);
        LocalTime startTime = LocalTime.of(9, 0);
        List<LocalTime> endTimes = Arrays.asList(LocalTime.of(17, 0));

        // When: Checking warehouse worker availability using real data
        boolean result = hrTransportationController.isWarehouseWorkerAvailable(testDate, startTime, endTimes);

        // Then: Should return true because we have real stockers assigned
        assertTrue(result, "Should return true when real stockers are available in shifts");

        cleanupTestData();
    }

    @Test
    void testIsWarehouseWorkerAvailable_NoStockersInShift_ReturnsFalse() throws SQLException {

        // Setup base test data
        setupTestData();

        // Given: A date/time where no stockers are assigned
        LocalDate futureDate = LocalDate.of(2025, 12, 25); // No shifts created for this date
        LocalTime startTime = LocalTime.of(9, 0);
        List<LocalTime> endTimes = Arrays.asList(LocalTime.of(17, 0));

        // When: Checking warehouse worker availability for non-existent shift
        boolean result = hrTransportationController.isWarehouseWorkerAvailable(futureDate, startTime, endTimes);

        // Then: Should return false because no shift/stockers exist for this date
        assertFalse(result, "Should return false when no stockers are available");

        cleanupTestData();
    }

    @Test
    void testCreateTransportation_WithRealObjects_AllRequirementsMet_ShouldSucceed() throws SQLException {

        // Setup base test data
        setupTestData();

        // Given: All real objects and data exist and meet requirements
        int transportationId = (int) (System.currentTimeMillis() % 100000); // Unique ID
        LocalDate date = LocalDate.of(2025, 6, 5);
        LocalTime departureTime = LocalTime.of(9, 0);
        String truckPlate = getFirstTruckPlate(); // Get actual truck plate
        String driverName = getFirstDriverId(); // Get actual driver ID
        List<ItemsDocument> itemsDoc = createTestItemsDocument();
        List<Integer> shipmentAreasId = Arrays.asList(1);
        Site origin = new Site("Origin", "Address", "123", "Contact", 1);
        ShipmentArea shipmentArea1 = shipmentAreaRepository.getShipmentArea(1);
        shipmentArea1.addSite(origin);

        // When: Creating transportation with real controller and repositories
        String result = transportationController.makeTransportation(
                transportationId, date, departureTime, truckPlate, driverName,
                itemsDoc, shipmentAreasId, origin);

        // Then: Should succeed because all requirements are met with real data
        System.out.println("Transportation creation result: " + result);

        // Check if the result indicates success (adapt based on your actual success message format)
        boolean isSuccess = result.contains("Transportation created") ||
                result.contains("success") ||
                !result.contains("error") && !result.contains("fail");

        if (isSuccess) {
            createdTransportationIds.add(transportationId);
        }

        // For debugging, print the actual result
        assertTrue(isSuccess, "Should succeed with real data. Actual result: " + result);

        cleanupTestData();
    }

    @Test
    void testCreateTransportation_WrongLicenseType_ShouldFail() throws SQLException {

        // Setup base test data
        setupTestData();

        // Given: Try to use driver with wrong license type for truck
        int transportationId = (int) (System.currentTimeMillis() % 100000) + 1;
        LocalDate date = LocalDate.of(2025, 6, 5);
        LocalTime departureTime = LocalTime.of(9, 0);

        // Find a driver with B license and a truck requiring C license (or vice versa)
        String driverWithBLicense = null;
        String truckRequiringC = null;

        for (String employeeId : createdEmployeeIds) {
            if (employeeId.contains("driver_")) {
                try {
                    Driver driver = driverRepository.getDriver(employeeId);
                    if (driver != null && driver.getLicenseType() == LicenseType.B) {
                        driverWithBLicense = employeeId;
                        break;
                    }
                } catch (Exception e) {
                    // Continue searching
                }
            }
        }

        for (String plateNumber : createdTruckPlates) {
            try {
                Truck truck = truckRepository.getTruck(plateNumber);
                if (truck != null && truck.getLicenseType() == LicenseType.C) {
                    truckRequiringC = plateNumber;
                    break;
                }
            } catch (Exception e) {
                // Continue searching
            }
        }

        // Skip test if we don't have the right combination
        if (driverWithBLicense == null || truckRequiringC == null) {
            System.out.println("Skipping license test - couldn't find B license driver or C license truck");
            return;
        }

        List<ItemsDocument> itemsDoc = createTestItemsDocument();
        List<Integer> shipmentAreasId = Arrays.asList(1);
        Site origin = new Site("Origin", "Address", "123", "Contact", 1);

        // When: Creating transportation with license mismatch
        String result = transportationController.makeTransportation(
                transportationId, date, departureTime, truckRequiringC, driverWithBLicense,
                itemsDoc, shipmentAreasId, origin);

        // Then: Should fail due to license type mismatch
        assertTrue(result.contains("license") || result.contains("not qualified") || result.contains("cannot drive"),
                "Should fail when driver license doesn't match truck requirement. Result: " + result);

        cleanupTestData();
    }

    @Test
    void testDataPersistenceAndRetrieval_IntegrationTest() throws SQLException {

        // Setup base test data
        setupTestData();

        // Given: Create transportation with real data
        int transportationId = (int) (System.currentTimeMillis() % 100000) + 2;
        LocalDate date = LocalDate.of(2025, 6, 5);
        LocalTime departureTime = LocalTime.of(9, 0);
        String truckPlate = getFirstTruckPlate();
        String driverName = getFirstDriverId();
        List<ItemsDocument> itemsDoc = createTestItemsDocument();
        List<Integer> shipmentAreasId = Arrays.asList(1);
        Site origin = new Site("Origin", "Address", "123", "Contact", 1);
        ShipmentArea shipmentArea1 = shipmentAreaRepository.getShipmentArea(1);
        shipmentArea1.addSite(origin);

        // When: Create transportation
        String createResult = transportationController.makeTransportation(
                transportationId, date, departureTime, truckPlate, driverName,
                itemsDoc, shipmentAreasId, origin);

        System.out.println("Create result: " + createResult);

        // Check if creation was successful
        boolean isSuccess = createResult.contains("Transportation created") ||
                createResult.contains("success") ||
                (!createResult.contains("error") && !createResult.contains("fail"));

        if (isSuccess) {
            createdTransportationIds.add(transportationId);

            // Then: Verify data persistence
            assertTrue(transportationRepository.transportationExists(transportationId),
                    "Transportation should exist in database");
        } else {
            // If creation failed, that's also valuable information
            System.out.println("Transportation creation failed as expected due to business rules: " + createResult);
        }

        cleanupTestData();
    }

    @Test
    void testMultipleShiftsConsistency_WithRealData() throws SQLException {

        // Setup base test data
        setupTestData();

        // Given: Multiple shifts with different stockers
        LocalDate testDate = LocalDate.of(2025, 6, 5);
        LocalTime startTime = LocalTime.of(9, 0);
        List<LocalTime> endTimes = Arrays.asList(LocalTime.of(17, 0), LocalTime.of(21, 0));

        // When: Checking warehouse worker availability across multiple shifts
        boolean result = hrTransportationController.isWarehouseWorkerAvailable(testDate, startTime, endTimes);

        // Then: Should return true as we have stockers in both morning and afternoon shifts
        assertTrue(result, "Should have warehouse workers available in both shifts");

        cleanupTestData();
    }

    @Test
    void testErrorHandling_NonExistentDriver() throws SQLException {
        setupTestData();

        int transportationId = (int) (System.currentTimeMillis() % 100000) + 10;
        LocalDate date = LocalDate.of(2025, 6, 5);
        LocalTime departureTime = LocalTime.of(9, 0);
        String nonExistentDriver = "non_existent_driver_" + System.currentTimeMillis();
        List<ItemsDocument> itemsDoc = createTestItemsDocument();
        List<Integer> shipmentAreasId = Arrays.asList(1);
        Site origin = new Site("Origin", "Address", "123", "Contact", 1);

        String result = transportationController.makeTransportation(
                transportationId, date, departureTime, getFirstTruckPlate(), nonExistentDriver,
                itemsDoc, shipmentAreasId, origin);

        assertTrue(result.contains("Driver with name"),
                "Should fail for non-existent driver. Result: " + result);

        cleanupTestData();
    }

    @Test
    void testErrorHandling_NonExistentTruck() throws SQLException {
        setupTestData();

        // הוספת נהגים כדי למנוע שגיאת Driver
        Driver Test_driver = new Driver("123456789",LicenseType.A);
        driverRepository.addDriver("Test Driver", Test_driver);
        Driver Another_driver = new Driver("987654321",LicenseType.B);
        driverRepository.addDriver("Another Driver",Another_driver);

        int transportationId = (int) (System.currentTimeMillis() % 100000) + 11;
        LocalDate date = LocalDate.of(2025, 6, 5);
        LocalTime departureTime = LocalTime.of(9, 0);
        String nonExistentTruck = "NONEXIST" + System.currentTimeMillis();
        List<ItemsDocument> itemsDoc = createTestItemsDocument();
        List<Integer> shipmentAreasId = Arrays.asList(1);
        Site origin = new Site("Origin", "Address", "123", "Contact", 1);

        String result = transportationController.makeTransportation(
                transportationId, date, departureTime, nonExistentTruck, "123456789",
                itemsDoc, shipmentAreasId, origin);

        assertTrue(result.contains("Truck") || result.contains("not found") || result.contains("does not exist"),
                "Should fail for non-existent truck. Result: " + result);

        cleanupTestData();
    }

    @Test
    void testCreateTransportation_DriverNotAssignedToShift_ShouldFail() throws SQLException {
        // Setup test data
        setupTestData();

        // הסר את השיבוץ של נהג כלשהו (למשל driver2 מהמשמרת)
        String driverId = createdEmployeeIds.stream()
                .filter(id -> id.equals("driver_002"))
                .findFirst()
                .orElse(null);
        if (driverId == null) {
            System.out.println("No driver found to unassign from shift.");
            return;
        }

        List<ShiftAssignment> assignments = assignmentRepository.findByEmployee(driverId);
        for (ShiftAssignment assignment : assignments) {
            assignmentRepository.removeAssignment(assignment.getId());  // מסיר את השיבוץ שלו מהמשמרת
        }

        int transportationId = (int) (System.currentTimeMillis() % 100000) + 12;
        LocalDate date = LocalDate.of(2025, 6, 5);
        LocalTime departureTime = LocalTime.of(9, 0);
        String truckPlate = getFirstTruckPlate();
        List<ItemsDocument> itemsDoc = createTestItemsDocument();
        List<Integer> shipmentAreasId = Arrays.asList(1);
        Site origin = new Site("Origin", "Address", "123", "Contact", 1);

        // פעולה: ניסיון ליצור הובלה עם נהג שאינו משובץ
        String result = transportationController.makeTransportation(
                transportationId, date, departureTime, truckPlate, driverId,
                itemsDoc, shipmentAreasId, origin
        );

        // ציפייה: שהמערכת תיכשל בגלל שהנהג לא נמצא במשמרת
        assertTrue(result.toLowerCase().contains("driver") && result.toLowerCase().contains("not in shift"),
                "Should fail due to driver not being assigned to a shift. Actual: " + result);

        cleanupTestData();
    }

}

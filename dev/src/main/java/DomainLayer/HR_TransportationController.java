package DomainLayer;

import DTO.Transportation.LicenseType;
import DTO.Transportation.driverDTO;
import DomainLayer.HR.Controllers.ShiftController;
import DomainLayer.HR.Employee;
import DomainLayer.HR.Repositories.AssignmentRepository;
import DomainLayer.HR.Repositories.EmployeeRepository;
import DomainLayer.HR.Repositories.RoleRepository;
import DomainLayer.HR.Repositories.ShiftRepository;
import DomainLayer.HR.Role;
import DomainLayer.HR.Shift;
import DomainLayer.HR.ShiftAssignment;
import DomainLayer.Transportation.Repositories.DriverRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class HR_TransportationController {

    private final ShiftController shiftController;
    private final ShiftRepository shiftRepository;
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final AssignmentRepository assignmentRepository;
    private final DriverRepository driverRepository;

    public HR_TransportationController() {
        this.driverRepository = new DriverRepository();
        this.shiftRepository = ShiftRepository.getInstance();
        this.roleRepository = RoleRepository.getInstance();
        this.assignmentRepository = AssignmentRepository.getInstance();
        this.shiftController = new ShiftController();
        this.employeeRepository = EmployeeRepository.getInstance();
    }

    /**
     * מחזירה רשימת נהגים זמינים למשמרת בתאריך ובשעה הנתונה
     */
    public List<driverDTO> getAvailableDrivers(LocalDate date, LocalTime startTime) throws SQLException {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        if (startTime == null) {
            throw new IllegalArgumentException("Start time cannot be null");
        }
        List<driverDTO> driverDTOList = new ArrayList<>();
        List<driverDTO> allDrivers = getAllDrivers();

        for (driverDTO driver : allDrivers) {
            if (driver == null) continue;

            // בדיקה אם הנהג פנוי בתאריך ובשעה
            if (!driverRepository.isDriverAvailable(driver.getId(), date, startTime)) continue;

            LicenseType licenseType = driverRepository.getLicenseByDriverId(driver.getId());
            if (licenseType == null) continue;

            driverDTO dto = new driverDTO(licenseType);
            dto.setId(driver.getId());
            dto.setName(driver.getName());

            driverDTOList.add(dto);
        }

        return driverDTOList;
    }





    /**
     * מחזירה true אם קיים מחסנאי בכל אחת מהמשמרות שמתאימות לתאריך, שעת התחלה ורשימת שעות סיום
     */
    public boolean isWarehouseWorkerAvailable(LocalDate date, LocalTime startTime, List<LocalTime> endTimes) throws SQLException {
        Role stockerRole = roleRepository.getRoleByName("stocker");

        for (LocalTime endTime : endTimes) {
            Shift targetShift = shiftRepository.findByDateAndTime(date.toString(), endTime.toString());
            if (targetShift == null){ //no shift found!
                return false;
            }
            List<ShiftAssignment> hasStocker = assignmentRepository.findByShiftAndRole(targetShift.getId(), stockerRole.getId());
            if (hasStocker.isEmpty()) { //no stocker assigned in this shift
                return false;
            }
        }
        Shift targetShift_originBranch = shiftRepository.findByDateAndTime(date.toString(), startTime.toString());
        List<ShiftAssignment> hasStocker_originBranch = assignmentRepository.findByShiftAndRole(targetShift_originBranch.getId(), stockerRole.getId());
        if (hasStocker_originBranch.isEmpty()) {
            return false;
        }
        return true;
    }

    public List<driverDTO> getAllDrivers() throws SQLException {
        Role driverRole = roleRepository.getRoleByName("driver");
        List<Employee> allEmployees = employeeRepository.getAllEmployees();
        List<driverDTO> driverList = new ArrayList<>();

        for (Employee employee : allEmployees) {
            if (employee.getRoles().contains(driverRole)) {
                LicenseType licenseType = driverRepository.getLicenseByDriverId(employee.getId());
                if (licenseType != null) {
                    driverDTO dto = new driverDTO(licenseType);
                    dto.setId(employee.getId());
                    dto.setName(employee.getName());
                    dto.setLicenseType(licenseType);
                    driverList.add(dto);
                }
            }
        }
        return driverList;
    }
}
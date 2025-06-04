package DomainLayer;

import DTO.driverDTO;
import DomainLayer.HR.Controllers.ShiftController;
import DomainLayer.HR.Employee;
import DomainLayer.HR.Repositories.AssignmentRepository;
import DomainLayer.HR.Repositories.EmployeeRepository;
import DomainLayer.HR.Repositories.RoleRepository;
import DomainLayer.HR.Repositories.ShiftRepository;
import DomainLayer.HR.Role;
import DomainLayer.HR.Shift;
import DomainLayer.HR.ShiftAssignment;

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

    public HR_TransportationController() {
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
        //first get the driver role from the list of roles:
        Role driverRole = roleRepository.getRoleByName("driver");
        Shift targetShift = shiftRepository.findByDateAndTime(date.toString(), startTime.toString());
        List<Employee> availableDrivers = shiftController.AvailableAndUnavailableEmpForRoleInShift(targetShift, driverRole).get(0);
        List<driverDTO> driverDTOList = new ArrayList<>();

        for (Employee driver : availableDrivers) {
            driverDTOList.add(new driverDTO());
        }

        return driverDTOList;
    }

    /**
     * מחזירה true אם קיים לפחות מחסנאי אחד באחת המשמרות שמתאימות לתאריך, שעת התחלה ורשימת שעות סיום
     */
    public boolean isWarehouseWorkerAvailable(LocalDate date, LocalTime startTime, List<LocalTime> endTimes) throws SQLException {
        Role stockerRole = roleRepository.getRoleByName("stocker");

        for (LocalTime endTime : endTimes) {
            Shift targetShift = shiftRepository.findByDateAndTime(date.toString(), endTime.toString());
            List<ShiftAssignment> hasStocker = assignmentRepository.findByShiftAndRole(targetShift.getId(), stockerRole.getId());
            if (hasStocker == null) {
                return false;
            }
        }
        Shift targetShift_originBranch = shiftRepository.findByDateAndTime(date.toString(), startTime.toString());
        List<ShiftAssignment> hasStocker_originBranch = assignmentRepository.findByShiftAndRole(targetShift_originBranch.getId(), stockerRole.getId());
        if (hasStocker_originBranch == null) {
            return false;
        }
        return true;
    }
}

package DomainLayer.HR.Controllers;

import DomainLayer.HR.*;
import DomainLayer.HR.Repositories.AssignmentRepository;
import DomainLayer.HR.Repositories.EmployeeRepository;
import DomainLayer.HR.Repositories.SwapRequestRepository;
import DomainLayer.HR.Repositories.WeeklyPreferencesRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

public class EmployeeController {

    private final SwapRequestRepository swapRequestRepository;
    private final WeeklyPreferencesRepository availabilityRepository;
    private final EmployeeRepository employeeRepository;

    public EmployeeController() {
        this.swapRequestRepository = SwapRequestRepository.getInstance();
        this.availabilityRepository = WeeklyPreferencesRepository.getInstance();
        this.employeeRepository = EmployeeRepository.getInstance();
    }

    // שולף את בקשות ההחלפה של העובד
    public List<ShiftSwapRequest> getEmployeeSwapRequests(String employeeId) {
        List<ShiftSwapRequest> requests = new ArrayList<>();
        for (ShiftSwapRequest request : swapRequestRepository.getAllSwapRequests()) {
            if (request.getRequester().getId().equals(employeeId)) {
                requests.add(request);
            }
        }
        return requests;
    }

    // מגיש בקשת החלפת משמרת
    public ShiftSwapRequest submitSwapRequest(Employee requester, Shift fromShift, Shift toShift) {
        String requestId = String.valueOf(swapRequestRepository.getAllSwapRequests().size() + 1);
        LocalDate date = LocalDate.now(); // the date of the creation of the swap request.
        ShiftSwapRequestController service = new ShiftSwapRequestController();
        return service.createRequest(requestId, requester, fromShift, toShift, date);
    }

    public List<Shift> getEligibleShiftsForNextWeek(String employeeId) throws Exception {
        ShiftController shiftService = new ShiftController();
        Employee employee = employeeRepository.getEmployeeById(employeeId);

        if (employee == null)
            throw new IllegalArgumentException("Employee not found.");

        LocalDate today = LocalDate.now();
        DayOfWeek todayDay = today.getDayOfWeek();
        int weeksToAdd = (todayDay.getValue() > DayOfWeek.THURSDAY.getValue() ||
                (todayDay == DayOfWeek.THURSDAY && LocalTime.now().getHour() >= 16)) ? 2 : 1;

        LocalDate targetWeekStart = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).plusWeeks(weeksToAdd - 1);
        LocalDate targetWeekEnd = targetWeekStart.plusDays(6);

        List<Shift> allShifts = shiftService.getShiftsBetween(targetWeekStart, targetWeekEnd);

        List<Shift> eligibleShifts = new ArrayList<>();
        for (Shift shift : allShifts) {
            for (Role role : shift.getRequiredRoles()) {
                if (employee.getRoles().stream().anyMatch(r -> r.getId().equals(role.getId()))) {
                    eligibleShifts.add(shift);
                    break;
                }
            }
        }

        return eligibleShifts;
    }
    //helper function
    public static LocalDate getStartOfWeekSunday(LocalDate date) {
        return date.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
    }
    public void submitSelectedShifts(String employeeId, List<Shift> selectedShifts, String status) {

        availabilityRepository.addWeeklyPreferences(employeeId, selectedShifts,  getStartOfWeekSunday(selectedShifts.get(0).getDate()), status);
    }

    public List<ShiftAssignment> getAssignmentsForEmployee(String employeeId) {
        return AssignmentRepository.getInstance().findByEmployee(employeeId);
    }
}
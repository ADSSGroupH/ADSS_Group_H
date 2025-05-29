package DomainLayer;

import java.time.DayOfWeek;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class EmployeeController {


    // שולף את בקשות ההחלפה של העובד
    public List<ShiftSwapRequest> getEmployeeSwapRequests(String employeeId) {
        List<ShiftSwapRequest> requests = new ArrayList<>();
        for (ShiftSwapRequest request : DAO.swapRequests) {
            if (request.getRequester().getId().equals(employeeId)) {
                requests.add(request);
            }
        }
        return requests;
    }

    // מגיש בקשת החלפת משמרת
    public ShiftSwapRequest submitSwapRequest(Employee requester, Shift fromShift, Shift toShift) {
        String requestId = String.valueOf(DAO.swapRequests.size() + 1);
        LocalDate date = LocalDate.now(); //the date of the *creation* of the swap request.
        ShiftSwapRequestController service = new ShiftSwapRequestController();
        return service.createRequest(requestId, requester, fromShift, toShift, date);
    }


    public List<Shift> getEligibleShiftsForNextWeek(String employeeId) throws Exception {
        ShiftController shiftService = new ShiftController();
        Employee employee = new ManagerController().getEmployeeById(employeeId);

        if (employee == null)
            throw new IllegalArgumentException("Employee not found.");

        LocalDate today = LocalDate.now();
        DayOfWeek todayDay = today.getDayOfWeek();
        int weeksToAdd = (todayDay.getValue() > DayOfWeek.THURSDAY.getValue() || (todayDay == DayOfWeek.THURSDAY && LocalTime.now().getHour() >= 16)) ? 2 : 1;

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

    public void submitSelectedShifts(String employeeId, List<Shift> selectedShifts) {
        DAO.WeeklyPreferneces.put(employeeId, selectedShifts);
    }



}
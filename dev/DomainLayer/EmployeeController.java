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


    public void submitWeeklyShiftPreferences(String employeeId) {
        try {
            ShiftController shiftService = new ShiftController();

            LocalDate today = LocalDate.now();
            DayOfWeek todayDay = today.getDayOfWeek();
            int weeksToAdd = (todayDay.getValue() > DayOfWeek.THURSDAY.getValue() || (todayDay == DayOfWeek.THURSDAY && LocalTime.now().getHour() >= 16)) ? 2 : 1;

            LocalDate targetWeekStart = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).plusWeeks(weeksToAdd - 1);
            LocalDate targetWeekEnd = targetWeekStart.plusDays(6);

            List<Shift> allShifts = shiftService.getShiftsBetween(targetWeekStart, targetWeekEnd);
            Employee employee = new ManagerController().getEmployeeById(employeeId);
            if (employee == null) {
                System.out.println("Employee not found.");
                return;
            }

            // סינון משמרות שרלוונטיות לפי תפקידים של העובד
            List<Shift> eligibleShifts = new ArrayList<>();
            for (Shift shift : allShifts) {
                for (Role role : shift.getRequiredRoles()) {
                    if (employee.getRoles().stream().anyMatch(r -> r.getId().equals(role.getId()))) {
                        eligibleShifts.add(shift);
                        break;
                    }
                }
            }

            if (eligibleShifts.isEmpty()) {
                System.out.println("No eligible shifts available for you this week.");
                return;
            }

            Scanner scanner = new Scanner(System.in);
            List<Shift> selectedShifts = new ArrayList<>();

            if (allShifts.isEmpty()) {
                System.out.println("No shifts available for the selected week.");
                return;
            }

            System.out.println("Available Shifts for the week: " + targetWeekStart + " to " + targetWeekEnd);

            int index = 1;
            for (Shift shift : allShifts) {
                System.out.println(index + ". Date: " + shift.getDate() + ", Type: " + shift.getType() + " (ID: " + shift.getId() + ")");
                index++;
            }

            System.out.println("Enter the numbers of the shifts you want to select (separated by commas):");
            String input = scanner.nextLine();
            String[] shiftNumbers = input.split(",");

            for (String numStr : shiftNumbers) {
                try {
                    int num = Integer.parseInt(numStr.trim());
                    if (num >= 1 && num <= allShifts.size()) {
                        selectedShifts.add(allShifts.get(num - 1)); // נבחר לפי מספר
                    } else {
                        System.out.println("Invalid number: " + num);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input: " + numStr);
                }
            }

            if (selectedShifts.isEmpty()) {
                System.out.println("No valid shifts selected.");
            } else {
                DAO.WeeklyPreferneces.put(employeeId, selectedShifts);
                System.out.println("Shift preferences submitted successfully!");
            }

        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }



}

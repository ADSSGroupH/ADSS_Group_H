import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class ShiftService {

    public Shift createShift(String id, String date, String startTime, String endTime, String type, Employee shiftManager, List<Role> requiredRoles, List<ShiftAssignment> assignments) {
        // creating the shift
        Shift newShift = new Shift(id, date, startTime, endTime, type, shiftManager, requiredRoles, assignments);

        // adding the shift to the archive (data store)
        this.archiveShift(newShift,date);


        return newShift;
    }

//    public boolean updateShift(String shiftId, String date, String startTime, String endTime, String type, Employee shiftManager, List<Role> requiredRoles, List<ShiftAssignment> assignments) {
//        for (Shift shift : DataStore.shifts) {
//            if (shift.getId().equals(shiftId)) {
//                // עדכון שדות המשמרת
//                shift.setArchived(false);  // ביכולתך לשנות גם את הסטטוס של המשמרת אם צריך
//                // אם אתה מעוניין לשדרג שדות אחרים גם, תוכל לעדכן את שדות השעה, תאריך, ועוד
//                return true;
//            }
//        }
//        return false;
//    }


    public void archiveShift(Shift shift, String date_of_being_archived) {
                DataStore.shifts.add(shift);  // הוספה לרשימה הסטטית של shifts ב-DataStore
                shift.setArchived(true);
                shift.setArchivedAt(date_of_being_archived);
    }


    public boolean deleteShift(String shiftId) {
        Iterator<Shift> iterator = DataStore.shifts.iterator();
        while (iterator.hasNext()) {
            Shift shift = iterator.next();
            if (shift.getId().equals(shiftId)) {
                iterator.remove();  // deleting the shift from the memory.
                return true;
            }
        }
        return false;  // we didn't find a matching shift.
    }

    // searching a shift by id
    public Shift getShiftById(String shiftId) {
        for (Shift shift : DataStore.shifts) {
            if (shift.getId().equals(shiftId)) {
                return shift;
            }
        }
        return null;  // didn't find a matching shift
    }


    public List<Shift> getAllShifts() {
        return new ArrayList<>(DataStore.shifts);  // returning all the shifts that happened in the last 7 years in a list.
    }


    public List<Shift> getAllShiftsForThisWeek() { //check!!
        List<Shift> allShifts = new ArrayList<>();

        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.with(java.time.DayOfWeek.SUNDAY);
        LocalDate weekEnd = weekStart.plusDays(6);

        String startDateStr = weekStart.toString(); // yyyy-MM-dd
        String endDateStr = weekEnd.toString();     // yyyy-MM-dd

        for (Shift shift : DataStore.shifts) {
            String shiftDate = shift.getDate();

            if (!shift.isArchived()
                    && shiftDate.compareTo(startDateStr) >= 0
                    && shiftDate.compareTo(endDateStr) <= 0) {

                allShifts.add(shift);
            }
        }

        return allShifts;
    }

    public List<Shift> getShiftsByDate(String date) {
        List<Shift> result = new ArrayList<>();

        for (Shift shift : DataStore.shifts) {
            if (!shift.isArchived() && shift.getDate().equals(date)) {
                result.add(shift);
            }
        }

        return result;
    }

}

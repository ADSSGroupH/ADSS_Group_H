import java.text.SimpleDateFormat;
import java.util.*;

public class ShiftService {

    // יצירת משמרת חדשה ושמירה שלה ב-DataStore
    public Shift createShift(String id, String date, String startTime, String endTime, String type, Employee shiftManager, List<Role> requiredRoles, List<ShiftAssignment> assignments) {
        // יצירת האובייקט של Shift
        Shift newShift = new Shift(id, date, startTime, endTime, type, shiftManager, requiredRoles, assignments);

        // הוספת המשמרת לרשימה ב-DataStore
        DataStore.shifts.add(newShift);  // הוספה לרשימה הסטטית של shifts ב-DataStore

        return newShift;
    }

    // עדכון משמרת
    public boolean updateShift(String shiftId, String date, String startTime, String endTime, String type, Employee shiftManager, List<Role> requiredRoles, List<ShiftAssignment> assignments) {
        for (Shift shift : DataStore.shifts) {
            if (shift.getId().equals(shiftId)) {
                // עדכון שדות המשמרת
                shift.setArchived(false);  // ביכולתך לשנות גם את הסטטוס של המשמרת אם צריך
                // אם אתה מעוניין לשדרג שדות אחרים גם, תוכל לעדכן את שדות השעה, תאריך, ועוד
                return true;
            }
        }
        return false;
    }

    // ארכוב משמרת
    public boolean archiveShift(String shiftId, String archivedAt) { //צריך גם לאחסן!
        for (Shift shift : DataStore.shifts) {
            if (shift.getId().equals(shiftId)) {
                shift.setArchived(true);
                shift.setArchivedAt(archivedAt);
                return true;  // המשמרת הושלמה והארכוב בוצע בהצלחה
            }
        }
        return false;
    }

    // מחיקת משמרת
    public boolean deleteShift(String shiftId) {
        Iterator<Shift> iterator = DataStore.shifts.iterator();
        while (iterator.hasNext()) {
            Shift shift = iterator.next();
            if (shift.getId().equals(shiftId)) {
                iterator.remove();  // הסרת המשמרת
                return true;
            }
        }
        return false;  // לא נמצאה משמרת עם מזהה זה
    }

    // חיפוש משמרת לפי ID
    public Shift getShiftById(String shiftId) {
        for (Shift shift : DataStore.shifts) {
            if (shift.getId().equals(shiftId)) {
                return shift;
            }
        }
        return null;  // אם לא נמצאה משמרת עם מזהה זה
    }

    // קבלת כל המשמרות
    public List<Shift> getAllShifts() {
        return new ArrayList<>(DataStore.shifts);  // מחזירים את כל המשמרות ברשימה חדשה
    }

    // קבלת משמרות לפי ארכוב
    public List<Shift> getShiftsByArchiveStatus(boolean isArchived) {
        List<Shift> filteredShifts = new ArrayList<>();
        for (Shift shift : DataStore.shifts) {
            if (shift.isArchived() == isArchived) {
                filteredShifts.add(shift);
            }
        }
        return filteredShifts;
    }

    public List<Shift> getAllShiftsForThisWeek() {
        List<Shift> allShifts = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0); // לניקוי השעה

        // התחלת השבוע (ראשון)
        Calendar weekStart = (Calendar) today.clone();
        weekStart.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

        // סוף השבוע (שבת)
        Calendar weekEnd = (Calendar) weekStart.clone();
        weekEnd.add(Calendar.DAY_OF_WEEK, 6);

        for (Shift shift : DataStore.shifts) {
            try {
                Date shiftDate = sdf.parse(shift.getDate());

                if (!shift.isArchived() &&
                        !shiftDate.before(weekStart.getTime()) &&
                        !shiftDate.after(weekEnd.getTime())) {

                    allShifts.add(shift);
                }

            } catch (ParseException e) {
                System.out.println("שגיאה בפיענוח תאריך במשמרת: " + shift.getDate());
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

package DomainLayer.Repositories;

import DomainLayer.*;
import java.time.LocalDate;
import java.util.*;

public class ShiftRepository {
    private static ShiftRepository instance;

    private Map<String, Shift> shifts;
    private DAO dao;

    private ShiftRepository() {
        shifts = new HashMap<>();
        dao = new DAO();
        loadShiftsFromDAO();
    }

    public static ShiftRepository getInstance() {
        if (instance == null) {
            synchronized (ShiftRepository.class) {
                if (instance == null) {
                    instance = new ShiftRepository();
                }
            }
        }
        return instance;
    }

    private void loadShiftsFromDAO() {
        for (Shift shift : dao.shifts) {
            shifts.put(shift.getId(), shift);
        }
    }

    public void addShift(Shift newShift) {
        if (!shifts.containsKey(newShift.getId())) {
            shifts.put(newShift.getId(), newShift);
            dao.shifts.add(newShift);  // סנכרון ל-DAO
        }
    }

    // עדכון משמרת קיימת
    public void updateShift(Shift updatedShift) {
        if (updatedShift == null || updatedShift.getId() == null) return;

        if (shifts.containsKey(updatedShift.getId())) {
            shifts.put(updatedShift.getId(), updatedShift);
            // עדכון ב-DAO: מחליף את האובייקט הישן ברשימה
            for (int i = 0; i < dao.shifts.size(); i++) {
                if (dao.shifts.get(i).getId().equals(updatedShift.getId())) {
                    dao.shifts.set(i, updatedShift);
                    break;
                }
            }
        }
    }

    public Shift findShiftById(String id) {
        if (shifts.containsKey(id)) {
            return shifts.get(id);
        }
        // טען מה-DAO במקרה והקאש לא מעודכן
        for (Shift shift : dao.shifts) {
            if (shift.getId().equals(id)) {
                shifts.put(id, shift);
                return shift;
            }
        }
        return null;
    }

    public List<Shift> getAllShifts() {
        return new ArrayList<>(shifts.values());
    }

    // מחיקת משמרת (לדוגמה סימון ארכיון אם יש צורך, כאן פשוט הסרה)
    public void deleteShift(String id) {
        Shift shift = shifts.get(id);
        if (shift != null) {
            shifts.remove(id);
            dao.shifts.removeIf(s -> s.getId().equals(id));
        }
    }

    // מחזיר את כל ההקצאות במשמרת לפי ID של המשמרת
    public List<ShiftAssignment> getAssignmentsForShift(String shiftId) {
        Shift shift = findShiftById(shiftId);
        if (shift != null) {
            return shift.getAssignments();
        }
        return Collections.emptyList();
    }

    // בדיקה אם משמרת קיימת לפי תאריך
    public boolean existsShiftOnDateAndLocation(LocalDate date) {
        for (Shift shift : shifts.values()) {
            if (shift.getDate().equals(date)) {
                return true;
            }
        }
        return false;
    }
}

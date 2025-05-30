package DomainLayer.Repositories;

import DomainLayer.*;
import java.time.LocalDate;
import java.util.*;

public class AssignmentRepository {
    private static AssignmentRepository instance;

    private static Map<String, ShiftAssignment> assignments;
    private DAO dao;

    private AssignmentRepository() {
        assignments = new HashMap<>();
        dao = new DAO();  // ב-DAO שלך כל השדות סטטיים, אבל נשמור את האובייקט פה
        loadAssignmentsFromDAO();
    }

    public static AssignmentRepository getInstance() {
        if (instance == null) {
            synchronized (AssignmentRepository.class) {
                if (instance == null) {
                    instance = new AssignmentRepository();
                }
            }
        }
        return instance;
    }

    private void loadAssignmentsFromDAO() {
        for (ShiftAssignment assignment : DAO.assignments) {
            assignments.put(generateId(assignment), assignment);
        }
    }

    // שים לב - ב-ShiftAssignment אין שדה id מפורש, אז נבנה מזה id ייחודי לדוגמה
    private String generateId(ShiftAssignment assignment) {
        // לדוגמה: מזהה מורכב ממזהי עובד + משמרת + תפקיד
        return assignment.getEmployee().getId() + "_" + assignment.getShift().getId() + "_" + assignment.getRole().getId();
    }

    public void saveAssignment(ShiftAssignment newAssignment) {
        String id = generateId(newAssignment);
        if (!assignments.containsKey(id)) {
            assignments.put(id, newAssignment);
            DAO.assignments.add(newAssignment); // סנכרון ל-DAO
        }
    }

    public void updateAssignment(ShiftAssignment updatedAssignment) {
        if (updatedAssignment == null) return;

        String id = generateId(updatedAssignment);
        if (assignments.containsKey(id)) {
            assignments.put(id, updatedAssignment);
            // עדכון ב-DAO: מחליף את האובייקט הישן ברשימה
            for (int i = 0; i < DAO.assignments.size(); i++) {
                ShiftAssignment a = DAO.assignments.get(i);
                String currentId = generateId(a);
                if (currentId.equals(id)) {
                    DAO.assignments.set(i, updatedAssignment);
                    break;
                }
            }
        }
    }

    public ShiftAssignment getAssignmentById(String id) {
        if (assignments.containsKey(id)) {
            return assignments.get(id);
        }
        // טען מה-DAO במקרה והקאש לא מעודכן
        for (ShiftAssignment assignment : DAO.assignments) {
            String currentId = generateId(assignment);
            if (currentId.equals(id)) {
                assignments.put(id, assignment);
                return assignment;
            }
        }
        return null;
    }

    public static List<ShiftAssignment> getAllAssignments() {
        return new ArrayList<>(assignments.values());
    }

    public void deleteAssignment(String id) {
        ShiftAssignment assignment = assignments.get(id);
        if (assignment != null) {
            assignment.setArchived(true);
            assignment.setArchiveDate(LocalDate.now());
            updateAssignment(assignment); // עדכון ב-DAO
        }
    }


    public List<ShiftAssignment> findByShiftAndRole(String shiftId, String roleId) {
        List<ShiftAssignment> result = new ArrayList<>();

        if (shiftId == null || roleId == null) {
            return result; // רשימה ריקה אם הפרמטרים לא תקינים
        }

        String trimmedShiftId = shiftId.trim();
        String trimmedRoleId = roleId.trim();

        // חיפוש ב-cache הפנימי
        for (ShiftAssignment assignment : assignments.values()) {
            if (!assignment.isArchived() && // רק משימות פעילות
                    assignment.getShift() != null &&
                    assignment.getRole() != null &&
                    assignment.getShift().getId().equals(trimmedShiftId) &&
                    assignment.getRole().getId().equals(trimmedRoleId)) {
                result.add(assignment);
            }
        }

        // חיפוש נוסף ב-DAO במקרה שה-cache לא מעודכן לחלוטין
        for (ShiftAssignment assignment : DAO.assignments) {
            String generatedId = generateId(assignment);
            if (!assignments.containsKey(generatedId) && // רק אם עדיין לא ב-cache
                    !assignment.isArchived() &&
                    assignment.getShift() != null &&
                    assignment.getRole() != null &&
                    assignment.getShift().getId().equals(trimmedShiftId) &&
                    assignment.getRole().getId().equals(trimmedRoleId)) {

                // הוסף ל-cache
                assignments.put(generatedId, assignment);
                result.add(assignment);
            }
        }

        return result;
    }

    // פונקציית עזר נוספת - חיפוש לפי משמרת בלבד
    public List<ShiftAssignment> findByShift(String shiftId) {
        List<ShiftAssignment> result = new ArrayList<>();

        if (shiftId == null) {
            return result;
        }

        String trimmedShiftId = shiftId.trim();

        for (ShiftAssignment assignment : assignments.values()) {
            if (!assignment.isArchived() &&
                    assignment.getShift() != null &&
                    assignment.getShift().getId().equals(trimmedShiftId)) {
                result.add(assignment);
            }
        }

        return result;
    }

    // פונקציית עזר נוספת - חיפוש לפי תפקיד בלבד
    public List<ShiftAssignment> findByRole(String roleId) {
        List<ShiftAssignment> result = new ArrayList<>();

        if (roleId == null) {
            return result;
        }

        String trimmedRoleId = roleId.trim();

        for (ShiftAssignment assignment : assignments.values()) {
            if (!assignment.isArchived() &&
                    assignment.getRole() != null &&
                    assignment.getRole().getId().equals(trimmedRoleId)) {
                result.add(assignment);
            }
        }

        return result;
    }
}

package DTO.HR;

public class ShiftDTO {
    private String id;
    private String date;           // תאריך במבנה String (לדוגמה: "2025-06-02")
    private String startTime;      // זמן התחלה כ-String (לדוגמה: "08:00")
    private String endTime;        // זמן סיום כ-String (לדוגמה: "16:00")
    private String type;
    private String requiredRolesCsv;  // רשימת תפקידים כמחרוזת מופרדת בפסיקים (למשל: "1,3,5")
    private String assignmentsCsv;
    private String shiftManagerId;     // מזהה מנהל המשמרת (ID כ-String)
    private String archivedAt;          // תאריך ארכיון כ-String (או ריק)
    private boolean isArchived;

    public ShiftDTO() {}

    // בנאי מלא
    public ShiftDTO(String id, String date, String startTime, String endTime, String type,
                    String requiredRolesCsv, String assignmentsCsv, String shiftManagerId,
                    String archivedAt, boolean isArchived) {
        this.id = id;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.type = type;
        this.requiredRolesCsv = requiredRolesCsv;
        this.assignmentsCsv = assignmentsCsv;
        this.shiftManagerId = shiftManagerId;
        this.archivedAt = archivedAt;
        this.isArchived = isArchived;
    }

    // גטרים וסטרים
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getRequiredRolesCsv() { return requiredRolesCsv; }
    public void setRequiredRolesCsv(String requiredRolesCsv) { this.requiredRolesCsv = requiredRolesCsv; }

    public String getAssignmentsCsv() { return assignmentsCsv; }
    public void setAssignmentsCsv(String assignmentsCsv) { this.assignmentsCsv = assignmentsCsv; }

    public String getShiftManagerId() { return shiftManagerId; }
    public void setShiftManagerId(String shiftManagerId) { this.shiftManagerId = shiftManagerId; }

    public String getArchivedAt() { return archivedAt; }
    public void setArchivedAt(String archivedAt) { this.archivedAt = archivedAt; }

    public boolean isArchived() { return isArchived; }
    public void setArchived(boolean archived) { isArchived = archived; }
}

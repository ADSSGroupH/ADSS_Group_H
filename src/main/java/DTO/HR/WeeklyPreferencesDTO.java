package DTO.HR;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class WeeklyPreferencesDTO {
    private String employeeId;
    private String preferredShiftIdsCsv; // IDs של המשמרות מופרדים בפסיקים
    private String weekStartDate;   // תאריך כמחרוזת
    private String createdAt;       // תאריך כמחרוזת
    private String lastModified;    // תאריך כמחרוזת
    private String status; // DRAFT, SUBMITTED, APPROVED
    private String notes;

    private String employeeIdSimple; // פרטי עובד פשוטים, רק מזהה לדוגמה

    public WeeklyPreferencesDTO() {
        this.preferredShiftIdsCsv = "";
        this.status = "DRAFT";
        this.createdAt = java.time.LocalDate.now().toString();
        this.lastModified = java.time.LocalDate.now().toString();
    }

    public WeeklyPreferencesDTO(String employeeId, String weekStartDate) {
        this();
        this.employeeId = employeeId;
        this.weekStartDate = weekStartDate;
    }

    // גטרים וסטרים רגילים
    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getPreferredShiftIdsCsv() {
        return preferredShiftIdsCsv;
    }

    public void setPreferredShiftIdsCsv(String preferredShiftIdsCsv) {
        this.preferredShiftIdsCsv = preferredShiftIdsCsv;
    }

    public String getWeekStartDate() {
        return weekStartDate;
    }

    public void setWeekStartDate(String weekStartDate) {
        this.weekStartDate = weekStartDate;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getEmployeeIdSimple() {
        return employeeIdSimple;
    }

    public void setEmployeeIdSimple(String employeeIdSimple) {
        this.employeeIdSimple = employeeIdSimple;
    }

    // --- שיטות עזר לעבודה עם רשימת מזהי המשמרות (ללא שינוי טיפוסים) ---

    public List<String> getPreferredShiftIds() {
        if (preferredShiftIdsCsv == null || preferredShiftIdsCsv.isBlank()) {
            return Collections.emptyList();
        }
        return Arrays.stream(preferredShiftIdsCsv.split(","))
                .map(String::trim)
                .collect(Collectors.toList());
    }

    public void setPreferredShiftIds(List<String> preferredShiftIds) {
        if (preferredShiftIds == null || preferredShiftIds.isEmpty()) {
            this.preferredShiftIdsCsv = "";
        } else {
            this.preferredShiftIdsCsv = String.join(",", preferredShiftIds);
        }
    }
}

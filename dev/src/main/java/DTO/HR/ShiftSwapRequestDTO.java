package DTO.HR;

public class ShiftSwapRequestDTO {

    public enum Status {
        pending,
        approved,
        rejected
    }

    private String id;
    private String requestorId; // במקום EmployeeDTO
    private String fromShiftId; // במקום ShiftDTO
    private String toShiftId;   // במקום ShiftDTO
    private String status;      // נשתמש במחרוזת במקום enum
    private String date;        // במקום LocalDate
    private String archivedAt;  // במקום LocalDate
    private boolean isArchived;

    // Default constructor
    public ShiftSwapRequestDTO() {
    }

    // Constructor with all fields
    public ShiftSwapRequestDTO(String id, String requestorId, String fromShiftId, String toShiftId,
                               String status, String date, String archivedAt, boolean isArchived) {
        this.id = id;
        this.requestorId = requestorId;
        this.fromShiftId = fromShiftId;
        this.toShiftId = toShiftId;
        this.status = status;
        this.date = date;
        this.archivedAt = archivedAt;
        this.isArchived = isArchived;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getRequestorId() {
        return requestorId;
    }

    public String getFromShiftId() {
        return fromShiftId;
    }

    public String getToShiftId() {
        return toShiftId;
    }

    public String getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }

    public String getArchivedAt() {
        return archivedAt;
    }

    public boolean isArchived() {
        return isArchived;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setRequestorId(String requestorId) {
        this.requestorId = requestorId;
    }

    public void setFromShiftId(String fromShiftId) {
        this.fromShiftId = fromShiftId;
    }

    public void setToShiftId(String toShiftId) {
        this.toShiftId = toShiftId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setArchivedAt(String archivedAt) {
        this.archivedAt = archivedAt;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }
}

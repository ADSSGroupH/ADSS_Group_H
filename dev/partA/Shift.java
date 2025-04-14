import java.util.List;

public class Shift {
    private String id;
    private String date;
    private String startTime;
    private String endTime;
    private String type;
    private List<Role> requiredRoles;
    private List<ShiftAssignment> assignments;
    private Employee shiftManager;
    private String archivedAt;
    private boolean isArchived;

    public Shift(String id, String date, String startTime, String endTime, String type, Employee shiftManager, List<Role> requiredRoles, List<ShiftAssignment> assignments) {
        this.id = id;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.type = type;
        this.shiftManager = shiftManager;
        this.requiredRoles = requiredRoles;
        this.assignments = assignments;
        this.isArchived = false;
        this.archivedAt = null;
    }

    // Getters and setters (רשות, אם רוצים נגישות מבוקרת – תלוי בכם ובסגנון העבודה שלכם)
    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getType() {
        return type;
    }

    public List<Role> getRequiredRoles() {
        return requiredRoles;
    }

    public List<ShiftAssignment> getAssignments() {
        return assignments;
    }

    public Employee getShiftManager() {
        return shiftManager;
    }

    public String getArchivedAt() {
        return archivedAt;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }

    public void setArchivedAt(String archivedAt) {
        this.archivedAt = archivedAt;
    }
}

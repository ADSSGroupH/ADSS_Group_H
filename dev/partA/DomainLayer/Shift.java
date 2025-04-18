import java.time.LocalDate;
import java.util.Date;
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
    private LocalDate archivedAt; //date of putting the date in the archive
    private boolean isArchived;

    // constructor
    public Shift(String id, String date, String startTime, String endTime, String type, Employee shiftManager, List<Role> requiredRoles, List<ShiftAssignment> assignments, LocalDate ArchivedAt) {
        this.id = id;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.type = type;
        this.shiftManager = shiftManager;
        this.requiredRoles = requiredRoles;
        this.assignments = assignments;
        this.isArchived = false;
        this.archivedAt = ArchivedAt;
    }

    // Getters
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

    public LocalDate getArchivedAt() {
        return archivedAt;
    }

    public boolean isArchived() {
        return isArchived;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setRequiredRoles(List<Role> requiredRoles) {
        this.requiredRoles = requiredRoles;
    }

    public void setAssignments(List<ShiftAssignment> assignments) {
        this.assignments = assignments;
    }

    public void setShiftManager(Employee shiftManager) {
        this.shiftManager = shiftManager;
    }

    public void setArchivedAt(LocalDate archivedAt) {
        this.archivedAt = archivedAt;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }


}

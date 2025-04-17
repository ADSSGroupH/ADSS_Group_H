public class ShiftSwapRequest {

    public enum Status {
        pending,
        approved,
        rejected
    }

    private String id;
    private Employee requestor;
    private Shift fromShift;
    private Shift toShift;
    private Status status;
    private String date;
    private String archivedAt;
    private boolean isArchived;

    // constructor
    public ShiftSwapRequest(String id, Employee requester, Shift fromShift, Shift toShift, String date) {
        this.id = id;
        this.requestor = requester;
        this.fromShift = fromShift;
        this.toShift = toShift;
        this.date = date;
        this.status = Status.pending;
        this.isArchived = false;
        this.archivedAt = null;
    }

    // Getters
    public String getId() {
        return id;
    }

    public Employee getRequester() {
        return requestor;
    }

    public Shift getFromShift() {
        return fromShift;
    }

    public Shift getToShift() {
        return toShift;
    }

    public Status getStatus() {
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
    public void setStatus(Status status) {
        this.status = status;
    }

    public void setArchivedAt(String archivedAt) {
        this.archivedAt = archivedAt;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }
}

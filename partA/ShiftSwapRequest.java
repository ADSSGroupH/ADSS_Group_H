


public class ShiftSwapRequest {
    public enum Status {
        pending,
        approved,
        rejected
    }
    private String Id;
    private Employee requestor;
    private Shift fromShift;
    private  Shift toShift;
    private Status status;
    private String date;
    private String ArchivedAt;
    private boolean IsArchived;
}

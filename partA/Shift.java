import java.util.List;

public class Shift {
    private String Id;
    private String date;
    private String StartTime;
    private String EndTime;
    private String type;
    private List <Role> requiredRoles;
    private List<ShiftAssignment> assignments;
    private Employee shiftManager;
    private String ArchivedAt;
    private boolean IsArchived;
}

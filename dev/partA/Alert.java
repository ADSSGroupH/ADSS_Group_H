public class Alert {
    public enum AlertStatus {
        info,
        warning,
        error
    }
    private String message;
    private AlertStatus type;
    private String CreationDate;
}

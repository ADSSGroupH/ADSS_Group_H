import java.time.LocalDate;
import java.util.Set;

public class HRManager extends Employee {
    public HRManager(String id, String name, String phoneNumber, Branch branch,
                     Set<Role> roles, EmployeeContract contract, String bankDetails,
                     boolean isArchived, LocalDate archivedAt, String password) {
        super(id, name, phoneNumber, branch, roles, contract, bankDetails, isArchived, archivedAt, true, password);
    }
}

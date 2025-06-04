package DomainLayer.HR;

import java.time.LocalDate;
import java.util.Set;

public class HRManager extends Employee {
    public HRManager(String id, String name, String phoneNumber, String branchId,
                     Set<Role> roles, EmployeeContract contract, String bankDetails,
                     boolean isArchived, LocalDate archivedAt, String password) {
        super(id, name, phoneNumber, branchId, roles, contract, bankDetails, isArchived, archivedAt, true, password);
    }
}
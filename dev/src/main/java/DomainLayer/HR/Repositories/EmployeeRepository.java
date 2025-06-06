package DomainLayer.HR.Repositories;

import DTO.HR.EmployeeDTO;
import DTO.HR.RoleDTO;
import DTO.HR.ShiftDTO;
import DTO.Transportation.driverDTO;
import Dal.HR.JdbcBranchDAO;
import Dal.HR.JdbcEmployeeDAO;
import DomainLayer.HR.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class EmployeeRepository {
    private static EmployeeRepository instance;
    private final Map<String, Employee> employeeMap;
    private final JdbcEmployeeDAO jdbcEmployeeDAO;
    private static ContractsRepository contractsRepository;
    private static RoleRepository roleRepository;


    private EmployeeRepository() {
        this.employeeMap = new HashMap<>();
        this.jdbcEmployeeDAO = new JdbcEmployeeDAO();
        this.contractsRepository = ContractsRepository.getInstance();
        this.roleRepository = RoleRepository.getInstance();
    }

    public static synchronized EmployeeRepository getInstance() {
        if (instance == null) {
            instance = new EmployeeRepository();
        }
        return instance;
    }

    public void addEmployee(Employee employee) throws SQLException {
        employeeMap.put(employee.getId(), employee);
        try {
            EmployeeDTO empDTO = fromEntity(employee);
            jdbcEmployeeDAO.save(empDTO);
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }


    public void addEmployeeToDriverTB(Employee employee) throws SQLException {
        try {
            EmployeeDTO empDTO = fromEntity(employee);
            jdbcEmployeeDAO.saveIfDriver(empDTO);
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    public void addDriverLicense(String id, String licenseType) throws SQLException {
        jdbcEmployeeDAO.addDriverLicense(id, licenseType);
    }

    public void updateEmployee(Employee employee) {
        employeeMap.put(employee.getId(), employee);
        try {
            EmployeeDTO empDTO = fromEntity(employee);
            jdbcEmployeeDAO.update(empDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Employee getEmployee(String id) {
        if (employeeMap.containsKey(id)) {
            return employeeMap.get(id);
        }
        try {
            Optional<EmployeeDTO> optionalEmployeeDTO = jdbcEmployeeDAO.findById(id);
            if (optionalEmployeeDTO.isEmpty()) {
                return null;
            }
            EmployeeDTO empDTO = optionalEmployeeDTO.get();
            Employee employee = toEntity(empDTO);
            employeeMap.put(id, employee);
            return employee;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Alias לשם ברור יותר
    public Employee getEmployeeById(String id) {
        return getEmployee(id);
    }

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // המרה מ-Employee ל-EmployeeDTO
    public static EmployeeDTO fromEntity(Employee employee) {
        if (employee == null) return null;

        String roleIds = employee.getRoles().stream()
                .map(Role::getId) // מניח ש-Role יש שיטה getId שמחזירה String
                .collect(Collectors.joining(","));

        String contractStartDate = employee.getContract() != null ?
                employee.getContract().getStartDate().format(formatter) : null;

        String contractEndDate = employee.getContract() != null ?
                employee.getContract().getArchivedAt().format(String.valueOf(formatter)) : null;

        String archivedAt = employee.getArchivedAt() != null ?
                employee.getArchivedAt().format(formatter) : null;

        return new EmployeeDTO(
                employee.getId(),
                employee.getName(),
                employee.getPhoneNumber(),
                employee.getBranchId() != null ? employee.getBranchId() : null,
                roleIds,
                employee.getContract() != null ? employee.getContract().getSalary() : 0,
                employee.getContract().getId(),
                employee.getBankDetails(),
                employee.isArchived(),
                archivedAt,
                employee.IsManager()
        );
    }


    public static Employee toEntity(EmployeeDTO dto) throws SQLException {
        if (dto == null) return null;

        Set<Role> roles = Arrays.stream(dto.getRoleIds().split(","))
                .map(String::trim)
                .map(roleRepository::getRole)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        EmployeeContract contract = contractsRepository.getContractById(dto.getContractId());


        LocalDate archivedAtDate = null;
        if (dto.getArchivedAt() != null && !dto.getArchivedAt().isEmpty()) {
            archivedAtDate = LocalDate.parse(dto.getArchivedAt(), formatter);
        }


        return new Employee(
                dto.getId(),
                dto.getName(),
                dto.getPhoneNumber(),
                dto.getBranchId(),
                roles,
                contract,
                dto.getBankDetails(),
                dto.isArchived(),
                archivedAtDate,
                dto.isManager(),
                null
        );
    }

    public List<Employee> getActiveEmployees() {
        try {
            List<EmployeeDTO> dtos = jdbcEmployeeDAO.findActiveEmployees();
            List<Employee> list = new ArrayList<>();
            for (EmployeeDTO dto : dtos) {
                Employee entity = toEntity(dto);
                list.add(entity);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // מחזיר את כל העובדים המשתייכים לסניף מסוים לפי id של הסניף
    public static List<Employee> getEmployeesByBranch(String branchId) {
        try {
            JdbcEmployeeDAO jdbcEmployeeDAO = new JdbcEmployeeDAO();
            List<EmployeeDTO> dtos = jdbcEmployeeDAO.findByBranch(branchId);
            List<Employee> list = new ArrayList<>();
            for (EmployeeDTO dto : dtos) {
                Employee entity = toEntity(dto);
                list.add(entity);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // מחזיר את כל העובדים שהם מנהלים (לפי שדה isManager)
    public List<Employee> getManagers() {
        try {
            List<EmployeeDTO> dtos = jdbcEmployeeDAO.findManagers();
            List<Employee> list = new ArrayList<>();
            for (EmployeeDTO dto : dtos) {
                Employee entity = toEntity(dto);
                list.add(entity);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // בודק אם עובד קיים לפי id וסיסמא - לאימות
    public Employee authenticateEmployee(String id, String password) {
        try {
            Optional<EmployeeDTO> optionalDTO = jdbcEmployeeDAO.findByIdAndPassword(id, password);
            if (optionalDTO.isPresent()) {
                return toEntity(optionalDTO.get());
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // מחפש את העובדים הכשירים (qualified) לתפקיד מסוים בסניף נתון
    public List<Employee> findQualified(String branchId, String roleId) {
        try {
            List<EmployeeDTO> dtos = jdbcEmployeeDAO.findQualified(branchId, roleId);
            List<Employee> list = new ArrayList<>();
            for (EmployeeDTO dto : dtos) {
                Employee entity = toEntity(dto);
                list.add(entity);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // בודק אם עובד עם id מסוים קיים
    public boolean employeeExists(String id) {
        try {
            return jdbcEmployeeDAO.exists(id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Employee> getAllEmployees() throws SQLException {
        List<EmployeeDTO> AllEmployeesDTO = jdbcEmployeeDAO.findAll();
        List<Employee> result = new ArrayList<>();
        for (EmployeeDTO empDTO : AllEmployeesDTO) {
            result.add(toEntity(empDTO));
        }
        return result;
    }

    // מחפש עובדים זמינים למשמרת מסוימת (על פי תאריך ושעות)
    public List<List<Employee>> findAvailableAndUnavailableForShift(Shift targetShift, Role requiredRole) {
        try {
            ShiftDTO targetShiftDTO = ShiftRepository.fromEntity(targetShift);
            RoleDTO requiredRoleDTO = roleRepository.fromEntity(requiredRole);

            // נניח שה-DAO מחזיר List<List<EmployeeDTO>> שבו:
            // index 0 = רשימת עובדים זמינים
            // index 1 = רשימת עובדים לא זמינים
            List<List<EmployeeDTO>> dtos = jdbcEmployeeDAO.findAvailableForShift(targetShiftDTO, requiredRoleDTO);

            // ממירים כל רשימת EmployeeDTO לרשימת Employee
            List<List<Employee>> result = new ArrayList<>();

            for (List<EmployeeDTO> dtoList : dtos) {
                List<Employee> employees = new ArrayList<>();
                for (EmployeeDTO employeeDTO : dtoList) {
                    Employee entity = toEntity(employeeDTO);
                    employees.add(entity);
                }
                result.add(employees);
            }

            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // פונקצית מחיקת עובד
    public void deleteEmployee(String id) throws SQLException {
        try {
            // מחיקה מהמפה המקומית
            employeeMap.remove(id);

            // מחיקה מבסיס הנתונים
            jdbcEmployeeDAO.delete(id);
        } catch (Exception e) {
            throw new SQLException("Failed to delete employee with id: " + id, e);
        }
    }

    // פונקצית ארכוב עובד (מחיקה רכה)
    public void archiveEmployee(String id) throws SQLException {
        try {
            Employee employee = getEmployee(id);
            if (employee != null) {
                employee.setArchived(true);
                employee.setArchivedAt(LocalDate.now());
                updateEmployee(employee);
            }
        } catch (Exception e) {
            throw new SQLException("Failed to archive employee with id: " + id, e);
        }


    }
}



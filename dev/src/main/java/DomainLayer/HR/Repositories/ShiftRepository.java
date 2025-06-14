package DomainLayer.HR.Repositories;

import DTO.HR.RoleDTO;
import DTO.HR.ShiftAssignmentDTO;
import DTO.HR.ShiftDTO;
import Dal.HR.JdbcAssignmentDAO;
import Dal.HR.JdbcEmployeeDAO;
import Dal.HR.JdbcRoleDAO;
import Dal.HR.JdbcShiftDAO;
import DomainLayer.HR.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ShiftRepository {
    private static ShiftRepository instance;

    private Map<String, Shift> shiftMap;
    private JdbcShiftDAO jdbcShiftDAO;
    private static AssignmentRepository assignmentRepository;


    private ShiftRepository() {
        this.shiftMap = new HashMap<>();
        this.jdbcShiftDAO = new JdbcShiftDAO();
        this.assignmentRepository = AssignmentRepository.getInstance();
    }

    public static ShiftRepository getInstance() {
        if (instance == null) {
            synchronized (ShiftRepository.class) {
                if (instance == null) {
                    instance = new ShiftRepository();
                }
            }
        }
        return instance;
    }

    public void addShift(Shift shift) {
        shiftMap.put(shift.getId(), shift);
        try {
            ShiftDTO shiftDTO = fromEntity(shift);
            jdbcShiftDAO.save(shiftDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateShift(Shift shift) {
        shiftMap.put(shift.getId(), shift);
        try {
            ShiftDTO shiftDTO = fromEntity(shift);
            jdbcShiftDAO.update(shiftDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Shift getShift(String id) {
        if (shiftMap.containsKey(id)) {
            return shiftMap.get(id);
        }
        try {
            Optional<ShiftDTO> optionalShiftDTO = jdbcShiftDAO.findById(id);
            if (!optionalShiftDTO.isPresent()) {
                return null;
            }
            Shift shift = toEntity(optionalShiftDTO.get());
            shiftMap.put(id, shift);
            return shift;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Shift> getAllShifts() throws SQLException {
        List<ShiftDTO> shiftDTOs = jdbcShiftDAO.findAll();
        List<Shift> shifts = new ArrayList<>();
        for (ShiftDTO dto : shiftDTOs) {
            shifts.add(toEntity(dto));
            shiftMap.put(dto.getId(), shifts.get(shifts.size()-1));
        }
        return shifts;
    }


    public void removeShift(String id) {
        shiftMap.remove(id);
        try {
            jdbcShiftDAO.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean shiftExists(String id) {
        if (shiftMap.containsKey(id)) {
            return true;
        }
        try {
            return jdbcShiftDAO.exists(id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getShiftCount() {
        try {
            return jdbcShiftDAO.getShiftCount();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // המרת Shift ל-ShiftDTO
    public static ShiftDTO fromEntity(Shift shift) {
        if (shift == null) {
            return null;
        }

        // לדוגמה המרת רשימות לתווי ID מופרדים בפסיקים
        String requiredRolesCsv = "";
        if (shift.getRequiredRoles() != null && !shift.getRequiredRoles().isEmpty()) {
            requiredRolesCsv = shift.getRequiredRoles().stream()
                    .map(Role::getId)
                    .collect(Collectors.joining(","));
        }

        String assignmentsCsv = "";
        if (shift.getAssignments() != null && !shift.getAssignments().isEmpty()) {
            assignmentsCsv = shift.getAssignments().stream()
                    .map(a -> String.valueOf(a.getId()))
                    .collect(Collectors.joining(","));
        }

        String archivedAtStr = (shift.getArchivedAt() != null) ? shift.getArchivedAt().toString() : "";

        return new ShiftDTO(
                shift.getId(),
                shift.getDate().toString(),
                shift.getStartTime(),
                shift.getEndTime(),
                shift.getType(),
                requiredRolesCsv,
                assignmentsCsv,
                shift.getShiftManager() != null ? shift.getShiftManager().getId() : null,
                archivedAtStr,
                shift.isArchived()
        );
    }

    // המרת ShiftDTO ל-Shift
    public static Shift toEntity(ShiftDTO dto) throws SQLException {
        if (dto == null) {
            return null;
        }

        List<Role> requiredRoles = new ArrayList<>();
        if (dto.getRequiredRolesCsv() != null && !dto.getRequiredRolesCsv().isEmpty()) {
            String[] roleIds = dto.getRequiredRolesCsv().split(",");
            for (String id : roleIds) {
                JdbcRoleDAO roleDAO = new JdbcRoleDAO();
                Role role = roleDAO.findById(id.trim())
                        .map(RoleRepository::toEntity)
                        .orElse(null); // או throw exception, או ברירת מחדל אחרת
                if (role != null) {
                    requiredRoles.add(role);
                }
            }
        }

        List<ShiftAssignment> assignments = new ArrayList<>();
        if (dto.getAssignmentsCsv() != null && !dto.getAssignmentsCsv().isEmpty()) {
            String[] assignmentIds = dto.getAssignmentsCsv().split(",");
            for (String id : assignmentIds) {
                JdbcAssignmentDAO assignmentDAO = new JdbcAssignmentDAO();
                Optional<ShiftAssignmentDTO> assignmentDTO = assignmentDAO.findById(id.trim());
                ShiftAssignment assignment = assignmentDTO.map(assignmentRepository :: toEntity).orElse(null);
                if (assignment != null) {
                    assignments.add(assignment);
                }
            }
        }

        Employee shiftManager = null;
        if (dto.getShiftManagerId() != null && !dto.getShiftManagerId().isEmpty()) {
            JdbcEmployeeDAO  employeeDAO = new JdbcEmployeeDAO();
            shiftManager = employeeDAO.findById(dto.getShiftManagerId())
                    .map(e -> {
                        try {
                            return EmployeeRepository.toEntity(e);
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    })
                    .orElse(null);
        }

        LocalDate archivedAt = null;
        if (dto.getArchivedAt() != null && !dto.getArchivedAt().isEmpty()) {
            archivedAt = LocalDate.parse(dto.getArchivedAt());
        }

        Shift shift = new Shift(
                dto.getId(),
                LocalDate.parse(dto.getDate()),
                dto.getStartTime(),
                dto.getEndTime(),
                dto.getType(),
                shiftManager,
                requiredRoles,
                assignments,
                archivedAt
        );
        shift.setArchived(dto.isArchived());

        return shift;
    }
    public Shift findByDateAndTime(String date, String startTime) throws SQLException{
        ShiftDTO dto = jdbcShiftDAO.findByDateAndTime(date, startTime);
        Shift shift = toEntity(dto);
        return shift;
    }
}

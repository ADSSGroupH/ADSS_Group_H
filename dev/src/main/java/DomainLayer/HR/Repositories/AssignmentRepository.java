package DomainLayer.HR.Repositories;

import DTO.HR.ShiftAssignmentDTO;
import Dal.HR.JdbcAssignmentDAO;
import Dal.HR.JdbcEmployeeDAO;
import Dal.HR.JdbcRoleDAO;
import Dal.HR.JdbcShiftDAO;
import DomainLayer.HR.Employee;
import DomainLayer.HR.Role;
import DomainLayer.HR.Shift;
import DomainLayer.HR.ShiftAssignment;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class AssignmentRepository {
    private static AssignmentRepository instance;
    private Map<String, ShiftAssignment> assignmentMap;
    private JdbcAssignmentDAO jdbcShiftAssignmentDAO;
    private JdbcShiftDAO jdbcShiftDAO;

    private AssignmentRepository() {
        this.assignmentMap = new HashMap<>();
        this.jdbcShiftAssignmentDAO = new JdbcAssignmentDAO();
    }

    public static AssignmentRepository getInstance() {
        if (instance == null) {
            synchronized (AssignmentRepository.class) {
                if (instance == null) {
                    instance = new AssignmentRepository();
                }
            }
        }
        return instance;
    }

    public void addAssignment(ShiftAssignment assignment) throws SQLException {
        String id = generateId(assignment);
        assignment.setId(id);
        assignmentMap.put(id, assignment);
        try {
            ShiftAssignmentDTO assignmentDTO = fromEntity(assignment);
            jdbcShiftAssignmentDAO.save(assignmentDTO);
        } catch (Exception e) {
            throw new SQLException();
        }
    }

    public void saveAssignment(ShiftAssignment assignment) throws SQLException {
        addAssignment(assignment);
    }

    public void updateAssignment(ShiftAssignment assignment) {
        String id = assignment.getId();
        if (id == null) {
            id = generateId(assignment);
            assignment.setId(id);
        }
        assignmentMap.put(id, assignment);
        try {
            ShiftAssignmentDTO assignmentDTO = fromEntity(assignment);
            jdbcShiftAssignmentDAO.update(assignmentDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ShiftAssignment getAssignment(String id) {
        if (assignmentMap.containsKey(id)) {
            return assignmentMap.get(id);
        }
        try {
            Optional<ShiftAssignmentDTO> tempAssignment = jdbcShiftAssignmentDAO.findById(id);
            if (!tempAssignment.isPresent()) {
                return null;
            }
            ShiftAssignment assignment = toEntity(tempAssignment.get());
            assignmentMap.put(id, assignment);
            return assignment;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<ShiftAssignment> getAllAssignments() {
        try {
            List<ShiftAssignmentDTO> assignmentDTOs = jdbcShiftAssignmentDAO.findAll();
            return assignmentDTOs.stream()
                    .map(this::toEntity)
                    .filter(Objects::nonNull)
                    .peek(assignment -> assignmentMap.put(assignment.getId(), assignment))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }


    // דוגמא לשיטות אחרות (ניתן להוסיף גם להן המרה בהתאם לדוגמה למעלה)
    public List<ShiftAssignment> getActiveAssignments() {
        try {
            List<ShiftAssignmentDTO> assignmentDTOs = jdbcShiftAssignmentDAO.findActiveAssignments();
            return assignmentDTOs.stream()
                    .map(this::toEntity)
                    .peek(assignment -> assignmentMap.put(assignment.getId(), assignment))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public void removeAssignment(String id) {
        assignmentMap.remove(id);
        try {
            jdbcShiftAssignmentDAO.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean assignmentExists(String id) {
        if (assignmentMap.containsKey(id)) {
            return true;
        }
        try {
            return jdbcShiftAssignmentDAO.exists(id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // פונקציית עזר ליצירת ID ייחודי
    private String generateId(ShiftAssignment assignment) {
        if (assignment.getEmployee() != null && assignment.getShiftId() != null && assignment.getRole() != null) {
            return assignment.getEmployee().getId() + "_" +
                    assignment.getShiftId() + "_" +
                    assignment.getRole().getId();
        }
        return "assignment_" + System.currentTimeMillis();
    }


    public ShiftAssignmentDTO fromEntity(ShiftAssignment entity) {
        if (entity == null) return null;

        return new ShiftAssignmentDTO(
                entity.getId(),
                entity.getEmployee().getId(),
                entity.getShiftId(),
                entity.getRole().getId(),
                entity.getArchiveDate() != null ? entity.getArchiveDate().toString() : null,
                entity.isArchived()
        );
    }

    public ShiftAssignment toEntity(ShiftAssignmentDTO dto) {
        if (dto == null) return null;

        try {
            JdbcEmployeeDAO employeeDAO = new JdbcEmployeeDAO();
            JdbcShiftDAO shiftDAO = new JdbcShiftDAO();
            JdbcRoleDAO roleDAO = new JdbcRoleDAO();

            Employee employee;
            try {
                employee = employeeDAO.findById(dto.getEmployeeId())
                        .map(e -> {
                            try {
                                return EmployeeRepository.toEntity(e);
                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }
                        })
                        .orElse(null);
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }


            Role role;
            try {
                role = roleDAO.findById(dto.getRoleId())
                        .map(r -> {
                            return RoleRepository.toEntity(r);
                        })
                        .orElse(null);
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }

            if (employee == null  || role == null) return null;

            LocalDate archiveDate = dto.getArchiveDate() != null ? LocalDate.parse(dto.getArchiveDate()) : null;

            ShiftAssignment entity = new ShiftAssignment(employee, dto.getShiftId(), role, archiveDate);
            entity.setId(dto.getId());
            entity.setArchived(dto.isArchived());

            return entity;

        } catch (RuntimeException re) {
            if (re.getCause() instanceof SQLException) {
                re.getCause().printStackTrace();
                return null;
            }
            throw re;  // לא צפוי, העבר הלאה
        }
    }


    public List<ShiftAssignment> findByShiftAndRole(String shiftId, String roleId) throws SQLException {
        List<ShiftAssignmentDTO> listOfDTO = jdbcShiftAssignmentDAO.findByShiftAndRole(shiftId,roleId);
        List<ShiftAssignment> result = new ArrayList<>();
        for (ShiftAssignmentDTO DTO : listOfDTO){
            result.add(toEntity(DTO));
        }
        return result;
    }

    public List<ShiftAssignment> findByEmployee(String employeeId) {
        try {
            List<ShiftAssignmentDTO> dtos = jdbcShiftAssignmentDAO.findByEmployee(employeeId);
            List<ShiftAssignment> result = new ArrayList<>();
            for (ShiftAssignmentDTO dto : dtos) {
                ShiftAssignment assignment = toEntity(dto);
                if (assignment != null) {
                    assignmentMap.put(assignment.getId(), assignment);
                    result.add(assignment);
                }
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}
package DomainLayer.HR.Repositories;

import DTO.HR.BranchDTO;
import Dal.HR.JdbcBranchDAO;
import Dal.HR.JdbcEmployeeDAO;
import DomainLayer.HR.Branch;
import DomainLayer.HR.Employee;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class BranchRepository {
    private static BranchRepository instance;

    private Map<String, Branch> branchMap;
    private JdbcBranchDAO jdbcBranchDAO;

    private BranchRepository() {
        this.branchMap = new HashMap<>();
        this.jdbcBranchDAO = new JdbcBranchDAO();
    }

    public static BranchRepository getInstance() {
        if (instance == null) {
            synchronized (BranchRepository.class) {
                if (instance == null) {
                    instance = new BranchRepository();
                }
            }
        }
        return instance;
    }

    public void addBranch(Branch branch) {
        branchMap.put(branch.getId(), branch);
        try {
            BranchDTO branchDTO = fromEntity(branch);  // המרה פה בתוך הריפוזיטורי
            jdbcBranchDAO.save(branchDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateBranch(Branch branch) {
        branchMap.put(branch.getId(), branch);
        try {
            BranchDTO branchDTO = fromEntity(branch);  // המרה פה בתוך הריפוזיטורי
            jdbcBranchDAO.update(branchDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Branch getBranch(String id) {
        if (branchMap.containsKey(id)) {
            return branchMap.get(id);
        }
        try {
            var tempBranchDTO = jdbcBranchDAO.findById(id);
            if (!tempBranchDTO.isPresent()) {
                return null;
            }
            Branch branch = toEntity(tempBranchDTO.get());  // המרה פה בתוך הריפוזיטורי
            branchMap.put(id, branch);
            return branch;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Branch> getAllBranches() {
        try {
            List<BranchDTO> branchDTOs = jdbcBranchDAO.findAll(); // ← ייתכן וזורק SQLException
            List<Branch> list = new ArrayList<>();
            for (BranchDTO branchDTO : branchDTOs) {
                Branch branch = toEntity(branchDTO);
                branchMap.put(branch.getId(), branch);
                list.add(branch);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace(); // או לוג מתאים
            return List.of();     // תחזיר רשימה ריקה במקום לזרוק החרגה
        }
    }

    public List<BranchDTO> getAllBranchesDTO() throws SQLException {
            List<BranchDTO> branchDTOs = jdbcBranchDAO.findAll();
            return branchDTOs;
    }


    public List<Branch> getBranchesByName(String name) {
        try {
            List<BranchDTO> branchDTOs = jdbcBranchDAO.findByName(name);
            List<Branch> list = new ArrayList<>();
            for (BranchDTO branchDTO : branchDTOs) {
                Branch branch = toEntity(branchDTO);
                branchMap.put(branch.getId(), branch);
                list.add(branch);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public List<Branch> getBranchesByAddress(String address) {
        try {
            List<BranchDTO> branchDTOs = jdbcBranchDAO.findByAddress(address);
            // המרה פה בתוך הריפוזיטורי
            List<Branch> list = new ArrayList<>();
            for (BranchDTO branchDTO : branchDTOs) {
                Branch branch = toEntity(branchDTO);
                branchMap.put(branch.getId(), branch);
                list.add(branch);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public void removeBranch(String id) {
        branchMap.remove(id);
        try {
            jdbcBranchDAO.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean branchExists(String id) {
        if (branchMap.containsKey(id)) {
            return true;
        }
        try {
            return jdbcBranchDAO.exists(id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getBranchCount() {
        try {
            return jdbcBranchDAO.getBranchCount();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // ממיר Branch (entity) ל-BranchDTO
    public static BranchDTO fromEntity(Branch branch) {
        if (branch == null) {
            return null;
        }

        // המרת רשימת העובדים למחרוזת IDs מופרדת בפסיקים
        String employeeIds = null;
        if (branch.getEmployees() != null && !branch.getEmployees().isEmpty()) {
            employeeIds = branch.getEmployees().stream()
                    .map(Employee::getId)  // נניח של-Employee יש getId()
                    .collect(Collectors.joining(","));
        }

        return new BranchDTO(
                branch.getId(),
                branch.getName(),
                branch.getAddress(),
                employeeIds
        );
    }

    // ממיר BranchDTO ל-Branch (entity)
    public static Branch toEntity(BranchDTO dto) throws SQLException {
        if (dto == null) {
            return null;
        }

        ArrayList<Employee> employees = new ArrayList<>();

        if (dto.getEmployeeIds() != null && !dto.getEmployeeIds().isEmpty()) {
            // מחלק את מחרוזת ה-IDs לפסיקים וממיר ל-Employee דרך קריאה ל-EmployeeRepository
            List<String> employeeIdList = Arrays.asList(dto.getEmployeeIds().split(","));

            for (String empId : employeeIdList) {
                // כאן נניח שיש לך את ה-EmployeeRepository שיכול להחזיר את העובד לפי ID
                JdbcEmployeeDAO empDAO = new JdbcEmployeeDAO();
                Employee emp = empDAO.findById(empId.trim())
                        .map(s -> {
                            try {
                                return EmployeeRepository.toEntity(s);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .orElse(null);
                if (emp != null) {
                    employees.add(emp);
                }
            }
        }

        return new Branch(dto.getId(), dto.getName(), dto.getAddress(), employees);
    }
}

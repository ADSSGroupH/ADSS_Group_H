package DomainLayer.HR.Repositories;

import DTO.HR.RoleDTO;
import Dal.HR.JdbcRoleDAO;
import DomainLayer.HR.Role;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class RoleRepository {
    private static RoleRepository instance;

    private Map<String, Role> roleMap;
    private JdbcRoleDAO jdbcRoleDAO;

    private RoleRepository() {
        this.roleMap = new HashMap<>();
        this.jdbcRoleDAO = new JdbcRoleDAO();
    }

    public static RoleRepository getInstance() {
        if (instance == null) {
            synchronized (RoleRepository.class) {
                if (instance == null) {
                    instance = new RoleRepository();
                }
            }
        }
        return instance;
    }

    // המרות בין Entity ל-DTO:
    public static RoleDTO fromEntity(Role role) {
        if (role == null) {
            return null;
        }
        return new RoleDTO(role.getId(), role.getName());
    }

    public static Role toEntity(RoleDTO roleDTO) {
        if (roleDTO == null) {
            return null;
        }
        return new Role(roleDTO.getId(), roleDTO.getName());
    }

    public void addRole(Role role) {
        roleMap.put(role.getId(), role);
        try {
            RoleDTO roleDTO = fromEntity(role);
            jdbcRoleDAO.save(roleDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateRole(Role role) {
        roleMap.put(role.getId(), role);
        try {
            RoleDTO roleDTO = fromEntity(role);
            jdbcRoleDAO.update(roleDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Role getRole(String id) {
        if (roleMap.containsKey(id)) {
            return roleMap.get(id);
        }
        try {
            Optional<RoleDTO> tempRole = jdbcRoleDAO.findById(id);
            if (!tempRole.isPresent()) {
                return null;
            }
            Role role = toEntity(tempRole.get());
            roleMap.put(id, role);
            return role;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Role getRoleByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }

        String trimmedName = name.trim();

        // חיפוש בקאש
        for (Role role : roleMap.values()) {
            if (role.getName().equalsIgnoreCase(trimmedName)) {
                return role;
            }
        }

        // חיפוש בבסיס הנתונים
        try {
            Optional<RoleDTO> tempRole = jdbcRoleDAO.findByName(trimmedName);
            if (tempRole.isPresent()) {
                Role role = toEntity(tempRole.get());
                roleMap.put(role.getId(), role);
                return role;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Role> getAllRoles() {
        try {
            List<RoleDTO> roleDTOs = jdbcRoleDAO.findAll();
            return roleDTOs.stream()
                    .map(dto -> {
                        Role role = toEntity(dto);
                        roleMap.put(role.getId(), role);
                        return role;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public List<Role> getActiveRoles() {
        try {
            List<RoleDTO> roleDTOs = jdbcRoleDAO.findActiveRoles();
            return roleDTOs.stream()
                    .map(dto -> {
                        Role role = toEntity(dto);
                        roleMap.put(role.getId(), role);
                        return role;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public void removeRole(String id) {
        roleMap.remove(id);
        try {
            jdbcRoleDAO.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean roleExists(String id) {
        if (roleMap.containsKey(id)) {
            return true;
        }
        try {
            return jdbcRoleDAO.exists(id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean roleExistsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        String trimmedName = name.trim();

        // בדיקה בקאש
        for (Role role : roleMap.values()) {
            if (role.getName().equalsIgnoreCase(trimmedName)) {
                return true;
            }
        }

        // בדיקה בבסיס הנתונים
        try {
            return jdbcRoleDAO.existsByName(trimmedName);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getRoleCount() {
        try {
            return jdbcRoleDAO.getRoleCount();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

}

package DomainLayer.Repositories;

import DomainLayer.*;
import java.util.*;

public class RoleRepository {
    private static RoleRepository instance;

    private Map<String, Role> roles;
    private DAO dao;

    private RoleRepository() {
        roles = new HashMap<>();
        dao = new DAO();  // ב-DAO שלך השדות סטטיים, אז אפשר גם לוותר על זה
        loadRolesFromDAO();
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

    private void loadRolesFromDAO() {
        for (Role role : DAO.roles) {
            roles.put(role.getId(), role);
        }
    }

    public void addRole(Role newRole) {
        if (!roles.containsKey(newRole.getId())) {
            roles.put(newRole.getId(), newRole);
            DAO.roles.add(newRole); // סנכרון ל-DAO
        }
    }

    public void updateRole(Role updatedRole) {
        if (updatedRole == null || updatedRole.getId() == null) return;

        if (roles.containsKey(updatedRole.getId())) {
            roles.put(updatedRole.getId(), updatedRole);
            // עדכון ב-DAO: מחליף את האובייקט הישן ברשימה
            for (int i = 0; i < DAO.roles.size(); i++) {
                if (DAO.roles.get(i).getId().equals(updatedRole.getId())) {
                    DAO.roles.set(i, updatedRole);
                    break;
                }
            }
        }
    }

    public Role getRoleById(String id) {
        if (roles.containsKey(id)) {
            return roles.get(id);
        }
        // טען מה-DAO במקרה והקאש לא מעודכן
        for (Role role : DAO.roles) {
            if (role.getId().equals(id)) {
                roles.put(id, role);
                return role;
            }
        }
        return null;
    }

    public List<Role> getAllRoles() {
        return new ArrayList<>(roles.values());
    }

    public void deleteRole(String id) {
        // אם רוצים סימון ארכיון, אפשר להוסיף שדה ב-Role ואז לסמן אותו כאן
        // כרגע נמחק פשוט מהרשימה וה-DAO
        Role role = roles.get(id);
        if (role != null) {
            roles.remove(id);
            DAO.roles.removeIf(r -> r.getId().equals(id));
        }
    }


    public Role getRoleByName(String roleName) {
        if (roleName == null || roleName.trim().isEmpty()) {
            return null;
        }

        String trimmedRoleName = roleName.trim();

        // חיפוש ב-cache (המפה המקומית)
        for (Role role : roles.values()) {
            if (role.getName().equalsIgnoreCase(trimmedRoleName)) {
                return role;
            }
        }

        // אם לא נמצא ב-cache, חפש ב-DAO במקרה והקאש לא מעודכן
        for (Role role : DAO.roles) {
            if (role.getName().equalsIgnoreCase(trimmedRoleName)) {
                // עדכן את ה-cache
                roles.put(role.getId(), role);
                return role;
            }
        }

        return null; // תפקיד לא נמצא
    }
}

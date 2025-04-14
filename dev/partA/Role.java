public class Role {
    private String roleId;
    private String name;

    // --- Constructor ---
    public Role(String roleId, String name) {
        this.roleId = roleId;
        this.name = name;
    }

    // --- Getters ---
    public String getId() {
        return roleId;
    }
    public String getName() {
        return name;
    }

    // --- Setters ---
    public void setId(String roleId) {
        this.roleId = roleId;
    }
    public void setName(String name) {
        this.name = name;
    }
}

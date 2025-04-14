public class EmployeeContract {
    private Employee employee;
    private String startDate;
    private int freeDays;
    private int sicknessDays;
    private int monthlyWorkHours;
    private String socialContributions;
    private String advancedStudyFund;
    private int salary;
    private String archivedAt;
    private boolean isArchived;

    // --- Constructor ---
    public EmployeeContract(Employee employee, String startDate, int freeDays, int sicknessDays,
                            int monthlyWorkHours, String socialContributions,
                            String advancedStudyFund, int salary,
                            String archivedAt, boolean isArchived) {
        this.employee = employee;
        this.startDate = startDate;
        this.freeDays = freeDays;
        this.sicknessDays = sicknessDays;
        this.monthlyWorkHours = monthlyWorkHours;
        this.socialContributions = socialContributions;
        this.advancedStudyFund = advancedStudyFund;
        this.salary = salary;
        this.archivedAt = archivedAt;
        this.isArchived = isArchived;
    }

    // --- Getters and Setters ---
    public Employee getEmployee() {
        return employee;
    }
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getStartDate() {
        return startDate;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getFreeDays() {
        return freeDays;
    }
    public void setFreeDays(int freeDays) {
        this.freeDays = freeDays;
    }

    public int getSicknessDays() {
        return sicknessDays;
    }
    public void setSicknessDays(int sicknessDays) {
        this.sicknessDays = sicknessDays;
    }

    public int getMonthlyWorkHours() {
        return monthlyWorkHours;
    }
    public void setMonthlyWorkHours(int monthlyWorkHours) {
        this.monthlyWorkHours = monthlyWorkHours;
    }

    public String getSocialContributions() {
        return socialContributions;
    }
    public void setSocialContributions(String socialContributions) {
        this.socialContributions = socialContributions;
    }

    public String getAdvancedStudyFund() {
        return advancedStudyFund;
    }
    public void setAdvancedStudyFund(String advancedStudyFund) {
        this.advancedStudyFund = advancedStudyFund;
    }

    public int getSalary() {
        return salary;
    }
    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getArchivedAt() {
        return archivedAt;
    }
    public void setArchivedAt(String archivedAt) {
        this.archivedAt = archivedAt;
    }

    public boolean isArchived() {
        return isArchived;
    }
    public void setArchived(boolean archived) {
        isArchived = archived;
    }
}

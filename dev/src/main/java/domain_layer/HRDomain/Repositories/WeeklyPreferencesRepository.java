package DomainLayer.Repositories;

import DomainLayer.Shift;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeeklyPreferencesRepository {

    private static WeeklyPreferencesRepository instance;

    private final Map<String, List<Shift>> weeklyPreferences;

    private WeeklyPreferencesRepository() {
        this.weeklyPreferences = new HashMap<>();
    }

    public static WeeklyPreferencesRepository getInstance() {
        if (instance == null) {
            instance = new WeeklyPreferencesRepository();
        }
        return instance;
    }

    public void addWeeklyPreferences(String employeeId, List<Shift> shifts) {
        weeklyPreferences.put(employeeId, shifts);
    }

    public List<Shift> getWeeklyPreferencesByEmployeeId(String employeeId) {
        return weeklyPreferences.get(employeeId);
    }

    public void updateWeeklyPreferences(String employeeId, List<Shift> shifts) {
        weeklyPreferences.put(employeeId, shifts);
    }

    public void removeWeeklyPreferences(String employeeId) {
        weeklyPreferences.remove(employeeId);
    }

    public Map<String, List<Shift>> getAllWeeklyPreferences() {
        return new HashMap<>(weeklyPreferences);
    }
}

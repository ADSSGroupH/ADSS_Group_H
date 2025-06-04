package DomainLayer.HR.Repositories;

import DTO.HR.ShiftDTO;
import DTO.HR.WeeklyPreferencesDTO;
import Dal.HR.JdbcShiftDAO;
import Dal.HR.JdbcWeeklyPreferencesDAO;
import DomainLayer.HR.Shift;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class WeeklyPreferencesRepository {
    private static WeeklyPreferencesRepository instance;

    // קאש בזיכרון: employeeId -> List<Shift>
    private final Map<String, List<Shift>> weeklyPreferences;
    private final JdbcShiftDAO shiftDAO;

    // DAO לעבודה עם בסיס הנתונים
    private JdbcWeeklyPreferencesDAO jdbcWeeklyPreferencesDAO;

    private ShiftRepository shiftRepository;


    private WeeklyPreferencesRepository() {
        this.weeklyPreferences = new HashMap<>();
        this.jdbcWeeklyPreferencesDAO = new JdbcWeeklyPreferencesDAO();
        this.shiftDAO = new JdbcShiftDAO();
        this.shiftRepository = ShiftRepository.getInstance();
    }

    public static WeeklyPreferencesRepository getInstance() {
        if (instance == null) {
            synchronized (WeeklyPreferencesRepository.class) {
                if (instance == null) {
                    instance = new WeeklyPreferencesRepository();
                }
            }
        }
        return instance;
    }

    public void addWeeklyPreferences(String employeeId, List<Shift> shifts, LocalDate weekStartDate,String status) {
        weeklyPreferences.put(employeeId, shifts);

        try {
            WeeklyPreferencesDTO dto = fromEntity(employeeId, shifts, weekStartDate,status);
            if (jdbcWeeklyPreferencesDAO.exists(employeeId, String.valueOf(weekStartDate))) {
                jdbcWeeklyPreferencesDAO.update(dto);
            } else {
                jdbcWeeklyPreferencesDAO.save(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Shift> getWeeklyPreferencesByEmployeeId(String employeeId, LocalDate weekStartDate) {
        // בדיקה בקאש
        if (weeklyPreferences.containsKey(employeeId)) {
            return weeklyPreferences.get(employeeId);
        }

        try {
            Optional<WeeklyPreferencesDTO> optionalDto = jdbcWeeklyPreferencesDAO.findByEmployeeAndWeek(employeeId, String.valueOf(weekStartDate));
            if (optionalDto.isPresent()) {
                WeeklyPreferencesDTO dto = optionalDto.get();
                List<Shift> shifts = loadShiftsFromDTO(dto);
                weeklyPreferences.put(employeeId, shifts);
                return shifts;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return List.of();
    }

    public void updateWeeklyPreferences(String employeeId, List<Shift> shifts, LocalDate weekStartDate, String status) {
        addWeeklyPreferences(employeeId, shifts, weekStartDate, status); // זהה לפונקציית הוספה
    }

    public void removeWeeklyPreferences(String employeeId, LocalDate weekStartDate) {
        weeklyPreferences.remove(employeeId);

        try {
            jdbcWeeklyPreferencesDAO.delete(employeeId, String.valueOf(weekStartDate));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, List<Shift>> getAllWeeklyPreferences() throws SQLException {
        List<WeeklyPreferencesDTO> allPreferences = jdbcWeeklyPreferencesDAO.findAll();
        Map<String, List<Shift>> result = new HashMap<>();

        for (WeeklyPreferencesDTO pref : allPreferences) {
            String employeeId = pref.getEmployeeId();
            List<String> shiftIds = pref.getPreferredShiftIds();

            List<Shift> shifts = shiftIds.stream()
                    .map(id -> shiftRepository.getShift(id))
                    .filter(Objects::nonNull) // מסנן מזהים שלא קיימים
                    .collect(Collectors.toList());

            result.put(employeeId, shifts);
        }

        return result;
    }


    // פונקציות נוספות לעבודה עם בסיס הנתונים
    public List<WeeklyPreferencesDTO> getWeeklyPreferencesForWeek(LocalDate weekStartDate) {
        try {
            return jdbcWeeklyPreferencesDAO.findByWeek(String.valueOf(weekStartDate));
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public List<WeeklyPreferencesDTO> getPreferencesByStatus(String status) {
        try {
            return jdbcWeeklyPreferencesDAO.findByStatus(status);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public Optional<WeeklyPreferencesDTO> getEmployeePreferencesForWeek(String employeeId, LocalDate weekStartDate) {
        try {
            return jdbcWeeklyPreferencesDAO.findByEmployeeAndWeek(employeeId, String.valueOf(weekStartDate));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public void submitPreferences(String employeeId, LocalDate weekStartDate) {
        try {
            Optional<WeeklyPreferencesDTO> preferences = jdbcWeeklyPreferencesDAO.findByEmployeeAndWeek(employeeId, String.valueOf(weekStartDate));
            if (preferences.isPresent()) {
                WeeklyPreferencesDTO dto = preferences.get();
                dto.setStatus("SUBMITTED");
                jdbcWeeklyPreferencesDAO.update(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void approvePreferences(String employeeId, LocalDate weekStartDate) {
        try {
            Optional<WeeklyPreferencesDTO> preferences = jdbcWeeklyPreferencesDAO.findByEmployeeAndWeek(employeeId, String.valueOf(weekStartDate));
            if (preferences.isPresent()) {
                WeeklyPreferencesDTO dto = preferences.get();
                dto.setStatus("APPROVED");
                jdbcWeeklyPreferencesDAO.update(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<WeeklyPreferencesDTO> getAllEmployeePreferences(String employeeId) {
        try {
            return jdbcWeeklyPreferencesDAO.findByEmployee(employeeId);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    // פונקציות עזר
    private WeeklyPreferencesDTO createDTOFromShifts(String employeeId, List<Shift> shifts, LocalDate weekStartDate) {
        WeeklyPreferencesDTO dto = new WeeklyPreferencesDTO();
        dto.setEmployeeId(employeeId);
        dto.setPreferredShiftIds(shifts.stream()
                .map(Shift::getId)
                .collect(Collectors.toList())
        );
        dto.setWeekStartDate(weekStartDate.toString());
        dto.setCreatedAt(java.time.LocalDate.now().toString());
        dto.setLastModified(java.time.LocalDate.now().toString());
        dto.setStatus("DRAFT");
        return dto;
    }

    private List<Shift> loadShiftsFromDTO(WeeklyPreferencesDTO dto) {
        JdbcShiftDAO shiftDAO = new JdbcShiftDAO();

        return dto.getPreferredShiftIds().stream()
                .map(id -> {
                    try {
                        return shiftDAO.findById(id)
                                .map(e -> {
                                    try {
                                        return ShiftRepository.toEntity(e);
                                    } catch (SQLException ex) {
                                        throw new RuntimeException(ex);
                                    }
                                })
                                .orElse(null);
                    } catch (SQLException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(shift -> shift != null)
                .collect(Collectors.toList());
    }

    // פונקציות לטעינת נתונים מבסיס הנתונים לקאש
    public void loadAllPreferencesToCache() {
        try {
            List<WeeklyPreferencesDTO> allPreferences = jdbcWeeklyPreferencesDAO.findAll();
            for (WeeklyPreferencesDTO dto : allPreferences) {
                if ("APPROVED".equals(dto.getStatus())) { // טוען רק עדפות מאושרות לקאש
                    List<Shift> shifts = loadShiftsFromDTO(dto);
                    weeklyPreferences.put(dto.getEmployeeId(), shifts);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearCache() {
        weeklyPreferences.clear();
    }

    public static Map.Entry<String, List<Shift>> toEntity(WeeklyPreferencesDTO dto) throws SQLException {
        String employeeId = dto.getEmployeeId();

        List<Shift> shifts = new ArrayList<>();
        JdbcShiftDAO shiftDAO = new JdbcShiftDAO();
        for (String id : dto.getPreferredShiftIds()) {
            Shift shift = shiftDAO.findById(id)
                    .map(e -> {
                        try {
                            return ShiftRepository.toEntity(e);
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    })
                    .orElse(null);
            if (shift != null) {
                shifts.add(shift);
            }
        }
        return new AbstractMap.SimpleEntry<>(employeeId, shifts);
    }


    /**
     * ממיר Entity ל-DTO, כולל פרטי שבוע וסטטוס
     */
    public WeeklyPreferencesDTO fromEntity(String employeeId, List<Shift> shifts, LocalDate weekStartDate, String status) {
        WeeklyPreferencesDTO dto = new WeeklyPreferencesDTO();
        dto.setEmployeeId(employeeId);

        // המר את רשימת המשמרות למחרוזת IDs מופרדת בפסיקים
        String preferredShiftIdsCsv = shifts.stream()
                .map(Shift::getId)
                .collect(Collectors.joining(","));
        dto.setPreferredShiftIdsCsv(preferredShiftIdsCsv);

        dto.setWeekStartDate(weekStartDate.toString());
        dto.setStatus(status);

        return dto;
    }

}

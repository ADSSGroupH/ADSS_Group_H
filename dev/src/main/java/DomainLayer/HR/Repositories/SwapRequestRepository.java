package DomainLayer.HR.Repositories;

import DTO.HR.ShiftSwapRequestDTO;
import Dal.HR.JdbcSwapRequestDAO;
import DomainLayer.HR.Employee;
import DomainLayer.HR.Shift;
import DomainLayer.HR.ShiftSwapRequest;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class SwapRequestRepository {
    private static SwapRequestRepository instance;

    private Map<String, ShiftSwapRequest> swapRequestMap;
    private JdbcSwapRequestDAO jdbcSwapRequestDAO;

    private SwapRequestRepository() {
        this.swapRequestMap = new HashMap<>();
        this.jdbcSwapRequestDAO = new JdbcSwapRequestDAO();
    }

    public static SwapRequestRepository getInstance() {
        if (instance == null) {
            synchronized (SwapRequestRepository.class) {
                if (instance == null) {
                    instance = new SwapRequestRepository();
                }
            }
        }
        return instance;
    }

    private ShiftSwapRequestDTO fromEntity(ShiftSwapRequest request) {
        return new ShiftSwapRequestDTO(
                request.getId(),
                request.getRequester().getId(),           // ממיר Employee ל-string id
                request.getFromShift().getId(),           // ממיר Shift ל-string id
                request.getToShift().getId(),             // ממיר Shift ל-string id
                request.getStatus().name(),                // ממיר enum ל-string
                request.getDate() != null ? request.getDate().toString() : null,               // ממיר LocalDate ל-string
                request.getArchivedAt() != null ? request.getArchivedAt().toString() : null,   // ממיר LocalDate ל-string
                request.isArchived()                      // boolean ישיר
        );
    }

    private ShiftSwapRequest toEntity(ShiftSwapRequestDTO dto) {
        // כאן צריך לקבל את ה-Employee וה-Shift לפי ה-ids (ייתכן דרך ריפוזיטורי אחר)
        Employee requestor = EmployeeRepository.getInstance().getEmployeeById(dto.getRequestorId());
        Shift fromShift = ShiftRepository.getInstance().getShift(dto.getFromShiftId());
        Shift toShift = ShiftRepository.getInstance().getShift(dto.getToShiftId());

        ShiftSwapRequest.Status statusEnum = ShiftSwapRequest.Status.valueOf(dto.getStatus());

        ShiftSwapRequest request = new ShiftSwapRequest(
                dto.getId(),
                requestor,
                fromShift,
                toShift,
                dto.getDate() != null ? LocalDate.parse(dto.getDate()) : null
        );

        request.setStatus(statusEnum);
        request.setArchived(dto.isArchived());
        request.setArchivedAt(dto.getArchivedAt() != null ? LocalDate.parse(dto.getArchivedAt()) : null);

        return request;
    }


    public void addSwapRequest(ShiftSwapRequest swapRequest) {
        swapRequestMap.put(swapRequest.getId(), swapRequest);
        try {
            ShiftSwapRequestDTO swapRequestDTO = fromEntity(swapRequest);
            jdbcSwapRequestDAO.save(swapRequestDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateSwapRequest(ShiftSwapRequest swapRequest) {
        swapRequestMap.put(swapRequest.getId(), swapRequest);
        try {
            ShiftSwapRequestDTO swapRequestDTO = fromEntity(swapRequest);
            jdbcSwapRequestDAO.update(swapRequestDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ShiftSwapRequest getSwapRequestById(String id) {
        if (swapRequestMap.containsKey(id)) {
            return swapRequestMap.get(id);
        }
        try {
            Optional<ShiftSwapRequestDTO> tempSwapRequest = jdbcSwapRequestDAO.findById(id);
            if (!tempSwapRequest.isPresent()) {
                return null;
            }
            ShiftSwapRequest swapRequest = toEntity(tempSwapRequest.get());
            swapRequestMap.put(id, swapRequest);
            return swapRequest;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<ShiftSwapRequest> getAllSwapRequests() {
        try {
            List<ShiftSwapRequestDTO> swapRequestDTOs = jdbcSwapRequestDAO.findAll();
            return swapRequestDTOs.stream()
                    .map(this::toEntity)
                    .peek(swapRequest -> swapRequestMap.put(swapRequest.getId(), swapRequest))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public List<ShiftSwapRequest> getActiveSwapRequests() {
        try {
            List<ShiftSwapRequestDTO> swapRequestDTOs = jdbcSwapRequestDAO.findActiveRequests();
            return swapRequestDTOs.stream()
                    .map(this::toEntity)
                    .peek(swapRequest -> swapRequestMap.put(swapRequest.getId(), swapRequest))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public List<ShiftSwapRequest> getSwapRequestsByRequestor(String requestorId) {
        try {
            List<ShiftSwapRequestDTO> swapRequestDTOs = jdbcSwapRequestDAO.findByRequestor(requestorId);
            return swapRequestDTOs.stream()
                    .map(this::toEntity)
                    .peek(swapRequest -> swapRequestMap.put(swapRequest.getId(), swapRequest))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public List<ShiftSwapRequest> getSwapRequestsByStatus(ShiftSwapRequestDTO.Status status) {
        try {
            List<ShiftSwapRequestDTO> swapRequestDTOs = jdbcSwapRequestDAO.findByStatus(String.valueOf(status));
            return swapRequestDTOs.stream()
                    .map(this::toEntity)
                    .peek(swapRequest -> swapRequestMap.put(swapRequest.getId(), swapRequest))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public List<ShiftSwapRequest> getSwapRequestsByDateRange(String startDate, String endDate) {
        try {
            List<ShiftSwapRequestDTO> swapRequestDTOs = jdbcSwapRequestDAO.findByDateRange(startDate, endDate);
            return swapRequestDTOs.stream()
                    .map(this::toEntity)
                    .peek(swapRequest -> swapRequestMap.put(swapRequest.getId(), swapRequest))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public List<ShiftSwapRequest> getSwapRequestsByShift(String shiftId) {
        try {
            List<ShiftSwapRequestDTO> swapRequestDTOs = jdbcSwapRequestDAO.findByShift(shiftId);
            return swapRequestDTOs.stream()
                    .map(this::toEntity)
                    .peek(swapRequest -> swapRequestMap.put(swapRequest.getId(), swapRequest))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public List<ShiftSwapRequest> getPendingSwapRequests() {
        try {
            List<ShiftSwapRequestDTO> swapRequestDTOs = jdbcSwapRequestDAO.findPendingRequests();
            return swapRequestDTOs.stream()
                    .map(this::toEntity)
                    .peek(swapRequest -> swapRequestMap.put(swapRequest.getId(), swapRequest))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public void removeSwapRequest(String id) {
        swapRequestMap.remove(id);
        try {
            jdbcSwapRequestDAO.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean swapRequestExists(String id) {
        if (swapRequestMap.containsKey(id)) {
            return true;
        }
        try {
            return jdbcSwapRequestDAO.exists(id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getSwapRequestCount() {
        try {
            return jdbcSwapRequestDAO.getRequestCount();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getSwapRequestCountByStatus(ShiftSwapRequestDTO.Status status) {
        try {
            return jdbcSwapRequestDAO.getRequestCountByStatus(String.valueOf(status));
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}

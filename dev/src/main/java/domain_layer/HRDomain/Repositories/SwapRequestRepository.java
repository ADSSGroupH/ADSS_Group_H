package DomainLayer.Repositories;

import DomainLayer.*;
import java.util.*;

public class SwapRequestRepository {
    private static SwapRequestRepository instance;

    private Map<String, ShiftSwapRequest> swapRequests;
    private DAO dao;

    private SwapRequestRepository() {
        swapRequests = new HashMap<>();
        dao = new DAO();
        loadSwapRequestsFromDAO();
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

    private void loadSwapRequestsFromDAO() {
        for (ShiftSwapRequest request : DAO.swapRequests) {
            swapRequests.put(request.getId(), request);
        }
    }

    public void addSwapRequest(ShiftSwapRequest newRequest) {
        if (!swapRequests.containsKey(newRequest.getId())) {
            swapRequests.put(newRequest.getId(), newRequest);
            DAO.swapRequests.add(newRequest); // סנכרון ל-DAO
        }
    }

    public void updateSwapRequest(ShiftSwapRequest updatedRequest) {
        if (updatedRequest == null || updatedRequest.getId() == null) return;

        if (swapRequests.containsKey(updatedRequest.getId())) {
            swapRequests.put(updatedRequest.getId(), updatedRequest);
            // עדכון ב-DAO: מחליף את האובייקט הישן ברשימה
            for (int i = 0; i < DAO.swapRequests.size(); i++) {
                if (DAO.swapRequests.get(i).getId().equals(updatedRequest.getId())) {
                    DAO.swapRequests.set(i, updatedRequest);
                    break;
                }
            }
        }
    }

    public ShiftSwapRequest getSwapRequestById(String id) {
        if (swapRequests.containsKey(id)) {
            return swapRequests.get(id);
        }
        // טען מה-DAO במקרה והקאש לא מעודכן
        for (ShiftSwapRequest req : DAO.swapRequests) {
            if (req.getId().equals(id)) {
                swapRequests.put(id, req);
                return req;
            }
        }
        return null;
    }

    public List<ShiftSwapRequest> getAllSwapRequests() {
        return new ArrayList<>(swapRequests.values());
    }

    public void deleteSwapRequest(String id) {
        ShiftSwapRequest req = swapRequests.get(id);
        if (req != null) {
            swapRequests.remove(id);
            DAO.swapRequests.removeIf(r -> r.getId().equals(id));
        }
    }
}

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShiftSwapRequestService {

    // יצירת בקשה חדשה ושמירה שלה ב-DataStore
    public ShiftSwapRequest createRequest(String id, Employee requestor, Shift fromShift, Shift toShift, String date) {
        // יצירת הבקשה החדשה
        ShiftSwapRequest newRequest = new ShiftSwapRequest(id, requestor, fromShift, toShift, date);

        // שמירה של הבקשה ב-DataStore
        DataStore.swapRequests.add(newRequest);  // הוספה לרשימת הבקשות ב-DataStore

        return newRequest;
    }

    // עדכון סטטוס של בקשה
    public boolean updateRequestStatus(String requestId, ShiftSwapRequest.Status status) {
        for (ShiftSwapRequest request : DataStore.swapRequests) {
            if (request.getId().equals(requestId)) {
                request.setStatus(status);
                return true;  // עדכון בוצע
            }
        }
        return false;  // לא נמצא בקשה עם מזהה זה
    }

    // ארכוב בקשה
    public boolean archiveRequest(String requestId, String archivedAt) {
        for (ShiftSwapRequest request : DataStore.swapRequests) {
            if (request.getId().equals(requestId)) {
                request.setArchived(true);
                request.setArchivedAt(archivedAt);
                return true;  // עדכון בוצע
            }
        }
        return false;  // לא נמצא בקשה עם מזהה זה
    }

    // מחיקת בקשה
    public boolean deleteRequest(String requestId) {
        Iterator<ShiftSwapRequest> iterator = DataStore.swapRequests.iterator();
        while (iterator.hasNext()) {
            ShiftSwapRequest request = iterator.next();
            if (request.getId().equals(requestId)) {
                iterator.remove();  // הסרת הבקשה מהרשימה
                return true;
            }
        }
        return false;  // לא נמצא בקשה עם מזהה זה
    }

    // חיפוש בקשה לפי ID
    public ShiftSwapRequest getRequestById(String requestId) {
        for (ShiftSwapRequest request : DataStore.swapRequests) {
            if (request.getId().equals(requestId)) {
                return request;  // חזר עם הבקשה אם נמצאה
            }
        }
        return null;  // אם לא נמצאה בקשה
    }

    // קבלת כל הבקשות
    public List<ShiftSwapRequest> getAllRequests() {
        return new ArrayList<>(DataStore.swapRequests);  // החזרת כל הבקשות ברשימה חדשה
    }

    // קבלת בקשות לפי סטטוס
    public List<ShiftSwapRequest> getRequestsByStatus(ShiftSwapRequest.Status status) {
        List<ShiftSwapRequest> filteredRequests = new ArrayList<>();
        for (ShiftSwapRequest request : DataStore.swapRequests) {
            if (request.getStatus() == status) {
                filteredRequests.add(request);
            }
        }
        return filteredRequests;  // החזרת בקשות לפי סטטוס
    }
}

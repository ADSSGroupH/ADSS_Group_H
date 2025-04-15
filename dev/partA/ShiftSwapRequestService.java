import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShiftSwapRequestService {

    public ShiftSwapRequest createRequest(String id, Employee requester, Shift fromShift, Shift toShift, String date) {
        // creating the request
        ShiftSwapRequest newRequest = new ShiftSwapRequest(id, requester, fromShift, toShift, date);

        // archiving the request
        this.archiveRequest(newRequest,date);

        return newRequest;
    }


    public boolean updateRequestStatus(String requestId, ShiftSwapRequest.Status status) { //updating a request status
        for (ShiftSwapRequest request : DataStore.swapRequests) {
            if (request.getId().equals(requestId)) { //found the request
                request.setStatus(status);
                return true;
            }
        }
        return false;  //didn't find a matching request
    }

    public void archiveRequest(ShiftSwapRequest request, String date_of_being_archived) {
                DataStore.swapRequests.add(request);
                request.setArchived(true);
                request.setArchivedAt(date_of_being_archived);

    }


    public boolean deleteRequest(String requestId) {
        Iterator<ShiftSwapRequest> iterator = DataStore.swapRequests.iterator();
        while (iterator.hasNext()) {
            ShiftSwapRequest request = iterator.next();
            if (request.getId().equals(requestId)) {
                iterator.remove();
                return true;
            }
        }
        return false;  // didn't find a matching request
    }


    public ShiftSwapRequest getRequestById(String requestId) { //searching request by id
        for (ShiftSwapRequest request : DataStore.swapRequests) {
            if (request.getId().equals(requestId)) {
                return request;
            }
        }
        return null;  // didn't find the request
    }


    public List<ShiftSwapRequest> getAllRequests() { //getting all the requests from the archive
        return new ArrayList<>(DataStore.swapRequests);
    }

    // getting all requests by status (approved, rejected,pending)
    public List<ShiftSwapRequest> getRequestsByStatus(ShiftSwapRequest.Status status) {
        List<ShiftSwapRequest> filteredRequests = new ArrayList<>();
        for (ShiftSwapRequest request : DataStore.swapRequests) {
            if (request.getStatus() == status) {
                filteredRequests.add(request);
            }
        }
        return filteredRequests;
    }
}

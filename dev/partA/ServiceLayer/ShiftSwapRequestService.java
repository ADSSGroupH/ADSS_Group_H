import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShiftSwapRequestService {

    public ShiftSwapRequest createRequest(String id, Employee requester, Shift fromShift, Shift toShift, LocalDate date) {
        // creating the request
        ShiftSwapRequest newRequest = new ShiftSwapRequest(id, requester, fromShift, toShift, date);

        // archiving the request
        this.archiveRequest(newRequest);

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

    public void archiveRequest(ShiftSwapRequest request) {
                DataStore.swapRequests.add(request);
                request.setArchived(true);
                request.setArchivedAt(LocalDate.now()); // entering the time of creating the request

    }


    public boolean deleteRequest(String requestId) {
        Iterator<ShiftSwapRequest> iterator = DataStore.swapRequests.iterator();
        while (iterator.hasNext()) {
            ShiftSwapRequest request = iterator.next();
            if (request.getId().equals(requestId)) {
                request.setArchived(true);
                request.setArchivedAt(LocalDate.now());
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

    public boolean applyApprovedRequest(ShiftSwapRequest request) {
        if (request.getStatus() != ShiftSwapRequest.Status.approved) {
            System.out.println("Request is not approved. Cannot apply.");
            return false;
        }

        Employee requester = request.getRequester();
        Shift fromShift = request.getFromShift();
        Shift toShift = request.getToShift();

        ShiftAssignment fromAssignment = null;
        ShiftAssignment toAssignment = null;

        // מחפשים את השיבוצים הנוכחיים
        for (ShiftAssignment assignment : DataStore.assignments) {
            if (assignment.getShift().getId().equals(fromShift.getId()) &&
                    assignment.getEmployee().getId().equals(requester.getId())) {
                fromAssignment = assignment;
            }

            if (assignment.getShift().getId().equals(toShift.getId())) {
                toAssignment = assignment;
            }
        }

        if (fromAssignment == null) {
            System.out.println("Requester is not assigned to the original shift.");
            return false;
        }

        // מחליפים בין העובדים
        if (toAssignment != null) {
            // שני העובדים קיימים – מבצעים החלפה
            Employee otherEmployee = toAssignment.getEmployee();

            fromAssignment.setEmployee(otherEmployee);
            toAssignment.setEmployee(requester);
        } else {
            // רק מבקש הבקשה קיים – הוא עובר למשמרת החדשה
            fromAssignment.setShift(toShift);
        }

        System.out.println("Shift swap applied successfully.");
        return true;
    }
}

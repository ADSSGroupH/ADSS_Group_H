package DomainLayer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShiftSwapRequestController {

    public ShiftSwapRequest createRequest(String id, Employee requester, Shift fromShift, Shift toShift, LocalDate date) {
        // creating the request
        ShiftSwapRequest newRequest = new ShiftSwapRequest(id, requester, fromShift, toShift, date);

        // archiving the request
        this.archiveRequest(newRequest);

        return newRequest;
    }


    public boolean updateRequestStatus(String requestId, ShiftSwapRequest.Status status) { //updating a request status
        for (ShiftSwapRequest request : DAO.swapRequests) {
            if (request.getId().equals(requestId)) { //found the request
                request.setStatus(status);
                return true;
            }
        }
        return false;  //didn't find a matching request
    }

    public void archiveRequest(ShiftSwapRequest request) {
        DAO.swapRequests.add(request);
        request.setArchived(true);
        request.setArchivedAt(LocalDate.now()); // entering the time of creating the request

    }


    public boolean deleteRequest(String requestId) {
        Iterator<ShiftSwapRequest> iterator = DAO.swapRequests.iterator();
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
        for (ShiftSwapRequest request : DAO.swapRequests) {
            if (request.getId().equals(requestId)) {
                return request;
            }
        }
        return null;  // didn't find the request
    }


    public List<ShiftSwapRequest> getAllRequests() { //getting all the requests from the archive
        return new ArrayList<>(DAO.swapRequests);
    }

    // getting all requests by status (approved, rejected,pending)
    public List<ShiftSwapRequest> getRequestsByStatus(ShiftSwapRequest.Status status) {
        List<ShiftSwapRequest> filteredRequests = new ArrayList<>();
        for (ShiftSwapRequest request : DAO.swapRequests) {
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

        for (ShiftAssignment assignment : DAO.assignments) {
            if (assignment.getShift().equals(fromShift) &&
                    assignment.getEmployee().getId().equals(requester.getId())) {
                fromAssignment = assignment;
                break;
            }
        }

        if (fromAssignment == null) {
            System.out.println("Requester is not assigned to the original shift.");
            return false;
        }

        Role targetRole = fromAssignment.getRole();

        // בדיקה שהעובד כשיר לתפקיד
        boolean requesterQualified = requester.getRoles().stream()
                .anyMatch(role -> role.getId().equals(targetRole.getId()));

        if (!requesterQualified) {
            System.out.printf("Cannot approve request: %s is not qualified for the role \"%s\" in the new shift.%n",
                    requester.getName(), targetRole.getName());
            return false;
        }

        // נבדוק אם יש שיבוץ קיים בתפקיד הזה במשמרת החדשה
        ShiftAssignment toAssignment = null;

        for (ShiftAssignment assignment : DAO.assignments) {
            if (assignment.getShift().equals(toShift) &&
                    assignment.getRole().getId().equals(targetRole.getId())) {
                toAssignment = assignment;
                break;
            }
        }

        if (toAssignment != null) {
            // החלפה מול עובד אחר
            Employee other = toAssignment.getEmployee();

            // בדיקה שגם הוא כשיר לתפקיד המקורי של מבקש ההחלפה
            boolean otherQualified = other.getRoles().stream()
                    .anyMatch(role -> role.getId().equals(targetRole.getId()));

            if (!otherQualified) {
                System.out.printf("Cannot swap: %s is not qualified for role \"%s\".%n",
                        other.getName(), targetRole.getName());
                return false;
            }

            fromAssignment.setEmployee(other);
            toAssignment.setEmployee(requester);
        } else {
            // אין שיבוץ בתפקיד – פשוט מעבירים את השיבוץ של המבקש
            fromAssignment.setShift(toShift);
            fromShift.getAssignments().remove(fromAssignment);
            toShift.getAssignments().add(fromAssignment);
        }

        System.out.println("Shift swap applied successfully.");
        return true;
    }

}

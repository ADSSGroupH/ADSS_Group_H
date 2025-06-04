package DomainLayer.HR.Controllers;

import DomainLayer.HR.*;
import DomainLayer.HR.Enums.ShiftSwapRequest_Status;
import DomainLayer.HR.Repositories.AssignmentRepository;
import DomainLayer.HR.Repositories.SwapRequestRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ShiftSwapRequestController {

    private SwapRequestRepository swapRequestRepository;
    private AssignmentRepository shiftAssignmentRepository;

    public ShiftSwapRequestController() {
        swapRequestRepository = SwapRequestRepository.getInstance();
        shiftAssignmentRepository = AssignmentRepository.getInstance();
    }

    public ShiftSwapRequest createRequest(String id, Employee requester, Shift fromShift, Shift toShift, LocalDate date) {
        ShiftSwapRequest newRequest = new ShiftSwapRequest(id, requester, fromShift, toShift, date);
        this.archiveRequest(newRequest);
        return newRequest;
    }

    public boolean updateRequestStatus(String requestId, ShiftSwapRequest.Status status) {
        ShiftSwapRequest request = swapRequestRepository.getSwapRequestById(requestId);
        if (request != null) {
            request.setStatus(status);
            swapRequestRepository.updateSwapRequest(request);
            return true;
        }
        return false;
    }

    public void archiveRequest(ShiftSwapRequest request) {
        request.setArchived(true);
        request.setArchivedAt(LocalDate.now());
        swapRequestRepository.addSwapRequest(request);
    }

    public boolean deleteRequest(String requestId) {
        ShiftSwapRequest request = swapRequestRepository.getSwapRequestById(requestId);
        if (request != null) {
            request.setArchived(true);
            request.setArchivedAt(LocalDate.now());
            swapRequestRepository.updateSwapRequest(request);
            return true;
        }
        return false;
    }

    public ShiftSwapRequest getRequestById(String requestId) {
        return swapRequestRepository.getSwapRequestById(requestId);
    }

    public List<ShiftSwapRequest> getAllRequests() {
        return swapRequestRepository.getAllSwapRequests();
    }

    public List<ShiftSwapRequest> getRequestsByStatus(ShiftSwapRequest.Status status) {
        List<ShiftSwapRequest> filteredRequests = new ArrayList<>();
        for (ShiftSwapRequest request : swapRequestRepository.getAllSwapRequests()) {
            if (request.getStatus() == status) {
                filteredRequests.add(request);
            }
        }
        return filteredRequests;
    }

    public ShiftSwapRequest_Status applyApprovedRequest(ShiftSwapRequest request) {
        if (request.getStatus() != ShiftSwapRequest.Status.approved) {
            return ShiftSwapRequest_Status.RequestNotApproved;
        }

        Employee requester = request.getRequester();
        Shift fromShift = request.getFromShift();
        Shift toShift = request.getToShift();

        ShiftAssignment fromAssignment = null;

        // כאן קריאה לריפוזיטורי של משימות שיבוץ
        for (ShiftAssignment assignment : shiftAssignmentRepository.getAllAssignments()) {
            if (assignment.getShiftId().equals(fromShift.getId()) && assignment.getEmployee().getId().equals(requester.getId())) {
                fromAssignment = assignment;
                break;
            }
        }

        if (fromAssignment == null) {
            return ShiftSwapRequest_Status.RequesterIsntAssignedToOgShift;
        }

        Role targetRole = fromAssignment.getRole();

        // בדיקה שהעובד כשיר לתפקיד
        boolean requesterQualified = requester.getRoles().stream().anyMatch(role -> role.getId().equals(targetRole.getId()));

        if (!requesterQualified) {
            return ShiftSwapRequest_Status.RequesterIsNotQualifiedForNewShift;
        }

        ShiftAssignment toAssignment = null;

        for (ShiftAssignment assignment : shiftAssignmentRepository.getAllAssignments()) {
            if (assignment.getShiftId().equals(toShift.getId()) && assignment.getRole().getId().equals(targetRole.getId())) {
                toAssignment = assignment;
                break;
            }
        }

        if (toAssignment != null) {
            Employee other = toAssignment.getEmployee();

            boolean otherQualified = other.getRoles().stream().anyMatch(role -> role.getId().equals(targetRole.getId()));

            if (!otherQualified) {
                return ShiftSwapRequest_Status.OtherIsNotQualifiedForRequesterRole;
            }

            fromAssignment.setEmployee(other);
            toAssignment.setEmployee(requester);

        } else {
            fromAssignment.setShift(toShift.getId());
            fromShift.getAssignments().remove(fromAssignment);
            toShift.getAssignments().add(fromAssignment);
        }

        return ShiftSwapRequest_Status.RequestApproved;
    }
}

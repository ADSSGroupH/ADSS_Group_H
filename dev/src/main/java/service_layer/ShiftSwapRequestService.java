package ServiceLayer;

import DomainLayer.HR.Controllers.ShiftSwapRequestController;
import DomainLayer.HR.Employee;
import DomainLayer.HR.Enums.ShiftSwapRequest_Status;
import DomainLayer.HR.Shift;
import DomainLayer.HR.ShiftSwapRequest;

import java.time.LocalDate;
import java.util.List;

public class ShiftSwapRequestService {
    ShiftSwapRequestController shiftSwapRequestController = new ShiftSwapRequestController();

    public ShiftSwapRequest createRequest(String id, Employee requester, Shift fromShift, Shift toShift, LocalDate date) {
        return shiftSwapRequestController.createRequest(id,requester,fromShift,toShift,date);
    }


    public boolean updateRequestStatus(String requestId, ShiftSwapRequest.Status status) {
        return shiftSwapRequestController.updateRequestStatus(requestId,status);
    }

    public void archiveRequest(ShiftSwapRequest request) {
        shiftSwapRequestController.archiveRequest(request);
    }


    public boolean deleteRequest(String requestId) {
        return  shiftSwapRequestController.deleteRequest(requestId);
    }


    public ShiftSwapRequest getRequestById(String requestId) {
        return shiftSwapRequestController.getRequestById(requestId);
    }


    public List<ShiftSwapRequest> getAllRequests() {
        return  shiftSwapRequestController.getAllRequests();
    }


    public List<ShiftSwapRequest> getRequestsByStatus(ShiftSwapRequest.Status status) {
        return shiftSwapRequestController.getRequestsByStatus(status);
    }

    public ShiftSwapRequest_Status applyApprovedRequest(ShiftSwapRequest request) {
        return shiftSwapRequestController.applyApprovedRequest(request);
    }
}

package ServiceLayer;

import DomainLayer.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
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

    public boolean applyApprovedRequest(ShiftSwapRequest request) {
        return shiftSwapRequestController.applyApprovedRequest(request);
    }
}

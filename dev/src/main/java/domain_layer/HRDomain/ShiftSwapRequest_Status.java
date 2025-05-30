package DomainLayer;

public enum ShiftSwapRequest_Status {
    RequestNotApproved,
    RequesterIsntAssignedToOgShift,
    RequesterIsNotQualifiedForNewShift,
    OtherIsNotQualifiedForRequesterRole,
    RequestApproved;
}

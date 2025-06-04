package DomainLayer.HR.Enums;

public enum ShiftSwapRequest_Status {
    RequestNotApproved,
    RequesterIsntAssignedToOgShift,
    RequesterIsNotQualifiedForNewShift,
    OtherIsNotQualifiedForRequesterRole,
    RequestApproved;
}

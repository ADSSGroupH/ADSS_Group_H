package DTO.Transportation;

public class SiteDTO {
    private String name;
    private int shipmentAreaId;
    private String address;
    private String phoneNumber;
    private String contactPersonName;
    private String branchOrSupplierId;


    public SiteDTO(String name, int shipmentAreaId, String address, String phoneNumber, String contactPersonName, String branchOrSupplierId) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.contactPersonName = contactPersonName;
        this.shipmentAreaId = shipmentAreaId;
        this.branchOrSupplierId = branchOrSupplierId;
    }

    public String getName() {
        return name;
    }

    public int getShipmentAreaId() {
        return shipmentAreaId;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getContactPersonName() {
        return contactPersonName;
    }

    public String getBranchOrSupplierId() {
        return branchOrSupplierId;
    }
}

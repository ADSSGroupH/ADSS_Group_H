package DomainLayer.Transportation;

import DTO.Transportation.SiteDTO;

public class Site {
    private String name;
    private String address;
    private String phoneNumber;
    private String contactPersonName;
    private int shipmentAreaId;
    private String branchOrSupplierId;

    public Site(String name, String address, String phoneNumber, String contactPersonName, int shipmentAreaId, String branchOrSupplierId) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.contactPersonName = contactPersonName;
        this.shipmentAreaId = shipmentAreaId;
        this.branchOrSupplierId = branchOrSupplierId;
    }
    public Site(SiteDTO siteDTO) {
        this.name = siteDTO.getName();
        this.address = siteDTO.getAddress();
        this.phoneNumber = siteDTO.getPhoneNumber();
        this.contactPersonName = siteDTO.getContactPersonName();
        this.shipmentAreaId = siteDTO.getShipmentAreaId();
        this.branchOrSupplierId = siteDTO.getBranchOrSupplierId();
    }
    public String getName() {
        return name;
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
    public int getShipmentAreaId() {
        return shipmentAreaId;
    }
    public String getBranchOrSupplierId() {
        return branchOrSupplierId;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void setContactPersonName(String contactPersonName) {
        this.contactPersonName = contactPersonName;
    }
    public void setShipmentAreaId(int shipmentAreaId) {
        this.shipmentAreaId = shipmentAreaId;
    }
    public void setBranchOrSupplierId(String branchOrSupplierId) {
        this.branchOrSupplierId = branchOrSupplierId;
    }

    public String display() {
        return "Site{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", contactPersonName='" + contactPersonName + '\'' +
                ", shipmentAreaId=" + shipmentAreaId +
                '}' + "\n";
    }
    public SiteDTO toDTO() {
        return new SiteDTO(name, shipmentAreaId, address, phoneNumber, contactPersonName, branchOrSupplierId);
    }
}

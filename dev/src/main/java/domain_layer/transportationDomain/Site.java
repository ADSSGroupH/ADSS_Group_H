package domain_layer.transportationDomain;

public class Site {
    private String name;
    private String address;
    private String phoneNumber;
    private String contactPersonName;
    private int shipmentAreaId;

    public Site(String name, String address, String phoneNumber, String contactPersonName, int shipmentAreaId) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.contactPersonName = contactPersonName;
        this.shipmentAreaId = shipmentAreaId;
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

    public String display() {
        return "Site{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", contactPersonName='" + contactPersonName + '\'' +
                ", shipmentAreaId=" + shipmentAreaId +
                '}' + "\n";
    }
}

package domain_layer.transportationDomain;

import dto_folder.LicenseType;

public class Driver {
    private String id;
    private LicenseType licenseType;
    private boolean occupied;

    public Driver(String id, LicenseType licenseType) {
        this.id = id;
        this.licenseType = licenseType;
        occupied = false;
    }

    public boolean isOccupied() {
        return occupied;
    }
    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }
    public LicenseType getLicenseType() {
        return licenseType;
    }
    public String getName() {
        return id;
    }

    public String display() {
        return "Driver{" +
                "id='" + id + '\'' +
                ", licenseType=" + licenseType +
                ", occupied=" + occupied +
                '}';
    }

    
}

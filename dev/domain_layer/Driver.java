package dev.domain_layer;

public class Driver {
    private String name;
    private String licenseType;
    private boolean occupied;

    public Driver(String name, String licenseType) {
        this.name = name;
        this.licenseType = licenseType;
        occupied = false;
    }

    public boolean isOccupied() {
        return occupied;
    }
    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }
    public String getLicenseType() {
        return licenseType;
    }
    public String getName() {
        return name;
    }

    
}

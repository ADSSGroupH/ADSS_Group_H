package domain_layer;

public class Driver {
    private String name;
    private LicenseType licenseType;
    private boolean occupied;
    public enum LicenseType {
        A, B, C, D, E
    }

    public Driver(String name, LicenseType licenseType) {
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
    public LicenseType getLicenseType() {
        return licenseType;
    }
    public String getName() {
        return name;
    }

    public String display() {
        return "Driver{" +
                "name='" + name + '\'' +
                ", licenseType=" + licenseType +
                ", occupied=" + occupied +
                '}';
    }

    
}

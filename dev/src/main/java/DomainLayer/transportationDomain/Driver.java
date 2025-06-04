package DomainLayer.transportationDomain;

import DTO.LicenseType;

public class Driver {
    private String id;
    private LicenseType licenseType;

    public Driver(String id, LicenseType licenseType) {
        this.id = id;
        this.licenseType = licenseType;
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
                '}';
    }

    
}

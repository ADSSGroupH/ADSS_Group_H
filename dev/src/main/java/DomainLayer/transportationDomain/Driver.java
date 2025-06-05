package DomainLayer.transportationDomain;

import DTO.LicenseType;

public class Driver {
    private String name;
    private LicenseType licenseType;

    public Driver(String name, LicenseType licenseType) {
        this.name = name;
        this.licenseType = licenseType;
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
                '}';
    }

    
}

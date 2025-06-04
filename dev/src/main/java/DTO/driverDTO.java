package DTO.Transportation;


import DTO.HR.EmployeeDTO;

public class driverDTO extends EmployeeDTO {
    private LicenseType licenseType;

    public driverDTO(LicenseType licenseType) {
        super(); // שוב, רק אם צריך
        this.licenseType = licenseType;
    }


     public LicenseType getLicenseType (){
         return licenseType;
     }

     public void setLicenseType(LicenseType licenseType){
        this.licenseType = licenseType;
     }
}

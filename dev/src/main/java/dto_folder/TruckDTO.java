package dto_folder;

public class TruckDTO {
    private String plateNumber;
    private String model;
    private int netWeight;
    private int maxWeight;
    private LicenseType licenseType;

    public TruckDTO(String plateNumber, String model, int netWeight, int maxWeight, LicenseType licenseType) {
        this.plateNumber = plateNumber;
        this.model = model;
        this.netWeight = netWeight;
        this.maxWeight = maxWeight;
        this.licenseType = licenseType;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public String getModel() {
        return model;
    }

    public int getNetWeight() {
        return netWeight;
    }

    public int getMaxWeight() {
        return maxWeight;
    }

    public LicenseType getLicenseType() {
        return licenseType;
    }

    
}

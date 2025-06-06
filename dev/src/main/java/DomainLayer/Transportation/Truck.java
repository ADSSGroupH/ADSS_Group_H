package DomainLayer.Transportation;


import DTO.Transportation.LicenseType;
import DTO.Transportation.TruckDTO;

public class Truck {
    private String plateNumber;
    private String model;
    private int netWeight;
    private int maxWeight;
    private LicenseType licenseType;

    public Truck(String plateNumber, String model, int netWeight, int maxWeight, LicenseType licenseType) {
        this.plateNumber = plateNumber;
        this.model = model;
        this.netWeight = netWeight;
        this.maxWeight = maxWeight;
        this.licenseType = licenseType;
    }

    public Truck(TruckDTO truckDTO) {
        this.plateNumber = truckDTO.getPlateNumber();
        this.model = truckDTO.getModel();
        this.netWeight = truckDTO.getNetWeight();
        this.maxWeight = truckDTO.getMaxWeight();
        this.licenseType = truckDTO.getLicenseType();
    }

    public LicenseType getLicenseType() {
        return licenseType;
    }

    public int getMaxWeight() {
        return maxWeight;
    }
    public int getNetWeight() {
        return netWeight;
    }
    public String getModel() {
        return model;
    }
    public String getPlateNumber() {
        return plateNumber;
    }

    public String display(){
        return "Truck{" +
                "plateNumber='" + plateNumber + '\'' +
                ", model='" + model + '\'' +
                ", netWeight=" + netWeight +
                ", maxWeight=" + maxWeight +
                ", licenseType=" + licenseType +
                '}';
    }

    public TruckDTO toDTO() {
        return new TruckDTO(plateNumber, model, netWeight, maxWeight, licenseType);
    }
}
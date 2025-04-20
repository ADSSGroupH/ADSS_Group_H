package dev.domain_layer;

public class Truck {
    private String plateNumber;
    private String model;
    private int netWeight;
    private int maxWeight;
    private String licenseType;

    public Truck(String plateNumber, String model, int netWeight, int maxWeight, String licenseType) {
        this.plateNumber = plateNumber;
        this.model = model;
        this.netWeight = netWeight;
        this.maxWeight = maxWeight;
        this.licenseType = licenseType;
    }

    public String getLicenseType() {
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
}

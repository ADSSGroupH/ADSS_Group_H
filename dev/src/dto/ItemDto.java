package dto;

import java.util.Date;

/**
 * Data Transfer Object for Item entity
 */
public class ItemDto {
    private String iid;
    private String name;
    private String location;           // e.g., "WareHouse" or "Shelf"
    private Date expirationDate;
    private String classification;
    private String product;
    private boolean defect;

    public ItemDto(String iid,
                   String name,
                   String location,
                   Date expirationDate,
                   String classification,
                   String product,
                   boolean defect) {
        this.iid = iid;
        this.name = name;
        this.location = location;
        this.expirationDate = expirationDate;
        this.classification = classification;
        this.product = product;
        this.defect = defect;
    }

    // Getters and Setters
    public String getIid() { return iid; }
    public void setIid(String iid) { this.iid = iid; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Date getExpirationDate() { return expirationDate; }
    public void setExpirationDate(Date expirationDate) { this.expirationDate = expirationDate; }

    public String getClassification() { return classification; }

    public String getProduct() { return product; }

    public boolean isDefect() { return defect; }
    public void setDefect(boolean defect) { this.defect = defect; }}

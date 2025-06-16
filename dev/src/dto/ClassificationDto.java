package dto;

public class ClassificationDto {
    private String id;
    private String category;
    private String subcategory;
    private double size;

    public ClassificationDto(String id, String category, String subcategory, double size) {
        this.id = id;
        this.category = category;
        this.subcategory = subcategory;
        this.size = size;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }
    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public double getSize() {
        return size;
    }
    public void setSize(double size) {
        this.size = size;
    }}

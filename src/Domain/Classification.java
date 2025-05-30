package Domain;

public class Classification {
    private String id;
    private String category;
    private String subcategory;
    private double size;

    public Classification(String id, String category, String subcategory, double size) {
        this.id = id;
        this.category = category;
        this.subcategory = subcategory;
        this.size = size;
    }

    // Getters / Setters
    public String getId() { return id; }
    public String getCategory() { return category; }
    public String getSubcategory() { return subcategory; }
    public double getsize() { return size; }
}
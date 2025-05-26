package Domain;

import Data.ProductsDAO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductRepository {
    // Singleton instance
    private static ProductRepository instance;

    private ProductsDAO productsDAO;

    // Private constructor to prevent instantiation
    private ProductRepository() {
        // Private constructor to prevent instantiation
        productsDAO = ProductsDAO.getInstance();
    }

    // Method to get the singleton instance
    public static ProductRepository getInstance() {
        if (instance == null) {
            synchronized (ProductRepository.class) {
                if (instance == null) {
                    instance = new ProductRepository();
                }
            }
        }
        return instance;
    }

    // Method to add a product
    public void addProduct(String product) {
        //TODO:: implement
    }

    // Method to update discount
    public void updateDiscount(List<JsonObject> products_json_list, int discount_id) {
        Map<String, Object> fieldsAndValuesToUpdates = new HashMap<>() {{
            put("discount_id", discount_id);
        }};

        // Iterate over the products_json_list to update each product individually
        for (JsonObject json_item : products_json_list) {
            Map<String, Object> fieldsAndValuesConditions = new HashMap<>() {{
                put("product_number", json_item.get("product_number").getAsInt());
            }};
            productsDAO.update(fieldsAndValuesConditions, fieldsAndValuesToUpdates);
        }
    }

    // Method to search for a product
    public JsonObject search(int catalog_num) throws SQLException {
        return productsDAO.search(catalog_num);
    }

    public void incrementProductAmount(JsonObject json_item) throws SQLException {
        //search for current amount in item's catalog number
        JsonObject product_json = productsDAO.search(json_item.get("product_number").getAsInt());
        int curr_amount = product_json.get("current_amount").getAsInt();

        Map<String, Object> fieldsAndValuesConditions = new HashMap<>(){{put("product_number", json_item.get("product_number").getAsInt());}};
        Map<String, Object> fieldsAndValuesToUpdates = new HashMap<>(){{put("current_amount", curr_amount+1);}};
        productsDAO.update(fieldsAndValuesConditions, fieldsAndValuesToUpdates);
    }

    public void decrementProductAmount(JsonObject json_item) throws SQLException {
        //search for current amount in item's catalog number
        JsonObject product_json = productsDAO.search(json_item.get("product_number").getAsInt());
        int curr_amount = product_json.get("current_amount").getAsInt();

        Map<String, Object> fieldsAndValuesConditions = new HashMap<>(){{put("product_number", json_item.get("product_number").getAsInt());}};
        Map<String, Object> fieldsAndValuesToUpdates = new HashMap<>(){{put("current_amount", curr_amount-1);}};
        productsDAO.update(fieldsAndValuesConditions, fieldsAndValuesToUpdates);
    }

    public boolean checkForAmountAlert(JsonObject product_json) throws SQLException {
        int min_amount_for_alert = product_json.get("min_amount_for_alert").getAsInt();
        int current_amount = product_json.get("current_amount").getAsInt();

        if(current_amount < min_amount_for_alert){ //alert!!
            return true;
        }
        return false;
    }
}


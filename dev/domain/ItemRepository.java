package Domain;

import Data.IDAO;
import Data.ItemsDAO;
import Data.StoreDiscountDAO;
import com.google.gson.JsonObject;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class ItemRepository {
    // Singleton instance
    private static ItemRepository instance;

    private ProductRepository product_repo;
    private Map<Integer,Item> items;

    private ItemsDAO item_dao;

    // Private constructor to prevent instantiation
    private ItemRepository() {
        // Private constructor to prevent instantiation
        items = new HashMap<>();
        item_dao  = ItemsDAO.getInstance();
        product_repo = ProductRepository.getInstance();
    }

    // Method to get the singleton instance
    public static ItemRepository getInstance() {
        if (instance == null) {
            synchronized (ItemRepository.class) {
                if (instance == null) {
                    instance = new ItemRepository();
                }
            }
        }
        return instance;
    }

    // Method to add an item
    public void addItem(JsonObject item_json) throws SQLException {
        try {
            int item_id = item_json.get("id").getAsInt();

            if (!items.containsKey(item_id) && item_dao.search(item_id) == null) {
                String expiring_date = item_json.get("expiring_date").getAsString();
                String section = item_json.get("section").getAsString();
                int location = item_json.get("location").getAsInt();
                boolean isDefect = false; //default
                int supplier_dis = item_json.get("supplier_discount").getAsInt();
                double costPrice = item_json.get("cost_price").getAsDouble();
                double purchase_price; //calculate from product table with discount
                int product_number = item_json.get("product_number").getAsInt();

                //find store discount from product
                JsonObject product_object = product_repo.search(product_number);
                int discount_id = product_object.get("discount_id").getAsInt();
                JsonObject discount_json = StoreDiscountDAO.getInstance().search(discount_id);
                int store_discount = discount_json.get("discount").getAsInt();


                JsonObject product_json = product_repo.search(product_number);
                if (product_json != null) { //product exist

                    purchase_price = costPrice * (100 - store_discount) / 100;
                    String roundedPurchasePrice = String.format("%.2f", purchase_price);
                    purchase_price = Double.valueOf(roundedPurchasePrice);
                    item_json.addProperty("purchase_price", purchase_price); //add purchase price (round by 2)

                    //TODO: create item in cache and add to items list

                    /*Item item = new Item();
                    item.setItem_id(item_id);
                    item.setDefect(isDefect);
                    item.setCostPrice(costPrice);
                    item.setLocation(location == 0 ? Location.WareHouse : Location.interiorStore);
                    item.setSection(section.charAt(0));
                    item.setExpiring_date(Date.valueOf(expiring_date));
                    item.setSupplier_dis(supplier_dis);
                    item.setPurchase_price(purchase_price);
                    item.setProduct(product_json);

                    items.put(item_id, item); //add to item list in cache*/


                    item_dao.add(item_json); //add item to dataBase

                } else { //product does NOT exist
                    throw new IllegalArgumentException();
                }

            } else {
                throw new IllegalArgumentException();
            }
        }
        catch (Exception e){
            throw e;
        }
    }

    // Method to remove an item
    public void removeItem(int item_id) {
        item_dao.delete(item_id);

        //TODO: remove from cache
    }

    // Method to mark an item as defective
    public void markAsDefect(int item_id) throws Exception {
        try {
            //check if item exist
            if (item_dao.search(item_id) != null) {
                Map<String, Object> fieldsAndValuesConditions = new HashMap<>() {{
                    put("item_id", item_id);
                }};
                Map<String, Object> fieldsAndValuesToUpdate = new HashMap<>() {{
                    put("isDefect", true);
                }};
                item_dao.update(fieldsAndValuesConditions, fieldsAndValuesToUpdate);
            }
            else{
                throw new Exception("Item was not found");
            }
        }
        catch (Exception e){
            throw e;
        }
    }

    // Method to show item details
    public JsonObject showItemDetails(int item_id) throws SQLException {
        boolean inCache = false; //TODO: update cache checking!

        if(inCache){ //take from cache
            return null;
        }
        else{ //take from dataBase
            try {
                return item_dao.search(item_id);
            }
            catch (SQLException e){
                throw e;
            }
        }
    }

    // Method to generate defect report
    public List<JsonObject> makeDefectReport() throws SQLException {
        return item_dao.getAllDefectiveItems();
    }

    //updates purchase price for items that belongs to given products_numbers
    public void updatePurchasePrice(List<JsonObject> product_json_list, int discount){
        Map<String, Object> fieldsAndValuesConditions = new HashMap<>();
        List<JsonObject> item_json_list = new ArrayList<>();
        for (JsonObject productJson : product_json_list) {
            fieldsAndValuesConditions.put("product_number", productJson.get("product_number").getAsInt());
            item_json_list.addAll(item_dao.genericSearch(fieldsAndValuesConditions));
        }

        // Update the purchase price for each item in the item_json_list
        for (JsonObject itemJson : item_json_list) {
            Map<String, Object> updateConditions = new HashMap<>() {{
                put("item_id", itemJson.get("item_id").getAsInt());
            }};
            double discountedPrice = itemJson.get("costPrice").getAsDouble() * (1 - discount / 100.0);
            String roundedPurchasePrice = String.format("%.2f", discountedPrice);
            discountedPrice = Double.valueOf(roundedPurchasePrice);
            double finalDiscountedPrice = discountedPrice;
            Map<String, Object> updateValues = new HashMap<>() {{
                put("purchase_price", finalDiscountedPrice); //round 2
            }};
            item_dao.update(updateConditions, updateValues);
        }
    }
}

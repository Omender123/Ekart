package wrteam.ecommerce.app.model;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderTracker implements Serializable {

    public String username, id, user_id, order_id, product_variant_id, quantity, price, discount, sub_total, deliver_by, date_added, name, image, measurement, unit, status, statusdate, mobile, delivery_charge, payment_method, address, final_total, activeStatus, activeStatusDate;
    public ArrayList<OrderTracker> orderStatusArrayList;
    public ArrayList<OrderTracker> itemsList;

    public OrderTracker(String id, String user_id, String order_id, String product_variant_id, String quantity, String price, String discount,
                        String sub_total, String deliver_by, String date_added, String name, String image,
                        String measurement, String unit, String status, String statusdate,
                        ArrayList<OrderTracker> orderStatusArrayList, String mobile, String delivery_charge,
                        String payment_method, String address, String final_total, String username, ArrayList<OrderTracker> itemsList) {
        this.id = id;
        this.user_id = user_id;
        this.order_id = order_id;
        this.product_variant_id = product_variant_id;
        this.quantity = quantity;
        this.price = price;
        this.discount = discount;
        this.sub_total = sub_total;
        this.deliver_by = deliver_by;
        this.date_added = date_added;
        this.name = name;
        this.image = image;
        this.measurement = measurement;
        this.unit = unit;
        this.status = status;
        this.statusdate = statusdate;
        this.orderStatusArrayList = orderStatusArrayList;
        this.mobile = mobile;
        this.delivery_charge = delivery_charge;
        this.payment_method = payment_method;
        this.address = address;
        this.final_total = final_total;
        this.username = username;
        this.itemsList = itemsList;
    }

    public OrderTracker(String user_id, String order_id, String date_added, String status, String statusdate,
                        ArrayList<OrderTracker> orderStatusArrayList, String mobile,
                        String delivery_charge, String payment_method, String address,
                        String final_total, String username, ArrayList<OrderTracker> itemsList) {
        this.user_id = user_id;
        this.order_id = order_id;
        this.date_added = date_added;
        this.status = status;
        this.statusdate = statusdate;
        this.orderStatusArrayList = orderStatusArrayList;
        this.mobile = mobile;
        this.delivery_charge = delivery_charge;
        this.payment_method = payment_method;
        this.address = address;
        this.final_total = final_total;
        this.username = username;
        this.itemsList = itemsList;
    }

    public OrderTracker(String id, String order_id, String product_variant_id,
                        String quantity, String price, String discount,
                        String sub_total, String deliver_by, String name, String image,
                        String measurement, String unit, String payment_method, String activeStatus, String activeStatusDate, ArrayList<OrderTracker> orderStatusArrayList) {
        this.id = id;
        this.order_id = order_id;
        this.product_variant_id = product_variant_id;
        this.quantity = quantity;
        this.price = price;
        this.discount = discount;
        this.sub_total = sub_total;
        this.deliver_by = deliver_by;
        this.name = name;
        this.image = image;
        this.measurement = measurement;
        this.unit = unit;
        this.payment_method = payment_method;
        this.activeStatus = activeStatus;
        this.activeStatusDate = activeStatusDate;
        this.orderStatusArrayList = orderStatusArrayList;
    }

    public String getActiveStatus() {
        return activeStatus;
    }

    public String getActiveStatusDate() {
        return activeStatusDate;
    }

    public ArrayList<OrderTracker> getItemsList() {
        return itemsList;
    }

    public String getUsername() {
        return username;
    }

    public String getMobile() {
        return mobile;
    }

    public String getDelivery_charge() {
        return delivery_charge;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public String getAddress() {
        return address;
    }

    public String getFinal_total() {
        return final_total;
    }

    public ArrayList<OrderTracker> getOrderStatusArrayList() {
        return orderStatusArrayList;
    }

    public OrderTracker(String status, String statusdate) {
        this.status = status;
        this.statusdate = statusdate;
    }

    public String getId() {
        return id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getOrder_id() {
        return order_id;
    }


    public String getProduct_variant_id() {
        return product_variant_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getPrice() {
        return price;
    }

    public String getDiscount() {
        return discount;
    }

    public String getSub_total() {
        return sub_total;
    }

    public String getDeliver_by() {
        return deliver_by;
    }

    public String getDate_added() {
        return date_added;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getMeasurement() {
        return measurement;
    }

    public String getUnit() {
        return unit;
    }

    public String getStatus() {
        return status;
    }

    public String getStatusdate() {
        return statusdate;
    }
}

package wrteam.ecommerce.app.model;


public class City {
    private String city_name, city_id, pincode;

    /*  public City(String city_id, String city_name, String pincode) {
          this.city_name = city_name;
          this.city_id = city_id;
          this.pincode = pincode;
      }*/
    public City(String city_id, String city_name) {
        this.city_name = city_name;
        this.city_id = city_id;

    }

    public String getCity_name() {
        return city_name;
    }

    public String getCity_id() {
        return city_id;
    }

    public String getPincode() {
        return pincode;
    }

    @Override
    public String toString() {
        return city_name;
    }


}
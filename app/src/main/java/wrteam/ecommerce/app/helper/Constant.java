package wrteam.ecommerce.app.helper;

import com.android.volley.toolbox.ImageLoader;

public class Constant {


    public static String MAINBASEUrl = "https://ekart.wrteam.in/";

    public static String BaseUrl = MAINBASEUrl + "api/";
    public static ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    public static String FAQ_URL = MAINBASEUrl + "pages_web/faq.php";
    public static String DELIVERY_BOY_PAGE = MAINBASEUrl + "delivery-boy/";
    public static String SliderUrl = BaseUrl + "slider-images.php";
    public static String CategoryUrl = BaseUrl + "get-categories.php";
    public static String SubcategoryUrl = BaseUrl + "get-subcategories-by-category-id.php";
    public static String FeaturedProductUrl = BaseUrl + "sections.php";
    public static String RegisterUrl = BaseUrl + "user-registration.php";
    public static String PAPAL_URL = MAINBASEUrl + "paypal/create-payment.php";
    public static String LoginUrl = BaseUrl + "login.php";
    public static String OFFER_URL = BaseUrl + "offer-images.php";
    public static String PRODUCT_SEARCH_URL = BaseUrl + "products-search.php";
    public static String SETTING_URL = BaseUrl + "settings.php";
    public static String GET_PRODUCT_BY_CATE = BaseUrl + "get-products-by-category-id.php";
    public static String GET_PRODUCT_BY_SUB_CATE = BaseUrl + "get-products-by-subcategory-id.php";
    public static String GET_PRODUCT_DETAIL_URL = BaseUrl + "get-product-by-id.php";
    public static String CITY_URL = BaseUrl + "get-cities.php";
    public static String GET_AREA_BY_CITY = BaseUrl + "get-areas-by-city-id.php";
    public static String ORDERPROCESS_URL = BaseUrl + "order-process.php";
    public static String USER_DATA_URL = BaseUrl + "get-user-data.php";
    public static String PAYMENT_REQUEST_URL = BaseUrl + "payment-request.php";
    public static String GET_ALL_PRODUCT_URL = BaseUrl + "get-all-products.php";
    public static String PROMO_CODE_CHECK_URL = BaseUrl + "validate-promo-code.php";
    public static String VALIDATE_PROMO_CODE = "validate_promo_code";
    public static String DISCOUNTED_AMOUNT = "discounted_amount";
    public static String REGISTER_DEVICE = "register-device";
    public static String KEY_GET_ALL_PRODUCT = "get_all_products";
    public static String GET_WALLET_HISTORY = "get_payment_requests";
    public static String GET_WALLET_TRANSACTION = "get_wallet_transactions";
    public static String AccessKey = "accesskey";
    public static String AccessKeyVal = "90336";
    public static String GetVal = "1";
    public static String GET_OFFER_IMAGE = "get-offer-images";
    public static String GET_ALL_SECTIONS = "get-all-sections";
    public static String CANCELLED = "cancelled";
    public static String RETURNED = "returned";
    public static String GET_USER_DATA = "get_user_data";
    public static String KEY_BALANCE = "balance";
    public static String PAYMENT_REQUEST = "payment_request";
    public static String PAYMENT_TYPE = "payment_type";
    public static String PAYMENT_ADDRESS = "payment_address";
    public static String AMOUNT_REQUESTED = "amount_requested";
    public static String KEY_REFER_EARN_BONUS = "refer-earn-bonus";
    public static String KEY_MAX_EARN_AMOUNT = "max-refer-earn-amount";
    public static String KEY_MIN_WIDRAWAL = "minimum-withdrawal-amount";
    public static String KEY_WALLET_USED = "wallet_used";
    public static String KEY_WALLET_BALANCE = "wallet_balance";
    public static String KEY_VERSION_CODE = "current_version";
    public static String KEY_VERSION_NAME = "version_code_name";
    public static String KEY_MIN_VERSION_REQUIRED = "minimum_version_required";
    public static String KEY_UPDATE_STATUS = "is-version-system-on";
    public static String KEY_ORDER_RETURN_DAY_LIMIT = "max-product-return-days";
    public static String FIRST_NAME = "first_name";
    public static String LAST_NAME = "last_name";
    public static String PAYER_EMAIL = "payer_email";
    public static String ITEM_NAME = "item_name";
    public static String ITEM_NUMBER = "item_number";
    public static String UPDATE_ORDER_ITEM_STATUS = "update_order_item_status";
    public static String ORDER_ITEM_ID = "order_item_id";
    public static String PAYMENT_METHODS = "payment_methods";
    public static String PAY_M_KEY = "payumoney_merchant_key";
    public static String PAYU_M_ID = "payumoney_merchant_id";
    public static String PAYU_SALT = "payumoney_salt";
    public static String share_url = MAINBASEUrl;
    public static String REFER_EARN_BONUS = "";
    public static String MAX_EARN_AMOUNT = "";
    public static String MIN_EARN_ORDER = "";
    public static String CITY_NAME = "city_name";
    public static String CITY_ID = "city_id";
    public static String AREA_NAME = "area_name";
    public static String AREA_ID = "area_id";
    public static String REFERRAL_CODE = "referral_code";
    public static String FRIENDS_CODE = "friend_code";

    public static String VERSION_CODE;

    public static String REQUIRED_VERSION;
    public static String VERSION_STATUS;

    public static String SOLDOUT_TEXT = "Sold Out";
    public static int GRIDCOLUMN = 2;
    public static String LOAD_ITEM_LIMIT = "10";
    public static String ITEM_LIMIT = "10";
    public static int RELATEDPRODUCT_DISPLAY_COUNT = 6;
    public static int MAX_PRODUCT_LIMIT = 25;
    public static String SORT = "sort";
    public static String TYPE = "type";
    public static String IMAGE = "image";
    public static String NAME = "name";
    public static String TYPE_ID = "type_id";
    public static String ID = "id";
    public static String SUBTITLE = "subtitle";
    public static String PRODUCTS = "products";
    public static String SUC_CATE_ID = "subcategory_id";
    public static String DESCRIPTION = "description";
    public static String STATUS = "status";
    public static String DATE_ADDED = "date_added";
    public static String TITLE = "title";
    public static String SECTION_STYLE = "style";
    public static String SHORT_DESC = "short_description";
    public static String REGISTER = "register";
    public static String EMAIL = "email";
    public static String MOBILE = "mobile";
    public static String PASSWORD = "password";
    public static String FCM_ID = "fcm_id";
    public static String CITY = "city";
    public static String AREA = "area";
    public static String STREET = "street";
    public static String PINCODE = "pincode";
    public static String ERROR = "error";
    public static String VERIFY_USER = "verify-user";
    public static String VERIFY_EMAIL = "verify-user-email";
    public static String GET_SLIDER_IMAGE = "get-slider-images";
    public static String USER_ID = "user_id";
    public static String DOB = "dob";
    public static String CREATEDATE = "created_at";
    public static String DATE_CREATED = "date_created";
    public static String APIKEY = "apikey";
    public static String clickfreg = "";
    public static String ToolbarTitle = "";
    public static String backfreg = "";
    public static String SELECTEDPRODUCT_POS = "";
    public static String FORGOT_PSW_EMAIL = "forgot-password-email";
    public static String FORGOT_PSW_MOBILE = "forgot-password-mobile";
    public static String EDIT_PROFILE = "edit-profile";
    public static String CHANGE_PASSWORD = "change-password";
    public static String CATEGORY_ID = "category_id";
    public static String SUB_CATEGORY_ID = "subcategory_id";
    public static String CAT_ID = "cat_id";
    public static String PRODUCT_SEARCH = "products-search";
    public static String SEARCH = "search";
    public static String FROMSEARCH = "search";
    public static String Add_TRANSACTION = "add_transaction";
    public static String GET_PAYMENT_METHOD = "get_payment_methods";


    public static String DATA = "data";

    public static String SECTIONS = "sections";
    public static String VARIANT = "variants";
    public static String PRODUCT_ID = "product_id";
    public static String PROD_ID = "prod_id";
    public static String MEASUREMENT = "measurement";
    public static String MEASUREMENT_UNIT_ID = "measurement_unit_id";
    public static String PRICE = "price";
    public static String DISCOUNT = "discount";
    public static String DISCOUNTED_PRICE = "discounted_price";
    public static String SERVE_FOR = "serve_for";
    public static String STOCK = "stock";
    public static String STOCK_UNIT_ID = "stock_unit_id";
    public static String MEASUREMENT_UNIT_NAME = "measurement_unit_name";
    public static String STOCK_UNIT_NAME = "stock_unit_name";
    public static String SETTINGS = "settings";
    public static String GET_SETTINGS = "get_settings";

    //settings
    public static Double SETTING_DELIVERY_CHARGE = 0.0;
    public static Double SETTING_TAX = 0.0;
    public static Double SETTING_MINIMUM_AMOUNT_FOR_FREE_DELIVERY = 0.0;
    public static String SETTING_CURRENCY_SYMBOL = "";
    public static String SETTING_MAIL_ID = "";
    public static Double WALLET_BALANCE = 0.0;
    public static Double MINIMUM_WITHDRAW_AMOUNT = 0.0;
    public static int ORDER_DAY_LIMIT = 0;

    public static String CONTACT_US = "contact_us";
    public static String TERMSCONDITION = "terms_conditions";
    public static String PRIVACYPOLICY = "privacy_policy";
    public static String REPLY_TO = "reply_to";
    public static String MINIMUM_AMOUNT = "min_amount";
    public static String DELIEVERY_CHARGE = "delivery_charge";
    public static String CURRENCY = "currency";
    public static String TAX = "tax";
    public static String CAT = "cat";
    public static String SUBCAT = "subcat";
    public static String LIMIT = "limit";
    public static String OFFSET = "offset";
    public static String LATITUDE = "latitude";
    public static String LONGITUDE = "longitude";

    public static String OTHER_IMAGES = "other_images";
    public static String ADD_ORDER_TRANS = "add_order_and_transaction";
    public static String AMOUNT = "amount";
    public static String TOTAL = "total";
    public static String PRODUCT_VARIANT_ID = "product_variant_id";
    public static String QUANTITY = "quantity";
    public static String USER_NAME = "user_name";
    public static String DELIVERY_CHARGE = "delivery_charge";
    public static String PAYMENT_METHOD = "payment_method";
    public static String ADDRESS = "address";
    public static String TRANS_ID = "txn_id";
    public static String MESSAGE = "message";
    public static String FINAL_TOTAL = "final_total";
    public static String GETORDERS = "get_orders";
    public static String ORDER_ID = "order_id";
    public static String UPDATE_ORDER_STATUS = "update_order_status";
    public static String PLACE_ORDER = "place_order";

    public static String NEW = "new";
    public static String OLD = "old";
    public static String HIGH = "high";
    public static String LOW = "low";

    public static String SUB_TOTAL = "sub_total";
    public static String DELIVER_BY = "deliver_by";
    public static String UNIT = "unit";
    public static String SLUG = "slug";
    public static String PROMO_CODE = "promo_code";
    public static String TOKEN = "token";
    public static String ADDEDON = "addedon";
    public static boolean isOrderCancelled;
    public static CharSequence[] filtervalues = {" Newest to Oldest ", " Oldest to Newest ", " Price Highest to Lowest ", " Price Lowest to Highest "};
    public static String FRND_CODE = "";
    public static String PLAY_STORE_LINK = "https://play.google.com/store/apps/details?id=";
    public static String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghjiklmnopqrstuvwxyz";

    public static String MERCHANT_ID = "";
    public static String MERCHANT_KEY = "";
    public static String MERCHANT_SALT = "";

    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }


}

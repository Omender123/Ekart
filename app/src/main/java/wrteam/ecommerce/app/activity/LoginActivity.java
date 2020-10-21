package wrteam.ecommerce.app.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import wrteam.ecommerce.app.R;
import wrteam.ecommerce.app.helper.ApiConfig;
import wrteam.ecommerce.app.helper.AppController;
import wrteam.ecommerce.app.helper.Constant;
import wrteam.ecommerce.app.helper.GPSTracker;
import wrteam.ecommerce.app.helper.PinView;
import wrteam.ecommerce.app.helper.ProgressDisplay;
import wrteam.ecommerce.app.helper.SMSBroadcastReceiver;
import wrteam.ecommerce.app.helper.Session;
import wrteam.ecommerce.app.helper.Utils;
import wrteam.ecommerce.app.helper.VolleyCallback;
import wrteam.ecommerce.app.model.City;

public class LoginActivity extends AppCompatActivity implements OnMapReadyCallback {


    ProgressDisplay progress;

    LinearLayout lytchangpsw, lytforgot, lytlogin, signUpLyt, lytotp, lytverify;
    EditText edtnewpsw, edtRefer, edtoldpsw, edtConPass, edtforgotemail, edtforgotmobile, edtloginpassword, edtLoginEmail, edtname, edtemail, edtmobile, edtcity, edtPinCode, edtaddress, edtpsw, edtcpsw, edtEmailVerify;//edtpincode
    Button btnotpverify, btnEmailVerify, btnsubmit;
    String from, email, otp, fromto, pincode, city, area, cityId, areaId;
    PinView edtotp;
    TextView txtmobileno;
    ScrollView scrollView;
    SmsRetrieverClient client;
    Task<Void> retriever;
    SMSBroadcastReceiver smsBroadcastReceiver;
    AppCompatSpinner cityspinner, areaSpinner;
    ArrayList<City> cityArrayList, areaList;
    SupportMapFragment mapFragment;
    GPSTracker gps;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        from = getIntent().getStringExtra("from");
        fromto = getIntent().getStringExtra("fromto");
        email = getIntent().getStringExtra("txtmobile");
        otp = getIntent().getStringExtra("OTP");
        gps = new GPSTracker(LoginActivity.this);
        cityArrayList = new ArrayList<>();
        areaList = new ArrayList<>();
        txtmobileno = findViewById(R.id.txtmobileno);
        cityspinner = findViewById(R.id.cityspinner);
        areaSpinner = findViewById(R.id.areaSpinner);
        edtnewpsw = findViewById(R.id.edtnewpsw);
        edtConPass = findViewById(R.id.edtConPass);
        edtoldpsw = findViewById(R.id.edtoldpsw);

        edtforgotemail = findViewById(R.id.edtforgotemail);
        edtforgotmobile = findViewById(R.id.edtforgotmobile);
        edtloginpassword = findViewById(R.id.edtloginpassword);
        edtLoginEmail = findViewById(R.id.edtLoginEmail);
        lytchangpsw = findViewById(R.id.lytchangpsw);
        lytforgot = findViewById(R.id.lytforgot);
        lytlogin = findViewById(R.id.lytlogin);
        scrollView = findViewById(R.id.scrollView);
        edtotp = findViewById(R.id.edtotp);
        btnsubmit = findViewById(R.id.btnsubmit);
        btnEmailVerify = findViewById(R.id.btnEmailVerify);
        btnotpverify = findViewById(R.id.btnotpverify);
        edtEmailVerify = findViewById(R.id.edtEmailVerify);
        lytverify = findViewById(R.id.lytverify);
        signUpLyt = findViewById(R.id.signUpLyt);
        lytotp = findViewById(R.id.lytotp);
        edtotp = findViewById(R.id.edtotp);
        edtname = findViewById(R.id.edtname);
        edtemail = findViewById(R.id.edtemail);
        edtmobile = findViewById(R.id.edtmobile);
        edtaddress = findViewById(R.id.edtaddress);
        edtPinCode = findViewById(R.id.edtPinCode);
        edtpsw = findViewById(R.id.edtpsw);
        edtcpsw = findViewById(R.id.edtcpsw);
        edtRefer = findViewById(R.id.edtRefer);
        edtLoginEmail.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_email, 0, 0, 0);
        edtloginpassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_password, 0, R.drawable.ic_show, 0);
        edtoldpsw.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_password, 0, 0, 0);
        edtnewpsw.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_password, 0, 0, 0);
        progress = new ProgressDisplay(this);
        Utils.setHideShowPassword(edtloginpassword);


        if (from == null) {
            lytlogin.setVisibility(View.VISIBLE);

        } else if (from.equals("forgot")) {
            lytforgot.setVisibility(View.VISIBLE);
        } else if (from.equals("changepsw")) {
            lytchangpsw.setVisibility(View.VISIBLE);
        } else if (from.equals("register")) {
            lytverify.setVisibility(View.VISIBLE);
        } else if (from.equals("otp")) {
            lytotp.setVisibility(View.VISIBLE);
            txtmobileno.setText(getResources().getString(R.string.please_type_verification_code_sent_to) + " " + email);
        } else {
            signUpLyt.setVisibility(View.VISIBLE);
            edtemail.setText(email);
            edtRefer.setText(Constant.FRND_CODE);
            SetCitySpinnerData();
        }

        cityspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                city = cityspinner.getSelectedItem().toString();
                cityId = cityArrayList.get(position).getCity_id();
                SetAreaSpinnerData(cityId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                area = areaSpinner.getSelectedItem().toString();
                areaId = areaList.get(position).getCity_id();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        client = SmsRetriever.getClient(this);
        retriever = client.startSmsRetriever();
        smsBroadcastReceiver = new SMSBroadcastReceiver();

        retriever.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Toast.makeText(OTPVarifivationActivity.this, "Listener Started", Toast.LENGTH_SHORT).show();

                smsBroadcastReceiver.injectOTPListener(new SMSBroadcastReceiver.OTPListener() {
                    @Override
                    public void onOTPReceived(@NotNull String otp) {
                        if (from == null && from.equals("otp"))
                            edtotp.setText(otp);
                    }

                    @Override
                    public void onOTPTimeOut() {
                        //Toast.makeText(LoginActivity.this,"Timeout",Toast.LENGTH_SHORT).show();
                    }
                });

                registerReceiver(smsBroadcastReceiver, new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION));

            }
        });

        retriever.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Toast.makeText(OTPVarifivationActivity.this, "Problem to start listener", Toast.LENGTH_SHORT).show();
            }
        });

        ApiConfig.displayLocationSettingsRequest(LoginActivity.this);
        ApiConfig.getLocation(LoginActivity.this);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void SetCitySpinnerData() {
        Map<String, String> params = new HashMap<String, String>();
        cityArrayList.clear();
        ApiConfig.RequestToVolley(new VolleyCallback() {
            @Override
            public void onSuccess(boolean result, String response) {
                if (result) {
                    try {
                        // System.out.println("====res city " + response);
                        JSONObject objectbject = new JSONObject(response);
                        if (!objectbject.getBoolean(Constant.ERROR)) {
                            cityArrayList.add(0, new City("0", "Select your city"));
                            JSONArray jsonArray = objectbject.getJSONArray(Constant.DATA);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                //cityArrayList.add(new City(jsonObject.getString(Constant.ID), jsonObject.getString(Constant.NAME), jsonObject.getString("pincode")));
                                cityArrayList.add(new City(jsonObject.getString(Constant.ID), jsonObject.getString(Constant.NAME)));
                            }
                            cityspinner.setAdapter(new ArrayAdapter<City>(LoginActivity.this, android.R.layout.simple_spinner_dropdown_item, cityArrayList));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, LoginActivity.this, Constant.CITY_URL, params, false);
    }


    private void SetAreaSpinnerData(String cityId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constant.CITY_ID, cityId);
        areaList.clear();
        ApiConfig.RequestToVolley(new VolleyCallback() {
            @Override
            public void onSuccess(boolean result, String response) {
                if (result) {
                    try {
                        // System.out.println("====res area " + response);
                        areaList.add(0, new City("0", "Select your area"));
                        JSONObject objectbject = new JSONObject(response);
                        if (!objectbject.getBoolean(Constant.ERROR)) {
                            JSONArray jsonArray = objectbject.getJSONArray(Constant.DATA);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                //cityArrayList.add(new City(jsonObject.getString(Constant.ID), jsonObject.getString(Constant.NAME), jsonObject.getString("pincode")));
                                areaList.add(new City(jsonObject.getString(Constant.ID), jsonObject.getString(Constant.NAME)));
                            }
                            areaSpinner.setAdapter(new ArrayAdapter<City>(LoginActivity.this, android.R.layout.simple_spinner_dropdown_item, areaList));


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, LoginActivity.this, Constant.GET_AREA_BY_CITY, params, false);
    }

    @Override
    public void onResume() {
        //LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(smsBroadcastReceiver);
    }

    @Override
    public void onPause() {
        super.onPause();
        //LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    public void ChangePassword() {
        final Session sessionpsw = new Session(LoginActivity.this);

        String oldpsw = edtoldpsw.getText().toString();
        String password = edtnewpsw.getText().toString();
        String confirmPass = edtConPass.getText().toString();


        if (ApiConfig.CheckValidattion(oldpsw, false, false)) {
            edtoldpsw.setError("Enter Old Password");
        } else if (ApiConfig.CheckValidattion(password, false, false)) {
            edtnewpsw.setError("Enter new Password");
        } else if (!oldpsw.equals(sessionpsw.getData(Session.KEY_Password))) {
            edtoldpsw.setError("Old Password not matched");
        } else if (!password.equals(confirmPass)) {
            edtConPass.setError("Password Not Matched");
        } else if (AppController.isConnected(LoginActivity.this)) {
            Map<String, String> params = new HashMap<String, String>();
            params.put(Constant.TYPE, Constant.CHANGE_PASSWORD);
            params.put(Constant.PASSWORD, password);
            params.put(Constant.ID, sessionpsw.getData(Session.KEY_ID));


            ApiConfig.RequestToVolley(new VolleyCallback() {
                @Override
                public void onSuccess(boolean result, String response) {
                    // System.out.println("=================*changepsw " + response);
                    if (result) {
                        try {
                            JSONObject objectbject = new JSONObject(response);
                            if (!objectbject.getBoolean(Constant.ERROR)) {
                                sessionpsw.logoutUser(LoginActivity.this);
                            }
                            Toast.makeText(LoginActivity.this, objectbject.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, LoginActivity.this, Constant.RegisterUrl, params, true);
        }
    }

    public void RecoverPassword() {
        String mobile = edtforgotmobile.getText().toString();
        String email = edtforgotemail.getText().toString();

        if (ApiConfig.CheckValidattion(mobile, false, false) && ApiConfig.CheckValidattion(email, false, false)) {
            Toast.makeText(LoginActivity.this, "Enter Mobile Number or Email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mobile.length() != 0 && ApiConfig.CheckValidattion(mobile, false, true) && ApiConfig.CheckValidattion(email, true, false)) {
            edtforgotmobile.setError("Enter valid txtmobile number");
            return;
        }

        if (email.length() != 0 && ApiConfig.CheckValidattion(email, true, false)) {
            edtforgotemail.setError("Enter valid Email");
            return;
        }

        Map<String, String> params = new HashMap<String, String>();
        if (mobile.length() != 0 && !ApiConfig.CheckValidattion(mobile, false, true)) {
            params.put(Constant.TYPE, Constant.FORGOT_PSW_MOBILE);
            params.put(Constant.MOBILE, mobile);
        } else {
            params.put(Constant.TYPE, Constant.FORGOT_PSW_EMAIL);
            params.put(Constant.EMAIL, email);
        }

        if (AppController.isConnected(LoginActivity.this)) {
            ApiConfig.RequestToVolley(new VolleyCallback() {
                @Override
                public void onSuccess(boolean result, String response) {
                    //System.out.println("=================*forgot " + response);
                    if (result) {
                        try {
                            JSONObject objectbject = new JSONObject(response);
                            edtforgotemail.setText("");
                            edtforgotmobile.setText("");
                            Toast.makeText(LoginActivity.this, objectbject.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, LoginActivity.this, Constant.RegisterUrl, params, true);
        }

    }

    public void UserLogin() {
        String email = edtLoginEmail.getText().toString();
        final String password = edtloginpassword.getText().toString();

        if (ApiConfig.CheckValidattion(email, false, false)) {
            edtLoginEmail.setError("Enter Email");
        } else if (ApiConfig.CheckValidattion(email, true, false)) {
            edtLoginEmail.setError("Enter Valid Email");
        } else if (ApiConfig.CheckValidattion(password, false, false)) {
            edtloginpassword.setError("Enter Password");
        } else if (AppController.isConnected(LoginActivity.this)) {
            Map<String, String> params = new HashMap<String, String>();
            params.put(Constant.EMAIL, email);
            params.put(Constant.PASSWORD, password);
            params.put(Constant.FCM_ID, AppController.getInstance().getDeviceToken());

            System.out.println("=====lgin params  " + params.toString());
            ApiConfig.RequestToVolley(new VolleyCallback() {
                @Override
                public void onSuccess(boolean result, String response) {
                    System.out.println("=================*login " + response);
                    if (result) {
                        try {
                            JSONObject objectbject = new JSONObject(response);
                            if (!objectbject.getBoolean(Constant.ERROR)) {
                                StartMainActivity(objectbject, password);

                            }
                            Toast.makeText(LoginActivity.this, objectbject.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, LoginActivity.this, Constant.LoginUrl, params, true);
        }
    }

    public void EmailVerification() {
        final String email = edtEmailVerify.getText().toString().trim();
        if (ApiConfig.CheckValidattion(email, false, false)) {
            edtEmailVerify.setError("Enter Email Address");
        } else if (ApiConfig.CheckValidattion(email, true, false)) {
            edtEmailVerify.setError("Enter Valid Email Address");
        } else if (AppController.isConnected(LoginActivity.this)) {
            Map<String, String> params = new HashMap<String, String>();
            params.put(Constant.TYPE, Constant.VERIFY_EMAIL);
            params.put(Constant.EMAIL, email);


            ApiConfig.RequestToVolley(new VolleyCallback() {
                @Override
                public void onSuccess(boolean result, String response) {
                    //System.out.println("=================*otp " + response);
                    //startActivity(new Intent(LoginActivity.this, LoginActivity.class).putExtra("from", "info").putExtra("txtmobile", txtmobile));
                    if (result) {
                        try {
                            JSONObject objectbject = new JSONObject(response);
                            if (!objectbject.getBoolean(Constant.ERROR)) {
                                startActivity(new Intent(LoginActivity.this, LoginActivity.class)
                                        .putExtra("from", "otp")
                                        .putExtra("txtmobile", email)
                                        .putExtra("OTP", String.valueOf(objectbject.getInt("OTP"))));
                            }
                            Toast.makeText(LoginActivity.this, objectbject.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, LoginActivity.this, Constant.RegisterUrl, params, true);
        }
    }

    public void OTP_Varification() {
        String otptext = edtotp.getText().toString().trim();
        if (ApiConfig.CheckValidattion(otptext, false, false)) {
            edtotp.setError("Enter OTP");
        } else if (!otp.equals(otptext)) {
            edtotp.setError("OTP not matched");
        } else if (AppController.isConnected(LoginActivity.this)) {
            startActivity(new Intent(LoginActivity.this, LoginActivity.class).putExtra("from", "info").putExtra("txtmobile", email));
        }
    }

    public void UserSignUpSubmit(String latitude, String longitude) {

        String name = edtname.getText().toString().trim();
        String email = edtemail.getText().toString().trim();
        String mobile = edtmobile.getText().toString().trim();
        //String city = edtcity.getText().toString().trim();
        String city = cityspinner.getSelectedItem().toString();
        String area = areaSpinner.getSelectedItem().toString();
        String address = edtaddress.getText().toString().trim();
        String pincode = edtPinCode.getText().toString().trim();

        final String password = edtpsw.getText().toString().trim();
        String cpassword = edtcpsw.getText().toString().trim();


        if (ApiConfig.CheckValidattion(name, false, false)) {
            edtname.setError("Enter Name");
            scrollView.scrollTo(0, edtname.getBottom());
        }/* else if (ApiConfig.CheckValidattion(email, false, false)) {
            edtemail.setError("Enter Email Address");
            scrollView.scrollTo(0, edtemail.getBottom());
        } else if (ApiConfig.CheckValidattion(email, true, false)) {
            edtemail.setError("Enter Valid Email Address");
            scrollView.scrollTo(0, edtemail.getBottom());
        }*/ else if (cityId.equals("0")) {
            Toast.makeText(LoginActivity.this, getResources().getString(R.string.selectcity), Toast.LENGTH_LONG).show();
            scrollView.scrollTo(0, cityspinner.getBottom());
        } else if (areaId.equals("0")) {
            Toast.makeText(LoginActivity.this, getResources().getString(R.string.selectarea), Toast.LENGTH_LONG).show();
            scrollView.scrollTo(0, areaSpinner.getBottom());
        } else if (ApiConfig.CheckValidattion(pincode, false, false)) {
            edtPinCode.setError("Enter PinCode");
            //Toast.makeText(LoginActivity.this, getResources().getString(R.string.selectcity), Toast.LENGTH_LONG).show();
            scrollView.scrollTo(0, edtPinCode.getBottom());
        } else if (ApiConfig.CheckValidattion(address, false, false)) {
            edtaddress.setError("Enter Address");
            scrollView.scrollTo(0, edtaddress.getBottom());
        } else if (ApiConfig.CheckValidattion(password, false, false)) {
            edtpsw.setError("Enter Password");
            scrollView.scrollTo(0, edtpsw.getBottom());
        } else if (ApiConfig.CheckValidattion(cpassword, false, false)) {
            edtcpsw.setError("Enter Confirmation Password");
            scrollView.scrollTo(0, edtcpsw.getBottom());
        } else if (!password.equals(cpassword)) {
            edtcpsw.setError("Confirmation Password not matched");
            scrollView.scrollTo(0, edtcpsw.getBottom());
        } else if (latitude.equals("0") || longitude.equals("0"))
            Toast.makeText(LoginActivity.this, "Please select your location", Toast.LENGTH_LONG).show();
        else if (AppController.isConnected(LoginActivity.this)) {
            Map<String, String> params = new HashMap<String, String>();
            params.put(Constant.TYPE, Constant.REGISTER);
            params.put(Constant.NAME, name);
            params.put(Constant.EMAIL, email);
            params.put(Constant.MOBILE, mobile);
            params.put(Constant.PASSWORD, password);
            params.put(Constant.CITY_ID, cityId);
            params.put(Constant.AREA_ID, areaId);
            params.put(Constant.STREET, address);
            params.put(Constant.PINCODE, pincode);
            params.put(Constant.LONGITUDE, longitude);
            params.put(Constant.LATITUDE, latitude);
            params.put(Constant.REFERRAL_CODE, Constant.randomAlphaNumeric(8));
            params.put(Constant.FRIENDS_CODE, edtRefer.getText().toString());
            params.put(Constant.FCM_ID, "" + AppController.getInstance().getDeviceToken());

            ApiConfig.RequestToVolley(new VolleyCallback() {
                @Override
                public void onSuccess(boolean result, String response) {
                    //System.out.println("=================*register " + response);
                    if (result) {
                        try {
                            JSONObject objectbject = new JSONObject(response);
                            if (!objectbject.getBoolean(Constant.ERROR)) {

                                StartMainActivity(objectbject, password);

                            }
                            Toast.makeText(LoginActivity.this, objectbject.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, LoginActivity.this, Constant.RegisterUrl, params, true);
        }
    }

    public void OnBtnClick(View view) {
        int id = view.getId();

        if (id == R.id.txtterms) {
            Intent terms = new Intent(LoginActivity.this, WebViewActivity.class);
            terms.putExtra("type", "terms");
            startActivity(terms);
            //ApiConfig.OpenBottomDialog("terms", LoginActivity.this);
        } else if (id == R.id.txtprivacypolicy) {
            Intent privacy = new Intent(LoginActivity.this, WebViewActivity.class);
            privacy.putExtra("type", "privacy");
            startActivity(privacy);
            //ApiConfig.OpenBottomDialog("privacy", LoginActivity.this);
        } else if (id == R.id.txtnoregister) {
            startActivity(new Intent(LoginActivity.this, LoginActivity.class).putExtra("from", "register"));
        } else if (id == R.id.txtforgot) {
            startActivity(new Intent(LoginActivity.this, LoginActivity.class).putExtra("from", "forgot"));
        } else if (id == R.id.btnchangepsw) {

            ChangePassword();

        } else if (id == R.id.btnrecover) {

            RecoverPassword();

        } else if (id == R.id.btnlogin) {

            UserLogin();

        } else if (id == R.id.btnEmailVerify) {

            EmailVerification();

        } else if (id == R.id.btnotpverify) {

            OTP_Varification();

        } else if (id == R.id.btnsubmit) {

            double saveLatitude = Double.parseDouble(new Session(getApplicationContext()).getCoordinates(Session.KEY_LATITUDE));
            double saveLongitude = Double.parseDouble(new Session(getApplicationContext()).getCoordinates(Session.KEY_LONGITUDE));

            if (saveLatitude == 0 || saveLongitude == 0) {
                UserSignUpSubmit(String.valueOf(gps.latitude), String.valueOf(gps.longitude));
            } else {
                UserSignUpSubmit(new Session(getApplicationContext()).getCoordinates(Session.KEY_LATITUDE), new Session(getApplicationContext()).getCoordinates(Session.KEY_LONGITUDE));
            }


        }

    }

    private void StartMainActivity(JSONObject objectbject, String password) {
        try {
            new Session(LoginActivity.this).createUserLoginSession(AppController.getInstance().getDeviceToken(),
                    objectbject.getString(Constant.USER_ID),
                    objectbject.getString(Constant.NAME), objectbject.getString(Constant.EMAIL),
                    objectbject.getString(Constant.MOBILE), objectbject.getString(Constant.DOB),
                    objectbject.getString(Constant.CITY_NAME), objectbject.getString(Constant.AREA_NAME),
                    objectbject.getString(Constant.CITY_ID), objectbject.getString(Constant.AREA_ID),
                    objectbject.getString(Constant.STREET), objectbject.getString(Constant.PINCODE),
                    objectbject.getString(Constant.STATUS), objectbject.getString(Constant.CREATEDATE),
                    objectbject.getString(Constant.APIKEY), password, objectbject.getString("referral_code"),
                    objectbject.getString(Constant.LATITUDE), objectbject.getString(Constant.LONGITUDE));
            if (fromto != null && fromto.equals("checkout"))
                startActivity(new Intent(LoginActivity.this, CheckoutActivity.class));
            else
                startActivity(new Intent(LoginActivity.this, MainActivity.class));

            finishAffinity();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        final GoogleMap mMap = googleMap;
        mMap.clear();
        LatLng latLng;

        double saveLatitude = Double.parseDouble(new Session(getApplicationContext()).getCoordinates(Session.KEY_LATITUDE));
        double saveLongitude = Double.parseDouble(new Session(getApplicationContext()).getCoordinates(Session.KEY_LONGITUDE));

        if (saveLatitude == 0 || saveLongitude == 0) {
            latLng = new LatLng(gps.latitude, gps.longitude);
        } else {
            latLng = new LatLng(saveLatitude, saveLongitude);
        }

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .draggable(true)
                .title("Current Location"));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));

    }
}


package wrteam.ecommerce.app.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.format.DateFormat;
import android.view.Gravity;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import wrteam.ecommerce.app.R;

import wrteam.ecommerce.app.helper.ApiConfig;
import wrteam.ecommerce.app.helper.Constant;
import wrteam.ecommerce.app.helper.DatabaseHelper;
import wrteam.ecommerce.app.helper.PaymentModelClass;
import wrteam.ecommerce.app.helper.Session;
import wrteam.ecommerce.app.helper.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@SuppressLint("SetTextI18n")
public class CheckoutActivity extends AppCompatActivity implements OnMapReadyCallback {

    public Toolbar toolbar;
    public TextView tvDelivery, tvPayment, tvLocation, tvAlert, tvWltBalance, tvCity, tvName, tvTotal, tvDeliveryCharge, tvSubTotal, tvCurrent, tvWallet, tvPromoCode, tvPCAmount, tvPlaceOrder, tvConfirmOrder, tvPreTotal;
    LinearLayout lytOrderList, lytWallet, lytCLocation, paymentLyt, deliveryLyt;
    Button btnApply;
    EditText edtPromoCode;
    public ProgressBar prgLoading;
    Session session;
    JSONArray qtyList, variantIdList, nameList;
    DatabaseHelper databaseHelper;
    RadioGroup radiogroup;
    double total, subtotal;
    String deliveryCharge = "0";
    PaymentModelClass paymentModelClass;
    SupportMapFragment mapFragment;
    CheckBox chWallet, chHome, chWork;
    public RadioButton rMorning, rEvening, rToday, rTomorrow;
    public String morningCutOff = "", eveningCutOff = "";
    String deliveryTime = "", deliveryDay = "", pCode = "", paymentMethod = "", label = "";
    RadioButton rbCod, rbPayU, rbPayPal;
    ProgressDialog mProgressDialog;
    Map<String, String> upiParams;
    RelativeLayout walletLyt;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        paymentModelClass = new PaymentModelClass(CheckoutActivity.this);
        databaseHelper = new DatabaseHelper(CheckoutActivity.this);
        session = new Session(CheckoutActivity.this);

        rbCod = findViewById(R.id.rbcod);
        rbPayU = findViewById(R.id.rbPayU);
        rbPayPal = findViewById(R.id.rbPayPal);
        tvLocation = findViewById(R.id.tvLocation);
        tvDelivery = findViewById(R.id.tvDelivery);
        tvPayment = findViewById(R.id.tvPayment);
        tvPCAmount = findViewById(R.id.tvPCAmount);
        tvPromoCode = findViewById(R.id.tvPromoCode);
        tvAlert = findViewById(R.id.tvAlert);
        edtPromoCode = findViewById(R.id.edtPromoCode);
        radiogroup = findViewById(R.id.radiogroup);
        chWallet = findViewById(R.id.chWallet);
        chHome = findViewById(R.id.chHome);
        chWork = findViewById(R.id.chWork);
        tvSubTotal = findViewById(R.id.tvSubTotal);
        tvDeliveryCharge = findViewById(R.id.tvDeliveryCharge);
        tvTotal = findViewById(R.id.tvTotal);
        tvName = findViewById(R.id.tvName);
        tvCity = findViewById(R.id.tvCity);
        tvCurrent = findViewById(R.id.tvCurrent);
        lytOrderList = findViewById(R.id.lytOrderList);
        lytCLocation = findViewById(R.id.lytCLocation);
        lytWallet = findViewById(R.id.lytWallet);
        walletLyt = findViewById(R.id.walletLyt);
        paymentLyt = findViewById(R.id.paymentLyt);
        deliveryLyt = findViewById(R.id.deliveryLyt);
        tvWallet = findViewById(R.id.tvWallet);
        prgLoading = findViewById(R.id.prgLoading);
        tvPlaceOrder = findViewById(R.id.tvPlaceOrder);
        tvConfirmOrder = findViewById(R.id.tvConfirmOrder);
        lytWallet.setVisibility(View.GONE);

        rMorning = findViewById(R.id.rMorning);
        rEvening = findViewById(R.id.rEvening);
        rToday = findViewById(R.id.rToday);
        rTomorrow = findViewById(R.id.rTomorrow);
        tvWltBalance = findViewById(R.id.tvWltBalance);
        tvPreTotal = findViewById(R.id.tvPreTotal);
        btnApply = findViewById(R.id.btnApply);
        tvLocation.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_my_location, 0, 0, 0);
        tvCurrent.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_address, 0, 0, 0);
        tvDelivery.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_next_process, 0, 0, 0);
        tvPayment.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_next_process_gray, 0, 0, 0);
        tvConfirmOrder.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_confirm, 0);
        tvPlaceOrder.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_process, 0);
        tvPreTotal.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_info, 0, 0, 0);
        ApiConfig.getWalletBalance(CheckoutActivity.this, session);
        try {
            qtyList = new JSONArray(session.getData(Session.KEY_Orderqty));
            variantIdList = new JSONArray(session.getData(Session.KEY_Ordervid));
            nameList = new JSONArray(session.getData(Session.KEY_Ordername));

            for (int i = 0; i < nameList.length(); i++) {
                LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setWeightSum(4f);
                String[] name = nameList.getString(i).split("==");

                TextView tv1 = new TextView(this);
                tv1.setText(name[1] + " (" + name[0] + ")");
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.weight = 1.5f;
                tv1.setLayoutParams(lp);
                linearLayout.addView(tv1);

                TextView tv2 = new TextView(this);
                tv2.setText(qtyList.getString(i));
                LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp1.weight = 0.7f;
                tv2.setLayoutParams(lp1);
                tv2.setGravity(Gravity.CENTER);
                linearLayout.addView(tv2);

                TextView tv3 = new TextView(this);
                tv3.setText(Constant.SETTING_CURRENCY_SYMBOL + name[2]);
                LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp2.weight = 0.8f;
                tv3.setLayoutParams(lp2);
                tv3.setGravity(Gravity.CENTER);
                linearLayout.addView(tv3);

                TextView tv4 = new TextView(this);
                tv4.setText(Constant.SETTING_CURRENCY_SYMBOL + name[3]);
                LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp3.weight = 1f;
                tv4.setLayoutParams(lp3);
                tv4.setGravity(Gravity.END);
                linearLayout.addView(tv4);
                lytOrderList.addView(linearLayout);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        SetDataTotal();
        chWallet.setTag("false");
        tvWltBalance.setText("Total Balance: " + Constant.SETTING_CURRENCY_SYMBOL + Constant.WALLET_BALANCE);

        if (Constant.WALLET_BALANCE == 0) {
            chWallet.setEnabled(false);
            walletLyt.setEnabled(false);
        }
        chWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chWallet.getTag().equals("false")) {
                    chWallet.setChecked(true);
                    lytWallet.setVisibility(View.VISIBLE);
                    double usedBalance = 0;
                    if (Constant.WALLET_BALANCE > subtotal) {
                        usedBalance = subtotal;
                        tvWltBalance.setText("Remaining Wallet Balance: " + Constant.SETTING_CURRENCY_SYMBOL + (Constant.WALLET_BALANCE - usedBalance));
                    }
                    tvWallet.setText("-" + Constant.SETTING_CURRENCY_SYMBOL + usedBalance);
                    tvSubTotal.setText(Constant.SETTING_CURRENCY_SYMBOL + DatabaseHelper.decimalformatData.format(subtotal - usedBalance));
                    chWallet.setTag("true");
                } else {
                    tvWltBalance.setText("Total Balance: " + Constant.SETTING_CURRENCY_SYMBOL + Constant.WALLET_BALANCE);
                    lytWallet.setVisibility(View.GONE);
                    chWallet.setChecked(false);
                    chWallet.setTag("false");
                    SetDataTotal();
                }

            }
        });
        PromoCodeCheck();
        setPaymentMethod();

    }


    public void setPaymentMethod() {

        chHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chHome.setChecked(true);
                chWork.setChecked(false);
                label = chHome.getTag().toString();
            }
        });
        chWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chWork.setChecked(true);
                chHome.setChecked(false);
                label = chWork.getTag().toString();
            }
        });
        rbCod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbCod.setChecked(true);
                rbPayU.setChecked(false);
                rbPayPal.setChecked(false);
                paymentMethod = rbCod.getTag().toString();

            }
        });
        rbPayU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbPayU.setChecked(true);
                rbCod.setChecked(false);
                rbPayPal.setChecked(false);
                paymentMethod = rbPayU.getTag().toString();

            }
        });

        rbPayPal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbPayPal.setChecked(true);
                rbCod.setChecked(false);
                rbPayU.setChecked(false);
                paymentMethod = rbPayPal.getTag().toString();

            }
        });

    }

    private String getTime() {
        String delegate = "HH:mm aaa";
        return (String) DateFormat.format(delegate, Calendar.getInstance().getTime());
    }


    public void SetDataTotal() {
        total = databaseHelper.getTotalCartAmt(session);
        tvTotal.setText(Constant.SETTING_CURRENCY_SYMBOL + DatabaseHelper.decimalformatData.format(total));
        subtotal = total;
        if (total <= Constant.SETTING_MINIMUM_AMOUNT_FOR_FREE_DELIVERY) {
            tvDeliveryCharge.setText(Constant.SETTING_CURRENCY_SYMBOL + Constant.SETTING_DELIVERY_CHARGE);
            subtotal = subtotal + Constant.SETTING_DELIVERY_CHARGE;
            deliveryCharge = Constant.SETTING_DELIVERY_CHARGE + "";
        } else {
            tvDeliveryCharge.setText(getResources().getString(R.string.free));
            deliveryCharge = "0";
        }
        tvSubTotal.setText(Constant.SETTING_CURRENCY_SYMBOL + DatabaseHelper.decimalformatData.format(subtotal));
    }


    public void OnBtnClick(View view) {
        switch (view.getId()) {
            case R.id.tvConfirmOrder:
                tvPayment.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                tvPayment.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_next_process, 0, 0, 0);
                tvDelivery.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.light_green));
                tvDelivery.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check, 0, 0, 0);
                tvConfirmOrder.setVisibility(View.GONE);
                tvPlaceOrder.setVisibility(View.VISIBLE);
                paymentLyt.setVisibility(View.VISIBLE);
                deliveryLyt.setVisibility(View.GONE);

                break;
            case R.id.tvLocation:
                if (tvLocation.getTag().equals("hide")) {
                    tvLocation.setTag("show");
                    lytCLocation.setVisibility(View.VISIBLE);
                } else {
                    tvLocation.setTag("hide");
                    lytCLocation.setVisibility(View.GONE);
                }
                break;
            case R.id.tvPlaceOrder:
                PlaceOrderProcess();

                break;
            case R.id.imgedit:
                startActivity(new Intent(CheckoutActivity.this, ProfileActivity.class));
                break;
            case R.id.tvUpdate:
                if (ApiConfig.isGPSEnable(CheckoutActivity.this))
                    startActivity(new Intent(CheckoutActivity.this, MapActivity.class));
                else
                    ApiConfig.displayLocationSettingsRequest(CheckoutActivity.this);
                break;


            default:
                break;
        }
    }


    public void PlaceOrderProcess() {
         /*else if (deliveryDay.length() == 0) {
                    Toast.makeText(CheckoutActivity.this, "please select delivery day!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (deliveryTime.length() == 0) {
                    Toast.makeText(CheckoutActivity.this, "please select delivery time!", Toast.LENGTH_SHORT).show();
                    return;
                }*/
        if (paymentMethod.isEmpty()) {
            Toast.makeText(CheckoutActivity.this, "please select payment method!", Toast.LENGTH_SHORT).show();
            return;
        }
        final Map<String, String> sendparams = new HashMap<String, String>();
        sendparams.put(Constant.PLACE_ORDER, Constant.GetVal);
        sendparams.put(Constant.USER_ID, session.getData(Session.KEY_ID));
        sendparams.put(Constant.TOTAL, DatabaseHelper.decimalformatData.format(total));
        sendparams.put(Constant.PRODUCT_VARIANT_ID, String.valueOf(variantIdList));
        sendparams.put(Constant.QUANTITY, String.valueOf(qtyList));
        sendparams.put(Constant.LONGITUDE, session.getCoordinates(Session.KEY_LONGITUDE));
        sendparams.put(Constant.LATITUDE, session.getCoordinates(Session.KEY_LATITUDE));
        sendparams.put(Constant.MOBILE, session.getData(Session.KEY_MOBILE));
        sendparams.put(Constant.DELIVERY_CHARGE, deliveryCharge);
        sendparams.put("delivery_time", (deliveryDay + " - " + deliveryTime));
        sendparams.put(Constant.KEY_WALLET_USED, chWallet.getTag().toString());
        sendparams.put(Constant.KEY_WALLET_BALANCE, String.valueOf(Constant.WALLET_BALANCE));
        sendparams.put(Constant.PAYMENT_METHOD, paymentMethod);
        String address = session.getData(Session.KEY_ADDRESS)
                + ", " + session.getData(Session.KEY_AREA)
                + ", " + session.getData(Session.KEY_CITY)
                + ", " + session.getData(Session.KEY_PINCODE)
                + ", Deliver to " + label;

        if (!pCode.isEmpty())
            sendparams.put(Constant.PROMO_CODE, pCode);
        sendparams.put(Constant.ADDRESS, address);
        sendparams.put(Constant.FINAL_TOTAL, DatabaseHelper.decimalformatData.format(subtotal));
        sendparams.put(Constant.EMAIL, session.getData(Session.KEY_EMAIL));
        // System.out.println("=====params " + sendparams.toString());
        new AlertDialog.Builder(CheckoutActivity.this)
                .setTitle("Confirm Order Amount")
                .setMessage("Total Order Amount : " + tvTotal.getText().toString() + "\n" + getResources().getString(R.string.delivery_charge) + " : " + tvDeliveryCharge.getText().toString() + "\nTotal Payable Amount : " + tvSubTotal.getText().toString())
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Confirm Order", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (paymentMethod.equals(getResources().getString(R.string.codpaytype))) {
                            ApiConfig.RequestToVolley(new VolleyCallback() {
                                @Override
                                public void onSuccess(boolean result, String response) {
                                    System.out.println("=================*order- " + response);
                                    if (result) {
                                        try {
                                            JSONObject object = new JSONObject(response);
                                            if (!object.getBoolean(Constant.ERROR)) {
                                                startActivity(new Intent(CheckoutActivity.this, OrderPlacedActivity.class));
                                                finish();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }, CheckoutActivity.this, Constant.ORDERPROCESS_URL, sendparams, true);
                            dialog.dismiss();
                        } else if (paymentMethod.equals(getString(R.string.pay_u))) {
                            sendparams.put(Constant.USER_NAME, session.getData(Session.KEY_NAME));
                            paymentModelClass.OnPayClick(CheckoutActivity.this, sendparams);
                        } else if (paymentMethod.equals(getString(R.string.paypal))) {
                            sendparams.put(Constant.USER_NAME, session.getData(Session.KEY_NAME));
                            StartPayPalPayment(sendparams);
                        }
                    }
                })
                .show();
    }

    public void StartPayPalPayment(final Map<String, String> sendParams) {
        showProgressDialog("processing...");
        Map<String, String> params = new HashMap<>();
        params.put(Constant.FIRST_NAME, sendParams.get(Constant.USER_NAME));
        params.put(Constant.LAST_NAME, sendParams.get(Constant.USER_NAME));
        params.put(Constant.PAYER_EMAIL, sendParams.get(Constant.EMAIL));
        params.put(Constant.ITEM_NAME, "Card Order");
        params.put(Constant.ITEM_NUMBER, "1");
        params.put(Constant.AMOUNT, sendParams.get(Constant.AMOUNT));
        ApiConfig.RequestToVolley(new VolleyCallback() {
            @Override
            public void onSuccess(boolean result, String response) {
                hideProgressDialog();
                Intent intent = new Intent(getApplicationContext(), PayPalWebActivity.class);
                intent.putExtra("url", response);
                intent.putExtra("params", (Serializable) sendParams);
                startActivity(intent);


            }
        }, CheckoutActivity.this, Constant.PAPAL_URL, params, true);
    }

    public void PromoCodeCheck() {

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String promoCode = edtPromoCode.getText().toString().trim();
                if (promoCode.isEmpty()) {
                    tvAlert.setVisibility(View.VISIBLE);
                    tvAlert.setText("Enter Promo Code");
                } else {
                    tvAlert.setVisibility(View.GONE);
                    btnApply.setVisibility(View.GONE);
                    // pBar.setVisibility(View.VISIBLE);
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(Constant.VALIDATE_PROMO_CODE, Constant.GetVal);
                    params.put(Constant.USER_ID, session.getData(Session.KEY_ID));
                    params.put(Constant.PROMO_CODE, promoCode);
                    params.put(Constant.TOTAL, String.valueOf(subtotal));
                    ApiConfig.RequestToVolley(new VolleyCallback() {
                        @Override
                        public void onSuccess(boolean result, String response) {

                            if (result) {
                                try {
                                    JSONObject object = new JSONObject(response);
                                    //   System.out.println("===res " + response);
                                    if (!object.getBoolean(Constant.ERROR)) {
                                        pCode = object.getString(Constant.PROMO_CODE);
                                        tvPromoCode.setText(getString(R.string.promo_code) + "(" + pCode + ")");
                                        btnApply.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.light_green));
                                        btnApply.setText("Applied");
                                        tvPCAmount.setVisibility(View.VISIBLE);
                                        subtotal = Double.parseDouble(object.getString(Constant.DISCOUNTED_AMOUNT));
                                        tvPCAmount.setText(Constant.SETTING_CURRENCY_SYMBOL + object.getString(Constant.DISCOUNT));
                                        tvSubTotal.setText(Constant.SETTING_CURRENCY_SYMBOL + subtotal);
                                    } else {
                                        tvAlert.setVisibility(View.VISIBLE);
                                        tvAlert.setText(object.getString("message"));
                                    }
                                    // pBar.setVisibility(View.GONE);
                                    btnApply.setVisibility(View.VISIBLE);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }, CheckoutActivity.this, Constant.PROMO_CODE_CHECK_URL, params, false);

                }
            }
        });

    }

    public void showProgressDialog(String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(message);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null)
            paymentModelClass.TrasactionMethod(data, CheckoutActivity.this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        final GoogleMap mMap = googleMap;
        mMap.clear();
        LatLng latLng = new LatLng(Double.parseDouble(session.getCoordinates(Session.KEY_LATITUDE)), Double.parseDouble(session.getCoordinates(Session.KEY_LONGITUDE)));
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .draggable(true)
                .title("Current Location"));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(19));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
        mapFragment.getMapAsync(this);
        tvName.setText(session.getData(Session.KEY_NAME));
        tvCurrent.setText("Current Location : " + ApiConfig.getAddress(Double.parseDouble(session.getCoordinates(Session.KEY_LATITUDE)), Double.parseDouble(session.getCoordinates(Session.KEY_LONGITUDE)), CheckoutActivity.this));
        String address = session.getData(Session.KEY_ADDRESS) + ",<br>"
                + session.getData(Session.KEY_AREA)
                + ", " + session.getData(Session.KEY_CITY)
                + "- " + session.getData(Session.KEY_PINCODE)
                /*  + "<br>Email: " + session.getData(Session.KEY_EMAIL)*/
                + "<br><b>Phone:</b> " + session.getData(Session.KEY_MOBILE);
        tvCity.setText(Html.fromHtml(address));
    }

    @Override
    public void onBackPressed() {

        if (paymentLyt.getVisibility() == View.VISIBLE) {
            tvPayment.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray));
            tvPayment.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_next_process_gray, 0, 0, 0);
            tvDelivery.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
            tvDelivery.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_next_process, 0, 0, 0);
            tvConfirmOrder.setVisibility(View.VISIBLE);
            tvPlaceOrder.setVisibility(View.GONE);
            paymentLyt.setVisibility(View.GONE);
            deliveryLyt.setVisibility(View.VISIBLE);
        } else
            super.onBackPressed();
    }

    /*
     public void getWalletBalance() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("get_time_slots", Constant.GetVal);

        ApiConfig.RequestToVolley(new VolleyCallback() {
            @Override
            public void onSuccess(boolean result, String response) {
                // System.out.println("=================*setting " + response);
                if (result) {
                    try {
                        JSONObject object = new JSONObject(response);
                        if (!object.getBoolean(Constant.ERROR)) {
                            JSONObject object1 = object.getJSONArray("time_slots").getJSONObject(0);
                            rMorning.setText(object1.getString("title"));
                            morningCutOff = object1.getString("last_order_time");

                            JSONObject object2 = object.getJSONArray("time_slots").getJSONObject(1);
                            rEvening.setText(object2.getString("title"));
                            eveningCutOff = object2.getString("last_order_time");

                            if (getTime().compareTo(eveningCutOff) > 0) {
                                rToday.setClickable(false);
                                rToday.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray));
                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, CheckoutActivity.this, Constant.SETTING_URL, params, false);

    }




    public void DeliveryTimeLyt() {
        rToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getTime().compareTo(eveningCutOff) > 0) {
                    rToday.setClickable(false);
                    rToday.setChecked(false);
                } else {
                    rToday.setChecked(true);
                    rTomorrow.setChecked(false);
                    deliveryDay = getString(R.string.today);
                    if (getTime().compareTo(morningCutOff) > 0) {
                        rMorning.setClickable(false);
                        rMorning.setChecked(false);
                        rMorning.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray));
                    }

                    if (getTime().compareTo(eveningCutOff) > 0) {
                        rEvening.setClickable(false);
                        rEvening.setChecked(false);
                        rEvening.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray));
                    }
                }

            }
        });


        rTomorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deliveryDay = getString(R.string.tomorrow);
                rToday.setChecked(false);
                rTomorrow.setChecked(true);

                rMorning.setClickable(true);
                rMorning.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));

                rEvening.setClickable(true);
                rEvening.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));

            }

        });
        rEvening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rEvening.setChecked(true);
                rMorning.setChecked(false);
                deliveryTime = rEvening.getText().toString();
            }
        });
        rMorning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                *//*if (!rTomorrow.isChecked()) {
                    if (getTime().compareTo(morningCutOff) > 0) {
                        rMorning.setClickable(false);
                        rMorning.setChecked(false);
                    }
                } else {*//*
                rMorning.setChecked(true);
                rEvening.setChecked(false);
                deliveryTime = rMorning.getText().toString();
                //}
            }
        });

    }*/
}

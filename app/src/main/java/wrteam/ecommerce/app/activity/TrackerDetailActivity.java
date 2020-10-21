package wrteam.ecommerce.app.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import wrteam.ecommerce.app.R;

import wrteam.ecommerce.app.adapter.ItemsAdapter;
import wrteam.ecommerce.app.helper.ApiConfig;
import wrteam.ecommerce.app.helper.Constant;
import wrteam.ecommerce.app.helper.VolleyCallback;
import wrteam.ecommerce.app.model.OrderTracker;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class TrackerDetailActivity extends AppCompatActivity {

    OrderTracker order;
    TextView txtcanceldetail, txtotherdetails, txtorderid, txtorderdate, txtcancel, txtqty, txtprice, txtpaytype, txtstatus, txtstatusdate, txtname, txtOriginalPrice, txtDiscountedPrice, txtMeasurement;
    NetworkImageView imgorder;
    SpannableString spannableString;
    Toolbar toolbar;

    RecyclerView recyclerView;
    public static Button btnCancel;
    public static LinearLayout lyttracker;
    View l4;
    LinearLayout returnLyt;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker_detail);

        order = (OrderTracker) getIntent().getSerializableExtra("model");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txtorderid = findViewById(R.id.txtorderid);
        txtorderdate = findViewById(R.id.txtorderdate);
        txtcancel = findViewById(R.id.txtcancel);
        txtqty = findViewById(R.id.txtqty);
        txtprice = findViewById(R.id.txtprice);
        txtpaytype = findViewById(R.id.txtpaytype);
        txtstatus = findViewById(R.id.txtstatus);
        txtstatusdate = findViewById(R.id.txtstatusdate);
        txtname = findViewById(R.id.txtname);
        imgorder = findViewById(R.id.imgorder);
        txtOriginalPrice = findViewById(R.id.txtoriginalprice);
        txtDiscountedPrice = findViewById(R.id.txtdiscountPrice);
        txtMeasurement = findViewById(R.id.txtmeasurement);
        txtotherdetails = findViewById(R.id.txtotherdetails);
        txtcanceldetail = findViewById(R.id.txtcanceldetail);
        lyttracker = findViewById(R.id.lyttracker);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setNestedScrollingEnabled(false);
        btnCancel = findViewById(R.id.btncancel);
        l4 = findViewById(R.id.l4);
        returnLyt = findViewById(R.id.returnLyt);

        String[] date = order.getDate_added().split("\\s+");
        txtorderid.setText(order.getOrder_id());
        txtorderdate.setText(date[0]);
        txtotherdetails.setText("Name : " + order.getUsername() + "\nMobile No : " + order.getMobile() + "\nAddress : " + order.getAddress());

        if (!order.getStatus().equalsIgnoreCase("delivered") && !order.getStatus().equalsIgnoreCase("cancelled") && !order.getStatus().equalsIgnoreCase("returned")) {
            txtstatus.setText(getResources().getString(R.string.trackingstatus));
            btnCancel.setVisibility(View.VISIBLE);
        } else {
            txtstatus.setText(order.getStatus() + " on");
            btnCancel.setVisibility(View.GONE);
        }
        if (order.getStatus().equalsIgnoreCase("cancelled")) {
            lyttracker.setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);
            txtcanceldetail.setVisibility(View.VISIBLE);
            txtcanceldetail.setText("Order Cancelled On : " + order.getStatusdate());

        } else {

            if (order.getStatus().equals("returned")) {
                l4.setVisibility(View.VISIBLE);
                returnLyt.setVisibility(View.VISIBLE);
            }
            lyttracker.setVisibility(View.VISIBLE);
            for (int i = 0; i < order.getOrderStatusArrayList().size(); i++) {
                int img = getResources().getIdentifier("img" + i, "id", getPackageName());
                int view = getResources().getIdentifier("l" + i, "id", getPackageName());
                int txt = getResources().getIdentifier("txt" + i, "id", getPackageName());
                int textview = getResources().getIdentifier("txt" + i + "" + i, "id", getPackageName());

                // System.out.println("===============" + img + " == " + view);

                if (img != 0 && findViewById(img) != null) {
                    ImageView imageView = findViewById(img);
                    imageView.setColorFilter(getResources().getColor(R.color.colorPrimary));
                }

                if (view != 0 && findViewById(view) != null) {
                    View view1 = findViewById(view);
                    view1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }

                if (txt != 0 && findViewById(txt) != null) {
                    TextView view1 = findViewById(txt);
                    view1.setTextColor(getResources().getColor(R.color.black));
                }

                if (textview != 0 && findViewById(textview) != null) {
                    TextView view1 = findViewById(textview);
                    String str = order.getDate_added();
                    String[] splited = str.split("\\s+");
                    view1.setText(splited[0] + "\n" + splited[1]);
                }
            }
        }
        recyclerView.setAdapter(new ItemsAdapter(TrackerDetailActivity.this, order.itemsList, "detail"));

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void OnBtnClick(View view) {
        int id = view.getId();
        if (id == R.id.btncancel) {

            new AlertDialog.Builder(TrackerDetailActivity.this)
                    .setTitle("Cancel Order")
                    .setMessage("Are you sure , you want to Cancel Order ? ")
                    .setPositiveButton("Cancel Order", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put(Constant.UPDATE_ORDER_STATUS, Constant.GetVal);
                            params.put(Constant.ID, order.getOrder_id());
                            params.put(Constant.STATUS, Constant.CANCELLED);

                            ApiConfig.RequestToVolley(new VolleyCallback() {
                                @Override
                                public void onSuccess(boolean result, String response) {
                                    //   System.out.println("=================*cancelorder- " + response);
                                    if (result) {
                                        try {
                                            JSONObject objectbject = new JSONObject(response);
                                            if (!objectbject.getBoolean(Constant.ERROR)) {
                                                //startActivity(new Intent(TrackerDetailActivity.this, MainActivity.class).putExtra("from", "summary"));
                                                //finishAffinity();

                                                Constant.isOrderCancelled = true;
                                                finish();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }, TrackerDetailActivity.this, Constant.ORDERPROCESS_URL, params, true);
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    }
}

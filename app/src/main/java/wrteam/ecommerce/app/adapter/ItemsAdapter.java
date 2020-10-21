package wrteam.ecommerce.app.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.zip.CheckedOutputStream;

import wrteam.ecommerce.app.R;
import wrteam.ecommerce.app.activity.TrackerDetailActivity;
import wrteam.ecommerce.app.helper.ApiConfig;
import wrteam.ecommerce.app.helper.Constant;
import wrteam.ecommerce.app.helper.VolleyCallback;
import wrteam.ecommerce.app.model.OrderTracker;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.CartItemHolder> {

    Activity activity;
    ArrayList<OrderTracker> orderTrackerArrayList;
    String from = "";

    public ItemsAdapter(Activity activity, ArrayList<OrderTracker> orderTrackerArrayList) {
        this.activity = activity;
        this.orderTrackerArrayList = orderTrackerArrayList;
    }

    public ItemsAdapter(Activity activity, ArrayList<OrderTracker> orderTrackerArrayList, String from) {
        this.activity = activity;
        this.orderTrackerArrayList = orderTrackerArrayList;
        this.from = from;
    }

    @Override
    public CartItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_lyt, null);
        CartItemHolder cartItemHolder = new CartItemHolder(v);
        return cartItemHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final CartItemHolder holder, final int position) {

        final OrderTracker order = orderTrackerArrayList.get(position);
        holder.txtqty.setText(order.getQuantity());
        holder.txtprice.setText(Constant.SETTING_CURRENCY_SYMBOL + order.getPrice());
        holder.txtpaytype.setText("via " + order.getPayment_method());
        holder.txtstatus.setText(order.getActiveStatus());
        holder.txtstatusdate.setText(order.getActiveStatusDate());
        holder.txtname.setText(order.getName());
        holder.imgorder.setDefaultImageResId(R.drawable.placeholder);
        holder.imgorder.setErrorImageResId(R.drawable.placeholder);
        holder.imgorder.setImageUrl(order.getImage(), Constant.imageLoader);

        holder.carddetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, TrackerDetailActivity.class).putExtra("model", order));
            }
        });

        holder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateOrderStatus(activity, order, Constant.CANCELLED, holder, from);
            }
        });
        holder.btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

                Date date = new Date();
                System.out.println(myFormat.format(date));
                String inputString1 = order.getActiveStatusDate();
                String inputString2 = myFormat.format(date);
                try {
                    Date date1 = myFormat.parse(inputString1);
                    Date date2 = myFormat.parse(inputString2);
                    long diff = date2.getTime() - date1.getTime();
                    //  System.out.println("Days: "+TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));

                    if (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) <= Constant.ORDER_DAY_LIMIT) {
                        updateOrderStatus(activity, order, Constant.RETURNED, holder, from);

                    } else {
                        final Snackbar snackbar = Snackbar.make(activity.findViewById(android.R.id.content), "Product return " + Constant.ORDER_DAY_LIMIT + "day maximum limit", Snackbar.LENGTH_INDEFINITE);
                        snackbar.setAction("ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                snackbar.dismiss();

                            }
                        });
                        snackbar.setActionTextColor(Color.RED);
                        View snackbarView = snackbar.getView();
                        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
                        textView.setMaxLines(5);
                        snackbar.show();

                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        if (from.equals("detail")) {

            if (order.getActiveStatus().equalsIgnoreCase("cancelled")) {
                holder.txtstatus.setTextColor(Color.RED);
                holder.btnCancel.setVisibility(View.GONE);
            } else if (order.getActiveStatus().equalsIgnoreCase("delivered")) {
                holder.btnCancel.setVisibility(View.GONE);
                holder.btnReturn.setVisibility(View.VISIBLE);
            } else if (order.getActiveStatus().equalsIgnoreCase("returned")) {
                holder.btnCancel.setVisibility(View.GONE);
                holder.btnReturn.setVisibility(View.GONE);
            } else {
                holder.btnCancel.setVisibility(View.VISIBLE);
            }

            holder.lyttracker.setVisibility(View.VISIBLE);

            if (order.getActiveStatus().equalsIgnoreCase("cancelled")) {
                holder.lyttracker.setVisibility(View.GONE);
            } else {
                if (order.getActiveStatus().equals("returned")) {
                    holder.l4.setVisibility(View.VISIBLE);
                    holder.returnLyt.setVisibility(View.VISIBLE);
                }
                holder.lyttracker.setVisibility(View.VISIBLE);

                ApiConfig.setOrderTrackerLayout(activity, order, holder);
            }
        }
    }

    private void updateOrderStatus(final Activity activity, final OrderTracker order, final String status, final CartItemHolder holder, final String from) {
        final Map<String, String> params = new HashMap<>();
        params.put(Constant.UPDATE_ORDER_ITEM_STATUS, Constant.GetVal);
        params.put(Constant.ORDER_ITEM_ID, order.getId());
        params.put(Constant.ORDER_ID, order.getOrder_id());
        params.put(Constant.STATUS, status);
        // params.put(Constant.ID, sessionpsw.getData(Session.KEY_ID));

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        // Setting Dialog Message
        if (status.equals(Constant.CANCELLED)) {
            alertDialog.setTitle(activity.getResources().getString(R.string.cancel_order));
            alertDialog.setMessage(activity.getResources().getString(R.string.cancel_msg));
        } else if (status.equals(Constant.RETURNED)) {
            alertDialog.setTitle(activity.getResources().getString(R.string.return_order));
            alertDialog.setMessage(activity.getResources().getString(R.string.return_msg));
        }
        alertDialog.setCancelable(false);
        final AlertDialog alertDialog1 = alertDialog.create();

        // Setting OK Button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ApiConfig.RequestToVolley(new VolleyCallback() {
                    @Override
                    public void onSuccess(boolean result, String response) {
                        // System.out.println("================= " + response);
                        if (result) {
                            try {
                                JSONObject objectbject = new JSONObject(response);
                                if (!objectbject.getBoolean(Constant.ERROR)) {
                                    if (status.equals(Constant.CANCELLED)) {
                                        Toast.makeText(activity, activity.getResources().getString(R.string.cancel_success), Toast.LENGTH_LONG).show();
                                        holder.btnCancel.setVisibility(View.GONE);
                                        holder.txtstatus.setText(status);
                                        holder.txtstatus.setTextColor(Color.RED);
                                        holder.lyttracker.setVisibility(View.GONE);
                                        order.status = status;
                                        if (from.equals("detail")) {
                                            if (orderTrackerArrayList.size() == 1) {
                                                TrackerDetailActivity.btnCancel.setVisibility(View.GONE);
                                                TrackerDetailActivity.lyttracker.setVisibility(View.GONE);
                                            }
                                        }

                                    } else {
                                        Toast.makeText(activity, activity.getResources().getString(R.string.request_success), Toast.LENGTH_LONG).show();
                                        holder.btnReturn.setVisibility(View.GONE);
                                        holder.txtstatus.setText(status);
                                    }
                                    Constant.isOrderCancelled = true;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, activity, Constant.ORDERPROCESS_URL, params, true);

            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog1.dismiss();
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }

    public class CartItemHolder extends RecyclerView.ViewHolder {
        TextView txtqty, txtprice, txtpaytype, txtstatus, txtstatusdate, txtname;
        NetworkImageView imgorder;
        CardView carddetail;
        RecyclerView recyclerView;
        Button btnCancel, btnReturn;
        View l4;
        LinearLayout lyttracker, returnLyt;

        public CartItemHolder(View itemView) {
            super(itemView);

            txtqty = itemView.findViewById(R.id.txtqty);
            txtprice = itemView.findViewById(R.id.txtprice);
            txtpaytype = itemView.findViewById(R.id.txtpaytype);
            txtstatus = itemView.findViewById(R.id.txtstatus);
            txtstatusdate = itemView.findViewById(R.id.txtstatusdate);
            txtname = itemView.findViewById(R.id.txtname);
            btnCancel = itemView.findViewById(R.id.btnCancel);
            imgorder = itemView.findViewById(R.id.imgorder);
            carddetail = itemView.findViewById(R.id.carddetail);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            btnReturn = itemView.findViewById(R.id.btnReturn);
            lyttracker = itemView.findViewById(R.id.lyttracker);
            l4 = itemView.findViewById(R.id.l4);
            returnLyt = itemView.findViewById(R.id.returnLyt);
        }
    }


    @Override
    public int getItemCount() {
        return orderTrackerArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}

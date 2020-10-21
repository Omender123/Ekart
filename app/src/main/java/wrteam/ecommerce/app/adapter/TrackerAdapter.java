package wrteam.ecommerce.app.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;
import com.google.android.gms.common.api.Api;

import wrteam.ecommerce.app.R;
import wrteam.ecommerce.app.activity.TrackerDetailActivity;
import wrteam.ecommerce.app.helper.ApiConfig;
import wrteam.ecommerce.app.model.OrderTracker;
import java.util.ArrayList;

public class TrackerAdapter extends RecyclerView.Adapter<TrackerAdapter.CartItemHolder> {

    Activity activity;
    ArrayList<OrderTracker> orderTrackerArrayList;

    public TrackerAdapter(Activity activity, ArrayList<OrderTracker> orderTrackerArrayList) {
        this.activity = activity;
        this.orderTrackerArrayList = orderTrackerArrayList;
    }

    @Override
    public CartItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_trackorder, null);
        CartItemHolder cartItemHolder = new CartItemHolder(v);
        return cartItemHolder;
    }

    @Override
    public void onBindViewHolder(final CartItemHolder holder, final int position) {
        final OrderTracker order = orderTrackerArrayList.get(position);
        holder.txtorderid.setText(order.getOrder_id());
        String[] date = order.getDate_added().split("\\s+");
        holder.txtorderdate.setText(date[0]);

        holder.carddetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, TrackerDetailActivity.class).putExtra("model", order));
            }
        });

        if (order.getStatus().equalsIgnoreCase("cancelled")) {
            holder.lyttracker.setVisibility(View.GONE);
        } else {
            if (order.getStatus().equals("returned")) {
                holder.l4.setVisibility(View.VISIBLE);
                holder.returnLyt.setVisibility(View.VISIBLE);
            }
            holder.lyttracker.setVisibility(View.VISIBLE);
            ApiConfig.setOrderTrackerLayout(activity,order,holder);
        }
        if (!order.getStatus().equalsIgnoreCase("delivered") && !order.getStatus().equalsIgnoreCase("cancelled")) {
            holder.txtstatus.setText(activity.getResources().getString(R.string.trackingstatus));
            holder.txtcancel.setVisibility(View.GONE);
        } else {
            holder.txtstatus.setText(order.getStatus() + " on");
            holder.txtcancel.setVisibility(View.GONE);
        }

        holder.recyclerView.setAdapter(new ItemsAdapter(activity, orderTrackerArrayList.get(position).itemsList));


    }

    public class CartItemHolder extends RecyclerView.ViewHolder {
        TextView txtorderid, txtorderdate, txtcancel, txtqty, txtprice, txtpaytype, txtstatus, txtstatusdate, txtname;
        NetworkImageView imgorder;
        LinearLayout lyttracker, returnLyt;
        CardView carddetail;
        RecyclerView recyclerView;
        View l4;

        public CartItemHolder(View itemView) {
            super(itemView);
            txtorderid = itemView.findViewById(R.id.txtorderid);
            txtorderdate = itemView.findViewById(R.id.txtorderdate);
            txtcancel = itemView.findViewById(R.id.txtcancel);
            txtqty = itemView.findViewById(R.id.txtqty);
            txtprice = itemView.findViewById(R.id.txtprice);
            txtpaytype = itemView.findViewById(R.id.txtpaytype);
            txtstatus = itemView.findViewById(R.id.txtstatus);
            txtstatusdate = itemView.findViewById(R.id.txtstatusdate);
            txtname = itemView.findViewById(R.id.txtname);
            imgorder = itemView.findViewById(R.id.imgorder);
            lyttracker = itemView.findViewById(R.id.lyttracker);
            l4 = itemView.findViewById(R.id.l4);
            returnLyt = itemView.findViewById(R.id.returnLyt);
            carddetail = itemView.findViewById(R.id.carddetail);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(activity));

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

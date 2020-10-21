package wrteam.ecommerce.app.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import wrteam.ecommerce.app.R;
import wrteam.ecommerce.app.activity.ProductDetailActivity;
import wrteam.ecommerce.app.helper.AppController;
import wrteam.ecommerce.app.model.Product;

/**
 * Created by shree1 on 3/16/2017.
 */

public class AdapterStyle1 extends RecyclerView.Adapter<AdapterStyle1.VideoHolder> {

    public ArrayList<Product> productList;

    public Activity activity;
    public int itemResource;
    ImageLoader netImageLoader = AppController.getInstance().getImageLoader();

    public AdapterStyle1(Activity activity, ArrayList<Product> productList, int itemResource) {
        this.activity = activity;
        this.productList = productList;
        this.itemResource = itemResource;

    }

    public class VideoHolder extends RecyclerView.ViewHolder {

        public NetworkImageView thumbnail;
        public TextView v_title, v_date, description;
        public RelativeLayout relativeLayout;

        public VideoHolder(View itemView) {
            super(itemView);
            thumbnail = (NetworkImageView) itemView.findViewById(R.id.thumbnail);
            v_title = (TextView) itemView.findViewById(R.id.title);
            v_date = (TextView) itemView.findViewById(R.id.date);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.play_layout);

        }


    }


    @Override
    public int getItemCount() {
        int product;
        if (productList.size() > 4) {
            product = 4;
        } else {
            product = productList.size();
        }
        return product;
    }

    @Override
    public void onBindViewHolder(VideoHolder holder, final int position) {
        final Product product = productList.get(position);
        holder.thumbnail.setImageUrl(product.getImage(), netImageLoader);
        holder.v_title.setText(product.getName());
 /*       if (product.getExtraList().get(0).getDiscountedPrice() != 0) {
            holder.v_date.setText(product.getExtraList().get(0).getPercent() + "% off");
        } else {
            holder.v_date.setVisibility(View.INVISIBLE);
        }*/

        // System.out.println("-----product id "+product.getProductName());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                        activity.startActivity(new Intent(activity, ProductDetailActivity.class).putExtra("vpos",0).putExtra("model",product));


            }
        });
    }

    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(itemResource, parent, false);
        return new VideoHolder(view);
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

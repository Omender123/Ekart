package wrteam.ecommerce.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;

import wrteam.ecommerce.app.R;
import wrteam.ecommerce.app.activity.ProductDetailActivity;
import wrteam.ecommerce.app.helper.Constant;
import wrteam.ecommerce.app.model.Product;

import java.util.ArrayList;

public class FeaturedProductAdapter extends RecyclerView.Adapter<FeaturedProductAdapter.ViewHolder> {

    public ArrayList<Product> pro;
    Context context;
    int layout;

    public FeaturedProductAdapter(ArrayList<Product> pro, int layout, Context context) {
        this.pro = pro;
        this.layout = layout;
        this.context = context;
    }

    @NonNull
    @Override
    public FeaturedProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new FeaturedProductAdapter.ViewHolder(view);
    }

    @NonNull

    @Override
    public void onBindViewHolder(@NonNull final FeaturedProductAdapter.ViewHolder holder, int position) {
        final Product model = pro.get(position);
        holder.txttitle.setText(model.getName());
        holder.imgcategory.setDefaultImageResId(R.drawable.placeholder);
        holder.imgcategory.setErrorImageResId(R.drawable.placeholder);
        holder.imgcategory.setImageUrl(model.getImage(), Constant.imageLoader);

        holder.lytmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ProductDetailActivity.class).putExtra("vpos", 0).putExtra("model", model));
            }
        });
    }

    @Override
    public int getItemCount() {
        return pro.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txttitle;
        NetworkImageView imgcategory;
        CardView lytmain;

        public ViewHolder(View itemView) {
            super(itemView);
            lytmain = itemView.findViewById(R.id.lytmain);
            imgcategory = itemView.findViewById(R.id.imgcategory);
            txttitle = itemView.findViewById(R.id.txttitle);
        }

    }
}

package wrteam.ecommerce.app.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import wrteam.ecommerce.app.R;
import wrteam.ecommerce.app.activity.ProductListActivity;
import wrteam.ecommerce.app.activity.SubCategoryActivity;
import wrteam.ecommerce.app.helper.Constant;
import wrteam.ecommerce.app.model.Category;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    public ArrayList<Category> categorylist;
    int layout;
    String from = "";
    Activity activity;

    public CategoryAdapter(ArrayList<Category> categorylist, int layout) {
        this.categorylist = categorylist;
        this.layout = layout;
    }

    public CategoryAdapter(ArrayList<Category> categorylist, int layout, String from) {
        this.categorylist = categorylist;
        this.layout = layout;
        this.from = from;
    }

    public CategoryAdapter(Activity activity, ArrayList<Category> categorylist, int layout, String from) {
        this.categorylist = categorylist;
        this.layout = layout;
        this.activity = activity;
        this.from = from;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ViewHolder(view);
    }

    @NonNull
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Category model = categorylist.get(position);
        holder.txttitle.setText(model.getName());
        holder.imgcategory.setDefaultImageResId(R.drawable.placeholder);
        holder.imgcategory.setErrorImageResId(R.drawable.placeholder);
        holder.imgcategory.setImageUrl(model.getImage(), Constant.imageLoader);
        holder.lytMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                if (from.equals("cate")) {
                    intent = new Intent(activity, SubCategoryActivity.class);
                } else if (from.equals("sub_cate")) {
                    intent = new Intent(activity, ProductListActivity.class);
                    intent.putExtra("from","regular");
                }
                intent.putExtra("id", model.getId());
                intent.putExtra("name", model.getName());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categorylist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txttitle;
        NetworkImageView imgcategory;
        LinearLayout lytMain;

        public ViewHolder(View itemView) {
            super(itemView);
            lytMain = itemView.findViewById(R.id.lytMain);
            imgcategory = itemView.findViewById(R.id.imgcategory);
            txttitle = itemView.findViewById(R.id.txttitle);
        }

    }
}

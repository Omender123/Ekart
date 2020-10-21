package wrteam.ecommerce.app.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import wrteam.ecommerce.app.R;
import wrteam.ecommerce.app.adapter.ProductLoadMoreAdapter;
import wrteam.ecommerce.app.helper.ApiConfig;
import wrteam.ecommerce.app.helper.AppController;
import wrteam.ecommerce.app.helper.Constant;
import wrteam.ecommerce.app.helper.DatabaseHelper;
import wrteam.ecommerce.app.helper.VolleyCallback;
import wrteam.ecommerce.app.model.Product;

public class ProductListActivity extends AppCompatActivity {

    private String name, id, filterBy, from;
    private RecyclerView recyclerView;
    private ArrayList<Product> productArrayList;
    private ProductLoadMoreAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int offset, filterIndex;

    private TextView tvAlert;
    private DatabaseHelper databaseHelper;
    private Menu menu;
    private String GET_PRODUCT_URL;
    int total;
    Toolbar toolbar;
    int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        from = getIntent().getStringExtra("from");
        name = getIntent().getStringExtra("name");
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        databaseHelper = new DatabaseHelper(ProductListActivity.this);
        tvAlert = findViewById(R.id.txtnodata);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(ProductListActivity.this));
        productArrayList = new ArrayList<>();
        filterIndex = -1;
        if (from.equals("regular")) {

            id = getIntent().getStringExtra("id");
            GET_PRODUCT_URL = Constant.GET_PRODUCT_BY_SUB_CATE;
            ReLoadData();
        } else {
            position = getIntent().getIntExtra("position", 0);
            productArrayList = MainActivity.sectionList.get(position).getProductList();
            mAdapter = new ProductLoadMoreAdapter(ProductListActivity.this, productArrayList, recyclerView, R.layout.lyt_item_list);
            recyclerView.setAdapter(mAdapter);
        }
        mSwipeRefreshLayout = findViewById(R.id.swipeLayout);
        // mSwipeRefreshLayout.setColorSchemeColors(Color.YELLOW);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ReLoadData();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });

    }

    private void GetData(final int startoffset) {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.SUB_CATEGORY_ID, id);
        params.put(Constant.LIMIT, Constant.LOAD_ITEM_LIMIT);
        params.put(Constant.OFFSET, startoffset + "");
        if (filterIndex != -1)
            params.put(Constant.SORT, filterBy);

        ApiConfig.RequestToVolley(new VolleyCallback() {
            @Override
            public void onSuccess(boolean result, String response) {

                if (result) {
                    try {
                        JSONObject objectbject = new JSONObject(response);
                        if (!objectbject.getBoolean(Constant.ERROR)) {
                            total = Integer.parseInt(objectbject.getString(Constant.TOTAL));
                            if (startoffset == 0) {
                                productArrayList = new ArrayList<>();
                                tvAlert.setVisibility(View.GONE);
                            }
                            JSONObject object = new JSONObject(response);
                            JSONArray jsonArray = object.getJSONArray(Constant.DATA);
                            offset = startoffset + Integer.parseInt(Constant.LOAD_ITEM_LIMIT);
                            productArrayList.addAll(ApiConfig.GetProductList(jsonArray));

                            if (startoffset == 0) {
                                mAdapter = new ProductLoadMoreAdapter(ProductListActivity.this, productArrayList, recyclerView, R.layout.lyt_item_list);
                                mAdapter.setHasStableIds(true);
                                recyclerView.setAdapter(mAdapter);
                                mAdapter.setOnLoadMoreListener(new ProductLoadMoreAdapter.OnLoadMoreListener() {
                                    @Override
                                    public void onLoadMore() {
                                        if (productArrayList.size() < total) {
                                            productArrayList.add(null);
                                            mAdapter.notifyItemInserted(productArrayList.size() - 1);
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    productArrayList.remove(productArrayList.size() - 1);
                                                    mAdapter.notifyItemRemoved(productArrayList.size());

                                                    if (productArrayList.contains(null)) {
                                                        for (int i = 0; i < productArrayList.size(); i++) {
                                                            if (productArrayList.get(i) == null) {
                                                                productArrayList.remove(i);
                                                                break;
                                                            }
                                                        }
                                                    }
                                                    offset = startoffset + Integer.parseInt(Constant.LOAD_ITEM_LIMIT);
                                                    Map<String, String> params = new HashMap<>();
                                                    params.put(Constant.SUB_CATEGORY_ID, id);
                                                    params.put(Constant.LIMIT, Constant.LOAD_ITEM_LIMIT);
                                                    params.put(Constant.OFFSET, offset + "");
                                                    if (filterIndex != -1)
                                                        params.put(Constant.SORT, filterBy);
                                                    ApiConfig.RequestToVolley(new VolleyCallback() {
                                                        @Override
                                                        public void onSuccess(boolean result, String response) {
                                                            // System.out.println("=================*loadmore " + type + " = " + id + " start " + startoffset + " = " /*+ response*/);
                                                            if (result) {
                                                                try {
                                                                    // System.out.println("====product  " + response);
                                                                    JSONObject objectbject = new JSONObject(response);
                                                                    if (!objectbject.getBoolean(Constant.ERROR)) {

                                                                        JSONObject object = new JSONObject(response);
                                                                        JSONArray jsonArray = object.getJSONArray(Constant.DATA);
                                                                        productArrayList.remove(productArrayList.size() - 1);
                                                                        mAdapter.notifyItemRemoved(productArrayList.size());
                                                                        productArrayList.addAll(ApiConfig.GetProductList(jsonArray));
                                                                        mAdapter.notifyDataSetChanged();
                                                                        mAdapter.setLoaded();
                                                                    }
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        }
                                                    }, ProductListActivity.this, GET_PRODUCT_URL, params, false);

                                                }
                                            }, 3000);
                                        }

                                    }
                                });
                            }
                        } else {
                            if (startoffset == 0) {
                                tvAlert.setVisibility(View.VISIBLE);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, ProductListActivity.this, GET_PRODUCT_URL, params, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mAdapter != null && !Constant.SELECTEDPRODUCT_POS.equals("")) {
            if (!databaseHelper.getFavouriteById(Constant.SELECTEDPRODUCT_POS.split("=")[1])) {
                mAdapter.notifyItemChanged(Integer.parseInt(Constant.SELECTEDPRODUCT_POS.split("=")[0]));
                Constant.SELECTEDPRODUCT_POS = "";
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (from.equals("section"))
            menu.findItem(R.id.menu_sort).setVisible(false);
        menu.findItem(R.id.menu_search).setVisible(true);
        menu.findItem(R.id.menu_cart).setIcon(ApiConfig.buildCounterDrawable(databaseHelper.getTotalItemOfCart(), R.drawable.ic_cart, ProductListActivity.this));
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_search:
                startActivity(new Intent(ProductListActivity.this, SearchActivity.class).putExtra("from", Constant.FROMSEARCH));
                return true;
            case R.id.menu_cart:
                startActivity(new Intent(ProductListActivity.this, CartActivity.class));
                return true;
            case R.id.menu_sort:

                AlertDialog.Builder builder = new AlertDialog.Builder(ProductListActivity.this);
                builder.setTitle(ProductListActivity.this.getResources().getString(R.string.filterby));
                builder.setSingleChoiceItems(Constant.filtervalues, filterIndex, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        filterIndex = item;
                        switch (item) {
                            case 0:
                                filterBy = Constant.NEW;
                                break;
                            case 1:
                                filterBy = Constant.OLD;
                                break;
                            case 2:
                                filterBy = Constant.HIGH;
                                break;
                            case 3:
                                filterBy = Constant.LOW;
                                break;
                        }
                        if (item != -1)
                            ReLoadData();
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
        }
        return onOptionsItemSelected(item);
    }

    private void ReLoadData() {
        if (AppController.isConnected(ProductListActivity.this)) {
            GetData(0);
        }
    }
}


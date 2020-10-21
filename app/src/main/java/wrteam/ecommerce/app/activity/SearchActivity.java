package wrteam.ecommerce.app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import wrteam.ecommerce.app.R;
import wrteam.ecommerce.app.adapter.ProductAdapter;
import wrteam.ecommerce.app.helper.ApiConfig;
import wrteam.ecommerce.app.helper.Constant;
import wrteam.ecommerce.app.helper.DatabaseHelper;
import wrteam.ecommerce.app.helper.VolleyCallback;
import wrteam.ecommerce.app.model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    RecyclerView suggestionView, recycleview;

    ArrayList<Product> productArrayList;
    SearchView searchView;
    Toolbar toolbar;
    public SwipeRefreshLayout mSwipeRefreshLayout;
    String query;
    String from;
    TextView noResult, msg;
    public ProgressBar progressBar;
    ProductAdapter productAdapter;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        from = getIntent().getStringExtra("from");
        suggestionView = findViewById(R.id.suggestionView);
        recycleview = findViewById(R.id.recycleview);
        databaseHelper = new DatabaseHelper(SearchActivity.this);
        productArrayList = new ArrayList<>();
        searchView = findViewById(R.id.searchview);
        noResult = findViewById(R.id.noResult);
        msg =  findViewById(R.id.msg);
        progressBar =  findViewById(R.id.pBar);
        progressBar.setVisibility(View.INVISIBLE);

        recycleview.setLayoutManager(new LinearLayoutManager(this));
        suggestionView.setLayoutManager(new LinearLayoutManager(this));
        suggestionView.setVisibility(View.GONE);

        mSwipeRefreshLayout = findViewById(R.id.swipeLayout);
        mSwipeRefreshLayout.setColorSchemeColors(Color.YELLOW);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (from != null && from.equalsIgnoreCase(Constant.FROMSEARCH))
                    SearchRequest(query);
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                }, 1000);
            }
        });

        //AutoCompleteTextView searchTextView = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        //searchTextView.setBackgroundColor(getResources().getColor(R.color.white));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.equals("") || newText.length() != 0) {
                    query = newText;
                    if (from != null && from.equalsIgnoreCase(Constant.FROMSEARCH))
                        SearchRequest(newText);
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem item = menu.findItem(R.id.search);
        item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                if (suggestionView.getVisibility() == View.VISIBLE) {
                    suggestionView.setVisibility(View.GONE);
                }
                return true;
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }
        });

        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) item.getActionView();
        //searchView.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        searchView.setSearchableInfo(manager.getSearchableInfo(SearchActivity.this.getComponentName()));

        AutoCompleteTextView searchTextView = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.set(searchTextView, R.drawable.cursor_color);
        } catch (Exception e) {}

        searchView.setMaxWidth( Integer.MAX_VALUE );
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint(getString(R.string.search));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                *//*Intent intent = new Intent(SearchSuggestion.this, SearchActivity.class);
                intent.putExtra("query", "" + query);
                startActivity(intent);*//*
                suggestionView.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.equals("") || newText.length() != 0) {
                    query = newText;
                    if (from != null && from.equalsIgnoreCase(Constant.FROMSEARCH))
                        SearchRequest(newText);
                }
                return false;
            }
        });

        return true;

    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);

    }*/

    public void SearchRequest(final String query) {  //json request for product search
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constant.TYPE, Constant.PRODUCT_SEARCH);
        params.put(Constant.SEARCH, query);


        ApiConfig.RequestToVolley(new VolleyCallback() {
            @Override
            public void onSuccess(boolean result, String response) {
                //System.out.println("========search result " + response);
                if (result) {
                    try {
                        productArrayList = new ArrayList<>();
                        JSONObject objectbject = new JSONObject(response);
                        if (!objectbject.getBoolean(Constant.ERROR)) {


                            JSONObject object = new JSONObject(response);
                            JSONArray jsonArray = object.getJSONArray(Constant.DATA);

                            productArrayList = ApiConfig.GetProductList(jsonArray);
                            productAdapter = new ProductAdapter(productArrayList, R.layout.lyt_item_list, SearchActivity.this);
                            recycleview.setAdapter(productAdapter);
                            noResult.setVisibility(View.GONE);
                            msg.setVisibility(View.GONE);
                        }else{

                                noResult.setVisibility(View.VISIBLE);
                                msg.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            productArrayList.clear();
                            recycleview.setAdapter(new ProductAdapter(productArrayList, R.layout.lyt_item_list, SearchActivity.this));


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, SearchActivity.this, Constant.PRODUCT_SEARCH_URL, params, false);

    }

    @Override
    public void onResume() {
        super.onResume();

        if (productAdapter != null && !Constant.SELECTEDPRODUCT_POS.equals("")) {
            if (!databaseHelper.getFavouriteById(Constant.SELECTEDPRODUCT_POS.split("=")[1])) {
                productAdapter.notifyItemChanged(Integer.parseInt(Constant.SELECTEDPRODUCT_POS.split("=")[0]));
                Constant.SELECTEDPRODUCT_POS = "";
            }
        }
    }

}

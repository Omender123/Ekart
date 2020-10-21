package wrteam.ecommerce.app.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import wrteam.ecommerce.app.R;
import wrteam.ecommerce.app.adapter.CategoryAdapter;
import wrteam.ecommerce.app.adapter.OfferAdapter;
import wrteam.ecommerce.app.adapter.SectionAdapter;
import wrteam.ecommerce.app.adapter.SliderAdapter;
import wrteam.ecommerce.app.helper.ApiConfig;
import wrteam.ecommerce.app.helper.AppController;
import wrteam.ecommerce.app.helper.Constant;
import wrteam.ecommerce.app.helper.DatabaseHelper;
import wrteam.ecommerce.app.helper.Session;

import wrteam.ecommerce.app.helper.VolleyCallback;
import wrteam.ecommerce.app.model.Category;
import wrteam.ecommerce.app.model.Slider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends DrawerActivity {
    boolean doubleBackToExitPressedOnce = false;
    DatabaseHelper databaseHelper;
    public static Session session;

    Toolbar toolbar;
    public RelativeLayout layoutSearch;
    Activity activity;
    public LinearLayout lytBottom;
    Menu menu;
    String from;
    private RecyclerView categoryRecyclerView, sectionView,offerView;
    private ArrayList<Slider> sliderArrayList;
    public static ArrayList<Category> categoryArrayList, sectionList;
    private ViewPager mPager;
    private LinearLayout mMarkersLayout;
    private int size;
    private Timer swipeTimer;
    private Handler handler;
    private Runnable Update;
    private int currentPage = 0;

    private LinearLayout lytCategory;
    NestedScrollView nestedScrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_main, frameLayout);

        databaseHelper = new DatabaseHelper(MainActivity.this);
        session = new Session(MainActivity.this);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        activity = MainActivity.this;

        from = getIntent().getStringExtra("from");

        lytBottom = findViewById(R.id.lytBottom);
        layoutSearch = findViewById(R.id.layoutSearch);
        layoutSearch.setVisibility(View.VISIBLE);

        categoryRecyclerView = findViewById(R.id.categoryrecycleview);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
        sectionView = findViewById(R.id.sectionView);
        sectionView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        sectionView.setNestedScrollingEnabled(false);

        offerView = findViewById(R.id.offerView);
        offerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        offerView.setNestedScrollingEnabled(false);

        nestedScrollView = findViewById(R.id.nestedScrollView);
        mMarkersLayout = findViewById(R.id.layout_markers);
        lytCategory = findViewById(R.id.lytCategory);
        mPager = findViewById(R.id.pager);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int position) {
                ApiConfig.addMarkers(position, sliderArrayList, mMarkersLayout, MainActivity.this);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

        mPager.setPageMargin(5);
        if (session.isUserLoggedIn()) {
            tvName.setText(session.getData(Session.KEY_NAME));
            tvEmail.setText(session.getData(Session.KEY_EMAIL));
        } else {
            tvName.setText(getResources().getString(R.string.is_login));
        }


        drawerToggle = new ActionBarDrawerToggle
                (
                        this,
                        drawer, toolbar,
                        R.string.drawer_open,
                        R.string.drawer_close
                ) {
        };
        String token = AppController.getInstance().getDeviceToken();
        if (!session.getData(Session.KEY_FCM_ID).equals(token)) {
            UpdateToken(token);
        }
       /* FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String token = instanceIdResult.getToken();
                if (!token.equals(AppController.getInstance().getDeviceToken())) {
                    UpdateToken(token);
                }
            }
        });*/

        if (AppController.isConnected(MainActivity.this)) {
            GetOfferImage();
            GetSlider();
            GetCategory();
            SectionProductRequest();
            ApiConfig.displayLocationSettingsRequest(MainActivity.this);
            ApiConfig.GetSettings(MainActivity.this);


        }
        ApiConfig.getLocation(MainActivity.this);

        if (savedInstanceState==null){
                navigationView.setCheckedItem(R.id.menu_home);
        }


    }


    public void GetOfferImage() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constant.GET_OFFER_IMAGE, Constant.GetVal);

        ApiConfig.RequestToVolley(new VolleyCallback() {
            @Override
            public void onSuccess(boolean result, String response) {
                if (result) {
                    try {
                        ArrayList<String> offerList = new ArrayList<>();
                        JSONObject objectbject = new JSONObject(response);
                        if (!objectbject.getBoolean(Constant.ERROR)) {
                            JSONArray jsonArray = objectbject.getJSONArray(Constant.DATA);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                offerList.add(object.getString(Constant.IMAGE));
                            }
                            offerView.setAdapter(new OfferAdapter(offerList, R.layout.offer_lyt));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, MainActivity.this, Constant.OFFER_URL, params, false);
    }
    private void GetCategory() {
        Map<String, String> params = new HashMap<String, String>();
        categoryArrayList = new ArrayList<>();

        ApiConfig.RequestToVolley(new VolleyCallback() {
            @Override
            public void onSuccess(boolean result, String response) {
                //System.out.println("======cate " + response);
                if (result) {
                    try {
                        JSONObject object = new JSONObject(response);
                        if (!object.getBoolean(Constant.ERROR)) {
                            JSONArray jsonArray = object.getJSONArray(Constant.DATA);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                categoryArrayList.add(new Category(jsonObject.getString(Constant.ID),
                                        jsonObject.getString(Constant.NAME),
                                        jsonObject.getString(Constant.SUBTITLE),
                                        jsonObject.getString(Constant.IMAGE)));

                            }
                            categoryRecyclerView.setAdapter(new CategoryAdapter(MainActivity.this, categoryArrayList, R.layout.lyt_category, "cate"));

                        } else {
                            lytCategory.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, MainActivity.this, Constant.CategoryUrl, params, false);
    }

    public void SectionProductRequest() {  //json request for product search
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.FeaturedProductUrl,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        // System.out.println("====res sec " + response);
                        try {
                            JSONObject object1 = new JSONObject(response);
                            if (!object1.getBoolean(Constant.ERROR)) {
                                sectionList = new ArrayList<>();
                                JSONArray jsonArray = object1.getJSONArray(Constant.SECTIONS);
                                for (int j = 0; j < jsonArray.length(); j++) {
                                    Category section = new Category();
                                    JSONObject jsonObject = jsonArray.getJSONObject(j);
                                    section.setName(jsonObject.getString(Constant.TITLE));
                                    section.setStyle(jsonObject.getString(Constant.SECTION_STYLE));
                                    section.setSubtitle(jsonObject.getString(Constant.SHORT_DESC));
                                    JSONArray productArray = jsonObject.getJSONArray(Constant.PRODUCTS);
                                    section.setProductList(ApiConfig.GetProductList(productArray));
                                    sectionList.add(section);

                                }
                                sectionView.setVisibility(View.VISIBLE);
                                SectionAdapter sectionAdapter = new SectionAdapter(MainActivity.this, sectionList);
                                sectionView.setAdapter(sectionAdapter);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.AccessKey, Constant.AccessKeyVal);
                params.put(Constant.GET_ALL_SECTIONS, "1");
                return params;
            }

        };

        AppController.getInstance().getRequestQueue().getCache().clear();
        AppController.getInstance().addToRequestQueue(stringRequest);

    }


    private void GetSlider() {

        Map<String, String> params = new HashMap<>();
        params.put(Constant.GET_SLIDER_IMAGE, Constant.GetVal);
        ApiConfig.RequestToVolley(new VolleyCallback() {
            @Override
            public void onSuccess(boolean result, String response) {
                if (result) {
                    // System.out.println("===slider res " + response);
                    sliderArrayList = new ArrayList<>();
                    try {
                        JSONObject object = new JSONObject(response);
                        if (!object.getBoolean(Constant.ERROR)) {
                            JSONArray jsonArray = object.getJSONArray(Constant.DATA);
                            size = jsonArray.length();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                sliderArrayList.add(new Slider(jsonObject.getString(Constant.TYPE), jsonObject.getString(Constant.TYPE_ID), jsonObject.getString(Constant.NAME), jsonObject.getString(Constant.IMAGE)));
                            }
                            mPager.setAdapter(new SliderAdapter(sliderArrayList, MainActivity.this, R.layout.lyt_slider, "home"));
                            ApiConfig.addMarkers(0, sliderArrayList, mMarkersLayout, MainActivity.this);
                            handler = new Handler();
                            Update = new Runnable() {
                                public void run() {
                                    if (currentPage == size) {
                                        currentPage = 0;
                                    }
                                    try {
                                        mPager.setCurrentItem(currentPage++, true);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            swipeTimer = new Timer();
                            swipeTimer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    handler.post(Update);
                                }
                            }, 3000, 3000);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, MainActivity.this, Constant.SliderUrl, params, false);

    }


    @Override
    public void onResume() {
        super.onResume();
        invalidateOptionsMenu();
        deepChangeTextColor(1);

    }


    public void OnClickBtn(View view) {
        int id = view.getId();

        if (id == R.id.lythome) {
            finish();
            startActivity(new Intent(MainActivity.this, MainActivity.class));

        } else if (id == R.id.lytcategory) {
            OnViewAllClick(view);
        } else if (id == R.id.lytfav) {
            deepChangeTextColor(3);
            startActivity(new Intent(MainActivity.this, FavouriteActivity.class));
        } else if (id == R.id.layoutSearch) {
            startActivity(new Intent(MainActivity.this, SearchActivity.class).putExtra("from", Constant.FROMSEARCH));
        } else if (id == R.id.lytcart) {
            OpenCart();
        }
    }


    public void deepChangeTextColor(int changeId) {
        for (int i = 1; i <= 4; i++) {
            int img = getResources().getIdentifier("img" + i, "id", getPackageName());
            int txt = getResources().getIdentifier("txt" + i, "id", getPackageName());

            TextView textView = findViewById(txt);
            ImageView imageView = findViewById(img);

            if (changeId == i) {
                imageView.setColorFilter(getResources().getColor(R.color.darkyellow));
                textView.setTextColor(getResources().getColor(R.color.darkyellow));
              //  textView.setVisibility(View.VISIBLE);
            } else {
                imageView.setColorFilter(getResources().getColor(R.color.light_gray));
                textView.setTextColor(getResources().getColor(R.color.light_gray));
              //  textView.setVisibility(View.GONE);
            }
        }
    }

    public void OnViewAllClick(View view) {
        startActivity(new Intent(MainActivity.this, CategoryActivity.class));
    }


    public static void UpdateToken(final String token) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.RegisterUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject object = new JSONObject(response);
                    if (!object.getBoolean(Constant.ERROR)) {
                        session.setData(Session.KEY_FCM_ID, token);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Constant.AccessKey, Constant.AccessKeyVal);
                params.put(Constant.TYPE, Constant.REGISTER_DEVICE);
                params.put(Constant.TOKEN, token);
                params.put(Constant.USER_ID, session.getData(Session.KEY_ID));
                return params;
            }
        };
        AppController.getInstance().getRequestQueue().getCache().clear();
        AppController.getInstance().addToRequestQueue(stringRequest);
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(navigationView))
            drawer.closeDrawers();
        else
            doubleBack();


    }

    public void doubleBack() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_search).setVisible(false);
        menu.findItem(R.id.menu_sort).setVisible(false);
        menu.findItem(R.id.menu_cart).setIcon(ApiConfig.buildCounterDrawable(databaseHelper.getTotalItemOfCart(), R.drawable.ic_cart, MainActivity.this));

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_cart:
                OpenCart();
                break;

            case android.R.id.home:
                // todo: goto back activity from here

                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

                return true;


            default:

        }
        return super.onOptionsItemSelected(item);
    }


    private void OpenCart() {

        startActivity(new Intent(MainActivity.this, CartActivity.class));

    }


}

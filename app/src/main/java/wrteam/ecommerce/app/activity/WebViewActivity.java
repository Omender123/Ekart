package wrteam.ecommerce.app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import wrteam.ecommerce.app.R;
import wrteam.ecommerce.app.helper.ApiConfig;
import wrteam.ecommerce.app.helper.AppController;
import wrteam.ecommerce.app.helper.Constant;
import wrteam.ecommerce.app.helper.VolleyCallback;

public class WebViewActivity extends AppCompatActivity {
    public ProgressBar prgLoading;
    public WebView mWebView;
    public String type;
    public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        type = getIntent().getStringExtra("type");
        prgLoading = findViewById(R.id.prgLoading);
        mWebView = findViewById(R.id.webView1);

        try {
            if (AppController.isConnected(this)) {
                mWebView.setClickable(true);
                mWebView.setFocusableInTouchMode(true);
                mWebView.getSettings().setJavaScriptEnabled(true);
                switch (type) {
                    case "privacy":
                        getSupportActionBar().setTitle(getString(R.string.privacy_policy));
                        GetContent("get_privacy", type);
                        break;
                    case "terms":
                        getSupportActionBar().setTitle(getString(R.string.terms_conditions));
                        GetContent("get_terms", type);
                        break;
                    case "contact":
                        getSupportActionBar().setTitle(getString(R.string.contact));
                        GetContent("get_contact", type);
                        break;
                    case "about":
                        getSupportActionBar().setTitle(getString(R.string.about));
                        GetContent("get_about_us", type);
                        break;
                    case "faq":
                        getSupportActionBar().setTitle(getString(R.string.faq));
                        mWebView.loadUrl(Constant.FAQ_URL);
                        prgLoading.setVisibility(View.GONE);
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void GetContent(String type, final String key) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constant.SETTINGS, Constant.GetVal);
        params.put(type, Constant.GetVal);

        ApiConfig.RequestToVolley(new VolleyCallback() {
            @Override
            public void onSuccess(boolean result, String response) {
                //  System.out.println("=================*setting " + response);
                if (result) {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (!obj.getBoolean("error")) {

                            String privacyStr = obj.getString(key);
                            mWebView.setVerticalScrollBarEnabled(true);
                            mWebView.loadDataWithBaseURL("", privacyStr, "text/html", "UTF-8", "");
                            mWebView.setBackgroundColor(getResources().getColor(R.color.bg_color));

                        } else {
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                        }
                        prgLoading.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, WebViewActivity.this, Constant.SETTING_URL, params, false);
    }

    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @SuppressLint("NewApi")
    @Override
    protected void onPause() {
        mWebView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        finish();
        super.onBackPressed();

    }
}

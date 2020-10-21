package wrteam.ecommerce.app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import wrteam.ecommerce.app.R;
import wrteam.ecommerce.app.adapter.SliderAdapter;
import wrteam.ecommerce.app.helper.ApiConfig;
import wrteam.ecommerce.app.model.Slider;
import java.util.ArrayList;

public class FullScreenViewActivity extends AppCompatActivity {
    int pos;
    ArrayList<Slider> imglist;
    //NetworkImageView imgDisplay;
    //RelativeLayout lytimg;
    LinearLayout mMarkersLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_view);

        mMarkersLayout = findViewById(R.id.layout_markers);
        viewPager = findViewById(R.id.pager);

        imglist = new ArrayList<>();
        imglist = ProductDetailActivity.sliderArrayList;
        pos = getIntent().getIntExtra("pos",0);
        //imgDisplay = findViewById(R.id.imgDisplay);

        //SetImage(pos);

        viewPager.setAdapter(new SliderAdapter(imglist, FullScreenViewActivity.this, R.layout.lyt_fullscreenimg,"fullscreen"));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int position) {
                ApiConfig.addMarkers(position,imglist,mMarkersLayout,FullScreenViewActivity.this);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

        viewPager.setCurrentItem(pos);
        ApiConfig.addMarkers(pos,imglist,mMarkersLayout,FullScreenViewActivity.this);

    }

    public void OnBtnClick(View view) {
        onBackPressed();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

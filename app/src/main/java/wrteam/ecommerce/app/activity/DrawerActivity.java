package wrteam.ecommerce.app.activity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import wrteam.ecommerce.app.R;
import wrteam.ecommerce.app.helper.Constant;
import wrteam.ecommerce.app.helper.Session;

public class DrawerActivity extends AppCompatActivity {
    public NavigationView navigationView;
    public DrawerLayout drawer;
    public ActionBarDrawerToggle drawerToggle;
    protected FrameLayout frameLayout;
    public TextView tvEmail;
    public static TextView tvName;
    Session session;
    RelativeLayout lytProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ApiConfig.transparentStatusAndNavigation(DrawerActivity.this);
        setContentView(R.layout.activity_drawer);

        frameLayout = findViewById(R.id.content_frame);
        navigationView = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);
        View header = navigationView.getHeaderView(0);

        tvName = header.findViewById(R.id.header_name);
        tvEmail = header.findViewById(R.id.header_email);
        lytProfile = header.findViewById(R.id.lytProfile);
        session = new Session(DrawerActivity.this);

        if (session.isUserLoggedIn()) {
            tvName.setText(session.getData(Session.KEY_NAME));
            tvEmail.setText(session.getData(Session.KEY_EMAIL));
        } else {
            tvName.setText(getResources().getString(R.string.is_login));
        }
        lytProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawers();
                if (session.isUserLoggedIn())
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                else
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
        setupNavigationDrawer();
    }

    private void setupNavigationDrawer() {
        Menu nav_Menu = navigationView.getMenu();
        if (session.isUserLoggedIn()) {
            nav_Menu.findItem(R.id.menu_profile).setVisible(true);
            nav_Menu.findItem(R.id.menu_logout).setVisible(true);
        } else {
            nav_Menu.findItem(R.id.menu_logout).setVisible(false);
            nav_Menu.findItem(R.id.menu_profile).setVisible(false);
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                drawer.closeDrawers();
                // remove all colors of the items to the `unchecked` state of the selector
                removeColor(navigationView);

                // check the selected item to change its color set by the `checked` state of the selector
                menuItem.setChecked(true);

                switch (menuItem.getItemId()) {
                    case R.id.faq:
                        Intent faq = new Intent(getApplicationContext(), WebViewActivity.class);
                        faq.putExtra("type", "faq");
                        startActivity(faq);
                        break;
                    case R.id.menu_terms:
                        Intent terms = new Intent(getApplicationContext(), WebViewActivity.class);
                        terms.putExtra("type", "terms");
                        startActivity(terms);
                        // ApiConfig.OpenBottomDialog("terms", getApplicationContext());
                        break;
                    case R.id.contact:
                        Intent contact = new Intent(getApplicationContext(), WebViewActivity.class);
                        contact.putExtra("type", "contact");
                        startActivity(contact);
                        break;
                    case R.id.about_us:
                        Intent about = new Intent(getApplicationContext(), WebViewActivity.class);
                        about.putExtra("type", "about");
                        startActivity(about);
                        break;
                    case R.id.menu_privacy:
                        Intent privacy = new Intent(getApplicationContext(), WebViewActivity.class);
                        privacy.putExtra("type", "privacy");
                        startActivity(privacy);
                        // ApiConfig.OpenBottomDialog("privacy", getApplicationContext());
                        break;
                    case R.id.menu_home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));

                        break;
                    case R.id.menu_profile:
                        if (session.isUserLoggedIn())
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        else
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        break;
                    case R.id.delivery_boy:
                        if (session.isUserLoggedIn())
                            startActivity(new Intent(getApplicationContext(), DeliveryBoyActivity.class));
                        else
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        break;
                    case R.id.refer:
                        if (session.isUserLoggedIn())
                            startActivity(new Intent(getApplicationContext(), ReferEarnActivity.class));
                        else
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        break;
                    case R.id.cart:
                        startActivity(new Intent(getApplicationContext(), CartActivity.class));
                        break;

                    case R.id.changePass:
                        Intent intent1 = new Intent(getApplicationContext(), LoginActivity.class);
                        if (session.isUserLoggedIn())
                            intent1.putExtra("from", "changepsw");
                        startActivity(intent1);
                        break;
                    case R.id.menu_tracker:
                        if (session.isUserLoggedIn()) {
                            startActivity(new Intent(getApplicationContext(), OrderListActivity.class));
                        } else
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        break;
                    case R.id.menu_share:
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.app_name) + " App\n\"" + getString(R.string.app_name) + "\" \n" + Constant.PLAY_STORE_LINK + getPackageName());
                        shareIntent.setType("text/plain");
                        startActivity(Intent.createChooser(shareIntent, "Share"));
                        break;
                    case R.id.menu_rate:
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.PLAY_STORE_LINK + getPackageName())));
                        break;
                    case R.id.menu_logout:
                        session.logoutUser(DrawerActivity.this);
                        break;
                }

                return true;
            }
        });


    }
    private void removeColor(NavigationView view) {
        for (int i = 0; i < view.getMenu().size(); i++) {
            MenuItem item = view.getMenu().getItem(i);
            item.setChecked(false);
        }
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }
}

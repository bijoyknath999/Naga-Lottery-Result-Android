package nagalandlottery.result.daily.Activity;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.Locale;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;
import nagalandlottery.result.daily.BuildConfig;
import nagalandlottery.result.daily.R;
import nagalandlottery.result.daily.api.ApiInterface;
import nagalandlottery.result.daily.api.models.InstallData;
import nagalandlottery.result.daily.api.models.LatestResult;
import nagalandlottery.result.daily.api.models.LatestResultData;
import nagalandlottery.result.daily.utils.PermissionUtil;
import nagalandlottery.result.daily.utils.Tools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity implements PermissionUtil.PermissionsCallBack{

    private CardView OnePmClick, SixPmClick, EightPmClick, OldResultsClick, LiveVideosClick, WebsiteClick;
    private TextView OnePmText, SixPmText, EightPmText, ToolbarTitle, TodaysDate;
    private ImageView OnePmImg, SixPmImg, EightPmImg, ToolbarOptions;
    private DrawerLayout HomeLayout;
    private String adMobBannerID, FacebookBannerID, interstitialAdmob ,interstitialFacebook;
    private InterstitialAd mInterstitialAd;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean doubleBackToExitPressedOnce = false, interstitialStatus;
    private int Click = 0, icount, countInt, interstitialType, interstitialLimit, interstitialIndex;


    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    private ImageView DrawerClick;
    private SmoothBottomBar smoothBottomBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_home);

        OnePmClick = findViewById(R.id.home_1_pm_click);
        SixPmClick = findViewById(R.id.home_6_pm_click);
        EightPmClick = findViewById(R.id.home_8_pm_click);
        OldResultsClick = findViewById(R.id.home_old_results_click);
        //LiveVideosClick = findViewById(R.id.home_videos_click);
        OnePmText = findViewById(R.id.home_1_pm_text);
        SixPmText = findViewById(R.id.home_6_pm_text);
        EightPmText = findViewById(R.id.home_8_pm_text);
        OnePmImg = findViewById(R.id.home_1_pm_img);
        SixPmImg = findViewById(R.id.home_6_pm_img);
        EightPmImg = findViewById(R.id.home_8_pm_img);
        TodaysDate = findViewById(R.id.home_todays_date);
        swipeRefreshLayout = findViewById(R.id.home_swipe_refresh);
        //WebsiteClick = findViewById(R.id.home_websites_click);
        HomeLayout = findViewById(R.id.homeDrawerLayout);
        navigationView = findViewById(R.id.home_nav_View);
        DrawerClick = findViewById(R.id.home_drawer_click);
        smoothBottomBar = findViewById(R.id.home_bottomBar);

        toggle = new ActionBarDrawerToggle(Home.this, HomeLayout, R.string.drawer_open, R.string.drawer_close);
        HomeLayout.addDrawerListener(toggle);
        toggle.syncState();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Drawar click event
        // Drawer item Click event ------
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();

                if (id == R.id.change_language) {
                    changeLangauge();
                    return true;
                } else if (id == R.id.rate_us) {
                    String appPackageName = getPackageName();
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                    return true;
                } else if (id == R.id.disclaimer) {
                    Intent disIntent = new Intent(Home.this, UniversalActivity.class);
                    disIntent.putExtra("page", "disclaimer");
                    startActivity(disIntent);
                    return true;
                } else if (id == R.id.privacy_policy) {
                    Intent privacyPolicyIntent = new Intent(Home.this, UniversalActivity.class);
                    privacyPolicyIntent.putExtra("page", "privacy");
                    startActivity(privacyPolicyIntent);
                    return true;
                } else if (id == R.id.terms_conditions) {
                    Intent termsConditionsIntent = new Intent(Home.this, UniversalActivity.class);
                    termsConditionsIntent.putExtra("page", "terms");
                    startActivity(termsConditionsIntent);
                    return true;
                } else if (id == R.id.contact_us) {
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:app@dayup.in"));
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Support Request");
                    startActivity(Intent.createChooser(intent, "Send Email"));
                    return true;
                }
                return false;
            }
        });

        DrawerClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Code Here
                HomeLayout.openDrawer(GravityCompat.START);
            }
        });

        smoothBottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int i) {

                switch (i)
                {
                    case 0:
                        break;
                    case 1:
                        startActivity(new Intent(Home.this,Videos.class));
                        break;
                    case 2:
                        startActivity(new Intent(Home.this,WinnerActivity.class));
                        break;
                    case 3:
                        Intent websiteIntent = new Intent(Intent.ACTION_VIEW);
                        websiteIntent.setData(Uri.parse("https://www.nagalandstatelotteryresult.net/"));
                        startActivity(websiteIntent);
                        break;

                }

                return false;
            }
        });

        requestPermissions();

        String firsttime = Tools.getString(Home.this,"firsttime");
        if (firsttime.isEmpty())
        {
            Tools.saveString(Home.this,"firsttime","no");
            changeLangauge();
            ApiInterface.getApiRequestInterface().getInstallRef(BuildConfig.API_KEY).enqueue(new Callback<InstallData>() {
                @Override
                public void onResponse(Call<InstallData> call, Response<InstallData> response) {
                }

                @Override
                public void onFailure(Call<InstallData> call, Throwable t) {

                }
            });

        }

        /*adMobBannerID = Tools.getString(Home.this,"bannerAdmob");
        FacebookBannerID = Tools.getString(Home.this,"bannerFacebook");
        interstitialAdmob = Tools.getString(this,"interstitialAdmob");
        interstitialFacebook = Tools.getString(this,"interstitialFacebook");
        icount = Tools.getInt(this,"adcount");
        countInt = Integer.parseInt(adCounts);*/


        interstitialStatus = Tools.getBoolean(this, "interstitialStatus");
        interstitialType = Tools.getInt(this, "interstitialType");
        interstitialLimit = Tools.getInt(this, "interstitialLimit");
        icount = Tools.getInt(this,"interstitialCount");
        interstitialIndex = Tools.getInt(this,"interstitialIndex");


        OnePmClick.setOnClickListener(v -> {
            Tools.showSnackbar(Home.this,HomeLayout,getString(R.string.not_publish),getString(R.string.okay));
        });
        SixPmClick.setOnClickListener(v -> {
            Tools.showSnackbar(Home.this,HomeLayout,getString(R.string.not_publish),getString(R.string.okay));
        });
        EightPmClick.setOnClickListener(v -> {
            Tools.showSnackbar(Home.this,HomeLayout,getString(R.string.not_publish),getString(R.string.okay));
        });
        OldResultsClick.setOnClickListener(v -> {
            Intent oldResults = new Intent(Home.this,OldResults.class);
            startActivity(oldResults);
        });


        if (Tools.isInternetConnected())
            LoadData();
        else
            Tools.showDialogNoInternet(Home.this,"home");

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Tools.isInternetConnected())
                    LoadData();
                else
                    Tools.showDialogNoInternet(Home.this,"home");
            }
        });
    }

    private void ShowInterstitialAd()
    {
        if (interstitialStatus)
        {

            if (interstitialType==0)
            {
                Click = Tools.getInt(this,"click");

                if (icount<=interstitialLimit)
                {
                    if (Click==0){
                        Tools.showInterstitialAdfromAdmob(this);

                    }
                    else if (Click==interstitialIndex)
                    {
                        Tools.showInterstitialAdfromAdmob(this);

                    }
                    else
                    {
                        int value = Tools.getInt(this,"click");
                        Tools.saveInt(this,"click",1+value);
                    }
                }
            }
            else if(interstitialType==1)
            {
                Click = Tools.getInt(this,"click");
                Toast.makeText(this, ""+Click+" "+icount, Toast.LENGTH_SHORT).show();

                if (icount<=interstitialLimit)
                {
                    if (Click==0){
                        Tools.showInterstitialAdfromFacebook(this);

                    }
                    else if (Click==interstitialIndex)
                    {
                        Tools.showInterstitialAdfromAdmob(this);

                    }
                    else
                    {
                        int value = Tools.getInt(this,"click");
                        Tools.saveInt(this,"click",1+value);
                    }
                }
            }
        }
    }

    private void LoadData() {
        ApiInterface.getApiRequestInterface().getLatestResults(BuildConfig.API_KEY)
                .enqueue(new Callback<LatestResultData>() {
                    @Override
                    public void onResponse(Call<LatestResultData> call, Response<LatestResultData> response) {
                        if (response.isSuccessful())
                        {
                            if (swipeRefreshLayout.isRefreshing())
                            {
                                swipeRefreshLayout.setRefreshing(false);
                                Tools.showSnackbar(Home.this,HomeLayout,getString(R.string.refresh_successful),getString(R.string.okay));
                            }
                            LatestResult latestResults = response.body().getLatestResult();
                            TodaysDate.setText(""+Tools.DateToDateFormat(latestResults.getDate()));
                            String onepm = latestResults.getOnepmurl();
                            String sixpm = latestResults.getSixpmurl();
                            String eightpm = latestResults.getEighthpmurl();
                            String locale = Tools.getLocale(Home.this);
                            Tools.saveString(Home.this,"date",latestResults.getDate());
                            if (onepm!=null)
                            {
                                OnePmText.setText(R.string.home_1_pm_txt);
                                OnePmText.setTextColor(getResources().getColor(R.color.color3));
                                OnePmImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_loaded));
                                OnePmClick.setOnClickListener(v -> {
                                    Intent oneIntent = new Intent(Home.this, LatestResults.class);
                                    oneIntent.putExtra("result",getString(R.string.home_latest_1pm_results));
                                    oneIntent.putExtra("img",onepm);
                                    startActivity(oneIntent);
                                });
                            }
                            else
                            {
                                OnePmText.setText(R.string.morning_1_pm_result_not_published_yet);
                                OnePmText.setTextColor(getResources().getColor(R.color.color2));
                                OnePmImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_loading));
                                OnePmClick.setOnClickListener(v -> {
                                    Tools.showSnackbar(Home.this,HomeLayout,getString(R.string.not_publish),getString(R.string.okay));
                                });
                            }
                            if (sixpm!=null)
                            {
                                SixPmText.setText(R.string.home_6_pm_txt);
                                SixPmText.setTextColor(getResources().getColor(R.color.color3));
                                SixPmImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_loaded));
                                SixPmClick.setOnClickListener(v -> {
                                    Intent sixIntent = new Intent(Home.this, LatestResults.class);
                                    sixIntent.putExtra("result",getString(R.string.home_latest_results_6pm));
                                    sixIntent.putExtra("img",sixpm);
                                    startActivity(sixIntent);
                                });
                            }
                            else
                            {
                                SixPmText.setText(R.string.evening_6_pm_result_not_published_yet);
                                SixPmText.setTextColor(getResources().getColor(R.color.color2));
                                SixPmImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_loading));
                                SixPmClick.setOnClickListener(v -> {
                                    Tools.showSnackbar(Home.this,HomeLayout,getString(R.string.not_publish),getString(R.string.okay));
                                });
                            }
                            if (eightpm!=null)
                            {
                                EightPmText.setText(R.string.home_8_pm_txt);
                                EightPmText.setTextColor(getResources().getColor(R.color.color3));
                                EightPmImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_loaded));
                                EightPmClick.setOnClickListener(v -> {
                                    Intent eightIntent = new Intent(Home.this, LatestResults.class);
                                    eightIntent.putExtra("result",getString(R.string.home_latest_results_8pm));
                                    eightIntent.putExtra("img",eightpm);
                                    startActivity(eightIntent);
                                });
                            }
                            else
                            {
                                EightPmText.setText(R.string.night_8_pm_result_not_published_yet);
                                EightPmText.setTextColor(getResources().getColor(R.color.color2));
                                EightPmImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_loading));
                                EightPmClick.setOnClickListener(v -> {
                                    Tools.showSnackbar(Home.this,HomeLayout,getString(R.string.not_publish),getString(R.string.okay));
                                });
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<LatestResultData> call, Throwable t) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    private void loadLocale()
    {
        String localeStr = Tools.getString(Home.this,"locale");
        if (localeStr.isEmpty())
            localeStr = "en";

        Configuration config = getResources().getConfiguration();
        Locale locale = new Locale(localeStr);
        Locale.setDefault(locale);
        config.setLocale(locale);
        config.setLayoutDirection(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    private void setLocale(String value)
    {
        Configuration config = getResources().getConfiguration();
        Locale locale = new Locale(value); // or whatever language you want to support
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        Tools.saveString(Home.this,"locale", value);
        recreate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void changeLangauge() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.custom_sheet_dialog_language);
        LinearLayout banglaBtn, englishBtn, hindiBtn;
        banglaBtn = dialog.findViewById(R.id.sheet_lang_bangla);
        englishBtn = dialog.findViewById(R.id.sheet_lang_english);
        hindiBtn = dialog.findViewById(R.id.sheet_lang_hindi);

        banglaBtn.setOnClickListener(v ->
        {
            setLocale("bn");
            dialog.dismiss();
        });
        hindiBtn.setOnClickListener(v ->
        {
            setLocale("hi");
            dialog.dismiss();
        });
        englishBtn.setOnClickListener(v ->
        {
            setLocale("en");
            dialog.dismiss();
        });

        dialog.show();
    }


    public void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PermissionUtil.checkAndRequestPermissions(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.POST_NOTIFICATIONS,
                    Manifest.permission.ACCESS_NOTIFICATION_POLICY)) {
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onRequestPermissionsResult(this, requestCode, permissions, grantResults, this);
    }

    @Override
    public void permissionsGranted() {
    }

    @Override
    public void permissionsDenied() {
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
        /*if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }*/

        /*this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 4000);*/
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (interstitialStatus)
        {
            if (icount<=interstitialLimit)
            {
                Tools.loadInterstitialAdfromAdmob(Home.this);
                Tools.loadInterstitialAdromFacebook(Home.this);
            }
        }

        smoothBottomBar.setItemActiveIndex(0);
    }

    private void showExitDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.custom_sheet_dialog);
        Button rateusBtn = dialog.findViewById(R.id.modal_sheet_rate_us);
        Button shareBtn = dialog.findViewById(R.id.modal_sheet_share);
        Button quitBtn = dialog.findViewById(R.id.modal_sheet_quit);

        quitBtn.setOnClickListener(v -> {
            finishAffinity();
        });

        rateusBtn.setOnClickListener(v -> {
            String appPackageName = getPackageName();
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        });

        shareBtn.setOnClickListener(v -> {
            String appPackageName = getPackageName();
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out this app: https://play.google.com/store/apps/details?id=" + appPackageName);
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Share using"));
        });
        dialog.show();
    }
}
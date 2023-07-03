package nagalandlottery.result.daily.Activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import nagalandlottery.result.daily.BuildConfig;
import nagalandlottery.result.daily.R;
import nagalandlottery.result.daily.api.ApiInterface;
import nagalandlottery.result.daily.api.models.InstallData;
import nagalandlottery.result.daily.api.models.Setting;
import nagalandlottery.result.daily.api.models.SettingData;
import nagalandlottery.result.daily.utils.Constants;
import nagalandlottery.result.daily.utils.Tools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Splash extends AppCompatActivity {

    private String versionCode = "0";
    private int versionCodeInt = 0;
    private static final String TAG = "MyFirebaseMsgService";
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_splash);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Tools.saveInt(Splash.this,"click",0);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = dateFormat.format(new Date());
        String checkdate = Tools.getString(Splash.this,"date");
        String cleardate = Tools.getString(Splash.this,"clear");

        if (!cleardate.isEmpty())
        {
            if (!cleardate.equals(checkdate))
            {
                if (!checkdate.isEmpty())
                {
                    SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
                    Date dateObj1 = null;
                    Date dateObj2 = null;
                    try {
                        dateObj1 = dateFormat2.parse(currentDate);
                        dateObj2 = dateFormat2.parse(checkdate);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (dateObj1.compareTo(dateObj2) > 0) {
                        Tools.saveInt(Splash.this,"adcount",0);
                        Tools.saveInt(Splash.this,"bannercount",0);
                        Tools.saveInt(Splash.this,"appopencount",0);
                        Tools.saveString(Splash.this,"clear",checkdate);
                    }
                }
            }
        }
        else
        {
            if (!checkdate.isEmpty())
            {
                SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
                Date dateObj1 = null;
                Date dateObj2 = null;
                try {
                    dateObj1 = dateFormat2.parse(currentDate);
                    dateObj2 = dateFormat2.parse(checkdate);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (dateObj1.compareTo(dateObj2) > 0) {
                    Tools.saveInt(Splash.this,"adcount",0);
                    Tools.saveInt(Splash.this,"bannercount",0);
                    Tools.saveString(Splash.this,"clear",checkdate);
                }
            }
        }

        FirebaseMessaging.getInstance().subscribeToTopic("NGLottery")  //NGLottery
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Subscribed to topic!");
                        } else {
                            Log.d(TAG, "Failed to subscribe to topic");
                        }
                    }
                });

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionCode = String.valueOf(pInfo.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (!Tools.isInternetConnected())
            Tools.showDialogNoInternet(Splash.this,"splash");
        else
        {
            ApiInterface.getApiRequestInterface().getSettings(BuildConfig.API_KEY)
                    .enqueue(new Callback<SettingData>() {
                        @Override
                        public void onResponse(Call<SettingData> call, Response<SettingData> response) {
                            if (response.isSuccessful())
                            {
                                Setting settings = response.body().getSettings();
                                versionCodeInt = Integer.parseInt(versionCode);
                                if (versionCodeInt<=settings.getAppVersion())
                                {
                                    int interstitialIndex = settings.getInterstitialIndex();

                                    try {
                                        JSONObject obj2 = new JSONObject(settings.getAdsStatus());
                                        boolean bannerStatus = (boolean) obj2.get("banner");
                                        boolean interstitialStatus = (boolean) obj2.get("interstital");
                                        boolean appOpenStatus = (boolean) obj2.get("app_open");

                                        Tools.saveBoolean(Splash.this,"bannerStatus",bannerStatus);
                                        Tools.saveBoolean(Splash.this,"interstitialStatus",interstitialStatus);
                                        Tools.saveBoolean(Splash.this,"appOpenStatus",appOpenStatus);

                                        JSONObject obj = new JSONObject(settings.getAdsType());
                                        int bannerType = (int) obj.get("banner");
                                        int interstitialType = (int) obj.get("interstital");
                                        int appOpenType = (int) obj.get("app_open");

                                        Tools.saveInt(Splash.this,"bannerType", bannerType);
                                        Tools.saveInt(Splash.this,"interstitialType", interstitialType);
                                        Tools.saveInt(Splash.this,"appOpenType", appOpenType);

                                        JSONObject obj3 = new JSONObject(settings.getAdsLimit());
                                        int bannerLimit = (int) obj3.get("banner");
                                        int interstitialLimit = (int) obj3.get("interstital");
                                        int appOpenLimit = (int) obj3.get("app_open");

                                        Tools.saveInt(Splash.this,"bannerLimit", bannerLimit);
                                        Tools.saveInt(Splash.this,"interstitialLimit", interstitialLimit);
                                        Tools.saveInt(Splash.this,"appOpenLimit", appOpenLimit);
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }

                                    String bannerAdmob = settings.getAdmobBanner();
                                    String interstitialAdmob = settings.getAdmobInterstitial();
                                    String bannerFacebook = settings.getFacebookBanner();
                                    String interstitialFacebook = settings.getFacebookInterstitial();
                                    String appOpenAdmob = settings.getAdmobAppOpen();
                                    String imgPATH = response.body().getResultImagePath();
                                    String pdfPATH = response.body().getResultPdfPath();
                                    String winnerImgPATH = response.body().getWinnerProfilePath();


                                    Tools.saveInt(Splash.this,"interstitialIndex",interstitialIndex);
                                    Tools.saveString(Splash.this,"bannerAdmob",bannerAdmob);
                                    Tools.saveString(Splash.this,"interstitialAdmob",interstitialAdmob);
                                    Tools.saveString(Splash.this,"appOpenAdmob",appOpenAdmob);
                                    Tools.saveString(Splash.this,"bannerFacebook",bannerFacebook);
                                    Tools.saveString(Splash.this,"interstitialFacebook",interstitialFacebook);
                                    Tools.saveString(Splash.this,"imgPATH",imgPATH);
                                    Tools.saveString(Splash.this,"pdfPATH",pdfPATH);
                                    Tools.saveString(Splash.this,"winnerImgPATH",winnerImgPATH);
                                    startActivity(new Intent(Splash.this, Home.class));
                                    finish();
                                }
                                else
                                {
                                    if (!versionCode.equals("0"))
                                    {
                                        Tools.showDialog(Splash.this);
                                    }
                                }
                            }
                            else
                            {
                                Toast.makeText(Splash.this, "Server Down. Please wait we are fixing the issue", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<SettingData> call, Throwable t) {
                        }
                    });
        }

    }

    private void loadLocale()
    {
        String localeStr = Tools.getString(Splash.this,"locale");
        if (localeStr.isEmpty())
            localeStr = "en";

        Configuration config = getResources().getConfiguration();
        Locale locale = new Locale(localeStr); // or whatever language you want to support
        config.setLocale(locale);
        config.setLayoutDirection(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
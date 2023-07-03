package nagalandlottery.result.daily.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.AudienceNetworkAds;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.jsibbold.zoomage.ZoomageView;

import java.io.File;
import java.util.List;
import java.util.Locale;

import nagalandlottery.result.daily.BuildConfig;
import nagalandlottery.result.daily.R;
import nagalandlottery.result.daily.api.ApiInterface;
import nagalandlottery.result.daily.api.models.LatestResult;
import nagalandlottery.result.daily.api.models.LatestResultData;
import nagalandlottery.result.daily.utils.Tools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LatestResults extends AppCompatActivity {

    private TextView ToolbarTitle;
    private ImageView BackBtn;
    private Button DownloadBtn, ShareBtn;
    private ZoomageView ImageZoomView;
    private String FinalUrl, PdfUrl, BtnName;
    private SpinKitView spinKitView;
    private RelativeLayout LatestResultsLayout;

    private LinearLayout admobBanner;
    private com.facebook.ads.AdView adView;
    private LinearLayout facebookBanner;
    private String adMobBannerID, FacebookBannerID, interstitialAdmob ,interstitialFacebook, page;
    private int Click = 0,icount, bannerType, bannerLimit, bCount, interstitialType, interstitialLimit, interstitialIndex;
    private InterstitialAd mInterstitialAd;
    private final String TAG = LatestResults.class.getSimpleName();
    private com.facebook.ads.InterstitialAd interstitialAd;
    private String img;
    private boolean bannerStatus, interstitialStatus;
    private ReviewManager reviewManager;
    private ReviewInfo reviewInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_latest_results);

        ToolbarTitle = findViewById(R.id.latest_results_toolbar_title);
        BackBtn = findViewById(R.id.latest_results_toolbar_back);
        DownloadBtn = findViewById(R.id.latest_results_download);
        ShareBtn = findViewById(R.id.latest_results_share);
        ImageZoomView = findViewById(R.id.latest_results_image);
        spinKitView = findViewById(R.id.latest_results_spin_kit);
        admobBanner = findViewById(R.id.latest_results_adView);
        facebookBanner = findViewById(R.id.latest_results_facebook_banner);
        LatestResultsLayout = findViewById(R.id.latest_results_layout);

        interstitialIndex = Tools.getInt(this,"interstitialIndex");
        adMobBannerID = Tools.getString(this,"bannerAdmob");
        FacebookBannerID = Tools.getString(this,"bannerFacebook");
        interstitialAdmob = Tools.getString(this,"interstitialAdmob");
        interstitialFacebook = Tools.getString(this,"interstitialFacebook");
        Click = Tools.getInt(this,"click");
        icount = Tools.getInt(this,"interstitialCount");
        bannerStatus = Tools.getBoolean(LatestResults.this,"bannerStatus");
        bannerType = Tools.getInt(LatestResults.this, "bannerType");
        bannerLimit = Tools.getInt(LatestResults.this, "bannerLimit");
        bCount = Tools.getInt(this,"bannercount");
        interstitialStatus = Tools.getBoolean(LatestResults.this, "interstitialStatus");
        interstitialType = Tools.getInt(LatestResults.this, "interstitialType");
        interstitialLimit = Tools.getInt(LatestResults.this, "interstitialLimit");

        ShowInterstitialAd();
        getReviewInfo();


        if (!Tools.isInternetConnected())
            Tools.showDialogNoInternet(LatestResults.this,"latest");

        if (bannerStatus)
        {
            if (bannerType==0)
            {
                if (bCount<=bannerLimit)
                {
                    loadAdMobBanner();
                }
            }
            else
            {
                if (bCount<=bannerLimit)
                {
                    LoadFacebookBanner();
                }
            }
        }


        String title = getIntent().getStringExtra("result");
        img = getIntent().getStringExtra("img");
        page = getIntent().getStringExtra("page");
        String time = getIntent().getStringExtra("time");


        if (title!=null)
            ToolbarTitle.setText(title);
        if (page!=null)
        {
            if (page.equals("notify"))
            {
                loadDataFromNotify(time);
            }
            else
            {
                loadImage(img);
            }
        }
        else
            loadImage(img);


        BackBtn.setOnClickListener(v -> onBackPressed());

        DownloadBtn.setOnClickListener(v -> {
            String pdfUrl = Tools.getString(LatestResults.this,"pdfPATH");
            if ( img!=null)
            {
                int lastDotIndex = img.lastIndexOf(".");
                String modifiedString = img.substring(0, lastDotIndex) + ".PDF";
                PdfUrl = pdfUrl+modifiedString;

                if (!PdfUrl.isEmpty())
                {
                    /*Uri pdfUri = Uri.parse(PdfUrl);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(pdfUri, "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);*/
                    BtnName = "download";
                    fileDownload(PdfUrl,modifiedString);
                }
            }
            else
                Tools.showSnackbar(LatestResults.this,LatestResultsLayout,getString(R.string.reopen),getString(R.string.okay));

        });

        ShareBtn.setOnClickListener(v -> {
            String pdfUrl = Tools.getString(LatestResults.this,"pdfPATH");
            if (img!=null)
            {
                int lastDotIndex = img.lastIndexOf(".");
                String modifiedString = img.substring(0, lastDotIndex) + ".PDF";
                PdfUrl = pdfUrl+modifiedString;


                if (!PdfUrl.isEmpty())
                {
                    /*Uri pdfUri = Uri.parse(PdfUrl);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(pdfUri, "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);*/
                    BtnName = "share";
                    fileDownload(PdfUrl,modifiedString);
                }
            }
            else
                Tools.showSnackbar(LatestResults.this,LatestResultsLayout,getString(R.string.reopen),getString(R.string.okay));

        });

    }

    private void loadImage(String img) {
        String url = Tools.getString(LatestResults.this,"imgPATH");
        if (!url.isEmpty())
        {
            FinalUrl = url+img;
        }
        else
            Tools.showSnackbar(LatestResults.this,LatestResultsLayout,getString(R.string.reopen),getString(R.string.okay));

        if (!FinalUrl.isEmpty() && FinalUrl!=null)
        {
            Glide.with(getApplicationContext())
                    .load(FinalUrl)
                    .addListener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            spinKitView.setVisibility(View.VISIBLE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            spinKitView.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(ImageZoomView);
        }
    }

    private void loadDataFromNotify(String time) {
        ApiInterface.getApiRequestInterface().getLatestResults(BuildConfig.API_KEY)
                .enqueue(new Callback<LatestResultData>() {
                    @Override
                    public void onResponse(Call<LatestResultData> call, Response<LatestResultData> response) {
                        if (response.isSuccessful())
                        {
                            LatestResult latestResults = response.body().getLatestResult();
                            String onepm = latestResults.getOnepmurl();
                            String sixpm = latestResults.getSixpmurl();
                            String eightpm = latestResults.getEighthpmurl();
                            String title = null;
                            if (time.equals("1pm"))
                            {
                                title = getString(R.string.home_latest_1pm_results);
                                img = onepm;
                            }
                            else if (time.equals("6pm"))
                            {
                                title = getString(R.string.home_latest_results_6pm);
                                img = sixpm;
                            }

                            else if (time.equals("8pm"))
                            {
                                title = getString(R.string.home_latest_results_8pm);
                                img = eightpm;
                            }

                            ToolbarTitle.setText(title);

                            loadImage(img);

                        }
                    }

                    @Override
                    public void onFailure(Call<LatestResultData> call, Throwable t) {

                    }
                });
    }

    private void LoadFacebookBanner() {
        admobBanner.setVisibility(View.GONE);
        facebookBanner.setVisibility(View.VISIBLE);
        AudienceNetworkAds.initialize(this);
        adView = new com.facebook.ads.AdView(this, FacebookBannerID, AdSize.BANNER_HEIGHT_50);
        facebookBanner.addView(adView);
        com.facebook.ads.AdListener adListener = new com.facebook.ads.AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                loadAdMobBanner();
            }

            @Override
            public void onAdLoaded(Ad ad) {

            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {
                Tools.saveInt(LatestResults.this,"bannercount",bCount+1);
            }
        };
        adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());
    }

    private void loadAdMobBanner() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        admobBanner.setVisibility(View.VISIBLE);
        facebookBanner.setVisibility(View.GONE);
        AdView mAdView = new AdView(this);
        mAdView.setAdSize(com.google.android.gms.ads.AdSize.FULL_BANNER);
        mAdView.setAdUnitId(adMobBannerID);
        AdRequest adRequest = new AdRequest.Builder().build();
        if(mAdView.getAdSize() != null || mAdView.getAdUnitId() != null)
            mAdView.loadAd(adRequest);

        admobBanner.addView(mAdView);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                LoadFacebookBanner();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
                Tools.saveInt(LatestResults.this,"bannercount",bCount+1);
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }
        });
    }

    private void fileDownload(String PdfUrl, String modifiedString) {
        try {

            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(PdfUrl));
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            request.setTitle(modifiedString);
            request.setDescription("Downloading "+modifiedString);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, modifiedString);

            DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            long downloadID = downloadManager.enqueue(request);
        } catch (Exception e) {
            e.printStackTrace();
        }

        registerReceiver(downloadReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    public BroadcastReceiver downloadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long downloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            if (downloadID != -1) {
                DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(downloadID);
                Cursor cursor = downloadManager.query(query);

                if (cursor.moveToFirst()) {
                    @SuppressLint("Range") int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));

                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE));
                        if (BtnName.equals("download"))
                        {
                            BtnName = "";
                            Toast.makeText(context, "Download", Toast.LENGTH_SHORT).show();
                            CheckPdf(title);
                        }
                        else if (BtnName.equals("share"))
                        {
                            BtnName = "";
                            SharePdf(title);
                        }
                    }
                }

                cursor.close();
            }
        }
    };

    private void SharePdf(String title) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), title);
        // Create a content URI from a file path
        Uri contentUri = FileProvider.getUriForFile(LatestResults.this, BuildConfig.APPLICATION_ID + ".provider", file);
        if (file.exists()) {
            Intent pdfOpenIntent = new Intent(Intent.ACTION_SEND);
            pdfOpenIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pdfOpenIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            pdfOpenIntent.setType("application/pdf");
            pdfOpenIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION |  Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            try {
                startActivity(pdfOpenIntent);
                unregisterReceiver(downloadReceiver);
            } catch (ActivityNotFoundException activityNotFoundException) {
                Toast.makeText(LatestResults.this,"There is no app to load corresponding PDF",Toast.LENGTH_LONG).show();

            }
        } else {
            // Handle file not found
        }
    }

    private void CheckPdf(String title)
    {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), title);
        // Create a content URI from a file path
        Uri contentUri = FileProvider.getUriForFile(LatestResults.this, BuildConfig.APPLICATION_ID + ".provider", file);
        if (file.exists()) {
            Intent pdfOpenIntent = new Intent(Intent.ACTION_VIEW);
            pdfOpenIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pdfOpenIntent.setClipData(ClipData.newRawUri("", contentUri));
            pdfOpenIntent.setDataAndType(contentUri, "application/pdf");
            pdfOpenIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION |  Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            try {
                startActivity(pdfOpenIntent);
                unregisterReceiver(downloadReceiver);
            } catch (ActivityNotFoundException activityNotFoundException) {
                Toast.makeText(LatestResults.this,"There is no app to load corresponding PDF",Toast.LENGTH_LONG).show();

            }
        } else {
            // Handle file not found
        }
    }

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

        startReviewFlow();

        if (page!=null)
        {
            if (page.equals("notify"))
            {
                startActivity(new Intent(LatestResults.this,Home.class));
            }
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                        Tools.loadInterstitialAdfromAdmob(this);
                        Tools.loadInterstitialAdromFacebook(this);
                    }
                    else if (Click==interstitialIndex)
                    {
                        Tools.showInterstitialAdfromAdmob(this);
                        Tools.loadInterstitialAdfromAdmob(this);
                        Tools.loadInterstitialAdromFacebook(this);
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

                if (icount<=interstitialLimit)
                {
                    if (Click==0){
                        Tools.showInterstitialAdfromFacebook(this);
                        Tools.loadInterstitialAdfromAdmob(this);
                        Tools.loadInterstitialAdromFacebook(this);
                    }
                    else if (Click==interstitialIndex)
                    {
                        Tools.showInterstitialAdfromFacebook(this);
                        Tools.loadInterstitialAdfromAdmob(this);
                        Tools.loadInterstitialAdromFacebook(this);
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

    private void getReviewInfo() {
        reviewManager = ReviewManagerFactory.create(this);
        Task<ReviewInfo> manager = reviewManager.requestReviewFlow();
        manager.addOnCompleteListener(new OnCompleteListener<ReviewInfo>() {
            @Override
            public void onComplete(@NonNull Task<ReviewInfo> task) {
                if (task.isSuccessful()) {
                    reviewInfo = task.getResult();
                }
            }
        });
    }

    public void startReviewFlow() {
        if (reviewInfo != null) {
            Task<Void> flow = reviewManager.launchReviewFlow(this, reviewInfo);
            flow.addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                }
            });
        } else {

        }
    }

    private void loadLocale()
    {
        String localeStr = Tools.getString(LatestResults.this,"locale");
        if (localeStr.isEmpty())
            localeStr = "en";

        Configuration config = getResources().getConfiguration();
        Locale locale = new Locale(localeStr);
        Locale.setDefault(locale);
        config.setLocale(locale);
        config.setLayoutDirection(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }
}
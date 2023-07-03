package nagalandlottery.result.daily.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.facebook.ads.Ad;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import nagalandlottery.result.daily.Activity.Home;
import nagalandlottery.result.daily.Activity.LatestResults;
import nagalandlottery.result.daily.Activity.OldResults;
import nagalandlottery.result.daily.Activity.Splash;
import nagalandlottery.result.daily.Activity.Videos;
import nagalandlottery.result.daily.R;

public class Tools {

    public static InterstitialAd mInterstitialAd;
    public static com.facebook.ads.InterstitialAd FInterstitialAd;
    public static boolean failed;
    public static boolean isShowing;


    public static String getString(Context context, String key)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PRE_NAME,Context.MODE_PRIVATE);
        String value = sharedPreferences.getString(key,"");
        return value;
    }

    public static void saveString(Context context, String key, String value)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PRE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static int getInt(Context context, String key)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PRE_NAME,Context.MODE_PRIVATE);
        int value = sharedPreferences.getInt(key,0);
        return value;
    }

    public static void saveInt(Context context, String key, int value)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PRE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static boolean getBoolean(Context context, String key)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PRE_NAME,Context.MODE_PRIVATE);
        boolean value = sharedPreferences.getBoolean(key,false);
        return value;
    }

    public static void saveBoolean(Context context, String key, boolean value)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PRE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean isInternetConnected() {

        StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(gfgPolicy);
        try {
            URL url = new URL("http://www.google.com");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(3000);
            connection.connect();
            return connection.getResponseCode() == 200;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void showDialog(Context context)
    {

        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.update_message)
                .setCancelable(false)
                .setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String packageName = context.getPackageName();
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    }
                });
        AlertDialog mDialog = builder.create();
        mDialog.show();
    }

    public static void showDialogWithDismiss(Context context,String Title, String BtnText)
    {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        ViewGroup viewGroup = ((Activity) context).findViewById(android.R.id.content);

        View dialogView = LayoutInflater.from(context).inflate(R.layout.custom_dialog, viewGroup, false);
        builder.setView(dialogView);
        AlertDialog mDialog = builder.create();
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setCancelable(true);

        Button btn = mDialog.findViewById(R.id.custom_dialog_btn);
        TextView text = mDialog.findViewById(R.id.custom_dialog_text);
        text.setText(Title);
        btn.setText(BtnText);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });

        mDialog.show();
    }

    public static void showDialogNoInternet(Context context, String page)
    {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.internet_error)
                .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isInternetConnected()) {
                            dialog.dismiss();
                            if (page.equals("splash")) {
                                context.startActivity(new Intent(context, Splash.class));
                                ((Activity) context).finish();
                            }
                            else if (page.equals("home"))
                                context.startActivity(new Intent(context, Home.class));
                            else if (page.equals("latest")) {
                                context.startActivity(new Intent(context, Home.class));
                                ((Activity) context).finish();
                            }
                            else if (page.equals("videos")) {
                                context.startActivity(new Intent(context, Videos.class));
                                ((Activity) context).finish();
                            }
                            else if (page.equals("old")) {
                                context.startActivity(new Intent(context, OldResults.class));
                                ((Activity) context).finish();
                            }
                            else if (page.equals("universal")) {
                                context.startActivity(new Intent(context, Home.class));
                                ((Activity) context).finish();
                            }
                        }
                        else
                        {
                            showDialogNoInternet(context,page);
                        }
                    }
                })
                .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((Activity) context).finishAffinity();
                    }
                })
                .setCancelable(false);
        AlertDialog mDialog = builder.create();
        mDialog.show();
    }

    public static String getLocale(Context context)
    {
        Configuration config = context.getResources().getConfiguration();
        Locale currentLocale = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            currentLocale = config.getLocales().get(0);
        }
        else
        {
            Locale current = context.getResources().getConfiguration().locale;
        }
        return currentLocale.getLanguage().toString();
    }


    public static void loadInterstitialAdfromAdmob(Context context) {

        String interstitialAdmob = Tools.getString(context,"interstitialAdmob");

        String TAG = "Interstitial Admob";

        AdRequest adRequest = new AdRequest.Builder().build();

        mInterstitialAd.load(context ,interstitialAdmob, adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.e(TAG, "Interstitial ad Loaded.");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAd = null;
                        Log.e(TAG, ""+loadAdError.getMessage());
                    }
                });
    }

    public static void showInterstitialAdfromAdmob(Activity activity) {
        if (mInterstitialAd!=null) {
            if (!isShowing)
                mInterstitialAd.show(activity);

            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    isShowing = false;
                    super.onAdDismissedFullScreenContent();
                    int interstitialIndex = Tools.getInt(activity,"interstitialIndex");
                    int interstitialCount = Tools.getInt(activity, "interstitialCount");
                    Tools.saveInt(activity,"interstitialCount",interstitialCount+1);
                    int value = Tools.getInt(activity,"click");
                    if (value==interstitialIndex)
                        Tools.saveInt(activity,"click",1);
                    else
                        Tools.saveInt(activity,"click",value+1);
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    Tools.showInterstitialAdfromFacebook(activity);
                    isShowing = false;
                }


                @Override
                public void onAdImpression() {
                    super.onAdImpression();
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent();
                    isShowing = true;
                }
            });

        }
    }

    public static void loadInterstitialAdromFacebook(Context context) {

        if (FInterstitialAd!=null)
            if (FInterstitialAd.isAdLoaded())
                return;

        AudienceNetworkAds.initialize(context);

        String interstitialFacebook = Tools.getString(context,"interstitialFacebook");

        String TAG = "Interstitial Facebook";

        FInterstitialAd = new com.facebook.ads.InterstitialAd(context, interstitialFacebook);
        // Create listeners for the Interstitial Ad
        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.e(TAG, "Interstitial ad displayed.");
                isShowing = true;
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                isShowing = false;
                int interstitialIndex = Tools.getInt(context,"interstitialIndex");
                int value = Tools.getInt(context,"click");
                int interstitialCount = Tools.getInt(context, "interstitialCount");
                Tools.saveInt(context,"interstitialCount",interstitialCount+1);
                if (value==interstitialIndex)
                    Tools.saveInt(context,"click",1);
                else
                    Tools.saveInt(context,"click",value+1);
            }

            @Override
            public void onError(Ad ad, com.facebook.ads.AdError adError) {
                // Ad error callback
                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
                failed = true;
                isShowing = false;
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                // Show the ad
                failed = false;
                isShowing = false;
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d(TAG, "Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d(TAG, "Interstitial ad impression logged!");
            }
        };

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        FInterstitialAd.loadAd(
                FInterstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());
    }

    public static void showInterstitialAdfromFacebook(Activity activity) {
        if (FInterstitialAd.isAdLoaded()) {
            if (!isShowing)
                FInterstitialAd.show();
        }
        else {
            if (failed) {
                Tools.showInterstitialAdfromAdmob(activity);
            }
        }
    }

    public static void showSnackbar(Context context, View view, String title, String Btntext)
    {
        Snackbar snackbar = Snackbar.make(view,"",Snackbar.LENGTH_LONG);
        View customView = ((Activity) context).getLayoutInflater().inflate(R.layout.custom_snackbar,null);
        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        snackbarLayout.setPadding(0,0,0,0);

        TextView titleText = customView.findViewById(R.id.custom_snackbar_text);
        Button Btn = customView.findViewById(R.id.custom_snackbar_btn);

        titleText.setText(title);
        Btn.setText(Btntext);

        Btn.setOnClickListener(v -> snackbar.dismiss());

        snackbarLayout.addView(customView,0);
        snackbar.show();
    }

    public static String DateToDateFormat(String datestr)
    {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");

        Date date;
        try {
            date = inputFormat.parse(datestr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return outputFormat.format(date);
    }

    public static String getLastPartString(String value, String speciStr)
    {
        return value.substring(value.indexOf(speciStr) + 5);
    }
}

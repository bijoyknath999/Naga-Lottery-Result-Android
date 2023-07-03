package nagalandlottery.result.daily.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import nagalandlottery.result.daily.BuildConfig;
import nagalandlottery.result.daily.R;
import nagalandlottery.result.daily.api.ApiInterface;
import nagalandlottery.result.daily.api.models.OldResult;
import nagalandlottery.result.daily.api.models.OldResultData;
import nagalandlottery.result.daily.utils.Tools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OldResults extends AppCompatActivity {

    private Calendar selectedDate = Calendar.getInstance();
    private CardView CalenderClick;
    private TextView ResultDateTxt;
    private String checkDate;
    private Button OnePmClick, SixPmClick, EightPmClick;
    private LinearLayout linearLayout, OldResultsLayout;
    private ImageView BackBtn;

    private String adType, adStatus, adCounts, adMobBannerID, FacebookBannerID;
    private String AdCountsLocal;
    private int countInt, bcount, underAdsCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_old_results);

        CalenderClick = findViewById(R.id.old_results_select_date);
        ResultDateTxt = findViewById(R.id.old_results_date_text);
        OnePmClick = findViewById(R.id.old_results_one_pm_click);
        SixPmClick = findViewById(R.id.old_results_six_pm_click);
        EightPmClick = findViewById(R.id.old_results_eight_pm_click);
        linearLayout = findViewById(R.id.old_results_layout);
        BackBtn = findViewById(R.id.old_results_toolbar_back);
        OldResultsLayout = findViewById(R.id.old_results_main_layout);

        adType = Tools.getString(this,"AdsType");
        adStatus = Tools.getString(this,"AdsStatus");
        adCounts = Tools.getString(this,"AdsCount");
        adMobBannerID = Tools.getString(this,"bannerAdmob");
        FacebookBannerID = Tools.getString(this,"bannerFacebook");


        if (!Tools.isInternetConnected())
            Tools.showDialogNoInternet(OldResults.this,"old");


        CalenderClick.setOnClickListener(v -> {
            showCalendarDialog();
        });

        BackBtn.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void showCalendarDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                selectedDate.set(Calendar.YEAR, year);
                selectedDate.set(Calendar.MONTH, month);
                selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                // Update UI with selected date

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                checkDate = dateFormat.format(selectedDate.getTime());
                getResults();
            }
        },
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    private void getResults()
    {
        ApiInterface.getApiRequestInterface().getOldResults(BuildConfig.API_KEY,checkDate)
                .enqueue(new Callback<OldResultData>() {
                    @Override
                    public void onResponse(Call<OldResultData> call, Response<OldResultData> response) {
                        if (response.isSuccessful())
                        {
                            OldResult oldResult = response.body().getOldResult();
                            if (oldResult!=null)
                            {
                                String onepm = oldResult.getOnepmurl();
                                String sixpm = oldResult.getSixpmurl();
                                String eightpm = oldResult.getEighthpmurl();

                                linearLayout.setVisibility(View.VISIBLE);

                                ResultDateTxt.setText(""+oldResult.getDate());
                                if (onepm!=null)
                                {

                                    OnePmClick.setOnClickListener(v -> {
                                        Intent oneIntent = new Intent(OldResults.this, LatestResults.class);
                                        oneIntent.putExtra("result",getString(R.string.old_1pm_results)+oldResult.getDate()+")");
                                        oneIntent.putExtra("img",onepm);
                                        startActivity(oneIntent);
                                    });
                                }
                                else
                                {
                                    OnePmClick.setOnClickListener(v -> {
                                        Tools.showSnackbar(OldResults.this,OldResultsLayout,getString(R.string.not_publish),getString(R.string.okay));
                                    });
                                }
                                if (sixpm!=null)
                                {
                                    SixPmClick.setOnClickListener(v -> {
                                        Intent sixIntent = new Intent(OldResults.this, LatestResults.class);
                                        sixIntent.putExtra("result",getString(R.string.old_6pm_results)+oldResult.getDate()+")");
                                        sixIntent.putExtra("img",sixpm);
                                        startActivity(sixIntent);
                                    });
                                }
                                else
                                {
                                    SixPmClick.setOnClickListener(v -> {
                                        Tools.showSnackbar(OldResults.this,OldResultsLayout,getString(R.string.not_publish),getString(R.string.okay));
                                    });
                                }
                                if (eightpm!=null)
                                {
                                    EightPmClick.setOnClickListener(v -> {
                                        Intent eightIntent = new Intent(OldResults.this, LatestResults.class);
                                        eightIntent.putExtra("result",getString(R.string.old_8pm_results)+oldResult.getDate()+")");
                                        eightIntent.putExtra("img",eightpm);
                                        startActivity(eightIntent);
                                    });
                                }
                                else
                                {
                                    EightPmClick.setOnClickListener(v -> {
                                        Tools.showSnackbar(OldResults.this,OldResultsLayout,getString(R.string.not_publish),getString(R.string.okay));
                                    });
                                }
                            }
                            else
                            {
                                Tools.showSnackbar(OldResults.this,OldResultsLayout,getString(R.string.not_publish),getString(R.string.okay));
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<OldResultData> call, Throwable t) {
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void loadLocale()
    {
        String localeStr = Tools.getString(OldResults.this,"locale");
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
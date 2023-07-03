package nagalandlottery.result.daily.Activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import nagalandlottery.result.daily.BuildConfig;
import nagalandlottery.result.daily.R;
import nagalandlottery.result.daily.api.ApiInterface;
import nagalandlottery.result.daily.api.models.Policy;
import nagalandlottery.result.daily.api.models.PolicyData;
import nagalandlottery.result.daily.utils.Tools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UniversalActivity extends AppCompatActivity {

    private ImageView ToolbarBack;
    private TextView ToolbarTitle;
    //private LinearLayout DisclaimerLayout;
    private Button CloseBtn;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_universal);

        String page = getIntent().getStringExtra("page").toString();
        ToolbarBack = findViewById(R.id.universal_toolbar_back);
        ToolbarTitle = findViewById(R.id.universal_toolbar_title);
        //DisclaimerLayout = findViewById(R.id.universal_disclaimer_layout);
        //CloseBtn = findViewById(R.id.universal_disclaimer_close_btn);
        webView = findViewById(R.id.universal_webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        WebViewClient webViewClient = new WebViewClient();
        webView.setWebViewClient(webViewClient);

        if (!Tools.isInternetConnected())
            Tools.showDialogNoInternet(UniversalActivity.this,"universal");

        ApiInterface.getApiRequestInterface().getPolicyData(BuildConfig.API_KEY)
                        .enqueue(new Callback<PolicyData>() {
                            @Override
                            public void onResponse(Call<PolicyData> call, Response<PolicyData> response) {
                                if (response.isSuccessful())
                                {
                                    Policy policy = response.body().getPolicys().get(0);

                                    if (page.equals("disclaimer"))
                                    {
                                        ToolbarTitle.setText("Disclaimer");
                                        //DisclaimerLayout.setVisibility(View.VISIBLE);
                                        webView.setVisibility(View.VISIBLE);
                                        webView.loadData(""+policy.getDisclemar(),
                                                "text/html", "UTF-8");
                                    }
                                    else if (page.equals("privacy"))
                                    {
                                        ToolbarTitle.setText("Privacy Policy");
                                        //DisclaimerLayout.setVisibility(View.GONE);
                                        webView.setVisibility(View.VISIBLE);
                                        webView.loadData(""+policy.getPrivacyPolicy(),
                                                "text/html", "UTF-8");
                                    }
                                    else if (page.equals("terms"))
                                    {
                                        ToolbarTitle.setText("Terms Conditions");
                                       //DisclaimerLayout.setVisibility(View.GONE);
                                        webView.setVisibility(View.VISIBLE);
                                        webView.loadData(""+policy.getTermsConditions(),
                                                "text/html", "UTF-8");
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<PolicyData> call, Throwable t) {

                            }
                        });

        ToolbarBack.setOnClickListener(v -> onBackPressed());
        //CloseBtn.setOnClickListener(v -> onBackPressed());

    }
}
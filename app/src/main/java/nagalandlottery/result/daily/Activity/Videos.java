package nagalandlottery.result.daily.Activity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.DefaultPlayerUiController;

import java.util.List;
import java.util.Locale;

import nagalandlottery.result.daily.BuildConfig;
import nagalandlottery.result.daily.R;
import nagalandlottery.result.daily.api.ApiInterface;
import nagalandlottery.result.daily.api.models.LatestResult;
import nagalandlottery.result.daily.api.models.LatestResultData;
import nagalandlottery.result.daily.api.models.Video;
import nagalandlottery.result.daily.api.models.VideoData;
import nagalandlottery.result.daily.utils.Tools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Videos extends AppCompatActivity {

    private String youtubeID = "", OneID, SixID, EightID;
    private Button OnePmClick, SixPmClick, EightPmClick;
    private TextView VideosTitle;

    private LinearLayout BtnLayout;
    private Toolbar toolbar;
    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayerListener listener;
    private ImageView ToolbarBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_videos);

        ToolbarBack = findViewById(R.id.videos_toolbar_back);
        toolbar = findViewById(R.id.videos_toolbar);
        BtnLayout = findViewById(R.id.videos_btn_layout);
        OnePmClick = findViewById(R.id.videos_1_pm_click);
        SixPmClick = findViewById(R.id.videos_6_pm_click);
        EightPmClick = findViewById(R.id.videos_8_pm_click);
        VideosTitle = findViewById(R.id.videos_title);
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);

        ToolbarBack.setOnClickListener(v -> onBackPressed());

        if (!Tools.isInternetConnected())
            Tools.showDialogNoInternet(Videos.this,"videos");
        else {
            ApiInterface.getApiRequestInterface().getLatestResults(BuildConfig.API_KEY)
                    .enqueue(new Callback<LatestResultData>() {
                        @Override
                        public void onResponse(Call<LatestResultData> call, Response<LatestResultData> response) {
                            if (response.isSuccessful()) {
                                LatestResult latestResults = response.body().getLatestResult();
                                String onepm = latestResults.getOnepmurl();
                                String sixpm = latestResults.getSixpmurl();
                                String eightpm = latestResults.getEighthpmurl();
                                ApiInterface.getApiRequestInterface().getVideoIds(BuildConfig.API_KEY)
                                        .enqueue(new Callback<VideoData>() {
                                            @Override
                                            public void onResponse(Call<VideoData> call, Response<VideoData> response) {
                                                if (response.isSuccessful())
                                                {
                                                    Video video = response.body().getVideos().get(0);
                                                    OneID = video.getOnepmID();
                                                    SixID = video.getSixpmID();
                                                    EightID = video.getEightpmID();

                                                    if (eightpm==null && sixpm==null && onepm!=null) {
                                                        VideosTitle.setText(R.string.today_1pm);
                                                        youtubeID = OneID;
                                                    }
                                                    else if (eightpm == null && onepm!=null && sixpm!=null) {
                                                        VideosTitle.setText(R.string.today_6pm);
                                                        youtubeID = SixID;
                                                    }
                                                    else if (eightpm!=null && onepm!=null && sixpm!=null) {
                                                        VideosTitle.setText(R.string.today_8pm);
                                                        youtubeID = EightID;
                                                    }
                                                    else {
                                                        VideosTitle.setText(R.string.today_8pm);
                                                        youtubeID = EightID;
                                                    }

                                                    PlayVideo();
                                                    OnePmClick.setOnClickListener(v -> {
                                                        VideosTitle.setText(R.string.today_1pm);
                                                        youtubeID = OneID;
                                                        youTubePlayerView.getYouTubePlayerWhenReady(youTubePlayer -> youTubePlayer.loadVideo(youtubeID,0));
                                                    });
                                                    SixPmClick.setOnClickListener(v -> {
                                                        VideosTitle.setText(R.string.today_6pm);
                                                        youtubeID = SixID;
                                                        youTubePlayerView.getYouTubePlayerWhenReady(youTubePlayer -> youTubePlayer.loadVideo(youtubeID,0));

                                                    });
                                                    EightPmClick.setOnClickListener(v -> {
                                                        VideosTitle.setText(R.string.today_8pm);
                                                        youtubeID = EightID;
                                                        youTubePlayerView.getYouTubePlayerWhenReady(youTubePlayer -> youTubePlayer.loadVideo(youtubeID,0));
                                                    });
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<VideoData> call, Throwable t) {
                                            }
                                        });
                            }
                        }

                        @Override
                        public void onFailure(Call<LatestResultData> call, Throwable t) {
                        }
                    });
        }
    }

    private void PlayVideo() {
        listener = new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                // using pre-made custom ui
                DefaultPlayerUiController defaultPlayerUiController = new DefaultPlayerUiController(youTubePlayerView, youTubePlayer);
                youTubePlayerView.setCustomPlayerUi(defaultPlayerUiController.getRootView());
                youTubePlayer.loadVideo(youtubeID,0);
            }
        };

        youTubePlayerView.addFullScreenListener(new YouTubePlayerFullScreenListener() {
            @Override
            public void onYouTubePlayerEnterFullScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                toolbar.setVisibility(View.GONE);
                VideosTitle.setVisibility(View.GONE);
                BtnLayout.setVisibility(View.GONE);

            }

            @Override
            public void onYouTubePlayerExitFullScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                toolbar.setVisibility(View.VISIBLE);
                VideosTitle.setVisibility(View.VISIBLE);
                BtnLayout.setVisibility(View.VISIBLE);
            }
        });

        IFramePlayerOptions options = new IFramePlayerOptions.Builder().controls(0).build();
        youTubePlayerView.initialize(listener, options);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (youTubePlayerView.isFullScreen())
        {
            youTubePlayerView.exitFullScreen();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            toolbar.setVisibility(View.VISIBLE);
            VideosTitle.setVisibility(View.VISIBLE);
            BtnLayout.setVisibility(View.VISIBLE);
        }
        else
            super.onBackPressed();
    }

    private void loadLocale()
    {
        String localeStr = Tools.getString(Videos.this,"locale");
        if (localeStr.isEmpty())
            localeStr = "en";

        Configuration config = getResources().getConfiguration();
        Locale locale = new Locale(localeStr); // or whatever language you want to support
        config.setLocale(locale);
        config.setLayoutDirection(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }
}
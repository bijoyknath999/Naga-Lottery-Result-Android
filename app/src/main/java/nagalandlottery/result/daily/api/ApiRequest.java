package nagalandlottery.result.daily.api;

import java.util.List;

import nagalandlottery.result.daily.BuildConfig;
import nagalandlottery.result.daily.api.models.AllDatum;
import nagalandlottery.result.daily.api.models.InstallData;
import nagalandlottery.result.daily.api.models.LatestResult;
import nagalandlottery.result.daily.api.models.LatestResultData;
import nagalandlottery.result.daily.api.models.OldResultData;
import nagalandlottery.result.daily.api.models.PolicyData;
import nagalandlottery.result.daily.api.models.Setting;
import nagalandlottery.result.daily.api.models.SettingData;
import nagalandlottery.result.daily.api.models.Video;
import nagalandlottery.result.daily.api.models.VideoData;
import nagalandlottery.result.daily.api.models.WinnerSearchData;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiRequest {

    @FormUrlEncoded
    @POST(BuildConfig.SETTINGS_ENDPOINT)
    Call<SettingData> getSettings(@Field("apikey") String apikey);

    @FormUrlEncoded
    @POST(BuildConfig.LATESTRESULT_ENDPOINT)
    Call<LatestResultData> getLatestResults(@Field("apikey") String apikey);

    @FormUrlEncoded
    @POST(BuildConfig.YOUTUBE_ENDPOINT)
    Call<VideoData> getVideoIds(@Field("apikey") String apikey);

    @FormUrlEncoded
    @POST(BuildConfig.CHECKRECORD_ENDPOINT)
    Call<OldResultData> getOldResults(@Field("apikey") String apikey,
                                      @Field("date") String date);

    @FormUrlEncoded
    @POST(BuildConfig.INSTALL_ENDPOINT)
    Call<InstallData> getInstallRef(@Field("apikey") String apikey);

    @FormUrlEncoded
    @POST(BuildConfig.POLICY_ENDPOINT)
    Call<PolicyData> getPolicyData(@Field("apikey") String apikey);

    @FormUrlEncoded
    @POST(BuildConfig.WINNER_ENDPOINT)
    Call<AllDatum> getWinnersList(
            @Field("apikey") String apikey,
            @Field("page") int page
    );

    @FormUrlEncoded
    @POST(BuildConfig.SEARCH_WINNER_ENDPOINT)
    Call<WinnerSearchData> searchWinners(
            @Field("apikey") String apikey,
            @Field("name") String name
    );
}

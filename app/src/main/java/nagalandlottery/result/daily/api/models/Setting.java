package nagalandlottery.result.daily.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Setting {

    @SerializedName("app_version")
    @Expose
    private Integer appVersion;
    @SerializedName("ads_status")
    @Expose
    private String adsStatus;
    @SerializedName("ads_type")
    @Expose
    private String adsType;
    @SerializedName("interstitial_index")
    @Expose
    private Integer interstitialIndex;
    @SerializedName("ads_limit")
    @Expose
    private String adsLimit;
    @SerializedName("admob_banner")
    @Expose
    private String admobBanner;
    @SerializedName("admob_interstitial")
    @Expose
    private String admobInterstitial;
    @SerializedName("admob_native")
    @Expose
    private Object admobNative;
    @SerializedName("admob_app_open")
    @Expose
    private String admobAppOpen;
    @SerializedName("admob_reward")
    @Expose
    private Object admobReward;
    @SerializedName("facebook_banner")
    @Expose
    private String facebookBanner;
    @SerializedName("facebook_interstitial")
    @Expose
    private String facebookInterstitial;
    @SerializedName("facebook_reward")
    @Expose
    private Object facebookReward;

    public Integer getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(Integer appVersion) {
        this.appVersion = appVersion;
    }

    public String getAdsStatus() {
        return adsStatus;
    }

    public void setAdsStatus(String adsStatus) {
        this.adsStatus = adsStatus;
    }

    public String getAdsType() {
        return adsType;
    }

    public void setAdsType(String adsType) {
        this.adsType = adsType;
    }

    public Integer getInterstitialIndex() {
        return interstitialIndex;
    }

    public void setInterstitialIndex(Integer interstitialIndex) {
        this.interstitialIndex = interstitialIndex;
    }

    public String getAdsLimit() {
        return adsLimit;
    }

    public void setAdsLimit(String adsLimit) {
        this.adsLimit = adsLimit;
    }

    public String getAdmobBanner() {
        return admobBanner;
    }

    public void setAdmobBanner(String admobBanner) {
        this.admobBanner = admobBanner;
    }

    public String getAdmobInterstitial() {
        return admobInterstitial;
    }

    public void setAdmobInterstitial(String admobInterstitial) {
        this.admobInterstitial = admobInterstitial;
    }

    public Object getAdmobNative() {
        return admobNative;
    }

    public void setAdmobNative(Object admobNative) {
        this.admobNative = admobNative;
    }

    public String getAdmobAppOpen() {
        return admobAppOpen;
    }

    public void setAdmobAppOpen(String admobAppOpen) {
        this.admobAppOpen = admobAppOpen;
    }

    public Object getAdmobReward() {
        return admobReward;
    }

    public void setAdmobReward(Object admobReward) {
        this.admobReward = admobReward;
    }

    public String getFacebookBanner() {
        return facebookBanner;
    }

    public void setFacebookBanner(String facebookBanner) {
        this.facebookBanner = facebookBanner;
    }

    public String getFacebookInterstitial() {
        return facebookInterstitial;
    }

    public void setFacebookInterstitial(String facebookInterstitial) {
        this.facebookInterstitial = facebookInterstitial;
    }

    public Object getFacebookReward() {
        return facebookReward;
    }

    public void setFacebookReward(Object facebookReward) {
        this.facebookReward = facebookReward;
    }

}
package nagalandlottery.result.daily.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SettingData {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("settings")
    @Expose
    private Setting settings;
    @SerializedName("result_image_path")
    @Expose
    private String resultImagePath;
    @SerializedName("result_pdf_path")
    @Expose
    private String resultPdfPath;
    @SerializedName("winner_profile_path")
    @Expose
    private String winnerProfilePath;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Setting getSettings() {
        return settings;
    }

    public void setSettings(Setting settings) {
        this.settings = settings;
    }

    public String getResultImagePath() {
        return resultImagePath;
    }

    public void setResultImagePath(String resultImagePath) {
        this.resultImagePath = resultImagePath;
    }

    public String getResultPdfPath() {
        return resultPdfPath;
    }

    public void setResultPdfPath(String resultPdfPath) {
        this.resultPdfPath = resultPdfPath;
    }

    public String getWinnerProfilePath() {
        return winnerProfilePath;
    }

    public void setWinnerProfilePath(String winnerProfilePath) {
        this.winnerProfilePath = winnerProfilePath;
    }
}

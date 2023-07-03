package nagalandlottery.result.daily.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LatestResultData {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("latest_result")
    @Expose
    private LatestResult latestResult;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LatestResult getLatestResult() {
        return latestResult;
    }

    public void setLatestResult(LatestResult latestResult) {
        this.latestResult = latestResult;
    }

}
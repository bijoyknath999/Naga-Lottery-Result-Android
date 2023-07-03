package nagalandlottery.result.daily.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllDatum {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("Winners")
    @Expose
    private WinnerData winners;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public WinnerData getWinners() {
        return winners;
    }

    public void setWinners(WinnerData winners) {
        this.winners = winners;
    }
}

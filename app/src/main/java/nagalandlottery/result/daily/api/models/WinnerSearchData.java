package nagalandlottery.result.daily.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WinnerSearchData {

    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("Winners")
    @Expose
    private List<Winner> winners;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<Winner> getWinners() {
        return winners;
    }

    public void setWinners(List<Winner> winners) {
        this.winners = winners;
    }
}

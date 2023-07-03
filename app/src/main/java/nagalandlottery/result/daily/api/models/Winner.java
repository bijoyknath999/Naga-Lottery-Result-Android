package nagalandlottery.result.daily.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Winner {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("drawName")
    @Expose
    private String drawName;
    @SerializedName("winnerPosition")
    @Expose
    private String winnerPosition;
    @SerializedName("winAmount")
    @Expose
    private String winAmount;
    @SerializedName("ticketNumber")
    @Expose
    private String ticketNumber;
    @SerializedName("drawDate")
    @Expose
    private String drawDate;
    @SerializedName("profileImage")
    @Expose
    private String profileImage;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDrawName() {
        return drawName;
    }

    public void setDrawName(String drawName) {
        this.drawName = drawName;
    }

    public String getWinnerPosition() {
        return winnerPosition;
    }

    public void setWinnerPosition(String winnerPosition) {
        this.winnerPosition = winnerPosition;
    }

    public String getWinAmount() {
        return winAmount;
    }

    public void setWinAmount(String winAmount) {
        this.winAmount = winAmount;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getDrawDate() {
        return drawDate;
    }

    public void setDrawDate(String drawDate) {
        this.drawDate = drawDate;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

}

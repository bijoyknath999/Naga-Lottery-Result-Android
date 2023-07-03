package nagalandlottery.result.daily.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Video {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("onepmID")
    @Expose
    private String onepmID;
    @SerializedName("sixpmID")
    @Expose
    private String sixpmID;
    @SerializedName("eightpmID")
    @Expose
    private String eightpmID;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOnepmID() {
        return onepmID;
    }

    public void setOnepmID(String onepmID) {
        this.onepmID = onepmID;
    }

    public String getSixpmID() {
        return sixpmID;
    }

    public void setSixpmID(String sixpmID) {
        this.sixpmID = sixpmID;
    }

    public String getEightpmID() {
        return eightpmID;
    }

    public void setEightpmID(String ninepmID) {
        this.eightpmID = ninepmID;
    }

}
package nagalandlottery.result.daily.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LatestResult {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("onepmurl")
    @Expose
    private String onepmurl;
    @SerializedName("sixpmurl")
    @Expose
    private String sixpmurl;
    @SerializedName("eighthpmurl")
    @Expose
    private String eighthpmurl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOnepmurl() {
        return onepmurl;
    }

    public void setOnepmurl(String onepmurl) {
        this.onepmurl = onepmurl;
    }

    public String getSixpmurl() {
        return sixpmurl;
    }

    public void setSixpmurl(String sixpmurl) {
        this.sixpmurl = sixpmurl;
    }

    public String getEighthpmurl() {
        return eighthpmurl;
    }

    public void setEighthpmurl(String eighthpmurl) {
        this.eighthpmurl = eighthpmurl;
    }

}
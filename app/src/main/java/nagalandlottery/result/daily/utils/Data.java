package nagalandlottery.result.daily.utils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("sound")
    @Expose
    private String sound;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("priority")
    @Expose
    private String priority;
    @SerializedName("Activity")
    @Expose
    private String activity;
    @SerializedName("ExtraValue")
    @Expose
    private String extraValue;

    public Data(String body, String title, String sound, String icon, String priority, String activity, String extraValue) {
        this.body = body;
        this.title = title;
        this.sound = sound;
        this.icon = icon;
        this.priority = priority;
        this.activity = activity;
        this.extraValue = extraValue;
    }

    public String getBody() {
        return body;
    }

    public String getTitle() {
        return title;
    }

    public String getSound() {
        return sound;
    }

    public String getIcon() {
        return icon;
    }

    public String getPriority() {
        return priority;
    }

    public String getActivity() {
        return activity;
    }

    public String getExtraValue() {
        return extraValue;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public void setExtraValue(String extraValue) {
        this.extraValue = extraValue;
    }
}
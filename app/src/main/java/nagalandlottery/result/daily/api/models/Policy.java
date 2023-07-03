package nagalandlottery.result.daily.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Policy {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("privacy_policy")
    @Expose
    private String privacyPolicy;
    @SerializedName("terms_conditions")
    @Expose
    private String termsConditions;
    @SerializedName("disclemar")
    @Expose
    private String disclemar;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPrivacyPolicy() {
        return privacyPolicy;
    }

    public void setPrivacyPolicy(String privacyPolicy) {
        this.privacyPolicy = privacyPolicy;
    }

    public String getTermsConditions() {
        return termsConditions;
    }

    public void setTermsConditions(String termsConditions) {
        this.termsConditions = termsConditions;
    }

    public String getDisclemar() {
        return disclemar;
    }

    public void setDisclemar(String disclemar) {
        this.disclemar = disclemar;
    }
}

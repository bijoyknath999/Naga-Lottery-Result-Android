package nagalandlottery.result.daily.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PolicyData {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("policys")
    @Expose
    private List<Policy> policys;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<Policy> getPolicys() {
        return policys;
    }

    public void setPolicys(List<Policy> policys) {
        this.policys = policys;
    }
}

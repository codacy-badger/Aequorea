package nich.work.aequorea.model.entity;

import com.google.gson.annotations.SerializedName;

public class DataWrapper {

    @SerializedName("data")
    private Datum mData;

    public Datum getData() {
        return mData;
    }

    public void setData(Datum data) {
        mData = data;
    }

}

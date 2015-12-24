package itis.lectures.reactiveweather;

import com.google.gson.annotations.SerializedName;

/**
 * @author Artur Vasilov
 */
public class Weather {

    @SerializedName("id")
    private int mId;
    @SerializedName("name")
    private String mName;

    public MainParams getmParams() {
        return mParams;
    }

    @SerializedName("main")
    private MainParams mParams;

    public String getName() {
        return mName;
    }

    public int getTemperature() {
        return (int) (mParams.mTemperature - 273);
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    private class MainParams {

        @SerializedName("humidity")
        private float mHumidity;

        @SerializedName("pressure")
        private float mPressure;

        public float getmTemperature() {
            return mTemperature;
        }

        public void setmTemperature(float mTemperature) {
            this.mTemperature = mTemperature;
        }

        @SerializedName("temp")
        private float mTemperature;

    }
}

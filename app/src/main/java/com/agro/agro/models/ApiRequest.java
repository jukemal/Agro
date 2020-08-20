package com.agro.agro.models;

import com.agro.agro.utils.EnumButtonStatus;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ApiRequest implements Serializable {

    @SerializedName("feed_key")
    @Expose
    private String feedKey;
    @SerializedName("value")
    @Expose
    private EnumButtonStatus value;

    public ApiRequest() {
    }

    public ApiRequest(String feedKey, EnumButtonStatus value) {
        this.feedKey = feedKey;
        this.value = value;
    }

    public String getFeedKey() {
        return feedKey;
    }

    public void setFeedKey(String feedKey) {
        this.feedKey = feedKey;
    }

    public EnumButtonStatus getValue() {
        return value;
    }

    public void setValue(EnumButtonStatus value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ApiRequest{" +
                "feedKey='" + feedKey + '\'' +
                ", value=" + value +
                '}';
    }
}

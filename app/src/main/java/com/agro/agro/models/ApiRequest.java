package com.agro.agro.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

//Model for API request.
public class ApiRequest implements Serializable {

    @SerializedName("feed_key")
    @Expose
    private String feedKey;
    @SerializedName("value")
    @Expose
    private int value;

    public ApiRequest() {
    }

    public ApiRequest(String feedKey, int value) {
        this.feedKey = feedKey;
        this.value = value;
    }

    public String getFeedKey() {
        return feedKey;
    }

    public void setFeedKey(String feedKey) {
        this.feedKey = feedKey;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
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

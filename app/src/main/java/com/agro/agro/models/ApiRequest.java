package com.agro.agro.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ApiRequest implements Serializable {

    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("feed_id")
    @Expose
    private Long feedId;

    public ApiRequest() {
    }

    public ApiRequest(String value, Long feedId) {
        this.value = value;
        this.feedId = feedId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getFeedId() {
        return feedId;
    }

    public void setFeedId(Long feedId) {
        this.feedId = feedId;
    }

    @Override
    public String toString() {
        return "ApiRequest{" +
                "value='" + value + '\'' +
                ", feedId=" + feedId +
                '}';
    }
}

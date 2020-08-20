package com.agro.agro.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ApiResponse implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("feed_id")
    @Expose
    private Long feedId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    private final static long serialVersionUID = 7747897027848972890L;

    public ApiResponse() {
    }

    public ApiResponse(String id, String value, Long feedId, String createdAt) {
        this.id = id;
        this.value = value;
        this.feedId = feedId;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "id='" + id + '\'' +
                ", value='" + value + '\'' +
                ", feedId=" + feedId +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
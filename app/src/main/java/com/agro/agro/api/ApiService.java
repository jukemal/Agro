package com.agro.agro.api;

import com.agro.agro.models.ApiRequest;
import com.agro.agro.models.ApiResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * All API endpoints.
 */
public interface ApiService {
    // Get data as a list.
    @GET("feeds/{feed}/data")
    public Call<List<ApiResponse>> getDataList(@Path("feed") String feed, @Query("limit") int limit);

    // Get last value.
    @GET("feeds/{feed}/data/last")
    public Call<ApiResponse> getDataLast(@Path("feed") String feed);

    // Post value to API.
    @POST("feeds/{feed}/data")
    public Call<ApiResponse> postData(@Path("feed") String feed, @Body ApiRequest request);
}

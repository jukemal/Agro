package com.agro.agro.ui.dashboard;

import android.annotation.SuppressLint;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.agro.agro.api.ApiService;
import com.agro.agro.api.ApiServiceGenerator;
import com.agro.agro.models.ApiResponse;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class DashboardViewModel extends ViewModel {

    private MutableLiveData<List<ApiResponse>> humidityDataList;
    private MutableLiveData<List<ApiResponse>> temperatureDataList;
    private MutableLiveData<List<ApiResponse>> soilMoistureDataList;

    private MutableLiveData<ApiResponse> humidityDataLast;
    private MutableLiveData<ApiResponse> temperatureDataLast;
    private MutableLiveData<ApiResponse> soilMoistureDataLast;

    private MutableLiveData<String> showError;

    private List<Disposable> disposableList;

    public DashboardViewModel() {
        humidityDataList = new MutableLiveData<>();
        temperatureDataList = new MutableLiveData<>();
        soilMoistureDataList = new MutableLiveData<>();

        humidityDataLast = new MutableLiveData<>();
        temperatureDataLast = new MutableLiveData<>();
        soilMoistureDataLast = new MutableLiveData<>();

        showError = new MutableLiveData<>();

        makeApiCall("humidity");
        makeApiCall("temperature");
        makeApiCall("soil-moisture");

        disposableList = new ArrayList<>();

        disposableList.add(getLatestData("humidity", 10));
        disposableList.add(getLatestData("temperature", 10));
        disposableList.add(getLatestData("soil-moisture", 10));
    }

    public MutableLiveData<List<ApiResponse>> getHumidityDataList() {
        return humidityDataList;
    }

    public MutableLiveData<List<ApiResponse>> getTemperatureDataList() {
        return temperatureDataList;
    }

    public MutableLiveData<List<ApiResponse>> getSoilMoistureDataList() {
        return soilMoistureDataList;
    }

    public MutableLiveData<ApiResponse> getHumidityDataLast() {
        return humidityDataLast;
    }

    public MutableLiveData<ApiResponse> getTemperatureDataLast() {
        return temperatureDataLast;
    }

    public MutableLiveData<ApiResponse> getSoilMoistureDataLast() {
        return soilMoistureDataLast;
    }

    public MutableLiveData<String> getShowError() {
        return showError;
    }

    private void makeApiCall(String feed) {
        ApiService service = ApiServiceGenerator.createService(ApiService.class);

        Call<List<ApiResponse>> call = service.getDataList(feed, 20);

        call.enqueue(new Callback<List<ApiResponse>>() {
            @Override
            public void onResponse(@NotNull Call<List<ApiResponse>> call, @NotNull Response<List<ApiResponse>> response) {
                if (response.isSuccessful()) {
                    List<ApiResponse> apiResponses = response.body();

                    switch (feed) {
                        case "humidity":
                            humidityDataList.postValue(apiResponses);
                            break;
                        case "temperature":
                            temperatureDataList.postValue(apiResponses);
                            break;
                        case "soil-moisture":
                            soilMoistureDataList.postValue(apiResponses);
                            break;
                    }
                } else {
                    try {
                        assert response.errorBody() != null;
                        Timber.e("Res : %s", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    showError.postValue("Error Connection.");
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<ApiResponse>> call, Throwable t) {
                Timber.e(t, "Err : ");
                showError.postValue("Error Connection.");
            }
        });
    }

    @SuppressLint("CheckResult")
    private Disposable getLatestData(String feed, int interval) {
        return Observable.interval(interval, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        ApiService service = ApiServiceGenerator.createService(ApiService.class);

                        Call<ApiResponse> call = service.getDataLast(feed);

                        call.enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(@NotNull Call<ApiResponse> call, @NotNull Response<ApiResponse> response) {
                                if (response.isSuccessful()) {
                                    ApiResponse apiResponse = response.body();

                                    switch (feed) {
                                        case "humidity":
                                            humidityDataLast.postValue(apiResponse);
                                            break;
                                        case "temperature":
                                            temperatureDataLast.postValue(apiResponse);
                                            break;
                                        case "soil-moisture":
                                            soilMoistureDataLast.postValue(apiResponse);
                                            break;
                                    }
                                } else {
                                    try {
                                        assert response.errorBody() != null;
                                        Timber.e("Res : %s", response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    showError.postValue("Error Connection.");
                                }
                            }

                            @Override
                            public void onFailure(@NotNull Call<ApiResponse> call, @NotNull Throwable t) {
                                Timber.e(t, "Err : ");
                                showError.postValue("Error Connection.");
                            }
                        });
                    }
                });
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        for (Disposable disposable : disposableList) {
            disposable.dispose();
        }
    }
}

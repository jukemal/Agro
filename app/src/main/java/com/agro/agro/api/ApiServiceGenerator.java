package com.agro.agro.api;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiServiceGenerator {
    private static final String BASE_URL = "https://io.adafruit.com/api/v2/";

    private static String username;
    private static String key;

    private static Retrofit retrofit;

    public static void setup(final String username, final String token) {
        ApiServiceGenerator.username = username;
        ApiServiceGenerator.key = key;

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL + username + "/")
                .addConverterFactory(GsonConverterFactory.create());

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(logging);

        httpClient.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder builder1 = original.newBuilder()
                        .header("X-AIO-Key", token);
                Request request = builder1.build();
                return chain.proceed(request);
            }
        });

        builder.client(httpClient.build());
        retrofit = builder.build();
    }

    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }

    public static void setUsername(String username) {
        ApiServiceGenerator.username = username;

        ApiServiceGenerator.setup(ApiServiceGenerator.username, ApiServiceGenerator.key);
    }

    public static void setKey(String key) {
        ApiServiceGenerator.key = key;

        ApiServiceGenerator.setup(ApiServiceGenerator.username, ApiServiceGenerator.key);
    }
}

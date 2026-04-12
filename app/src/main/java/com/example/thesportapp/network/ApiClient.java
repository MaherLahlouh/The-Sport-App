package com.example.thesportapp.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class ApiClient {

    private static final String BASE_URL = "https://www.thesportsdb.com/api/";
    private static volatile TheSportsDbApi api;

    private ApiClient() {
    }

    public static TheSportsDbApi get() {
        if (api == null) {
            synchronized (ApiClient.class) {
                if (api == null) {
                    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                    logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
                    Interceptor userAgent = chain -> {
                        Request req = chain.request().newBuilder()
                                .header("User-Agent",
                                        "Mozilla/5.0 (Linux; Android 14) AppleWebKit/537.36 "
                                                + "(KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36")
                                .header("Accept", "application/json")
                                .build();
                        return chain.proceed(req);
                    };
                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(25, TimeUnit.SECONDS)
                            .readTimeout(25, TimeUnit.SECONDS)
                            .addInterceptor(userAgent)
                            .addInterceptor(logging)
                            .build();
                    Gson gson = new GsonBuilder().create();
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .client(client)
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .build();
                    api = retrofit.create(TheSportsDbApi.class);
                }
            }
        }
        return api;
    }
}

package com.appzone.freshcrops.remote;


import com.appzone.freshcrops.services.Services;
import com.appzone.freshcrops.tags.Tags;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {
    private static Retrofit retrofit = null;

    private static Retrofit getRetrofit()
    {
        if (retrofit==null)
        {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request();
                            Request request1 = request.newBuilder().addHeader("ACCEPT", "application/json").build();
                            return chain.proceed(request1);
                        }
                    })
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .writeTimeout(1,TimeUnit.MINUTES)
                    .readTimeout(1,TimeUnit.MINUTES)
                    .retryOnConnectionFailure(true)
                    .build();

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit = new Retrofit.Builder()
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(Tags.base_url)
                    .build();

        }
        return retrofit;
    }

    public static Services getService()
    {
        Retrofit retrofit = getRetrofit();
        return retrofit.create(Services.class);

    }
}

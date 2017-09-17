package com.ik.readthenews.repository.network;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.squareup.picasso.Picasso;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * A singleton class which constructs the network stack
 */
public class NetworkInstance {

    private static NetworkInstance mInstance;

    private ApiService mApiService;
    private Picasso mPicasso;

    /**
     * Returns the single instance of NetworkInstance class
     *
     * @return NetworkInstance object
     */
    public static NetworkInstance getInstance(Context context) {

        if (mInstance == null) {
            synchronized (NetworkInstance.class) {
                if (mInstance == null) {
                    if (context instanceof Application)
                        mInstance = new NetworkInstance(context);
                    else
                        mInstance = new NetworkInstance(context.getApplicationContext());
                }
            }
        }

        return mInstance;
    }

    private NetworkInstance(Context context) {
        createNetworkStack(context);
    }

    /**
     * This method is called from the private constructor.
     * It creates and initializes the network stack for the application.
     *
     * @param context Application mContext
     */
    private void createNetworkStack(Context context) {

        String DEFAULT_BASE_URL = "http://starlord.hackerearth.com/";

        OkHttpClient okHttpClient = createOkHttpClient(context);

        mApiService = getApiServiceInstance(createRetrofit(okHttpClient, DEFAULT_BASE_URL));
        mPicasso = getPicassoInstance(context, okHttpClient);
    }

    private Picasso getPicassoInstance(Context context, OkHttpClient okHttpClient) {
        return new Picasso.Builder(context)
                .downloader(new OkHttp3Downloader(okHttpClient))
                .build();
    }

    private ApiService getApiServiceInstance(Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }

    private Retrofit createRetrofit(OkHttpClient okHttpClient, String baseUrl) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    private OkHttpClient createOkHttpClient(Context context) {

        long CACHE_SIZE = 10 * 1024 * 1024; //10 MB
        String cacheFileName = "readthenews_cache";

        File cacheFile = createCacheFile(context, cacheFileName);
        Cache cache = createCache(cacheFile, CACHE_SIZE);
        HttpLoggingInterceptor loggingInterceptor = createLoggingInterceptor();

        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .cache(cache)
                .build();
    }

    private HttpLoggingInterceptor createLoggingInterceptor() {
        return new HttpLoggingInterceptor(message -> Timber.d(message));
    }

    private Cache createCache(File cacheFile, long cacheSize) {
        return new Cache(cacheFile, cacheSize);
    }

    private File createCacheFile(Context context, String fileName) {
        File cacheFile = new File(context.getCacheDir(), fileName);
        cacheFile.mkdirs();
        return cacheFile;
    }

    public ApiService getApiService() {
        return mApiService;
    }

    public Picasso getPicasso() {
        return mPicasso;
    }

}

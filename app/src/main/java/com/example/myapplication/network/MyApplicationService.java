package com.example.myapplication.network;

import android.content.Context;
import com.example.myapplication.model.RequestResponse;
import com.example.myapplication.session.SessionManager;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.realm.RealmObject;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class MyApplicationService {
    private static final String URL_BASE = "https://api.themoviedb.org/3/";
    public static final String URL_IMAGE_BASE = "https://image.tmdb.org/t/p/w500";
    public static String API_KEY = "0cfa5b299bff50c0f79a91d047565c4d";
    private static final String PARAMETER_APY_KEY = "api_key";
    private MyApplicationApi mMyApplicationApi;
    private Context mContext;


    public MyApplicationService(Context context) {
        this.mContext = context;

        OkHttpClient okHttpClient;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        builder.interceptors().add((new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                SessionManager sessionManager = new SessionManager(mContext);
                String token = sessionManager.getToken();

                if (!original.url().toString().contains("signin_client") || original.url().toString().contains("register_client")) {
                    Request request = original.newBuilder()
                            .header("Accept", "application/json")
                            .header("Authorization", "Bearer " + token)
                            .build();
                    return chain.proceed(request);
                } else {
                    return chain.proceed(original);
                }
            }
        }));

        builder.connectTimeout(30, TimeUnit.SECONDS);
        builder.writeTimeout(30, TimeUnit.SECONDS);
        builder.readTimeout(30, TimeUnit.SECONDS);
        okHttpClient = builder.build();

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getDeclaringClass().equals(RealmObject.class);
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                }).create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();



        mMyApplicationApi = retrofit.create(MyApplicationApi.class);

    }
    public interface MyApplicationApi{

        @GET("movie/popular")
        Observable<RequestResponse> getPopularMovieList(@Query(PARAMETER_APY_KEY) String api_key);

        @GET("movie/top_rated")
        Observable<RequestResponse> getTopRatedMovieList(@Query(PARAMETER_APY_KEY) String api_key);

        @GET("movie/upcoming")
        Observable<RequestResponse> getUpcomingMovieList(@Query(PARAMETER_APY_KEY) String api_key);

        @GET("movie/{movieId}/videos")
        Observable<RequestResponse> getVideos(@Path("movieId")String movieId, @Query(PARAMETER_APY_KEY) String api_key);

    }

    public MyApplicationApi getmMyApplicationApi() {
        return mMyApplicationApi;
    }

}

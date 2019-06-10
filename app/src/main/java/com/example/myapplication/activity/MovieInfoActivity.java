package com.example.myapplication.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;
import com.example.myapplication.model.Movie;
import com.example.myapplication.model.RequestResponse;
import com.example.myapplication.network.MyApplicationService;
import com.example.myapplication.util.Util;
import com.squareup.picasso.Picasso;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.example.myapplication.network.MyApplicationService.API_KEY;
import static com.example.myapplication.network.MyApplicationService.URL_IMAGE_BASE;
import static com.example.myapplication.util.Util.buildTrailerDataToVideo;

public class MovieInfoActivity extends AppCompatActivity {
    private TextView movieTitleTextView;
    private TextView overviewTetView;
    private ImageView imageMovieImageView;
    private WebView trailerWebView;
    private Movie movie;
    private MyApplicationService mMyApplicationService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);
        movieTitleTextView = findViewById(R.id.title_movie_text_view);
        imageMovieImageView = findViewById(R.id.image_view);
        overviewTetView = findViewById(R.id.overview_text_view);
        trailerWebView = findViewById(R.id.trailer_web_view);
        mMyApplicationService = new MyApplicationService(this);
        Intent intent = getIntent();
        movie = (Movie)intent.getSerializableExtra("movie");

        validateCacheImage();
        movieTitleTextView.setText(movie.getTitle());
        overviewTetView.setText("Overview: ".concat(movie.getOverview()));
        getInfoVideo(movie.getId());
    }
    private void validateCacheImage(){
        Bitmap imageCache = Util.getCacheImageById(movie.getId());
        if(imageCache != null){
            imageMovieImageView.setImageBitmap(imageCache);
        }else{
            Picasso.with(this).load(URL_IMAGE_BASE.concat(movie.getBackdropPath())).into(imageMovieImageView);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void watchTrailer(String key){
        String videoUrl = buildTrailerDataToVideo(key);

        if(videoUrl == null)
            return;

        trailerWebView.getSettings().setJavaScriptEnabled(true);
        trailerWebView.setWebChromeClient(new WebChromeClient() {

        } );
        trailerWebView.loadData(videoUrl, "text/html" , "utf-8");
        trailerWebView.setVisibility(View.VISIBLE);
    }

    private void getInfoVideo(String movieId){

        mMyApplicationService = new MyApplicationService(this);
        mMyApplicationService.getmMyApplicationApi().getVideos(String.valueOf(movieId),API_KEY)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<RequestResponse>() {

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("onError",e.getMessage());
                        Toast.makeText(MovieInfoActivity.this, "No es posible ver el trailer 401:Unauthorized", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(RequestResponse requestResponse) {
                        if(requestResponse.getResults() != null ){}
                        if(requestResponse.getResults().get(0).getTrailerSite().equals("YouTube")){
                            watchTrailer(requestResponse.getResults().get(0).getTrailerKey());
                        }
                    }
                    });

    }
}

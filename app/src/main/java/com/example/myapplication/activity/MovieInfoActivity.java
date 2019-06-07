package com.example.myapplication.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.myapplication.R;
import com.example.myapplication.model.Movie;
import com.example.myapplication.util.Util;
import com.squareup.picasso.Picasso;

import static com.example.myapplication.network.MyApplicationService.URL_IMAGE_BASE;

public class MovieInfoActivity extends AppCompatActivity {
    private TextView movieTitleTextView;
    private TextView overviewTetView;
    private ImageView imageMovieImageView;
    private Movie movie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);
        movieTitleTextView = findViewById(R.id.title_movie_text_view);
        imageMovieImageView = findViewById(R.id.image_view);
        overviewTetView = findViewById(R.id.overview_text_view);
        Intent intent = getIntent();
        movie = (Movie)intent.getSerializableExtra("movie");

        validateCacheImage();
        movieTitleTextView.setText(movie.getTitle());
        overviewTetView.setText("Overview: ".concat(movie.getOverview()));
    }
    private void validateCacheImage(){
        Bitmap imageCache = Util.getCacheImageById(movie.getId());
        if(imageCache != null){
            imageMovieImageView.setImageBitmap(imageCache);
        }else{
            Picasso.with(this).load(URL_IMAGE_BASE.concat(movie.getBackdropPath())).into(imageMovieImageView);
        }
    }
}

package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.example.myapplication.R;
import com.example.myapplication.adapter.MoviesRecyclerViewcAdapter;
import com.example.myapplication.helper.InternalStorage;
import com.example.myapplication.model.Movie;
import com.example.myapplication.model.RequestResponse;
import com.example.myapplication.network.MyApplicationService;
import com.facebook.drawee.backends.pipeline.Fresco;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.myapplication.network.MyApplicationService.API_KEY;
import static com.example.myapplication.util.Util.*;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    RecyclerView moviesRecyclerView;
    private MyApplicationService mMyApplicationService;
    private List<Movie> movies;
    private MoviesRecyclerViewcAdapter moviesRecyclerViewcAdapter;
    private MoviesRecyclerViewcAdapter.OnItemClickListener onItemClickListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fresco.initialize(this);
        moviesRecyclerView = findViewById(R.id.movies_recycler_view);
        movies = new ArrayList<>();

        Spinner spinner = findViewById(R.id.categories_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        onItemClickListener = new MoviesRecyclerViewcAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Movie movie, int adapterPosition) {

                Intent i = new Intent(MainActivity.this, MovieInfoActivity.class);
                i.putExtra("movie", movie);
                startActivity(i);
                overridePendingTransition(R.anim.slide_to_ride, R.anim.slide_out_left);
            }
        };

        moviesRecyclerViewcAdapter = new MoviesRecyclerViewcAdapter(movies, this, onItemClickListener);
        final LinearLayoutManager layoutManager = new  LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        moviesRecyclerView.setLayoutManager(layoutManager);
        moviesRecyclerView.setAdapter(moviesRecyclerViewcAdapter);

    }
    private void loadUpcomingMovieList() {
        RequestResponse localMovies = readCategoryMovies(UPCOMMING_CATEGORY_KEY);
        if(localMovies != null){
            showMovies(localMovies.getResults());
            return;
        }
        mMyApplicationService = new MyApplicationService(this);
        mMyApplicationService.getmMyApplicationApi().getUpcomingMovieList(API_KEY)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<RequestResponse>() {

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e){
                        Log.e("onError",e.getMessage());
                    }

                    @Override
                    public void onNext(RequestResponse requestResponse) {
                        showMovies(requestResponse.getResults());
                        saveCategoryMovies(UPCOMMING_CATEGORY_KEY, requestResponse);
                    }
                });
    }

    private void loadTopRatedMovieList() {
        RequestResponse localMovies = readCategoryMovies(TOP_RATED_CATEGORY_KEY);
        if(localMovies != null){
            showMovies(localMovies.getResults());
            return;
        }
        mMyApplicationService = new MyApplicationService(this);
        mMyApplicationService.getmMyApplicationApi().getTopRatedMovieList(API_KEY)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<RequestResponse>() {

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("onError",e.getMessage());
                    }

                    @Override
                    public void onNext(RequestResponse requestResponse) {
                        showMovies(requestResponse.getResults());
                        saveCategoryMovies(TOP_RATED_CATEGORY_KEY, requestResponse);
                    }
                });
    }

    private void loadPopularMovieList() {
        RequestResponse localMovies = readCategoryMovies(POPULAR_CATEGORY_KEY);
        if(localMovies != null){
            showMovies(localMovies.getResults());
            return;
        }
        mMyApplicationService = new MyApplicationService(this);
        mMyApplicationService.getmMyApplicationApi().getPopularMovieList(API_KEY)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<RequestResponse>() {

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("onError",e.getMessage());
                    }

                    @Override
                    public void onNext(RequestResponse requestResponse) {
                        showMovies(requestResponse.getResults());
                        saveCategoryMovies(POPULAR_CATEGORY_KEY, requestResponse);
                    }
                });
    }

    private void saveCategoryMovies(String categoryKey, RequestResponse movies){

        try {
            InternalStorage.writeObject(this,categoryKey, movies);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private RequestResponse readCategoryMovies(String categoryKey){

        try {
            return InternalStorage.readObject(this, categoryKey);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void showMovies(List<Movie> newMovies){
        movies.clear();
        movies.addAll(newMovies);
        moviesRecyclerViewcAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 0:
                loadPopularMovieList();
                break;
            case 1:
                loadTopRatedMovieList();
                break;
            case 2:
                loadUpcomingMovieList();
                break;
        }
    }



    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

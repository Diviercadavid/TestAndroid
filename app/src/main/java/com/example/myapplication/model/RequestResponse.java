package com.example.myapplication.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class RequestResponse extends PageableResponse implements Serializable  {

    @SerializedName("adult")
    private boolean adult;
    @SerializedName("backdrop_path")
    private String backdrop_path;
    @SerializedName("title")
    private String title;
    @SerializedName("results")
    ArrayList < Movie > results = new ArrayList< Movie>();
    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Movie> getResults() {
        return results;
    }

    public void setResults(ArrayList<Movie> results) {
        this.results = results;
    }
}

package com.example.myapplication.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.myapplication.R;
import com.example.myapplication.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.myapplication.network.MyApplicationService.URL_IMAGE_BASE;

public class MoviesRecyclerViewcAdapter extends  RecyclerView.Adapter<MoviesRecyclerViewcAdapter.GroupViewHolder> implements View.OnClickListener{

    private  View.OnClickListener listener;
    private List<Movie> movieList;
    private Context mContext;
    private OnItemClickListener itemClickListener;

    public MoviesRecyclerViewcAdapter(List<Movie> movieList, Context mContext, OnItemClickListener mListener) {
        this.movieList = movieList;
        this.mContext = mContext;
        this.itemClickListener = mListener;
    }

    @NonNull
    @Override
    public MoviesRecyclerViewcAdapter.GroupViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(mContext, R.layout.movie_template, null);
        GroupViewHolder groupViewHolder = new GroupViewHolder(view);
        view.setOnClickListener(this);

        return groupViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull MoviesRecyclerViewcAdapter.GroupViewHolder groupViewHolder, int i) {
        Movie movie = movieList.get(i);
        groupViewHolder.movieTitle.setText(movie.getTitle());
        Picasso.with(mContext).load(URL_IMAGE_BASE.concat(movie.getBackdropPath())).into(groupViewHolder.imageView);
        groupViewHolder.bind(movie, itemClickListener);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {

        if(listener != null){
            listener.onClick(v);
        }

    }

    public class GroupViewHolder extends RecyclerView.ViewHolder {

        TextView movieTitle;
        ImageView imageView;
        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            movieTitle =  itemView.findViewById(R.id.movie_name_template_text_view);
            imageView = itemView.findViewById(R.id.image_template_image_view);
        }
        public void bind(final Movie movie, final OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(movie, getAdapterPosition());
                }
            });

        }
    }
    public interface OnItemClickListener {
        void onItemClick(Movie movie, int adapterPosition);
    }


    public void clear() {
        movieList.clear();
        notifyDataSetChanged();
    }

}

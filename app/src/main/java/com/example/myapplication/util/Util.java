package com.example.myapplication.util;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import com.example.myapplication.helper.ImageCacheStore;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import static com.example.myapplication.helper.ImageCacheStore.cacheDir;

public class Util {
    public final static String POPULAR_CATEGORY_KEY = "popular";

    public final static String TOP_RATED_CATEGORY_KEY = "top_rated";

    public final static String UPCOMMING_CATEGORY_KEY = "upcoming";

    private Target target;
    public static Target targetImageCache(final String fileName){
        return new Target() {

            @Override
            public void onPrepareLoad(Drawable arg0) {
                return;
            }

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom arg1) {

                try {
                    ImageCacheStore.getInstance().saveCacheFile(cacheDir, bitmap, fileName);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onBitmapFailed(Drawable arg0) {
                return;
            }
        };
    }
    public static Bitmap getCacheImageById(String id){
        return ImageCacheStore.getInstance().getCacheFile(cacheDir+id+".PNG");
    }

    public  static String buildTrailerDataToVideo(String youtubeKey){
        if(youtubeKey != null){
            return "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/"+ youtubeKey +"\" frameborder=\"0\" allowfullscreen></iframe>";
        }
        return null;
    }
}

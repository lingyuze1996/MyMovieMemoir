package ling.yuze.mymoviememoir.network;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import ling.yuze.mymoviememoir.R;

public class ImageDownload {
    public static void setImage(ImageView image, String path) {
        final String base_url = "http://image.tmdb.org/t/p/w200/";
        String url = base_url + path;
        Picasso.get()
                .load(url)
                .placeholder(R.drawable.ic_cloud_off)
                .fit()
                .centerInside()
                .into(image);
    }
}

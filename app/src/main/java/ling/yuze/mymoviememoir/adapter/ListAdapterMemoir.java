package ling.yuze.mymoviememoir.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import ling.yuze.mymoviememoir.R;
import ling.yuze.mymoviememoir.data.MemoirItem;

import static ling.yuze.mymoviememoir.network.ImageDownload.setImage;

public class ListAdapterMemoir extends ArrayAdapter<MemoirItem> {
    private int resourceId;


    public ListAdapterMemoir(Context context, int resource, List<MemoirItem> memoirs) {
        super(context, resource, memoirs);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        MemoirItem memoir = getItem(position);
        View v = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        TextView tvMovieName = v.findViewById(R.id.tv_ls_memoir_movie_name);
        TextView tvReleaseYear = v.findViewById(R.id.tv_ls_memoir_release_year);
        ImageView imageMovie = v.findViewById(R.id.ls_memoir_image);
        TextView tvWatching = v.findViewById(R.id.tv_ls_memoir_watching_date);
        TextView tvPostcode = v.findViewById(R.id.tv_ls_memoir_watching_suburb);
        TextView tvComment = v.findViewById(R.id.tv_ls_memoir_comment);
        RatingBar myRating = v.findViewById(R.id.ratingBar_my);
        RatingBar publicRating = v.findViewById(R.id.ratingBar_public);

        tvMovieName.setText(memoir.getMovie().getName());
        tvReleaseYear.setText("(" + memoir.getMovie().getReleaseDate() + ")");
        setImage(imageMovie, memoir.getMovie().getImagePath());
        tvWatching.setText("Watched on: " + memoir.getWatching());
        tvPostcode.setText("At suburb: " + memoir.getSuburb());
        tvComment.setText(memoir.getComment());
        myRating.setRating(memoir.getMyRating());
        publicRating.setRating(Math.round(memoir.getMovie().getPublicRating()) / 2.0f);


        return v;
    }


}

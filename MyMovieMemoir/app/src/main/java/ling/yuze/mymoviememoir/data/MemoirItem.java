package ling.yuze.mymoviememoir.data;

import java.util.ArrayList;
import java.util.List;

public class MemoirItem {
    private String name;
    private String release;
    private String watching;
    private String suburb;
    private String comment;
    private String imagePath;
    private float myRating;
    private float publicRating;

    public MemoirItem(String mName, String mRelease, String mWatching, String mSuburb,
                      String path, String mComment, float mMyRating, float mPublicRating) {
        name = mName;
        release = mRelease;
        watching = mWatching;
        suburb = mSuburb;
        myRating = mMyRating;
        publicRating = mPublicRating;
        imagePath = path;
        comment = mComment;
    }

    public String getImagePath() {
        return imagePath;
    }

    public float getMyRating() {
        return myRating;
    }

    public float getPublicRating() {
        return publicRating;
    }

    public String getRelease() {
        return release;
    }

    public String getSuburb() {
        return suburb;
    }

    public String getComment() {
        return comment;
    }

    public String getWatching() {
        return watching;
    }

    public String getName() {
        return name;
    }

    public static List<MemoirItem> createList() {
        List<MemoirItem> list = new ArrayList<>();
        return list;
    }
}

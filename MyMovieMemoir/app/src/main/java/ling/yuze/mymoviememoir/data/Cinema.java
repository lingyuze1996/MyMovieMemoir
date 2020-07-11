package ling.yuze.mymoviememoir.data;

import androidx.annotation.NonNull;

public class Cinema {
    private int CId;
    private String CLocationPostcode;
    private String CName;

    public Cinema (int id, String name, String postcode) {
        CId = id;
        CName = name;
        CLocationPostcode = postcode;
    }

    public Cinema (String name, String postcode) {
        CName = name;
        CLocationPostcode = postcode;
    }

    public Cinema (int id) {
        CId = id;
    }

    public void setId(int id) {
        CId = id;
    }

    public int getCId() {
        return CId;
    }

    public String getCName() {
        return CName;
    }

    public String getCLocationPostcode() {
        return CLocationPostcode;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}

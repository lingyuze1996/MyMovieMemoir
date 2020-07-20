package ling.yuze.mymoviememoir.network;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetworkConnection {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client;
    private String url;
    public NetworkConnection() {
        client = new OkHttpClient();
    }

    public void setUrl(String urlString) {
        url = urlString;
    }

    public String httpGet() throws IOException {
        String responseString;
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        Request request = builder.build();
        Response response = client.newCall(request).execute();
        responseString = response.body().string();

        return responseString;
    }

    public int httpPost(String jsonString) throws IOException{
        int responseCode;
        RequestBody body = RequestBody.create(jsonString, JSON);
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        Request request = builder.post(body).build();
        Response response = client.newCall(request).execute();
        responseCode = response.code();
        return responseCode;
    }



}

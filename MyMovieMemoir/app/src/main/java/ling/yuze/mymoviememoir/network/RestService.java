package ling.yuze.mymoviememoir.network;

import java.io.IOException;

import ling.yuze.mymoviememoir.formatting.JsonParser;

public class RestService extends NetworkConnection {
    private static final String BASE_URL = "http://192.168.0.5:8080/MemoirREST/webresources/";
    public RestService() {
        super();
    }

    @Override
    public void setUrl(String path) {
        super.setUrl(BASE_URL + path);
    }

    public String getFirstNameByUsername (String usernameJson) {
        String firstName = "";
        Object valueObject = Json;
        Json
        if (valueObject != null)
            firstName = (String) valueObject;

    }

    public String getPasswordByUserName (String usernameJson) {
        String passwordHash = "";
        Object valueObject = JsonParser.getJsonValue("passwordHash", usernameJson, 1);
        if (valueObject != null)
            passwordHash = (String) valueObject;
    }

    public String getByUsername(String username) {
        String response = "";
        String path = "memoir.credentials/" + username;
        setUrl(path);
        try {
            response = httpGet();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            return response;
        }
    }
}

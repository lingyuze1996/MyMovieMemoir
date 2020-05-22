package ling.yuze.mymoviememoir.network;

import java.io.IOException;
import java.util.List;

import ling.yuze.mymoviememoir.utility.JsonParser;

public class RestService extends NetworkConnection {
    private static final String BASE_URL = "http://192.168.0.5:8080/MemoirREST/webresources/";
    public RestService() {
        super();
    }

    @Override
    public void setUrl(String path) {
        super.setUrl(BASE_URL + path);
    }

    public String getFirstNameByUsername (String credentialsJson) {
        String firstName = "";
        Object valueObject = JsonParser.getJsonValue(credentialsJson, "PId", "PFirstName");

        if (valueObject != null)
            firstName = (String) valueObject;
        return firstName;
    }

    public String getPasswordByUsername (String credentialsJson) {
        String passwordHash = "";
        Object valueObject = JsonParser.getJsonValue(credentialsJson,"passwordHash");
        if (valueObject != null)
            passwordHash = (String) valueObject;
        return passwordHash;
    }

    public String getByUsername(String username) {
        String response = "";
        final String path = "memoir.credentials/" + username;
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

    public String getAllPersons() {
        String response = "";
        final String path = "memoir.person/";
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

    public int getLargestPersonId(String personsJson) {
        Integer id = 0;
        List<Object> list = JsonParser.getValueList(personsJson, "PId");
        if (list.size() != 0) {
            for (Object object : list) {
                if ((Integer) object > id) {
                    id = (Integer) object;
                }
            }
        }
        return id;

    }

    public boolean postCredentials(String credentialsJson) {
        boolean success = false;
        final String path = "memoir.credentials/";
        setUrl(path);
        try {
            int responseCode = httpPost(credentialsJson);
            if (responseCode == 204) {
                success = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            return success;
        }
    }

    public boolean postPerson(String personJson) {
        boolean success = false;
        final String path = "memoir.person/";
        setUrl(path);
        try {
            int responseCode = httpPost(personJson);
            if (responseCode == 204) {
                success = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            return success;
        }
    }
}

package ling.yuze.mymoviememoir.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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

    public Integer getIdFromCredentials (String credentialsJson) {
        int id = 0;
        Object valueObject = JsonParser.getJsonValue(credentialsJson, "PId", "PId");
        if (valueObject != null)
            id = (Integer) valueObject;
        return id;
    }

    public String getFirstNameFromCredentials (String credentialsJson) {
        String firstName = "";
        Object valueObject = JsonParser.getJsonValue(credentialsJson, "PId", "PFirstName");

        if (valueObject != null)
            firstName = (String) valueObject;
        return firstName;
    }

    public String getLastNameFromCredentials (String credentialsJson) {
        String lastName = "";
        Object valueObject = JsonParser.getJsonValue(credentialsJson, "PId", "PSurname");

        if (valueObject != null)
            lastName = (String) valueObject;
        return lastName;
    }

    public String getPasswordByUsername (String credentialsJson) {
        String passwordHash = "";
        Object valueObject = JsonParser.getJsonValue(credentialsJson,"passwordHash");
        if (valueObject != null)
            passwordHash = (String) valueObject;
        return passwordHash;
    }

    public String getCredentialsByUsername(String username) {
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
    public HashMap<String, Integer> getMoviesPerSuburb(int id, String start, String end) {
        HashMap<String, Integer> map = new HashMap<>();
        final String path = "memoir.memoir/findSuburbTotalMovies/" + id + "/" + start + "/" + end;
        setUrl(path);
        try {
            String response = httpGet();
            map = JsonParser.toHashMap(response, "SuburbPostcode", "TotalMovieWatched");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return map;
        }
    }
    public List<Object[]> getTopFiveRecentMovies(int id) {
        List<Object[]> list = new ArrayList<>();
        final String path = "memoir.memoir/findTopFiveRecentMovies/" + id;
        setUrl(path);
        try {
            String response = httpGet();
            list = JsonParser.toList(response, "MovieName", "Rating", "ReleaseDate");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return list;
        }
    }

    public int[] getMoviesPerMonth(int id, int year) {
        int[] numbers = new int[12];
        final String path = "memoir.memoir/findMonthTotalMovies/" + id + "/" + year;
        setUrl(path);
        try {
            String response = httpGet();
            List<Object> list = JsonParser.getValueList(response, "TotalMoviesWatched");
            for (int index = 0; index < list.size(); index ++) {
                numbers[index] = (int) list.get(index);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return numbers;
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

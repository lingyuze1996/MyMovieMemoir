package ling.yuze.mymoviememoir.network;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ling.yuze.mymoviememoir.data.Memoir;
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

    public Integer getIdFromCredentials(String credentialsJson) {
        int id = 0;
        Object valueObject = JsonParser.getJsonValue(credentialsJson, "PId", "PId");
        if (valueObject != null)
            id = (Integer) valueObject;
        return id;
    }

    public String getFirstNameFromCredentials(String credentialsJson) {
        String firstName = "";
        Object valueObject = JsonParser.getJsonValue(credentialsJson, "PId", "PFirstName");

        if (valueObject != null)
            firstName = (String) valueObject;
        return firstName;
    }

    public String getLastNameFromCredentials(String credentialsJson) {
        String lastName = "";
        Object valueObject = JsonParser.getJsonValue(credentialsJson, "PId", "PSurname");

        if (valueObject != null)
            lastName = (String) valueObject;
        return lastName;
    }

    public String getPasswordByUsername(String credentialsJson) {
        String passwordHash = "";
        Object valueObject = JsonParser.getJsonValue(credentialsJson, "passwordHash");
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
        } finally {
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
            for (int index = 0; index < list.size(); index++) {
                numbers[index] = (int) list.get(index);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return numbers;
        }
    }

    public String getAddressByPersonId(int id) {
        String address = "";
        final String path = "memoir.person/" + id;
        setUrl(path);
        try {
            String response = httpGet();
            address = (String) JsonParser.getJsonValue(response, "PAddress");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return address;
        }
    }

    public List<Object[]> getAllMemoirsByPerson(int personId) {
        List<Object[]> list = new ArrayList<>();
        final String path = "memoir.memoir/findByPersonId/" + personId;
        setUrl(path);
        try {
            String response = httpGet();
            JSONArray jsonMemoirs = new JSONArray(response);
            for (int i = 0; i < jsonMemoirs.length(); i++) {
                JSONObject jsonMemoir = jsonMemoirs.getJSONObject(i);
                String name = jsonMemoir.getString("MMovieName");
                String comment = jsonMemoir.getString("MComment");
                String time = jsonMemoir.getString("MWatchingDatetime")
                        .substring(0, 10);
                String release = jsonMemoir.getString("MMovieReleaseDate")
                        .substring(0, 10);
                String suburb = jsonMemoir.getJSONObject("CId").
                        getString("CLocationPostcode");
                Double rating = jsonMemoir.getDouble("MRating");

                Object[] memoir = {name, release, time, comment, suburb, rating};

                list.add(memoir);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return list;
        }
    }

    public List<Object[]> getAllCinemas() {
        List<Object[]> list = new ArrayList<>();
        final String path = "memoir.cinema/";
        setUrl(path);
        try {
            String response = httpGet();
            JSONArray jsonCinemas = new JSONArray(response);
            for (int i = 0; i < jsonCinemas.length(); i++) {
                Object[] cinema = new Object[3];
                JSONObject jsonCinema = jsonCinemas.getJSONObject(i);
                cinema[0] = jsonCinema.getInt("CId");
                cinema[1] = jsonCinema.getString("CLocationPostcode");
                cinema[2] = jsonCinema.getString("CName");
                list.add(cinema);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return list;
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
        } finally {
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

    public int getMaxMemoirId() {
        Integer maxId = 0;
        final String path = "memoir.memoir/";
        setUrl(path);
        try {
            String response = httpGet();
            JSONArray jsonMemoirs = new JSONArray(response);
            for (int i = 0; i < jsonMemoirs.length(); i++) {
                JSONObject jsonCinema = jsonMemoirs.getJSONObject(i);
                int id = jsonCinema.getInt("MId");
                if (id > maxId)
                    maxId = id;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return maxId;
        }
    }

    public boolean post(String jsonString, String destination) {
        boolean success = false;
        final String path = "memoir." + destination + "/";
        setUrl(path);
        try {
            int responseCode = httpPost(jsonString);
            if (responseCode == 204) {
                success = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return success;
        }
    }


}

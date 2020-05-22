package ling.yuze.mymoviememoir.data;

public class Credentials {
    private String username;
    private String signUpDate;
    private String passwordHash;
    private Person PId;

    public Credentials(String name, String date, String pwHash) {
        username = name;
        signUpDate = date;
        passwordHash = pwHash;
    }

    public void setPId(int id) {
        PId = new Person(id);
    }
}

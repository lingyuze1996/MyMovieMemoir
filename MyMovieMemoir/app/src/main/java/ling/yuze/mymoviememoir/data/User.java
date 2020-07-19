package ling.yuze.mymoviememoir.data;

public class User {
    private String username;
    private String password;
    private String firstName;
    private String surname;
    private String gender;
    private String dob;
    private String address;
    private String state;
    private String postcode;

    public User(String username, String password, String firstName, String surname, String gender, String dob, String address, String state, String postcode) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.surname = surname;
        this.gender = gender;
        this.dob = dob;
        this.address = address;
        this.state = state;
        this.postcode = postcode;
    }

    public User(String username) {
        this.username = username;
    }
}



package ling.yuze.mymoviememoir.data;

public class User {
    private String userId;
    private String username;
    private String password;
    private String firstName;
    private String surname;
    private String gender;
    private String dob;
    private String address;
    private String state;
    private String postcode;

    public User() {
    }

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
    public User(String username, String password) {
        this.username = username;
        this.password = password;

    }

    public User(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

    public String getGender() {
        return gender;
    }

    public String getDob() {
        return dob;
    }

    public String getAddress() {
        return address;
    }

    public String getState() {
        return state;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }
}



package ling.yuze.mymoviememoir.data;

public class Person {
    private int PId;
    private String PFirstName;
    private String PSurname;
    private String PGender;
    private String PDob;
    private String PAddress;
    private String PState;
    private String PPostcode;

    public Person(int id) {
        PId = id;
    }

    public Person(int id, String fName, String lName, String gender,
                  String dob, String address, String state, String postcode) {
        PId = id;
        PFirstName = fName;
        PSurname = lName;
        PGender = gender;
        PDob = dob;
        PAddress = address;
        PState = state;
        PPostcode = postcode;
    }

    public void setPId(int id) {
        PId = id;
    }


}

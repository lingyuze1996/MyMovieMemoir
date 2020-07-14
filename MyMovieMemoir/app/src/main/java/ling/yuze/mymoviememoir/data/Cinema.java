package ling.yuze.mymoviememoir.data;

public class Cinema {
    private String id;
    private String name;
    private String address;
    private String state;
    private String region;

    public Cinema(String id, String name, String address, String state, String region) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.state = state;
        this.region = region;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getState() {
        return state;
    }

    public String getRegion() {
        return region;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}

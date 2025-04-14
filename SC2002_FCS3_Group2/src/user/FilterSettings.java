package user;

import enums.FlatType;

public class FilterSettings {
    private String location;
    private FlatType flatType;

    public FilterSettings() {
        this.location = null;
        this.flatType = null;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setFlatType(FlatType flatType) {
        this.flatType = flatType;
    }

    public String getLocation() {
        return location;
    }

    public FlatType getFlatType() {
        return flatType;
    }

    public void reset() {
        this.location = null;
        this.flatType = null;
    }
}
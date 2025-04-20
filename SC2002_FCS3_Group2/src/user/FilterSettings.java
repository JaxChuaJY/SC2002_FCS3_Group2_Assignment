package user;

import enums.FlatType;

public class FilterSettings {
    private String location;
    private FlatType flatType;
    private boolean managerViewALL; //see all? Y or N

    public FilterSettings() {
        this.location = null;
        this.flatType = null;
        managerViewALL = true;
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
    
    public void setmanagerViewALL(boolean b) {
    	managerViewALL = b;
    }

    public void reset() {
        this.location = null;
        this.flatType = null;
    }

    public boolean getmanagerViewALL(){
    	return managerViewALL;
    }
}
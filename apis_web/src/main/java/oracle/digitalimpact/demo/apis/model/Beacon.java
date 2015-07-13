package oracle.digitalimpact.demo.apis.model;

import java.io.Serializable;

public class Beacon implements Serializable {

    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1L;
    
    private String major = null;
    private String minor = null;
    private String proximity = null;
    
    public Beacon () {
        
    }

    public Beacon(String major, String minor, String proximity) {
        super();
        this.major = major;
        this.minor = minor;
        this.proximity = proximity;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getMajor() {
        return major;
    }

    public void setMinor(String minor) {
        this.minor = minor;
    }

    public String getMinor() {
        return minor;
    }

    public void setProximity(String proximity) {
        this.proximity = proximity;
    }

    public String getProximity() {
        return proximity;
    }
    

    @Override
    public String toString() {
        return "Beacon{" +
                        "major='" + major + "'," +
                        "minor='" + minor + "'," +
                        "proximity='" + proximity + "'" +
                        '}';
    }
}

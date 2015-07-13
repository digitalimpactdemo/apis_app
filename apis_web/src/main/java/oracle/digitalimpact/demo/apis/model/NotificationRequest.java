package oracle.digitalimpact.demo.apis.model;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

public class NotificationRequest implements Serializable {

    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1L;
    
    private String uuid = null;
    private String type = null;
    private String username = null;
    private String dataType = null;
    private List <Beacon> beacons = new ArrayList <Beacon> ();

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDataType() {
        return dataType;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void addBeacon(Beacon beacon) {
        this.beacons.add (beacon);
    }

    public void setBeacons(List<Beacon> beacons) {
        this.beacons = beacons;
    }

    public List<Beacon> getBeacons() {
        return beacons;
    }

    @Override
    public String toString() {
        return "NotificationRequest{" +
                        "uuid='" + uuid + "'," +
                        "username='" + username + "'," +
                        "type='" + type + "'," +
                        "dataType='" + dataType + "'," +
                        "items='" + beacons +
                        '}';
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

}

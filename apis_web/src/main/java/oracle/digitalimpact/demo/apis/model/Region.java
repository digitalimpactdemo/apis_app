package oracle.digitalimpact.demo.apis.model;

import java.io.Serializable;

public class Region implements Serializable {

    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1L;
    
    private String uuname = null;
    private String uuid = null;
    private boolean active = false;

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setUuname(String uuname) {
        this.uuname = uuname;
    }

    public String getUuname() {
        return uuname;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }
}

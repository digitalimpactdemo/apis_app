package oracle.digitalimpact.demo.apis.model;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

public class NotificationResponse implements Serializable {
    
    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1L;
    
    private String target = null;

    private List <NotificationItem> notifications = new ArrayList <NotificationItem> ();
    
    private Engage engage = new Engage ();

    public void addItem(NotificationItem item) {
        this.notifications.add (item);
    }

    public void setNotifications(List<NotificationItem> notifications) {
        this.notifications = notifications;
    }

    public List<NotificationItem> getNotifications() {
        return notifications;
    }
    
    public int getSize() {
        return getNotifications().size();
    }


    public void setTarget(String target) {
        this.target = target;
    }

    public String getTarget() {
        return target;
    }

    public void setEngage(Engage engage) {
        this.engage = engage;
    }

    public Engage getEngage() {
        return engage;
    }
}

package oracle.digitalimpact.demo.apis.model;

import java.io.Serializable;

public class NotificationItem implements Serializable {
    
    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1L;
    
    private String title    =   null;
    private String date     =   null;
    private String caption  =   null;
    private String url      =   null;


    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }
}

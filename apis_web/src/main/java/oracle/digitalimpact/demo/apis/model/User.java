package oracle.digitalimpact.demo.apis.model;

import java.io.Serializable;

public class User implements Serializable {
    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1L;
    
    private String firstName = null;
    private String lastName = null;
    private String email = null;
    private String username = null;
    private String status = null;
    private Context context = null;
    private String pin = null;
    private String picture = null;

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPicture() {
        if (picture == null) {
            picture = "";
        }
        return picture;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    @Override
    public String toString() {
        return "Registration{" +
                        "firstName='" + firstName + "'," +
                        "lastName='"  + lastName + "'," +
                        "username='"  + username + "'," +
                        "email='"     + email + "'," +
                        "pin='"       + pin + "'," +
                        "context='"   + context + "'," +
                        "picture='"   + picture + "'" +
                        '}';
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
    
    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getPin() {
        return pin;
    }

}

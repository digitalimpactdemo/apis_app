package oracle.digitalimpact.demo.apis.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties("password")
public class Security implements Serializable {
    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public Security(String username) {
        this.username = username;
    }
    private List <String> roles = new ArrayList <String> ();

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public void addRole(String role) {
        this.roles.add (role);
    }

    public List<String> getRoles() {
        return roles;
    }
}

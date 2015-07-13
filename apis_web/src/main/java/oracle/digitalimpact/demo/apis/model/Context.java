package oracle.digitalimpact.demo.apis.model;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

public class Context implements Serializable {

    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1L;
    
    private String name = null;
    private List <String> interests = new ArrayList <String> ();

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addInterest(String interest) {
        this.interests.add (interest);
    }
    
    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public List<String> getInterests() {
        return interests;
    }

    @Override
    public String toString() {
        return "Context{" +
                        "name='" + name + "'," +
                        "interests='" + interests +
                        '}';
    }
}

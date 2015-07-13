package oracle.digitalimpact.demo.apis.model;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

public class Engage implements Serializable {

    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1L;
    
    private String title    =   null;
    
    private List<String> choices = new ArrayList <String> ();

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void addChoice(String choice) {
        this.choices.add (choice);
    }
    
    public void setChoices(List<String> choices) {
        this.choices = choices;
    }

    public List<String> getChoices() {
        return choices;
    }
}

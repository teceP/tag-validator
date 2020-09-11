package de.mario.teklic.model;

public class Data {

    private String URL;

    private int cases;

    public Data(String URL, int cases){
        this.URL = URL;
        this.cases = cases;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public int getCases() {
        return cases;
    }

    public void setCases(int cases) {
        this.cases = cases;
    }

}

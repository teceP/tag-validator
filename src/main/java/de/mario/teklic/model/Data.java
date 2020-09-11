package de.mario.teklic.model;

public class Data {

    private String URL;

    private String result;

    public Data(String URL, String result){
        this.URL = URL;
        this.result = result;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}

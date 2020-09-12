package de.mario.teklic.model;

import java.util.List;

public class Result {

    private String domain;

    private int errors;

    private List<Data> dataList;

    public Result(List<Data> dataList, int errors){
        this.dataList = dataList;
        this.errors = errors;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public List<Data> getDataList() {
        return dataList;
    }

    public void setDataList(List<Data> dataList) {
        this.dataList = dataList;
    }

    public int getErrors() {
        return errors;
    }

    public void setErrors(int errors) {
        this.errors = errors;
    }
}

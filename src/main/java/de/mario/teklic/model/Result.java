package de.mario.teklic.model;

import java.util.List;

public class Result {

    private List<Data> dataList;

    private int errors;

    public Result(List<Data> dataList, int errors){
        this.dataList = dataList;
        this.errors = errors;
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

package com.ynov.tbu.schoolexplorer.response;

import com.google.gson.annotations.SerializedName;
import com.ynov.tbu.schoolexplorer.model.School;

import java.util.ArrayList;

/**
 * Created by titic on 05/06/2018.
 */

public class SchoolsResponse {

    @SerializedName("schools")
    private ArrayList<School> schools;
    @SerializedName("count")
    private Integer count;

    public ArrayList<School> getSchools() {
        return schools;
    }

    public void setSchools(ArrayList<School> schools) {
        this.schools = schools;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}

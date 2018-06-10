package com.ynov.tbu.schoolexplorer.response;

import com.google.gson.annotations.SerializedName;
import com.ynov.tbu.schoolexplorer.model.School;

import java.util.ArrayList;

/**
 * Created by titic on 05/06/2018.
 */

public class SchoolResponse {

    @SerializedName("school")
    private School school;

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }
}

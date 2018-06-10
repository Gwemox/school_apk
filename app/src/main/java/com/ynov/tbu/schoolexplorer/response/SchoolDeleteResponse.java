package com.ynov.tbu.schoolexplorer.response;

import com.google.gson.annotations.SerializedName;
import com.ynov.tbu.schoolexplorer.model.School;

/**
 * Created by titic on 05/06/2018.
 */

public class SchoolDeleteResponse {

    @SerializedName("elementDeleted")
    private School school;

    @SerializedName("success")
    private Boolean success;


    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}

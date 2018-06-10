package com.ynov.tbu.schoolexplorer.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by titic on 05/06/2018.
 */

public class AuthentificationResponse {

    @SerializedName("success")
    private Boolean success;
    @SerializedName("auth_token")
    private String auth_token;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getAuth_token() {
        return auth_token;
    }

    public void setAuth_token(String auth_token) {
        this.auth_token = auth_token;
    }
}

package com.ynov.tbu.schoolexplorer.body;

import com.google.gson.annotations.SerializedName;

/**
 * Created by titic on 05/06/2018.
 */

public class AuthentificationBody {
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;

    public AuthentificationBody(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

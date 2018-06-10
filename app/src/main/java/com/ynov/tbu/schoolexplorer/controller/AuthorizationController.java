package com.ynov.tbu.schoolexplorer.controller;

import com.ynov.tbu.schoolexplorer.body.AuthentificationBody;
import com.ynov.tbu.schoolexplorer.response.AuthentificationResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by titic on 17/04/2018.
 */

public interface AuthorizationController {
    @POST("/api/v1/users/sign_in")
    Call<AuthentificationResponse> getAuthorization(@Body AuthentificationBody authentificationBody);
}

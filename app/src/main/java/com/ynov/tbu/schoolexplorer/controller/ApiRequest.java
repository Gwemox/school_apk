package com.ynov.tbu.schoolexplorer.controller;

import com.ynov.tbu.schoolexplorer.SchoolExplorerApplication;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by titic on 17/04/2018.
 */

public class ApiRequest {

    private String token = null;

    /** Constructeur privé */
    private ApiRequest()
    {}

    /** Instance unique pré-initialisée */
    private static ApiRequest INSTANCE = new ApiRequest();

    /** Point d'accès pour l'instance unique du singleton */
    public static ApiRequest getInstance()
    {   return INSTANCE;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static <T> T getController(Class<T> interfaceController) {
        return new Retrofit.Builder()
                .baseUrl(BaseController.HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(interfaceController);

    }

    public static String getSchoolsStatus() {
        SchoolExplorerApplication schoolExplorerApplication = SchoolExplorerApplication.getInstance();
        if (schoolExplorerApplication.isSchoolPrivateEnabled() && schoolExplorerApplication.isSchoolPublicEnabled()) {
            return null;
        } else if (schoolExplorerApplication.isSchoolPublicEnabled()) {
            return "public";
        } else {
            return "private";
        }
    }

}

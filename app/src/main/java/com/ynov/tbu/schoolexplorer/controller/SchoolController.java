package com.ynov.tbu.schoolexplorer.controller;

import com.ynov.tbu.schoolexplorer.model.School;
import com.ynov.tbu.schoolexplorer.response.SchoolDeleteResponse;
import com.ynov.tbu.schoolexplorer.response.SchoolResponse;
import com.ynov.tbu.schoolexplorer.response.SchoolsResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by titic on 17/04/2018.
 */

public interface SchoolController {
    @GET("/api/v1/schools")
    Call<SchoolsResponse> getSchools(@Header("Authorization") String authorization, @Query("status") String status);

    @GET("/api/v1/schools/{id}")
    Call<SchoolResponse> getSchool(@Header("Authorization") String authorization, @Path("id") int id);

    @DELETE("/api/v1/schools/{id}")
    Call<SchoolDeleteResponse> deleteSchool(@Header("Authorization") String authorization, @Path("id") int id);

    @POST("/api/v1/schools")
    Call<ResponseBody> newSchool(@Header("Authorization") String authorization, @Body School school);

    @PATCH("/api/v1/schools/{id}")
    Call<ResponseBody> patchSchool(@Header("Authorization") String authorization, @Path("id") int id, @Body School school);
}

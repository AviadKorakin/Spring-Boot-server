package com.example.menu_editor.Service;

import com.example.menu_editor.Boundaries.MiniAppCommandBoundary;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MiniAppCommandService {

    @POST("superapp/miniapp/{miniAppName}")
    Call<MiniAppCommandBoundary> invokeCommand(
            @Path("miniAppName") String miniAppName,
            @Body MiniAppCommandBoundary message
    );
/*
    @POST("superapp/miniapp/{miniAppName}")
    Call<List<Object>> invokeCommand(
            @Path("miniAppName") String miniAppName,
            @Body MiniAppCommandBoundary message
    );

 */
}
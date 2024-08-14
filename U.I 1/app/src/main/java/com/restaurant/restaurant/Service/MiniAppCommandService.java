package com.restaurant.restaurant.Service;

import com.restaurant.restaurant.Boundaries.MiniAppCommandBoundary;
import com.restaurant.restaurant.Boundaries.UserBoundary;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MiniAppCommandService {

    @POST("superapp/miniapp/{miniAppName}")
    Call<List<Object>> invokeCommand(
            @Path("miniAppName") String miniAppName,
            @Body MiniAppCommandBoundary message
    );
}
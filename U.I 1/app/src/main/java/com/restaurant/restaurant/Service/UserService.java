package com.restaurant.restaurant.Service;

import com.restaurant.restaurant.Boundaries.NewUserBoundary;
import com.restaurant.restaurant.Boundaries.UserBoundary;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {

    @POST("superapp/users")
    Call<UserBoundary> storeInDatabase(
            @Body NewUserBoundary newuser
    );

    @GET("superapp/users/login/{superapp}/{email}")
    Call<UserBoundary> getSpecificUser(
            @Path("superapp") String eSuperapp,
            @Path("email") String email
    );

    @PUT("superapp/users/{superapp}/{userEmail}")
    Call<Void> updateUser(
            @Path("superapp") String eSuperapp,
            @Path("userEmail") String email,
            @Body UserBoundary update
    );
}

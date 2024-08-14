package com.restaurant.restaurant.Service;


import com.restaurant.restaurant.Boundaries.ObjectBoundary;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ObjectService {


    @POST("superapp/objects")
    Call<ObjectBoundary> storeInDatabase(
            @Query("userSuperapp") String userSuperapp,
            @Query("userEmail") String email,
            @Body ObjectBoundary newObj
    );

    @GET("superapp/objects/{superapp}/{id}")
    Call<ObjectBoundary> getSpecificObject(
            @Path("superapp") String superapp,
            @Path("id") String id,
            @Query("userSuperapp") String userSuperapp,
            @Query("userEmail") String email

    );

    @PUT("superapp/objects/{superapp}/{id}")
    Call<Void> updateObject(
            @Path("superapp") String eSuperapp,
            @Path("id") String id,
            @Query("userSuperapp") String userSuperapp,
            @Query("userEmail") String email,
            @Body ObjectBoundary update

    );

    @GET("superapp/objects")
    Call<List<ObjectBoundary>> getObjects(
            @Query("userSuperapp") String superapp,
            @Query("userEmail") String email,
            @Query("size") int size,
            @Query("page") int page
    );

    @GET("superapp/objects/search/byType/{type}")
    Call<List<ObjectBoundary>> getObjectByType(
            @Path("type") String type,
            @Query("userSuperapp") String superapp,
            @Query("userEmail") String email,
            @Query("size") int size,
            @Query("page") int page
    );

    @GET("superapp/objects/search/byAlias/{alias}")
    Call<List<ObjectBoundary>> getObjectByAlias(
            @Path("alias") String alias,
            @Query("userSuperapp") String superapp,
            @Query("userEmail") String email,
            @Query("size") int size,
            @Query("page") int page
    );

    @GET("superapp/objects/search/byAliasPattern/{pattern}")
    Call<List<ObjectBoundary>> getObjectByAliasPattern(
            @Path("pattern") String pattern,
            @Query("userSuperapp") String superapp,
            @Query("userEmail") String email,
            @Query("size") int size,
            @Query("page") int page
    );

    @GET("superapp/objects/search/byLocation/{lat}/{lng}/{distance}")
    Call<List<ObjectBoundary>> getObjectByLocation(
            @Path("lat") Double lat,
            @Path("lng") Double lng,
            @Path("distance") Double distance,
            @Query("units") String distanceUnits,
            @Query("userSuperapp") String superapp,
            @Query("userEmail") String email,
            @Query("size") int size,
            @Query("page") int page
    );
}

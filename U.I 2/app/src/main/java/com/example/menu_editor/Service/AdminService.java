package com.example.menu_editor.Service;


import com.example.menu_editor.Boundaries.MiniAppCommandBoundary;
import com.example.menu_editor.Boundaries.UserBoundary;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AdminService {

    @DELETE("superapp/admin/users")
    Call<Void> deleteAllUsers(
            @Query("userSuperapp") String userSuperapp,
            @Query("userEmail") String email
    );

    @DELETE("superapp/admin/objects")
    Call<Void> deleteAllObjects(
            @Query("userSuperapp") String userSuperapp,
            @Query("userEmail") String email
    );

    @DELETE("superapp/admin/miniapp")
    Call<Void> deleteAllCommands(
            @Query("userSuperapp") String userSuperapp,
            @Query("userEmail") String email
    );

    @GET("superapp/admin/users")
    Call<List<UserBoundary>> exportAllUsers(
            @Query("userSuperapp") String userSuperapp,
            @Query("userEmail") String email,
            @Query("size") int size,
            @Query("page") int page
    );

    @GET("superapp/admin/miniapp")
    Call<List<MiniAppCommandBoundary>> exportAllMiniAppCommands(
            @Query("userSuperapp") String userSuperapp,
            @Query("userEmail") String email,
            @Query("size") int size,
            @Query("page") int page
    );

    @GET("superapp/admin/miniapp/{miniApp}")
    Call<List<MiniAppCommandBoundary>> searchByMiniApp(
            @Path("miniApp") String miniApp,
            @Query("userSuperapp") String userSuperapp,
            @Query("userEmail") String email,
            @Query("size") int size,
            @Query("page") int page
    );
}

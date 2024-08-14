package com.restaurant.restaurant.Utilities;

import com.restaurant.restaurant.Boundaries.NewUserBoundary;
import com.restaurant.restaurant.Boundaries.UserBoundary;
import com.restaurant.restaurant.Enums.RoleEnum;
import com.restaurant.restaurant.General.UserId;

public class CurrentUser {
    private static CurrentUser instance;
    private UserBoundary userBoundary;

    public static void init() {
        instance = new CurrentUser();
    }

    public CurrentUser() {
        this.userBoundary = new UserBoundary();
    }

    public static void setSuperapp(String superapp) {
        instance.userBoundary.getUserId().setSuperapp(superapp);
    }

    public static void setEmail(String email) {
        instance.userBoundary.getUserId().setEmail(email);
    }

    public static void setUserBoundary(UserBoundary userBoundary) {
        instance.userBoundary = userBoundary;
    }

    public static UserId getUserId() {
        return instance.userBoundary.getUserId();
    }

    public static String getEmail() {
        return instance.userBoundary.getUserId().getEmail();
    }

    public static String getSuperapp() {
        return instance.userBoundary.getUserId().getSuperapp();
    }

    public static String getUsername() {
        return instance.userBoundary.getUsername();
    }

    public static RoleEnum getRole() {
        return instance.userBoundary.getRole();
    }



}

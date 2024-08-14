package com.example.menu_editor.Utilities;

import com.example.menu_editor.Boundaries.UserBoundary;
import com.example.menu_editor.Enums.RoleEnum;
import com.example.menu_editor.General.UserId;

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

    public static RoleEnum getRole() {
        return instance.userBoundary.getRole();
    }

    public static String getUsername() {
        return instance.userBoundary.getUsername();
    }
}

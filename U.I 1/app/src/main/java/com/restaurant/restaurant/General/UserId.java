package com.restaurant.restaurant.General;

public class UserId {
    private String superapp;
    private String email;

    public UserId() {

    }

    /**
     * This constructor creates a UserId object with the provided SuperApp name and
     * user's email address.
     *
     * @param superapp The name of the SuperApp this user belongs.
     * @param email    The email address of the user.
     */
    public UserId(String superapp, String email) {
        this.email = email;
        this.superapp = superapp;
    }


    public String getSuperapp() {
        return superapp;
    }

    public void setSuperapp(String superapp) {
        this.superapp = superapp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserId userId = (UserId) o;
        return superapp.equals(userId.superapp) && email.equals(userId.email);
    }
}



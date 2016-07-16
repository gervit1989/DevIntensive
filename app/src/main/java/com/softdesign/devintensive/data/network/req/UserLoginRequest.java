package com.softdesign.devintensive.data.network.req;

/**
 * Created by mvideo on 13.07.2016.
 */
public class UserLoginRequest {
    private String email;
    private String password;

    public UserLoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}

package com.example.lottoonline.connection;

import okhttp3.*;

import java.io.IOException;

public class OkhttpConnection {

    private static OkhttpConnection instance = null;
    private OkHttpClient client = null;

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    private OkhttpConnection() {
        client = new OkHttpClient.Builder()
                .build();
    }

    public static OkhttpConnection getInstance() {
        if(instance == null) {
            return new OkhttpConnection();
        }
        else
            return instance;
    }

    public Response getResponse(Request request) {
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public Request getRequestcheckAge(String url) {
        Request request = new Request.Builder()
                .addHeader("Content-Type","application/json")
                .url(url)
                .build();
        return request;
    }

    public Request getRequestCheckWithAccessToken(String url, String token) {
        Request request = new Request.Builder()
                .addHeader("Content-Type","application/json")
                .addHeader("Authorization","Bearer " + token)
                .url(url)
                .build();
        return request;
    }

}

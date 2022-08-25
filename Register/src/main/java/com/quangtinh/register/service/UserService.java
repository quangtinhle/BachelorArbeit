package com.quangtinh.register.service;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.quangtinh.register.connection.OkhttpConnection;
import com.quangtinh.register.convert.ReciverUserConvert;
import com.quangtinh.register.model.Credentials;
import com.quangtinh.register.model.User;
import com.quangtinh.register.model.UserDTO;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;


@Service
public class UserService {

    @Value("${endPointUser}")
    private String createUserEndPoint;
    @Value("${tokenEndPointUrl}")
    private String tokenEndPoint;
    @Value("${keycloak}")
    private String keycloak;
    @Value("${requiredAction}")
    private String requiredAction;
    @Value("${role}")
    private String ROLE;

    private String requestUrlofKeycloak = "http://localhost:8280/auth/admin/realms/E-Government-Portal/users/";


    private OkhttpConnection connection = OkhttpConnection.getInstance();


    private Gson gson = new Gson();

    //create new user on authorization server of keycloak
    public  String createUser(UserDTO userDTO)
    {
        String url = keycloak + createUserEndPoint;
        Credentials credentials = new Credentials(userDTO.getPassword());
        User user = ReciverUserConvert.converttoUser(userDTO, Arrays.asList(credentials));

        if(userDTO.isTwoFa()) {
            user.setRequiredActions(Arrays.asList("CONFIGURE_TOTP"));
        }
        Request request = connection.getRequestCreateUser(url,getAccessToken(),gson.toJson(user));
        Response response = connection.getResponse(request);
        String createdId = getCreatedUserId(response);

        if(userDTO.isTwoFa()) {

            setSubstanziellRole(createdId);
        }
        else {
            setUserRole(createdId);
        }
        String res = "";
        try {
            res = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.message();
    }


//get access token for keycloak admin cli.
    public String getAccessToken() {

        String url = keycloak + tokenEndPoint;
        Response response = connection.getResponse(connection.getRequestToken(url ));
        String body = "";
        try {
            body = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonObject bodyJson = new JsonParser().parse(body).getAsJsonObject();

        return bodyJson.get("access_token").getAsString();
    }

    public String getCreatedUserId(Response response) {
        String location = response.header("Location").toString();
        String createdUserId = location.substring(location.length() - 36);
        return createdUserId;
    }

    public JsonObject getRoleasJson() {

        String url = "http://localhost:8280/auth/admin/realms/E-Government-Portal/roles/";
        Request request = connection.getRequestRoleId(url + ROLE,getAccessToken());
        Response response = connection.getResponse(request);
        JsonObject jsonObject = null;

        try {
            jsonObject = new JsonParser().parse(response.body().string()).getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonObject jsonRole = new JsonObject();
        jsonRole.add("id",jsonObject.get("id"));
        jsonRole.add("name",jsonObject.get("name"));
        return jsonRole;

    }

    public void setSubstanziellRole (String createdUserId) {

        String requestUrl = requestUrlofKeycloak + createdUserId + "/role-mappings/realm";
        String json = "[{\n" +
                "        \"id\": \"2fcba79b-44f5-4b2b-bc8a-38d0bbeb69c7\",\n" +
                "        \"name\": \"substanziell\"\n" +
                "}]";
        Request requestsetUserRole = connection.getRequestsetUserRole(requestUrl,getAccessToken(),json);
        Response response = connection.getResponse(requestsetUserRole);

    }public void setUserRole(String createdUserId) {

        String requestUrl = requestUrlofKeycloak + createdUserId + "/role-mappings/realm";
        String json = "[{\n" +
                "        \"id\": \"8c4c5524-993f-4245-8629-0a9139868633\",\n" +
                "        \"name\": \"user\"\n" +
                "}]";
        Request requestsetUserRole = connection.getRequestsetUserRole(requestUrl,getAccessToken(),json);
        Response response = connection.getResponse(requestsetUserRole);
    }

}

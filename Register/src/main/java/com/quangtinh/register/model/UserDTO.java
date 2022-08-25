package com.quangtinh.register.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String password;
    private boolean twoFa;


}

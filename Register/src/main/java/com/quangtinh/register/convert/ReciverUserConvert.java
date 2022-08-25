package com.quangtinh.register.convert;



import com.quangtinh.register.model.Credentials;
import com.quangtinh.register.model.User;
import com.quangtinh.register.model.UserDTO;

import java.util.List;

public class ReciverUserConvert {

    private ReciverUserConvert() {}

    public static User converttoUser(UserDTO userDTO, List<Credentials> list) {
        return User.builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .enabled("true")
                .username(userDTO.getUserName())
                .credentials(list)
                //.requiredActions(requiresActions)
                .build();


    }
}

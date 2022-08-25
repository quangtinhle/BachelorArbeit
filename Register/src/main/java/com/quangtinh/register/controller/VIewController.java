package com.quangtinh.register.controller;

import com.quangtinh.register.model.UserDTO;
import com.quangtinh.register.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class VIewController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String getRegisterForm(Model model) {
        model.addAttribute("messsage", "Identität Registrieren");
        model.addAttribute("user", new UserDTO());
        return "registerform";
    }


    @PostMapping("/registed")
    public String getLoginForm(@ModelAttribute UserDTO userDTO, Model model) {
        model.addAttribute("messsage", "SIGN IN");
        userService.createUser(userDTO);
        //model.addAttribute("user", new User());
        return "registed";
    }
}

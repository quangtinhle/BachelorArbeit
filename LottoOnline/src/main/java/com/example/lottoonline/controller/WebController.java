package com.example.lottoonline.controller;

import com.example.lottoonline.connection.OkhttpConnection;
import com.example.lottoonline.model.AgeofUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class WebController {

    private final HttpServletRequest request;

    private OkhttpConnection connection = OkhttpConnection.getInstance();
    private final String url = "http://localhost:8089/check";

    public WebController(HttpServletRequest request) {
        this.request = request;
    }

    @GetMapping("/")
    public String getIndex() {
        return "index";
    }

    @GetMapping("/ageconfirm")
    public String getAgeconfirm(@ModelAttribute("user") AgeofUser user, Model model) {
        String mes = "";
        String value = "";
        model.addAttribute("user",user);
        if(user.isAgeover18()) {
            mes = user.getFirstName() + " " + user.getLastName() + " ist volljährig!";
            value = "Weiter";
        }
        else {
            mes = user.getFirstName() + " " + user.getLastName() + " ist minderjährig!";
            value = "Abbrechen";
        }

        model.addAttribute("message",mes);
        model.addAttribute("value",value);
        return "ageconfirm";
    }


    @RequestMapping(value = "users/token", method = RequestMethod.GET)
    public String control(
            @RequestParam("result") String accesstoken,
            Model model,
            RedirectAttributes redirectAttributes
    ) {

        Request request = connection.getRequestCheckWithAccessToken(url,accesstoken);
        Response response = connection.getResponse(request);
        ObjectMapper om = new ObjectMapper();
        AgeofUser ageofUser = new AgeofUser();
        try {
            String string = (response.body().string());
            ageofUser = om.readValue(string,AgeofUser.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        redirectAttributes.addFlashAttribute("user",ageofUser);

        return "redirect:/ageconfirm";
    }

    @GetMapping(value = "/logout")
    public String logout() throws ServletException {
        request.logout();
        return "redirect:/";
    }
}

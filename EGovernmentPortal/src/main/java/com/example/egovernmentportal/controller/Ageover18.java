package com.example.egovernmentportal.controller;

import com.example.egovernmentportal.model.AgeofUser;
import com.example.egovernmentportal.model.InformationResponse;
import com.example.egovernmentportal.service.UserService;
import org.keycloak.KeycloakSecurityContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;

@RestController
public class Ageover18 {


    private String idtoken;
    private final UserService userService;
    private final HttpServletRequest request;

    public Ageover18(HttpServletRequest request, UserService userService) {
        this.request = request;
        this.userService = userService;
    }

/*    @GetMapping("/checks")
    public ResponseEntity<Void> redirect() {
            return ResponseEntity.status(HttpStatus.OK)
                    .location(URI.create("https://google.com"))
                    .build();
        }

    @GetMapping("/check1")
    public void method(HttpServletResponse httpServletResponse) {
        httpServletResponse.setHeader("Location", "http://localhost:8080/");
        httpServletResponse.setStatus(200);
    }

    @GetMapping("/checkrc")
    public ModelAndView method(@ModelAttribute("res") final InformationResponse mapping1FormObject) {
        InformationResponse res = new InformationResponse("Ketqua tu 8089", true);
        ModelAndView modelAndView = new ModelAndView("redirect:" + "http://localhost:8080/users/mapping2","res",res);
        return modelAndView;
    }

    @GetMapping("/check")
    public ResponseEntity<String> method() {
        return new ResponseEntity<>("Hello World",HttpStatus.OK);
    }

    @RequestMapping(value = "/check12", method = RequestMethod.GET)
    public void methode(HttpServletResponse httpServletResponse) {
        KeycloakSecurityContext c = getKeycloakSecurityContext();
        System.out.println(c.getTokenString());
        //System.out.println(c.getIdTokenString());
        Cookie cookie = new Cookie("result","HelloWorldkjshdfkja1234123132123");
        httpServletResponse.addCookie(cookie);
        httpServletResponse.setHeader("Location", "http://localhost:8080/index");
        httpServletResponse.setStatus(302);
    }

    @RequestMapping(value = "/loginok", method = RequestMethod.GET)
    public void login(HttpServletResponse httpServletResponse) {
        KeycloakSecurityContext c = getKeycloakSecurityContext();
        System.out.println(c.getTokenString());
       // System.out.println(c.getIdTokenString());

    }

    private KeycloakSecurityContext getKeycloakSecurityContext() {
        return (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
    }

    @RequestMapping(value = { "/login1" }, method = RequestMethod.GET)
    public ModelAndView controlMapping1(RedirectAttributes redirectAttributes) {
        KeycloakSecurityContext c = getKeycloakSecurityContext();
        redirectAttributes.addAttribute("result", c.getTokenString());
        return new ModelAndView("redirect:http://localhost:8080/users/token");
    }*/


    private KeycloakSecurityContext getKeycloakSecurityContext() {
        return (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
    }

    @RequestMapping(value = { "/login" }, method = RequestMethod.GET)
    public ModelAndView controlMapping1(RedirectAttributes redirectAttributes) throws ServletException {
        KeycloakSecurityContext c = getKeycloakSecurityContext();
        idtoken = c.getIdToken().getSubject();
        redirectAttributes.addAttribute("result", c.getTokenString());
        //request.logout();
        return new ModelAndView("redirect:http://localhost:8080/users/token");
    }

    @GetMapping("/check")
    public ResponseEntity<AgeofUser> method() {
        AgeofUser ageofUser = userService.validateAgeofUser(idtoken);
        return new ResponseEntity<>(ageofUser,HttpStatus.OK);
    }

}

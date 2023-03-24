package org.jhoffmann.photostorybookwebclient.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/welcome")
public class WelcomeUserController {

    @GetMapping
    public String getWelcomeUser(Model model, @AuthenticationPrincipal OAuth2User user) {
        log.info("WelcomeUserController: getWelcomeUser " + user.toString());
        String userid = user.getName();
        log.info("WelcomeUserController: userid " + userid);
        model.addAttribute("psuserlongname", user.getAttribute("name"));

        return "welcomeuser";
    }
}

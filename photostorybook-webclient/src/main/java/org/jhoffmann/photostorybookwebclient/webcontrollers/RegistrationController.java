package org.jhoffmann.photostorybookwebclient.webcontrollers;

import lombok.extern.slf4j.Slf4j;
import org.jhoffmann.photostorybook.api.v1.model.CreateUserRequest;
import org.jhoffmann.photostorybook.api.v1.model.CreateUserResponse;
import org.jhoffmann.photostorybookwebclient.domain.PSUser;
import org.jhoffmann.photostorybookwebclient.security.RegistrationForm;
import org.jhoffmann.photostorybookwebclient.services.RestUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/register")
public class RegistrationController {


    private final RestUserService restUserService;

    private final PasswordEncoder passwordEncoder;


    public RegistrationController(RestUserService restUserService, PasswordEncoder passwordEncoder) {
        this.restUserService = restUserService;
        this.passwordEncoder = passwordEncoder;
    }


    @GetMapping
    public String registerForm() {
        return "registration";
    }

    @PostMapping
    public String processRegistration(@Valid RegistrationForm form) {

        PSUser psUser = form.toUser(passwordEncoder);

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setFullName(form.getFullname());
        createUserRequest.setUsername(form.getUsername());
        createUserRequest.setPassword(form.getPassword());
        ResponseEntity<CreateUserResponse> response = restUserService.createUser(createUserRequest);
        log.info("RegistrationController: processRegistration : created user with uuid " + response.getBody().getUuid());

        return "redirect:/login";
    }



}

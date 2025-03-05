package org.mysocket.testwebsocket.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class LoginController {


    @CrossOrigin(origins = "http://localhost:8080", exposedHeaders = "Authorization")
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}

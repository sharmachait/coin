package com.sharmachait.wazir.Controller;

import com.sharmachait.wazir.Model.Entity.USER_ROLE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @Autowired
    private Environment env;
    @GetMapping(value = "/home")
    public String home(){
        return USER_ROLE.ROLE_CUSTOMER.toString();
//        String key = env.getProperty("geckocoinkey");
//        return env.getProperty("geckocoinkey");
    }

    @GetMapping(value = "/api")
    public String secure(){
        return "home";
    }
}

package com.sharmachait.wazir.Controller;

import com.sharmachait.wazir.Model.Entity.USER_ROLE;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping(value = "/home")
    public String home(){
        return USER_ROLE.ROLE_CUSTOMER.toString();
    }
    @GetMapping(value = "/api")
    public String secure(){
        return "home";
    }
}

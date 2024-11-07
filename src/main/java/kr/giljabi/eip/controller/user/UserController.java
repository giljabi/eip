package kr.giljabi.eip.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/login")
    public String loginForm(){
        return "user/login";
    }

}


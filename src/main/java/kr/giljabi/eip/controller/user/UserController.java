package kr.giljabi.eip.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class UserController {

    @Operation(summary = "로그인 화면으로 이동")
    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping("/user/login")
    public String userLogin(){
        return login();
    }

    @RequestMapping("/")
    public String index(){
        return "index";
    }

}






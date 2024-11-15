package kr.giljabi.eip.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class UserController {

    /**
     * LoginFailureHandler dispatcher.forward(request, response) 원래 요청의 HTTP 메서드(GET, POST 등)를 그대로 유지
     * RequestMapping를 사용해서 POST/GET을 구분하지 않고, 하나의 메서드로 처리해야 에러메세지를 thymelaf에서 처리할 수 있음
     * @return
     */
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






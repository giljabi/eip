package kr.giljabi.eip.controller.questions;

import kr.giljabi.eip.dto.gpt.ChatGPTResponse;
import kr.giljabi.eip.dto.response.Response;
import kr.giljabi.eip.dto.response.ResponseCode;
import kr.giljabi.eip.model.User;
import kr.giljabi.eip.service.ChatGPTService;
import kr.giljabi.eip.service.JwtProviderService;
import kr.giljabi.eip.service.UserService;
import kr.giljabi.eip.util.ResponseGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/questions")
public class ChatGptController {

    private final ChatGPTService chatGPTService;
    private final JwtProviderService jwtProviderService;
    private final UserService userService;

    @Value("${openai.allUsageFlag}")
    private boolean allUsageFlag;

    public ChatGptController(ChatGPTService chatGPTService,
                             JwtProviderService jwtProviderService,
                             UserService userService) {
        this.chatGPTService = chatGPTService;
        this.jwtProviderService = jwtProviderService;
        this.userService = userService;
    }

    @GetMapping("/ask/{questionId}")
    public ResponseEntity<Response> askQuiz(@PathVariable Long questionId,
                                            HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        try {
            if(!allUsageFlag) {  //AI 응답을 저장해서 보여주면 관리자만 사용하지 않아도 비용을 아낄 수 있어 오픈
                String subEmail = jwtProviderService.getSessionByUserinfo(request);
                User userEntity = userService.selectOneByUserId(subEmail);
                if (userEntity == null) {
                    log.info("IP: " + ip + " tried to access admin function.");
                    return ResponseEntity.ok(ResponseGenerator.fail(ResponseCode.UNKNOWN_ERROR, "관리자 기능입니다."));
                }
            }
            Response<ChatGPTResponse> response = ResponseGenerator.success(
                    chatGPTService.getQuizResponse(questionId));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.info("IP:" + ip + ", Error: " + e.getMessage());
            return ResponseEntity.ok(ResponseGenerator.fail(ResponseCode.UNKNOWN_ERROR, "관리자 로그인이 필요한 기능입니다."));
        }
    }
}




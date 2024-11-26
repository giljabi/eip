package kr.giljabi.eip.controller.questions;

import kr.giljabi.eip.dto.gpt.ChatGPTResponse;
import kr.giljabi.eip.dto.response.Response;
import kr.giljabi.eip.dto.response.ResponseCode;
import kr.giljabi.eip.service.ChatGPTService;
import kr.giljabi.eip.util.ResponseGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/questions")
public class ChatGptController {

    @Autowired
    private ChatGPTService chatGPTService;

    @GetMapping("/ask/{questionId}")
    public ResponseEntity<Response> askQuiz(@PathVariable Long questionId) {
        try {
            Response<ChatGPTResponse> response = ResponseGenerator.success(
                    chatGPTService.getQuizResponse(questionId));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(ResponseGenerator.fail(ResponseCode.UNKNOWN_ERROR, e.getMessage()));
        }
    }
}


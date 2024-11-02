package kr.giljabi.eip.controller;

import kr.giljabi.eip.dto.request.AnswerDTO;
import kr.giljabi.eip.dto.request.AnswerRequest;
import kr.giljabi.eip.dto.response.AnswerCorrectPercentageDto;
import kr.giljabi.eip.dto.response.AnswerResult;
import kr.giljabi.eip.model.Question;
import kr.giljabi.eip.model.Results;
import kr.giljabi.eip.service.QuestionService;
import kr.giljabi.eip.service.ResultsService;
import kr.giljabi.eip.util.CommonUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/questions")
public class QuestionController {
    private final QuestionService questionService;
    private final ResultsService resultService;

    public QuestionController(QuestionService questionService, ResultsService resultService) {
        this.questionService = questionService;
        this.resultService = resultService;
    }


    @GetMapping("/generate-uuid")
    public String generateUuid(HttpServletRequest request, HttpServletResponse response) {
        String uuid = CommonUtils.getCookieValue(request, CommonUtils.UUID_COOKIE_NAME);        // UUID 생성
        if (uuid == null) {
            uuid = UUID.randomUUID().toString();

            // 쿠키 생성 및 설정
            Cookie uuidCookie = new Cookie(CommonUtils.UUID_COOKIE_NAME, uuid);
            uuidCookie.setHttpOnly(true); // JavaScript에서 접근 불가 (보안 강화)
            uuidCookie.setPath("/");
            uuidCookie.setMaxAge(356 * 24 * 60 * 60); // 쿠키 만료 시간: 365일
            response.addCookie(uuidCookie);
        }

        return "redirect:/";
    }

    @GetMapping("/random/{subjectId}")
    public String getRandomQuestions(@PathVariable Integer subjectId,
                                     HttpServletRequest request,
                                     Model model) {
        //uuid cookie가 없으면 첫화면부터 시작하게 한다.
        String uuid = CommonUtils.getCookieValue(request, CommonUtils.UUID_COOKIE_NAME);        // UUID 생성
        if (uuid == null) {
            model.addAttribute("results", null);
            return "redirect:/";
        }

        List<Question> questions = questionService.getRandomQuestions(subjectId,
                uuid, CommonUtils.getClientIp(request));
        model.addAttribute("questions", questions);
        return "questions/random";
    }

    // JSON 데이터를 List<AnswerDTO>로 수신
    @PostMapping("/submit-answers")
    public ResponseEntity<List<AnswerResult>> submitAnswers(@RequestBody AnswerRequest answerRequest,
                                                            HttpServletRequest request,
                                                            Model model) {
        List<AnswerResult> results = new ArrayList<AnswerResult>();

        //uuid cookie가 없으면 첫화면부터 시작하게 한다.
        String uuid = CommonUtils.getCookieValue(request, CommonUtils.UUID_COOKIE_NAME);        // UUID 생성
        if (uuid == null) {
            model.addAttribute("results", null);
            return ResponseEntity.ok(results);
        }
        // 사용자가 제출한 답변을 처리하고 정답 여부 판단
        String clientUUID = CommonUtils.getCookieValue(request, CommonUtils.UUID_COOKIE_NAME);
        System.out.println("clientUUID: " + clientUUID);

        for (AnswerDTO answer : answerRequest.getAnswers()) {
            Long questionId = Long.valueOf(answer.getId());
            Integer userAnswer = Integer.parseInt(answer.getAnswer());

            // 답변 횟수 증가
            questionService.incrementReplyCount(questionId);

            // 데이터베이스에서 정답 조회
            AnswerCorrectPercentageDto correctAnswer = questionService.findCorrectByQuestionId(questionId);

            // 정답 여부 확인
            boolean isCorrect = correctAnswer.getCorrect() == userAnswer;
            // 정답일 경우 정답 횟수 증가
            if (isCorrect) {
                questionService.incrementCorrectCount(questionId);
            }
            //
            correctAnswer = questionService.findCorrectByQuestionId(questionId);

            // AnswerResult 객체 생성 후 리스트에 추가
            AnswerResult result = new AnswerResult(questionId, userAnswer, correctAnswer.getCorrect(),
                    isCorrect, correctAnswer.getCorrectPercentage());
            results.add(result);

            // 결과 저장
            int correct = isCorrect ? 1 : 0;    // 숫자로 해야 나중에 합산이 편함
            Results userResult = new Results(clientUUID, questionId, userAnswer,
                    correct, CommonUtils.getClientIp(request));
            resultService.save(userResult);
            System.out.println("userResult: " + userResult.toString());
        }
        System.out.println(results.toString());
        // 결과를 모델에 추가하여 결과 페이지로 전달
        model.addAttribute("results", results);
        return ResponseEntity.ok(results);
    }


}
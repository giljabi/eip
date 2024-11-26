package kr.giljabi.eip.controller.questions;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.swagger.v3.oas.annotations.Operation;
import kr.giljabi.eip.dto.request.AnswerDTO;
import kr.giljabi.eip.dto.request.AnswerRequest;
import kr.giljabi.eip.dto.response.AnswerCorrectPercentageDto;
import kr.giljabi.eip.dto.response.AnswerResult;
import kr.giljabi.eip.model.*;
import kr.giljabi.eip.repository.SubjectRepository;
import kr.giljabi.eip.service.JwtProviderService;
import kr.giljabi.eip.service.QuestionService;
import kr.giljabi.eip.service.ResultsService;
import kr.giljabi.eip.util.CommonUtils;
import kr.giljabi.eip.util.JwtProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/questions")
public class QuestionController {
    private final QuestionService questionService;
    private final ResultsService resultService;
    private final SubjectRepository subjectRepository;
    private final JwtProviderService jwtProviderService;

    public QuestionController(QuestionService questionService,
                              ResultsService resultService,
                              SubjectRepository subjectRepository,
                              JwtProviderService jwtProviderService) {
        this.questionService = questionService;
        this.resultService = resultService;
        this.subjectRepository = subjectRepository;
        this.jwtProviderService = jwtProviderService;
    }

    @Operation(summary = "cookie 생성", description = "발급 후 1년 사용")
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

    @Operation(summary = "시험과목 목록", description = "정보처리기사 필기:1")
    @GetMapping("/random/{qid}")
    public ResponseEntity<List<Subject>> getSubject(@PathVariable Integer qid,
                             HttpServletRequest request,
                             Model model) {
        //uuid cookie가 없으면 첫화면부터 시작하게 한다.
        String uuid = CommonUtils.getCookieValue(request, CommonUtils.UUID_COOKIE_NAME);
        if (uuid == null) {
            model.addAttribute("results", null);
            return ResponseEntity.ok(null);
        }
        List<Subject> subjects = subjectRepository.findByQidOrderById(qid);
        model.addAttribute("results", subjects);
        return ResponseEntity.ok(subjects);
    }


    @Operation(summary = "자격증종류, 시험과목에서 랜덤하게 5개 리턴")
    @GetMapping("/random/{qid}/{subjectId}")
    public String getRandomQuestions(@PathVariable Integer qid,
                                     @PathVariable Integer subjectId,
                                     HttpServletRequest request,
                                     Model model) {
        try {
            //관리자만 AI기능을 사용하기 위해 세션정보를 추가
            String userInfo = jwtProviderService.getSessionByUserinfo(request);
            model.addAttribute("admin", userInfo);
        } catch (Exception e) {
            //log.error("error: {}", e.getMessage());
        }

        String uuid = CommonUtils.getCookieValue(request, CommonUtils.UUID_COOKIE_NAME);
        if (uuid == null) {
            model.addAttribute("results", null);
            return "redirect:/";
        }
        List<Question> questions = questionService.getRandomQuestions(qid, subjectId,
                uuid, CommonUtils.getClientIp(request));
        model.addAttribute("questions", questions);
        log.info("UUID: {}", uuid);
        return "questions/random";
    }

    @Operation(summary = "문제 정답을 리턴")
    @PostMapping("/submit-answers")
    public ResponseEntity<List<AnswerResult>> submitAnswers(@RequestBody AnswerRequest answerRequest,
                                                            HttpServletRequest request,
                                                            Model model) {
        List<AnswerResult> results = new ArrayList<AnswerResult>();

        String uuid = CommonUtils.getCookieValue(request, CommonUtils.UUID_COOKIE_NAME);
        if (uuid == null) {
            model.addAttribute("results", null);
            return ResponseEntity.ok(results);
        }
        // 사용자가 제출한 답변을 처리하고 정답 여부 판단
        String clientUUID = CommonUtils.getCookieValue(request, CommonUtils.UUID_COOKIE_NAME);

        for (AnswerDTO answer : answerRequest.getAnswers()) {
            Long questionId = Long.valueOf(answer.getId());
            QName qName = new QName(answer.getQid(), "");
            Integer userAnswer = Integer.parseInt(answer.getAnswer());

            // 답변 횟수 증가
            questionService.incrementReplyCount(questionId);

            // 데이터베이스에서 정답 조회
            AnswerCorrectPercentageDto correctAnswer = questionService.findCorrectByQuestionId(questionId, qName);

            // 정답 여부 확인
            boolean isCorrect = correctAnswer.getCorrect() == userAnswer;
            // 정답일 경우 정답 횟수 증가
            if (isCorrect) {
                questionService.incrementCorrectCount(questionId);
            }
            // 정답 확인 후 결과 저장을 위해 한번더 조회함
            correctAnswer = questionService.findCorrectByQuestionId(questionId, qName);

            // AnswerResult 객체 생성 후 리스트에 추가
            AnswerResult result = new AnswerResult(questionId, userAnswer, correctAnswer.getCorrect(),
                    isCorrect, correctAnswer.getCorrectPercentage());
            results.add(result);

            // 결과 저장
            int correct = isCorrect ? 1 : 0;    // 숫자로 해야 나중에 합산이 편함
            Results userResult = new Results(clientUUID, questionId, userAnswer,
                    correct, CommonUtils.getClientIp(request), qName.getId());
            resultService.save(userResult);
        }
        // 결과를 모델에 추가하여 결과 페이지로 전달
        model.addAttribute("results", results);
        log.info("UUID: {}", clientUUID);
        return ResponseEntity.ok(results);
    }
}





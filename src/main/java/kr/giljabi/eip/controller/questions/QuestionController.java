package kr.giljabi.eip.controller.questions;

import io.swagger.v3.oas.annotations.Operation;
import kr.giljabi.eip.dto.request.AnswerDTO;
import kr.giljabi.eip.dto.request.AnswerRequest;
import kr.giljabi.eip.dto.request.SubjectQuestionDTO;
import kr.giljabi.eip.dto.response.AnswerCorrectPercentageDto;
import kr.giljabi.eip.dto.response.AnswerResult;
import kr.giljabi.eip.model.*;
import kr.giljabi.eip.service.JwtProviderService;
import kr.giljabi.eip.service.QuestionService;
import kr.giljabi.eip.service.ResultsService;
import kr.giljabi.eip.service.SubjectService;
import kr.giljabi.eip.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/questions")
public class QuestionController {
    private final QuestionService questionService;
    private final ResultsService resultService;
    private final SubjectService subjectService;
    private final JwtProviderService jwtProviderService;

    @Value("${openai.allUsageFlag}")
    private boolean allUsageFlag;

    public QuestionController(QuestionService questionService,
                              ResultsService resultService,
                              SubjectService subjectService,
                              JwtProviderService jwtProviderService) {
        this.questionService = questionService;
        this.resultService = resultService;
        this.subjectService = subjectService;
        this.jwtProviderService = jwtProviderService;
    }

    @Operation(summary = "시험과목 목록", description = "정보처리기사 필기:1")
    @GetMapping("/random/{qid}")
    public ResponseEntity<List<SubjectQuestionDTO>> getSubject(@PathVariable Integer qid,
                             Model model) {
        List<SubjectQuestionDTO> subjects = subjectService.getSubjectQuestions(qid);
        model.addAttribute("results", subjects);
        return ResponseEntity.ok(subjects);
    }


    @Operation(summary = "자격증종류, 시험과목에서 랜덤하게 5개 리턴")
    @GetMapping("/random/{qid}/{subjectId}/{uuid}")
    public String getRandomQuestions(@PathVariable Integer qid,
                                     @PathVariable Integer subjectId,
                                     @PathVariable String uuid,
                                     HttpServletRequest request,
                                     Model model) {
        try {
            //관리자만 AI기능을 사용하기 위해 세션정보를 추가
            //String userInfo = jwtProviderService.getSessionByUserinfo(request);
            //model.addAttribute("admin", userInfo);
            model.addAttribute("allUsageFlag", allUsageFlag);
        } catch (Exception e) {
            //log.error("error: {}", e.getMessage());
        }

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

        if (answerRequest.getUuid() == null) {
            model.addAttribute("results", null);
            return ResponseEntity.ok(results);
        }

        // 사용자가 제출한 답변을 처리하고 정답 여부 판단
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
            Results userResult = new Results(answerRequest.getUuid(), questionId, userAnswer,
                    correct, CommonUtils.getClientIp(request), qName.getId());
            resultService.save(userResult);
        }
        // 결과를 모델에 추가하여 결과 페이지로 전달
        model.addAttribute("results", results);
        log.info("UUID: {}", answerRequest.getUuid());
        return ResponseEntity.ok(results);
    }

    @Operation(summary = "현재 장치의 UUID를 조회")
    @GetMapping("/usersync")
    public String getUsersync() {
        return "questions/usersync";
    }

}





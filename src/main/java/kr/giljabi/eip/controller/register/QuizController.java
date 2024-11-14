package kr.giljabi.eip.controller.register;

import io.swagger.v3.oas.annotations.Operation;
import kr.giljabi.eip.dto.query.ExamnoDayDTO;
import kr.giljabi.eip.dto.request.QuizRequest;
import kr.giljabi.eip.dto.response.ChoiceDTO;
import kr.giljabi.eip.dto.response.QuestionDTO;
import kr.giljabi.eip.dto.response.Response;
import kr.giljabi.eip.dto.response.ResponseCode;
import kr.giljabi.eip.model.*;
import kr.giljabi.eip.service.*;
import kr.giljabi.eip.util.ResponseGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/register")
public class QuizController {

    private final QuestionService questionService;
    private final ChoiceService choiceService;
    private final ExamNoService examNoService;
    private final SubjectService subjectService;
    private final QNameService qNameService;

    private final String contextPath;

    @Autowired
    public QuizController(QuestionService questionService,
                          ChoiceService choiceService,
                          ExamNoService examNoService,
                          SubjectService subjectService,
                          QNameService qNameService,
                          ServerProperties serverProperties) {
        this.questionService = questionService;
        this.choiceService = choiceService;
        this.examNoService = examNoService;
        this.subjectService = subjectService;
        this.qNameService = qNameService;
        this.contextPath = serverProperties.getServlet().getContextPath() == "/"
                ? "" : serverProperties.getServlet().getContextPath();

    }

    @Operation(summary = "퀴즈 등록화면")
    @GetMapping("/quiz")
    public String initQuestion(Model model) {
        initFormData(model);
        return "register/quiz";
    }

    @Operation(summary = "퀴즈 정보 조회")
    @GetMapping("/quiz/{id}")
    public String findQuestion(@PathVariable Long id,  Model model) {
        initFormData(model);

        Question question = questionService.findById(id);
        model.addAttribute("question", question); //종목
        model.addAttribute("choice", question.getChoices()); //선택지

        model.addAttribute("examNo", examNoService.findAllByOrderByIdAsc()); //시험일
        model.addAttribute("subject", subjectService.findByQidOrderById(question.getQid().getId())); //과목

        return "register/quiz";
    }

    private void initFormData(Model model) {
        Question question = new Question();
        model.addAttribute("question", question);

        ArrayList<ChoiceDTO>  choiceList = new ArrayList<>();
        choiceList.add(new ChoiceDTO());
        choiceList.add(new ChoiceDTO());
        choiceList.add(new ChoiceDTO());
        choiceList.add(new ChoiceDTO());
        model.addAttribute("choice", choiceList);

        model.addAttribute("qname", qNameService.findAll()); //종목
        model.addAttribute("examNo", examNoService.findAllByOrderByIdAsc()); //시험일
    }

/*
    @GetMapping("/quiz/qname/{qid}")
    public ResponseEntity<List<Subject>> getQname(@PathVariable Integer qid, Model model) {
        List<Subject> lists =  subjectService.findByQidOrderById(qid);
        return ResponseEntity.ok(lists);
    }
*/

    /**
     * 시험 종목: 정보처리기사(1)의 1과목 리턴
     * @param qid
     * @return
     */
    @Operation(summary = "자격증에서 시험과목 목록을 리턴")
    @GetMapping("/quiz/subject/{qid}")
    public ResponseEntity<Response<List<Subject>>> getQname(@PathVariable Integer qid) {
        List<Subject> lists = subjectService.findByQidOrderById(qid);
        Response<List<Subject>> response = ResponseGenerator.success(lists);
        return ResponseEntity.ok(response);
    }


    /**
     * 시험일: 지정된 종목의 문제정보가 있는 시험일을 리턴
     * @param qid
     * @return
     */
    @Operation(summary = "퀴즈가 등록된 시험일 목록을 리턴")
    @GetMapping("/quiz/examnoday/{qid}")
    public ResponseEntity<Response<List<ExamnoDayDTO>>> getExamnoDay(@PathVariable Integer qid) {
        List<ExamnoDayDTO> lists = examNoService.getExamnoDay(qid);
        Response<List<ExamnoDayDTO>> response = ResponseGenerator.success(lists);
        return ResponseEntity.ok(response);
    }

    /**
     * 신규 등록
     * 예외검증은 생략......
     */
    @Operation(summary = "퀴즈 저장")
    @PostMapping("/quiz")
    public ResponseEntity<Response<Void>> saveQuestion(
            @RequestPart("jsonData") QuizRequest quizRequest,
            @RequestParam(value = "questionImageFile", required = false) MultipartFile questionImageFile,
            @RequestParam(value = "choiceFile1", required = false) MultipartFile choiceFile1,
            @RequestParam(value = "choiceFile2", required = false) MultipartFile choiceFile2,
            @RequestParam(value = "choiceFile3", required = false) MultipartFile choiceFile3,
            @RequestParam(value = "choiceFile4", required = false) MultipartFile choiceFile4
    ) {
        log.info("saveQuestion");
        try {
            ExamNo examNo = examNoService.findById(quizRequest.getExamId());
            QName qName = qNameService.findById(quizRequest.getQid());

            if(quizRequest.getId() == null) { //신규등록
                boolean examNoExist = questionService.findByQidAndExamNoAndNo(
                        examNo, qName, quizRequest.getNo());
                if (examNoExist) {
                    Response<Void> response = ResponseGenerator.fail(ResponseCode.UNKNOWN_ERROR,
                            "종목, 시험차수, 문제 번호가 중복됩니다. 다시 입력해 주세요.");
                    return ResponseEntity.ok(response);
                }
            }
            MultipartFile[] choiceFiles = { choiceFile1, choiceFile2, choiceFile3, choiceFile4 };
            questionService.saveQuestionAndChoices(quizRequest, questionImageFile, choiceFiles, examNo);

            Response<Void> response = ResponseGenerator.success();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Response<Void> response = ResponseGenerator.fail(ResponseCode.UNKNOWN_ERROR, e.getMessage());
            return ResponseEntity.ok(response);
        }
    }




    @Operation(summary = "퀴즈 목록 초기화면으로 이동")
    @GetMapping("/quiz-list")
    public String initQuizList(Model model) {
        initFormData(model);
        return "register/quiz-list";
    }

    /**
     * 시험종목, 과목은 검색시 필수
     * @param qid
     * @param examNo
     * @param subjectId
     * @param name
     * @return
     */
    @Operation(summary = "자격증종류, 과목에서 퀴즈를 검색")
    @GetMapping("/quiz-list/{qid}/{subjectId}")
    public ResponseEntity<Response<List<QuestionDTO>>> listQuiz(
                @PathVariable Integer qid, @PathVariable Integer subjectId,
                @RequestParam(value = "examNo", required = false) Integer examNo,
                @RequestParam(value = "name", required = false) String name
    ) {
        QName qName = qNameService.findById(qid);
        ExamNo exam = examNoService.findById(examNo);
        Subject subject = subjectService.findById(subjectId);

        List<Question> questions = questionService.findQuestions(qName, subject, exam, name);

        //questions을 바로 리턴하면 순환참조가 발생하여 오류가 발생한다. DTO로 변환하여 리턴한다. 보안에도 도움됨
        List<QuestionDTO> questionDTOList = questions.stream().map(q ->  new QuestionDTO(
                q.getId(), q.getExamNo().getId(), q.getExamNo().getName(), q.getExamNo().getExamDay(),
                q.getSubject().getId(), q.getSubject().getName(),
                q.getCorrect(), q.getName(), contextPath + q.getImageUrl(), q.getNo(), q.isQuestionImageFlag(),
                q.isChoiceImageFlag(), q.isUseFlag(),
                q.getChoices().stream().map(choice -> new ChoiceDTO(
                        choice.getId(), choice.getNo(), choice.getName(), contextPath + choice.getImageUrl()))
                        .collect(Collectors.toList())
                )).collect(Collectors.toList());

        Response<List<QuestionDTO>> response = ResponseGenerator.success(questionDTOList);
        return ResponseEntity.ok(response);
    }

}




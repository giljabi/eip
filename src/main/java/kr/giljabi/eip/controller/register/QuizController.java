package kr.giljabi.eip.controller.register;

import kr.giljabi.eip.dto.request.QuizRequest;
import kr.giljabi.eip.dto.response.ChoiceDTO;
import kr.giljabi.eip.dto.response.QuestionDTO;
import kr.giljabi.eip.dto.response.Response;
import kr.giljabi.eip.dto.response.ResponseCode;
import kr.giljabi.eip.model.*;
import kr.giljabi.eip.service.*;
import kr.giljabi.eip.util.ResponseGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/register")
public class QuizController {

    private final QuestionService questionService;
    private final ChoiceService choiceService;
    private final ExamNoService examNoService;
    private final SubjectService subjectService;
    private final QNameService qNameService;

    @Autowired
    public QuizController(QuestionService questionService,
                          ChoiceService choiceService,
                          ExamNoService examNoService,
                          SubjectService subjectService,
                          QNameService qNameService) {
        this.questionService = questionService;
        this.choiceService = choiceService;
        this.examNoService = examNoService;
        this.subjectService = subjectService;
        this.qNameService = qNameService;

    }

    @GetMapping("/quiz")
    public String initQuiz(Model model) {
        initFormData(model);

        return "register/quiz";
    }
    private void initFormData(Model model) {
        QuestionDTO questionDTO = new QuestionDTO();
        model.addAttribute("question", questionDTO);

        ArrayList<ChoiceDTO>  choiceList = new ArrayList<>();
        choiceList.add(new ChoiceDTO());
        choiceList.add(new ChoiceDTO());
        choiceList.add(new ChoiceDTO());
        choiceList.add(new ChoiceDTO());
        model.addAttribute("choice", choiceList);

        model.addAttribute("examNo", examNoService.findAllByOrderByIdAsc()); //시험일
        model.addAttribute("qname", qNameService.findAll()); //종목
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
    @GetMapping("/quiz/qname/{qid}")
    public ResponseEntity<Response<List<Subject>>> getQname(@PathVariable Integer qid) {
        List<Subject> lists = subjectService.findByQidOrderById(qid);
        Response<List<Subject>> response = ResponseGenerator.success(lists);
        return ResponseEntity.ok(response);
    }

    /**
     * 예외검증은 생략......
     */
    @PostMapping("/quiz")
    public ResponseEntity<Response<Void>> uploadQuestion(
            @RequestPart("jsonData") QuizRequest quizRequest,
            @RequestParam(value = "questionImageFile", required = false) MultipartFile questionImageFile,
            @RequestParam(value = "choiceFile1", required = false) MultipartFile choiceFile1,
            @RequestParam(value = "choiceFile2", required = false) MultipartFile choiceFile2,
            @RequestParam(value = "choiceFile3", required = false) MultipartFile choiceFile3,
            @RequestParam(value = "choiceFile4", required = false) MultipartFile choiceFile4
    ) {
        System.out.println("uploadQuestion");
        try {
            ExamNo examNo = examNoService.findById(quizRequest.getExamId());
            QName qName = qNameService.findById(quizRequest.getQid());
            //질문지 번호가 이미 있는지 확인해야 함Long examNoId, Long qid, Integer no
            boolean examNoExist = questionService.findByQidAndExamNoAndNo(
                    examNo, qName, quizRequest.getNo());
            if(examNoExist) {
                Response<Void> response = ResponseGenerator.fail(ResponseCode.UNKNOWN_ERROR,
                        "종목, 시험차수, 문제 번호가 중복됩니다. 다시 입력해 주세요.");
                return ResponseEntity.ok(response);
            }
            questionService.saveQuestionAndChoices(quizRequest, questionImageFile,
                    choiceFile1, choiceFile2, choiceFile3, choiceFile4, examNo);

            Response<Void> response = ResponseGenerator.success();
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            Response<Void> response = ResponseGenerator.fail(ResponseCode.UNKNOWN_ERROR, e.getMessage());
            return ResponseEntity.ok(response);
        }
    }



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
                q.getCorrect(), q.getName(), q.getImageUrl(), q.getNo(), q.isQuestionImageFlag(),
                q.isChoiceImageFlag(), q.isUseFlag(),
                q.getChoices().stream().map(choice -> new ChoiceDTO(
                        choice.getId(), choice.getNo(), choice.getName(), choice.getImageUrl()))
                        .collect(Collectors.toList())
                )).collect(Collectors.toList());

        Response<List<QuestionDTO>> response = ResponseGenerator.success(questionDTOList);
        return ResponseEntity.ok(response);
    }

}


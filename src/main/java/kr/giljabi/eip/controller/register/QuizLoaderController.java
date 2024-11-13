package kr.giljabi.eip.controller.register;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import kr.giljabi.eip.dto.response.Response;
import kr.giljabi.eip.model.*;
import kr.giljabi.eip.service.QuizLoaderService;
import kr.giljabi.eip.util.ResponseGenerator;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
@Controller
@RequestMapping("/register")
public class QuizLoaderController {
    private final QuizLoaderService quizLoaderService;
    private ExamNoObject examNoData = new ExamNoObject();
    private final SpringTemplateEngine templateEngine;

    @Autowired
    public QuizLoaderController(QuizLoaderService quizLoaderService,
                                SpringTemplateEngine templateEngine) {
        this.quizLoaderService = quizLoaderService;
        this.templateEngine = templateEngine;
    }

    /**
     * 파일을 읽어서 배치처리한다.
data.txt
### ExamNO:8, 과목ID:2
1. εr = 81, μr = 1 인 매질의 고유 임피던스는 약 몇 Ω 인가? (단, εr은 비유전율이고 , μr은 비투자율이다.)
1 13.9
2 21.9
3 33.9
4 41.9
2. 강자성체의 B-H 곡선을 자세히 관찰하면 매끈한 곡선이 아니라 자속밀도가 어느 순간 급격히 계단적으로 증가 또는 감소하는 것을 알 수 있다. 이러한 현상을 무엇이라 하는가 ?
1 퀴리점 (Curie point)
2 자왜현상 (Magneto-striction)
3 바크하우젠 효과(Barkhausen effect)
4 자기여자 효과(Magnetic after effect)
{{qif:0,cif:1}}3. 진공 중에 무한 평면도체와 d(m)만큼 떨어진 곳에 선전하밀도 λ(C/m) 의 무한 직선도체가 평행하게 놓여 있는 경우 직선 도체의 단위 길이당 받는 힘은 몇 N/m 인가?
1
2
3
4
...
100. 과전류차단기로 저압전로에 사용하는 범용의 퓨즈(용품 및 생활용품 안전관리법 」에서 규정하는 것을 제외한다)의 정격전류가 16A인 경우 용단전류는 정격전류의 몇 배인가 ? (단, 퓨즈(gG)인 경우이다.)
1 1.25
2 1.5
3 1.6
4 1.9
correct 4332314241
correct 1343411142
correct 3242234241
correct 1342342141
correct 2434122441
correct 3124324343
correct 2124412213
correct 2144133313
correct 4312244332
correct 4423334313
     * @return
     */


    @GetMapping("/quizloader")
    public String quizInitialize(Model model) {
        model.addAttribute("results", null);
        return "register/quiz-loader";
    }

    @Operation(summary = "문제 파일 내용확인, 파일 내용이 맞는지 확인하는 절차는 없음")
    @PostMapping("/quizloader")
    public ResponseEntity<Response<List<QuestionObject>>>  quizLoader(
            @RequestParam(value = "formData") MultipartFile formData) {
        List<QuestionObject> questionList = loadQuestions(formData);
        Response<List<QuestionObject>> response = ResponseGenerator.success(questionList);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "문제 파일 DB Insert, 파일 내용이 맞는지 확인하는 절차는 없음")
    @PostMapping("/quizloader/save")
    public ResponseEntity<Response<Void>> quizInsert(
            @RequestParam(value = "formData") MultipartFile formData
    ) {
        log.info("formData: " + formData.getOriginalFilename());
        List<QuestionObject> questionList = loadQuestions(formData);
        insertData(questionList);
        Response<Void> response = ResponseGenerator.success(null);
        return ResponseEntity.ok(response);
    }

    public List<QuestionObject> loadQuestions(MultipartFile formData) {
        List<QuestionObject> questionList = new ArrayList<>();
        String answer = "";

        try (BufferedReader reader = new BufferedReader(
                             new InputStreamReader(formData.getInputStream(),
                             StandardCharsets.UTF_8))) {
            String readLine;
            readLine = reader.readLine();  // 첫 줄(제목)을 읽고 건너뜀
            if (readLine.indexOf("###") >= 0) { //시험차수, 과목ID
                String[] parts = readLine.substring(3).split(",");
                examNoData.id = Integer.parseInt(parts[0].split(":")[1].trim()); // 시험차수ID
                examNoData.qid = Integer.parseInt(parts[1].split(":")[1].trim()); // 과목ID
                log.info("examNoData: " + examNoData.toString());
            }

            log.info("시험차수:" + readLine);
            int questionNo = 1;
            int lineCount = 1;
            List<String> choices = new ArrayList<>();
            String quetionName = "";
            while ((readLine = reader.readLine()) != null) {
                if (readLine.startsWith("correct")) {
                    String numbers = readLine.replace("correct ", "").trim();
                    answer += numbers;
                } else {
                    if (lineCount % 5 == 0) { //1,2,3,4,5 5번째 줄마다 새로운 질문 세트 시작
                        if (questionNo <= 100) {
                            choices.add(readLine.trim());  // 선택지 4번 항목 추가

                            QuestionObject q = new QuestionObject(questionNo, quetionName, choices);
                            questionList.add(q);  // 이전 질문 세트를 저장
                            questionNo++;
                            log.info("{}", q);
                            choices = new ArrayList<>();
                        }
                    } else if(lineCount % 5 == 1) { //질문
                        quetionName = readLine.trim();
                    } else {
                        choices.add(readLine.trim());  // 선택지 1~3 추가
                    }
                }
                lineCount++;
            }
            String[] answerArray = answer.chars()
                    .mapToObj(c -> String.valueOf((char)c))
                    .toArray(String[]::new);
            for (int i = 0; i < questionList.size(); i++) {
                questionList.get(i).answer = answerArray[i];
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return questionList;
    }

    private Map<String, Boolean> checkImageFlag(String question) throws JsonProcessingException {
        Map<String, Boolean> imageFlagMap;
        imageFlagMap = new HashMap<>();  //질문 qif, 선택 cif에서 이미지 사용유무 flag

        String flagData = question.substring(0, question.indexOf("}}") + 2);
        flagData = flagData.replace("{{", "{").replace("}}", "}");
        flagData = flagData.replace("qif", "\"qif\"");
        flagData = flagData.replace("cif", "\"cif\"");
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Integer> map = mapper.readValue(flagData, Map.class);

        // Integer 값을 Boolean으로 변환
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            imageFlagMap.put(entry.getKey(), entry.getValue() == 1);
        }
        log.info("flagData: " + imageFlagMap);
        return imageFlagMap;
    }

    private void insertData(List<QuestionObject> questionList) {
        log.info("questionList.size(): " + questionList.size());

        QName qname = new QName(examNoData.qid, ""); //과목명은 여기서는 의미없음
        ExamNo examNo = quizLoaderService.findExamNoById(examNoData.id);
        log.info("examNo: " + examNo.toString());

        int count = quizLoaderService.deleteAllChoiceExamNoIdAndQid(examNo.getId(), qname.getId());
        log.info("deleteAllChoiceExamNoIdAndQid: " + count);
        count =  quizLoaderService.deleteAllByQuestionExamNoAndQid(examNo, qname);
        log.info("deleteAllByQuestionExamNoAndQid:" + count);

        int i = 0;
        try {
            for (; i < questionList.size(); i++) {
                QuestionObject questionObject = questionList.get(i);
                Question question = new Question();
                question.setSubject(getSubjectByQuestionNo(i + 1));
                question.setExamNo(examNo); //examNo.get()
                question.setCorrect(Integer.parseInt(questionObject.answer));

                //문제번호 분리
                String questionParts = questionObject.question.substring(questionObject.question.indexOf(" "));
                question.setName(questionParts.trim());  // 질문
                question.setNo(i + 1);
                question.setCorrectCount(0);
                question.setReplyCount(0);
                question.setQuestionImageFlag(questionObject.qif);
                question.setChoiceImageFlag(questionObject.cif);
                question.setUseFlag(false); //사용여부는 모든 문제 확인 후 true로 변경
                question.setQid(qname);
                quizLoaderService.saveQuestion(question);   //저장해야 id가 생성됨
                //log.info("Question: " + question.toString());

                //질문에 이미지가 있으면 이미지 경로를 미리 저장
                if(questionObject.qif) {
                    question.setImageUrl(String.format("/images/q/%s/%d.png",
                            examNo.getExamYear().getName(),
                            question.getId()));
                    //log.info("Question: " + question.toString());
                }

                List<String> items = questionObject.choices;
                List<Choice> itemList = new ArrayList<>();
                for (int j = 0; j < items.size(); j++) {
                    Choice choice = new Choice();
                    choice.setQuestion(question);
                    choice.setNo(j + 1);
                    choice.setName(items.get(j));

                    //선택지에 이미지가 있으면 이미지 경로를 미리 저장
                    if(questionObject.cif) {
                        choice.setImageUrl(String.format("/images/q/%s/%d-%d.png",
                                examNo.getExamYear().getName(),
                                question.getId(),
                                choice.getNo()));    //선택지 번호
                        //log.info("Choice image: " + choice.toString());
                    }
                    itemList.add(choice);
                }
                question.setChoices(itemList);
                //log.info("question: " + question.toString());
                quizLoaderService.saveQuestion(question);
                Thread.sleep(50);
            } // end of for questionList

        } catch (Exception e) {
            log.info(e.getMessage());
            log.info("questionList.get(i):%d, %s", i, questionList.get(i));
        }
    }

    // Helper method to assign subject based on question number
    private Subject getSubjectByQuestionNo(int questionNo) {
        int subjectId = 0;
        int subjectBase =  (examNoData.qid.intValue() - 1) * 5; //기사는 5과목
        if (questionNo >= 1 && questionNo <= 20) {
            subjectId = 1 + subjectBase;
        } else if (questionNo >= 21 && questionNo <= 40) {
            subjectId = 2 + subjectBase;
        } else if (questionNo >= 41 && questionNo <= 60) {
            subjectId = 3 + subjectBase;
        } else if (questionNo >= 61 && questionNo <= 80) {
            subjectId = 4 + subjectBase;
        } else if (questionNo >= 81 && questionNo <= 100) {
            subjectId = 5 + subjectBase;
        }

        // Subject 객체를 subjectId로 생성하거나 기존 값을 로드 (가정: 로드된 값)
        Subject subject = new Subject();
        subject.setId(subjectId); // subjectId를 설정
        log.info("subjectId: " + subjectId + ", questionNo:" + questionNo);
        return subject;
    }


    public class ExamNoObject {
        public Integer id;          //examno.id
        public Integer qid;         //시험과목 ID
        public ExamNoObject() {
        }
    }

    @ToString
    public class QuestionObject {
        public String question;
        public List<String> choices;
        public String answer;
        public boolean qif;
        public String questionImageUrl;
        public boolean cif;
        int no;

        public QuestionObject(int questionNo, String question, List<String> choices) throws Exception {
            this.question = question;
            this.choices = choices;
            this.no = questionNo;

            Map<String, Boolean> imageFlagMap = new HashMap<>();  //질문 qif, 선택 cif에서 이미지 사용유무 flag
            imageFlagMap.put("qif", false); //초기값
            imageFlagMap.put("cif", false);
            if(question.indexOf("}}") > 0) {
                imageFlagMap = checkImageFlag(question);
            }
            this.qif = imageFlagMap.get("qif");
            this.cif = imageFlagMap.get("cif");
            if(qif)
                this.questionImageUrl = "/images/....";

            for (int i = 0; i < choices.size(); i++) {
                ChoiceObject choice = new ChoiceObject();
                choice.no = i + 1;
                if(cif){
                    choice.choiceImageUrl = "/images/....";
                }
            }
        }
    }

    public class ChoiceObject {
        public int no;
        public String choice;
        public String choiceImageUrl;

        public ChoiceObject() {
        }
    }
}



package kr.giljabi.eip.util;
/*
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.giljabi.eip.model.*;
import kr.giljabi.eip.service.QuizLoaderService;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Component
public class QuizLoader {

    private final QuizLoaderService quizLoaderService;

    // 질문과 선택지를 저장할 리스트
    private List<String> questionList = new ArrayList<>();
    private List<List<String>> choiceList = new ArrayList<>();
    private List<String> answerList = new ArrayList<>();
    private ExamNoData  examNoData = new ExamNoData();

    public QuizLoader(QuizLoaderService quizLoaderService) {
        this.quizLoaderService = quizLoaderService;
    }

    public List<String> getQuestionList() {
        return questionList;
    }

    public void loadAndInsertData(String filename) {
        loadQuestions(filename);
        insertData();
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
        System.out.println("subjectId: " + subjectId + ", questionNo:" + questionNo);
        return subject;
    }

    private void insertData() {
        System.out.println("questionList.size(): " + questionList.size());

        QName qname = new QName(examNoData.qid, ""); //과목명은 여기서는 의미없음
        ExamNo examNo = quizLoaderService.findExamNoById(examNoData.id);
        System.out.println("examNo: " + examNo.toString());

        int count = quizLoaderService.deleteAllChoiceExamNoIdAndQid(examNo.getId(), qname.getId());
        System.out.println("deleteAllChoiceExamNoIdAndQid: " + count);
        count =  quizLoaderService.deleteAllByQuestionExamNoAndQid(examNo, qname);
        System.out.println("deleteAllByQuestionExamNoAndQid:" + count);

        int i = 0;
        try {
            for (; i < questionList.size(); i++) {
                Map<String, Boolean> imageFlagMap = new HashMap<>();  //질문 qif, 선택 cif에서 이미지 사용유무 flag
                imageFlagMap.put("qif", false); //초기값
                imageFlagMap.put("cif", false);

                Question question = new Question();
                question.setSubject(getSubjectByQuestionNo(i + 1));
                question.setExamNo(examNo); //examNo.get()
                question.setCorrect(Integer.parseInt(answerList.get(i)));

                if(questionList.get(i).indexOf("}}") > 0) {
                    imageFlagMap = checkImageFlag(i);
                }

                String questionParts = questionList.get(i).substring(questionList.get(i).indexOf(" "));
                question.setName(questionParts.trim());  // 질문
                question.setNo(i + 1);
                question.setCorrectCount(0);
                question.setReplyCount(0);
                question.setQuestionImageFlag(imageFlagMap.get("qif"));
                question.setChoiceImageFlag(imageFlagMap.get("cif"));
                question.setUseFlag(false); //사용여부는 모든 문제 확인 후 true로 변경
                question.setQid(qname);
                System.out.println("Question: " + question.toString());
                quizLoaderService.saveQuestion(question);   //저장해야 id가 생성됨

                //질문에 이미지가 있으면 이미지 경로를 미리 저장
                if(imageFlagMap.get("qif")) {
                    question.setImageUrl(String.format("/images/q/%s/%d.png",
                            examNo.getExamYear().getName(),
                            question.getId()));
                    System.out.println("Question: " + question.toString());
                }

                List<String> items = choiceList.get(i);
                List<Choice> itemList = new ArrayList<>();
                for (int j = 0; j < items.size(); j++) {
                    Choice choice = new Choice();
                    choice.setQuestion(question);
                    choice.setNo(j + 1);
                    choice.setName(items.get(j));

                    //선택지에 이미지가 있으면 이미지 경로를 미리 저장
                    if(imageFlagMap.get("cif")) {
                        choice.setImageUrl(String.format("/images/q/%s/%d-%d.png",
                                examNo.getExamYear().getName(),
                                question.getId(),
                                choice.getNo()));    //선택지 번호
                        System.out.println("Choice image: " + choice.toString());
                    }
                    itemList.add(choice);
                    System.out.println("Choice: " + choice.toString());
                }
                question.setChoices(itemList);
                quizLoaderService.saveQuestion(question);
                Thread.sleep(50);
            } // end of for questionList

        } catch (Exception e) {
            System.out.printf("questionList.get(i):%d, %s", i, questionList.get(i));
            System.out.println(e.getMessage());
        }
        questionList.clear();
        choiceList.clear();
        answerList.clear();
    }

    private Map<String, Boolean> checkImageFlag(int i) throws JsonProcessingException {
        Map<String, Boolean> imageFlagMap;
        imageFlagMap = new HashMap<>();  //질문 qif, 선택 cif에서 이미지 사용유무 flag

        String flagData = questionList.get(i).substring(0, questionList.get(i).indexOf("}}") + 2);
        flagData = flagData.replace("{{", "{").replace("}}", "}");
        flagData = flagData.replace("qif", "\"qif\"");
        flagData = flagData.replace("cif", "\"cif\"");
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Integer> map = mapper.readValue(flagData, Map.class);

        // Integer 값을 Boolean으로 변환
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            imageFlagMap.put(entry.getKey(), entry.getValue() == 1);
        }
        System.out.println("flagData: " + imageFlagMap);
        return imageFlagMap;
    }

    public void loadQuestions(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            String examno;
            int lineCounter = 0;
            List<String> currentChoices = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                if (line.indexOf("correct ") >= 0) {
                    //정답부분
                    System.out.println("Answer: " + line);
                    String answer = line.substring(8);
                    Arrays.stream(answer.split("")).forEach(answerList::add);
                } else {
                    lineCounter++;
                    if (line.isEmpty()) continue; // 빈 줄은 무시 (빈 줄이 있을 수 있음

                    if (line.indexOf("###") >= 0) { //시험, 과목ID
                        String[] parts = line.substring(3).split(",");
                        examNoData.id = Integer.parseInt(parts[0].split(":")[1].trim()); // 시험차수ID
                        examNoData.qid = Integer.parseInt(parts[1].split(":")[1].trim()); // 과목ID
                        System.out.println("examNoData: " + examNoData.toString());
                        lineCounter = 0;
                        continue;
                    }
                    // 첫 번째 줄은 질문이므로 questionList에 추가
                    if (lineCounter == 1) {
                        questionList.add(line.trim());
                        System.out.println("Question: " + line);
                    } else {
                        // 나머지 줄은 선택지에 해당
                        String choice = line.trim().replace("\t", "");
                        currentChoices.add(choice.trim());
                        System.out.println("lineCounter:"+lineCounter+", Choice: " + choice);

                        Integer choiceNo = Integer.parseInt(choice.split(" ")[0]);
                        if (choiceNo < 1 || choiceNo > 4) {
                            System.out.println("Choice: " + choice);
                            throw new IllegalArgumentException("No:" + questionList.get(questionList.size() - 1) + ", 선택지 번호가 1~4가 아닙니다.");
                        }
                    }

                    // 다섯 번째 줄까지 읽으면 choiceList에 저장하고 초기화
                    if (lineCounter == 5) {
                        choiceList.add(new ArrayList<>(currentChoices)); // 현재 선택지 리스트를 choiceList에 추가
                        currentChoices.clear(); // 다음 질문을 위해 선택지 초기화
                        lineCounter = 0; // 줄 카운터 초기화
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 질문 리스트 출력 (확인용)
    public void printQuestionsAndChoices() {
        for (int i = 0; i < questionList.size(); i++) {
            System.out.println("Question " + (i + 1) + ": " + questionList.get(i));
            List<String> choices = choiceList.get(i);
            for (int j = 0; j < choices.size(); j++) {
                System.out.println("Choice " + (j + 1) + ": " + choices.get(j));
            }
            System.out.println(); // 빈 줄로 구분
        }
    }

    @ToString
    //question 입력시 사용하는 ExanoNoData 클래스
    public class ExamNoData {
        public Integer id;          //examno.id
        public Integer qid;         //시험과목 ID
        public ExamNoData() {
        }
    }

//    public class Question {
//        public String question;
//        public List<Choice> choices;
//        public String answer;
//        public boolean qif;
//        public String questionImageUrl;
//        public boolean cif;
//    }
//
//    public class Choice {
//        public int no;
//        public String choice;
//        public String choiceImageUrl;
//    }

}

*/




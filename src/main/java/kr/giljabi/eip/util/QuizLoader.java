package kr.giljabi.eip.util;

import kr.giljabi.eip.model.*;
import kr.giljabi.eip.service.QuizLoaderService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class QuizLoader {

    private final QuizLoaderService quizLoaderService;

    // 질문과 선택지를 저장할 리스트
    private List<String> questionList = new ArrayList<>();
    private List<List<String>> choiceList = new ArrayList<>();
    private List<String> answerList = new ArrayList<>();
    private String[] examInfo = new String[3];

    public QuizLoader(QuizLoaderService quizLoaderService) {
        this.quizLoaderService = quizLoaderService;
    }

    public void loadAndInsertData(String filename) {
        loadQuestions(filename);
        insertData();
    }

    // Helper method to assign subject based on question number
    private Subject getSubjectByQuestionNo(int questionNo) {
        int subjectId = 0;
        if (questionNo >= 1 && questionNo <= 20) {
            subjectId = 1;
        } else if (questionNo >= 21 && questionNo <= 40) {
            subjectId = 2;
        } else if (questionNo >= 41 && questionNo <= 60) {
            subjectId = 3;
        } else if (questionNo >= 61 && questionNo <= 80) {
            subjectId = 4;
        } else if (questionNo >= 81 && questionNo <= 100) {
            subjectId = 5;
        }

        // Subject 객체를 subjectId로 생성하거나 기존 값을 로드 (가정: 로드된 값)
        Subject subject = new Subject();
        subject.setId(subjectId); // subjectId를 설정
        System.out.println("subjectId: " + subjectId + ", questionNo:" + questionNo);
        return subject;
    }

    private void insertData() {
        System.out.println("questionList.size(): " + questionList.size());

        ExamYear examYear = new ExamYear();
        examYear.setId(Integer.parseInt(examInfo[1])); // 시험년도ID

        ExamNo examNo = new ExamNo();
        examNo.setExamYear(examYear);
        examNo.setName(examInfo[2]);
        examNo.setExamDay(examInfo[0]);
        System.out.println("examNo: " + examNo.toString());
        quizLoaderService.saveExamNo(examNo);

        int i = 0;
        try {
            for (; i < questionList.size(); i++) {
/*                Answer answer = new Answer(); //question 테이블로 이동
                //answer.setQuestion(question);
                answer.setCorrect(Integer.parseInt(answerList.get(i)));
                answer.setExamnoId(examNo.getId());
                System.out.println("Answer: " + answer.toString());
                quizLoaderService.saveAnswer(answer);*/

                Question question = new Question();
                question.setSubject(getSubjectByQuestionNo(i + 1));
                question.setExamNo(examNo);
                question.setCorrect(Integer.parseInt(answerList.get(i)));
                String questionParts = questionList.get(i).substring(questionList.get(i).indexOf(" "));
                question.setName(questionParts.trim());  // 질문
                question.setNo(i + 1);
                question.setCorrectCount(0);
                question.setReplyCount(0);
                question.setQuestionImageFlag(false);
                question.setChoiceImageFlag(false);

                System.out.println("Question: " + question.toString());
                quizLoaderService.saveQuestion(question);

                List<String> items = choiceList.get(i);
                for (int j = 0; j < items.size(); j++) {
                    Choice choice = new Choice();
                    choice.setQuestion(question);
                    choice.setNo(j + 1);
                    choice.setName(items.get(j));
                    System.out.println("Choice: " + choice.toString());
                    quizLoaderService.saveChoice(choice);
                }
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

                    if (line.indexOf("###") >= 0) {
                        String[] parts = line.substring(3).split(", ");
                        examInfo[0] = parts[0].trim();
                        examInfo[1] = parts[1].split(":")[1].trim(); // "시험년도ID:
                        examInfo[2] = parts[2].split(":")[1].trim(); // 차수명, "차수ID:
                        System.out.println("examInfo: " + examInfo.toString());
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
/*
    public static void main(String[] args) {
        kr.giljabi.eip.QuizLoader loader = new kr.giljabi.eip.QuizLoader();
        loader.loadQuestions("data.txt");
        loader.printQuestionsAndChoices();
    }*/
}



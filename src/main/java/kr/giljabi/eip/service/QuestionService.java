package kr.giljabi.eip.service;

import kr.giljabi.eip.dto.request.QuizRequest;
import kr.giljabi.eip.dto.response.AnswerCorrectPercentageDto;
import kr.giljabi.eip.model.*;
import kr.giljabi.eip.repository.*;
import kr.giljabi.eip.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class QuestionService {
    @Value("${giljabi.eip.filePath}")
    private String physicalPath;

    private final QuestionRepository questionRepository;
    private final RandomQuestionRepository randomQuestionRepository;

    private final ChoiceService choiceService;
    private final SubjectService subjectService;
    private final QNameService qNameService;

    @Value("${giljabi.question.max}")
    public Integer maxQuestion;

    public QuestionService(QuestionRepository questionRepository,
                           RandomQuestionRepository randomQuestionRepository, ChoiceService choiceService, SubjectService subjectService, QNameService qNameService) {
        this.questionRepository = questionRepository;
        this.randomQuestionRepository = randomQuestionRepository;
        this.choiceService = choiceService;
        this.subjectService = subjectService;
        this.qNameService = qNameService;
    }

    public List<Question> getRandomQuestions(Integer qId, Integer subjectId, String uuid, String remoteip) {
        List<Question> lists;
        if(subjectId == 0) {
            lists = questionRepository.findRandomQuestions(maxQuestion, qId);
            //lists = questionRepository.findRandomQuestionsTest(qId);
        } else
            lists = questionRepository.findRandomQuestions(maxQuestion, subjectId, qId);

        // 가져온 List<Question>을 List<RandomQuestion>으로 변환
        List<RandomQuestion> randomQuestions = lists.stream()
                .map(question -> {
                    RandomQuestion randomQuestion = new RandomQuestion();
                    randomQuestion.setUuid(uuid);
                    randomQuestion.setQuestionId(question.getId());
                    randomQuestion.setRemoteip(remoteip);
                    return randomQuestion;
                })
                .collect(Collectors.toList());

        // 변환된 List<RandomQuestion>을 randomQuestionRepository에 저장
        randomQuestionRepository.saveAll(randomQuestions);

        return lists;
    }

    public AnswerCorrectPercentageDto findCorrectByQuestionId(Long questionId, QName qid) {
        return questionRepository.findCorrectByQuestionId(questionId, qid);
    }

    public void incrementReplyCount(Long questionId) {
        questionRepository.incrementReplyCount(questionId);
    }

    public void incrementCorrectCount(Long questionId) {
        questionRepository.incrementCorrectCount(questionId);
    }

    @Transactional(rollbackFor = Exception.class)
    public Question save(Question question) {
        return questionRepository.save(question);
    }

    public List<Question> findAll() {
        return questionRepository.findAll();
    }

    public boolean findByQidAndExamNoAndNo(ExamNo examNo, QName qName, Integer no) {
        return questionRepository.existsByExamNoIdAndQidAndNo(examNo.getId(), qName, no);
    }
/*
    public List<Question> findAllByQidAndExamNoAndSubject(QName qName, ExamNo examNo, Subject subject, String name) {
        if(name == null || name.isEmpty())
            return questionRepository.findAllByQidAndExamNoAndSubjectOrderByIdAscNoAsc(qName, examNo, subject);
        else
            return questionRepository.findAllByQidAndExamNoAndSubjectAndNameContainingOrderByIdAscNoAsc(qName, examNo, subject, name);
    }*/

    public List<Question> findQuestions(QName qName, Subject subject, ExamNo examNo, String name) {
        if(examNo == null)
            return questionRepository.findQuestions(qName, subject.getId(), null, name);
        else
            return questionRepository.findQuestions(qName, subject.getId(), examNo.getId(), name);
    }

    /**
     * 질문지와 선택지, 그림을 저장
     * @param quizRequest
     * @param questionImageFile
     * @param choiceFile1
     * @param choiceFile2
     * @param choiceFile3
     * @param choiceFile4
     * @param examNo
     */
    @Transactional(rollbackFor = Exception.class) // 예외 발생 시 전체 롤백, 이미지는 제외
    public void saveQuestionAndChoices(QuizRequest quizRequest, MultipartFile questionImageFile,
                                       MultipartFile choiceFile1, MultipartFile choiceFile2,
                                       MultipartFile choiceFile3, MultipartFile choiceFile4,
                                       ExamNo examNo) {
        // Question 생성 및 저장
        Question question = new Question();
        question.setExamNo(examNo);
        question.setSubject(subjectService.findById(quizRequest.getSubjectId()));
        question.setCorrect(quizRequest.getCorrect());
        question.setName(quizRequest.getName());
        question.setImageUrl(questionImageFile.getOriginalFilename());
        question.setNo(quizRequest.getNo());
        question.setQuestionImageFlag(!questionImageFile.getOriginalFilename().isEmpty());
        question.setChoiceImageFlag(quizRequest.isChoiceImageFlag());
        question.setCorrectCount(0);
        question.setReplyCount(0);
        question.setUseFlag(quizRequest.isUseFlag());
        question.setQid(qNameService.findById(quizRequest.getQid()));
        questionRepository.save(question);
        log.info("question = {}", question.toString());

        String year = examNo.getExamYear().getName();
        //X:/git/giljabi/eip/images/
        if(question.isQuestionImageFlag()) {
            //질문지의 이미지
            String fileName = String.format("%d.%s",
                    question.getId(), CommonUtils.getFileExtension(questionImageFile));
            String logicalPath = String.format("q/%s/", year);
            question.setImageUrl("/images/" + logicalPath + fileName);
            CommonUtils.saveFile(physicalPath + logicalPath, fileName, questionImageFile);
        }

        // Choice 생성 및 저장
        List<Choice> choices = new ArrayList<>();
        if (quizRequest.isChoiceImageFlag()) { // 선택지가 이미지일 경우
            MultipartFile[] choiceFiles = { choiceFile1, choiceFile2, choiceFile3, choiceFile4 };
            for (int i = 0; i < quizRequest.getChoices().size(); i++) {
                Choice choice = new Choice();
                choice.setNo(i + 1);
                choice.setQuestion(question);
                choice.setName(Integer.toString(i + 1)); //선택지 번호만 입력

                //물리적 저장위치
                String fileName = String.format("%d-%d.%s",
                        question.getId(), choice.getNo(), CommonUtils.getFileExtension(choiceFiles[i]));
                String logicalPath = String.format("q/%s/", year);
                choice.setImageUrl("/images/" + logicalPath + fileName);
                choices.add(choice);
                CommonUtils.saveFile(physicalPath + logicalPath, fileName, choiceFiles[i]);
            }
        } else { // 선택지가 이미지가 아닐 경우
            for (int i = 0; i < quizRequest.getChoices().size(); i++) {
                Choice choice = new Choice();
                choice.setNo(i + 1);
                choice.setQuestion(question);
                choice.setName(quizRequest.getChoices().get(i));
                choices.add(choice);
            }
        }
        // Choices 저장
        choiceService.saveAll(choices);
        log.info("choices = {}", choices.toString());
    }


}




package kr.giljabi.eip.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.giljabi.eip.dto.gpt.ChatGPTRequest;
import kr.giljabi.eip.dto.gpt.ChatGPTResponse;
import kr.giljabi.eip.model.AiQuery;
import kr.giljabi.eip.model.Choice;
import kr.giljabi.eip.model.Question;
import kr.giljabi.eip.model.TokenUsage;
import kr.giljabi.eip.repository.QuestionRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class ChatGPTService {
    private final QuestionRepository questionRepository;
    private final RestTemplate restTemplate;

    private final TokenUsageService tokenUsageService;
    private final AiQueryService aiQueryService;

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.api.model}")
    private String gptModel;

    @Value("${giljabi.baseurl}")
    private String baseUrl;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public ChatGPTService(QuestionRepository questionRepository,
                          RestTemplate restTemplate,
                          TokenUsageService tokenUsageService,
                          AiQueryService aiQueryService) {
        this.questionRepository = questionRepository;
        this.restTemplate = restTemplate;
        this.tokenUsageService = tokenUsageService;
        this.aiQueryService = aiQueryService;
    }

    public Question findById(Long id) {
        return questionRepository.findById(id).orElse(null);
    }

    public ChatGPTResponse getQuizResponse(Long id) throws Exception {
        boolean existFlag = aiQueryService.existsByQuestionIdAndAiModel(id, gptModel);
        ChatGPTResponse chatGPTResponse = new ChatGPTResponse();

        Question question = findById(id);
        log.info("Question: {}", question);

        if(existFlag) {
            log.info("이미 ai 결과가 존재합니다.");
            AiQuery aiQuery = aiQueryService.findByQuestionId(id);

            ChatGPTResponse.Usage usage = new ChatGPTResponse.Usage();
            usage.setPrompt_tokens(aiQuery.getPromptTokens());
            usage.setTotal_tokens(aiQuery.getTotalTokens());
            usage.setCompletion_tokens(aiQuery.getCompletionTokens());

            chatGPTResponse.setAnswer(aiQuery.getAnswer());
            chatGPTResponse.setModel(aiQuery.getAiModel());
            chatGPTResponse.setUsage(usage);
            return chatGPTResponse;
        }

        long startTime = System.currentTimeMillis();

        ArrayList<ChatGPTRequest.Message> messages = makeMessage(question); //ai 질문 생성
        chatGPTResponse = getChatGPTResponse(messages);                     //ai 답변 생성

        long endTime = System.currentTimeMillis();

        StringBuilder aiAnswer = new StringBuilder();
        for (ChatGPTResponse.Choice c : chatGPTResponse.getChoices()) {
            aiAnswer.append(c.getMessage().getContent()).append("\n");
        }
        chatGPTResponse.setAnswer(aiAnswer.toString()); //브라우저에서 보이는 결과

        //오늘 날짜를 YYYY-MM-DD 형식으로 저장, 날짜별로 AI 쿼리 건수 저장
        String date = java.time.LocalDate.now().toString();
        TokenUsage tokenUsage = new TokenUsage(date,
                chatGPTResponse.getUsage().getPrompt_tokens(),
                chatGPTResponse.getUsage().getCompletion_tokens(),
                chatGPTResponse.getUsage().getTotal_tokens(), 1); //요청 횟수 +1
        tokenUsageService.saveOrUpdateTokenUsage(tokenUsage);

        //ai 응답을 저장
        AiQuery aiQuery = new AiQuery();
        aiQuery.setQuestionId(id);
        aiQuery.setAiModel(gptModel);
        aiQuery.setCompletionTokens(chatGPTResponse.getUsage().getCompletion_tokens());
        aiQuery.setPromptTokens(chatGPTResponse.getUsage().getPrompt_tokens());
        aiQuery.setTotalTokens(chatGPTResponse.getUsage().getTotal_tokens());
        aiQuery.setResponseTime((int)(endTime - startTime));
        aiQuery.setAnswer(aiAnswer.toString());   //4096byte 넘어갈일이 있을까??
        aiQueryService.save(aiQuery);

        return chatGPTResponse;
    }

    private ArrayList<ChatGPTRequest.Message> makeMessage(Question question) {
        ArrayList<ChatGPTRequest.Message> messages = new ArrayList<>();
        //role: system
        messages.add(new ChatGPTRequest.Message("system",
                "당신은 나의 \"" + question.getQid().getName()
                        + "\" 자격 시험에서 \"" + question.getSubject().getName()+ "\" 과목 선생님입니다."));

        //role: user
        StringBuilder prompt = new StringBuilder()
                .append(question.getName()).append("\n");

        if(question.isQuestionImageFlag())
            prompt.append("![image](").append(baseUrl + question.getImageUrl()).append(")\n");

        prompt.append("선택지\n");
        for (int i = 0; i < question.getChoices().size(); i++) {
            if(question.isChoiceImageFlag()) { //선택지에 이미지가 있으면, 선택 번호와 이미지 연결
                prompt.append(question.getChoices().get(i).getNo() + " ");
                prompt.append("![image](").append(baseUrl + question.getChoices().get(i).getImageUrl()).append(")\n");
            } else {
                String choiceString = question.getChoices().get(i).getName();
                prompt.append(choiceString).append("\n");
            }
        }
        prompt.append("\n정답번호를 먼저 알려주고, 다음줄부터 풀이과정을 상세히 설명해주세요");
        messages.add(new ChatGPTRequest.Message("user", prompt.toString()));
        return messages;
    }

    private ChatGPTResponse getChatGPTResponse(ArrayList<ChatGPTRequest.Message> messages) throws JsonProcessingException {
        ChatGPTRequest request = new ChatGPTRequest(gptModel, new ArrayList<>());
        request.setMessages(messages);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(request);
        log.info("request : {}", jsonString);

        HttpEntity<String> entity = new HttpEntity<>(jsonString, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl, HttpMethod.POST, entity, String.class);
        log.info("response: {}", response.getBody());

        ChatGPTResponse chatGPTResponse = objectMapper.readValue(response.getBody(), ChatGPTResponse.class);
        //log.info("chatGPTResponse: {}", chatGPTResponse);
        return chatGPTResponse;
    }
}






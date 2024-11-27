package kr.giljabi.eip.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.giljabi.eip.dto.gpt.ChatGPTRequest;
import kr.giljabi.eip.dto.gpt.ChatGPTResponse;
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

@Service
@Slf4j
public class ChatGPTService {
    private final QuestionRepository questionRepository;
    private final RestTemplate restTemplate;

    private final TokenUsageService tokenUsageService;

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
                          TokenUsageService tokenUsageService) {
        this.questionRepository = questionRepository;
        this.restTemplate = restTemplate;
        this.tokenUsageService = tokenUsageService;
    }

    public Question findById(Long id) {
        return questionRepository.findById(id).orElse(null);
    }

    public ChatGPTResponse getQuizResponse(Long id) throws Exception {
        Question question = findById(id);
        log.info("Question: {}", question);
        // Combine question and choices into a single prompt
        StringBuilder prompt = new StringBuilder()
                .append(question.getName()).append("\n");

        if(question.isQuestionImageFlag())
            prompt.append("![image](").append(baseUrl + question.getImageUrl()).append(")\n");

        for (int i = 0; i < question.getChoices().size(); i++) {
            prompt.append(question.getChoices().get(i).getName()).append("\n");
            if(question.isChoiceImageFlag())
                prompt.append("![image](").append(baseUrl + question.getChoices().get(i).getImageUrl()).append(")\n");
        }
        prompt.append("정답을 먼저 알려주고 다음줄에 문제에서 알아야 하는 것이 무엇인지 설명해줘.");

        ChatGPTRequest request = new ChatGPTRequest(gptModel, new ArrayList<>());
        ArrayList<ChatGPTRequest.Message> messages = new ArrayList<>();

        // Prepare request body, 2개 조건
        messages.add(new ChatGPTRequest.Message("user",
                "시험 과목은 \"" +  question.getSubject().getName() + "\"이며, 시험 과목에 맞는 설명이 필요하다."));
        messages.add(new ChatGPTRequest.Message("user", prompt.toString()));
        request.setMessages(messages);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(request);
        //log.info("request : {}", jsonString);

        HttpEntity<String> entity = new HttpEntity<>(jsonString, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl, HttpMethod.POST, entity, String.class);
        //log.info("response: {}", response.getBody());

        ChatGPTResponse chatGPTResponse = objectMapper.readValue(response.getBody(), ChatGPTResponse.class);
        //log.info("chatGPTResponse: {}", chatGPTResponse);

        //오늘 날짜를 YYYY-MM-DD 형식으로 저장
        String date = java.time.LocalDate.now().toString();
        TokenUsage tokenUsage = new TokenUsage(date,
                chatGPTResponse.getUsage().getPrompt_tokens(),
                chatGPTResponse.getUsage().getCompletion_tokens(),
                chatGPTResponse.getUsage().getTotal_tokens(), 1); //요청 횟수는 +1
        tokenUsageService.saveOrUpdateTokenUsage(tokenUsage);

        return chatGPTResponse;
    }
}




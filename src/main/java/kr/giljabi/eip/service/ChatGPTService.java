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

        ArrayList<ChatGPTRequest.Message> messages = new ArrayList<>();
        //role: system
        messages.add(new ChatGPTRequest.Message("system",
                "당신은 나의 " + question.getSubject().getName()+ " 선생님입니다."));

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
        prompt.append("\n정답번호를 먼저 알려주고, 다음줄부터 풀이과정을 상세히 설명해 주세요.");
        messages.add(new ChatGPTRequest.Message("user", prompt.toString()));

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





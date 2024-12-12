package kr.giljabi.eip.service;

import kr.giljabi.eip.model.AiQuery;
import kr.giljabi.eip.repository.AiQueryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AiQueryService {
    private final AiQueryRepository aiQueryRepository;

    public AiQueryService(AiQueryRepository aiQueryRepository) {
        this.aiQueryRepository = aiQueryRepository;
    }

    public AiQuery save(AiQuery aiModel) {
        log.info("save aiModel: {}", aiModel);
        return aiQueryRepository.save(aiModel);
    }

    public boolean existsByQuestionIdAndAiModel(Long questionId, String aiModel) {
        return aiQueryRepository.existsByQuestionIdAndAiModel(questionId, aiModel);
    }

    public AiQuery findByQuestionId(Long questionId) {
        return aiQueryRepository.findByQuestionId(questionId);
    }

}

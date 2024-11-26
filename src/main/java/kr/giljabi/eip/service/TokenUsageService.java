package kr.giljabi.eip.service;

import kr.giljabi.eip.model.TokenUsage;
import kr.giljabi.eip.repository.TokenUsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
class TokenUsageService {

    private final TokenUsageRepository tokenUsageRepository;

    @Autowired
    public TokenUsageService(TokenUsageRepository tokenUsageRepository) {
        this.tokenUsageRepository = tokenUsageRepository;
    }

    public TokenUsage saveOrUpdateTokenUsage(TokenUsage tokenUsage) {
        Optional<TokenUsage> existingTokenUsage = tokenUsageRepository.findByDate(tokenUsage.getDate());
        if (existingTokenUsage.isPresent()) {
            TokenUsage existing = existingTokenUsage.get();
            existing.setPromptTokens(existing.getPromptTokens() + tokenUsage.getPromptTokens());
            existing.setCompletionTokens(existing.getCompletionTokens() + tokenUsage.getCompletionTokens());
            existing.setTotalTokens(existing.getTotalTokens() + tokenUsage.getTotalTokens());
            return tokenUsageRepository.save(existing);
        } else {
            return tokenUsageRepository.save(tokenUsage);
        }
    }

//    public Optional<TokenUsage> getTokenUsageByDate(String date) {
//        return tokenUsageRepository.findByDate(date);
//    }
//
//    public void deleteTokenUsage(Long id) {
//        tokenUsageRepository.deleteById(id);
//    }
/*
    public TokenUsage getTokenUsageByDate(String date) {
        return tokenUsageRepository.findByDate(date);
    }

    public void deleteTokenUsage(Long id) {
        tokenUsageRepository.deleteById(id);
    }*/
}
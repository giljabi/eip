package kr.giljabi.eip.service;

import kr.giljabi.eip.model.Choice;
import kr.giljabi.eip.repository.ChoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChoiceService {
    private final ChoiceRepository choiceRepository;

    @Autowired
    public ChoiceService(ChoiceRepository choiceRepository) {
        this.choiceRepository = choiceRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public Choice save(Choice choice) {
        return choiceRepository.save(choice);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveAll(List<Choice> choices) {
        choiceRepository.saveAll(choices);
    }
}


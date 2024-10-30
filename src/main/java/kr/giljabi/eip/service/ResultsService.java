package kr.giljabi.eip.service;

import kr.giljabi.eip.dto.request.UserResultDTO;
import kr.giljabi.eip.model.Results;
import kr.giljabi.eip.repository.ResultsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResultsService {

    private final ResultsRepository resultsRepository;

    @Autowired
    public ResultsService(ResultsRepository resultsRepository) {
        this.resultsRepository = resultsRepository;
    }

    public Results save(Results result) {
        return resultsRepository.save(result);
    }

    public List<UserResultDTO> findByUuid(String uuid) {
        return resultsRepository.findByUuid(uuid);
    }

    /*
    public List<Results> findAll() {
        return resultsRepository.findAll();
    }

    public Optional<Results> findById(Long id) {
        return resultsRepository.findById(id);
    }

    public void deleteById(Long id) {
        resultsRepository.deleteById(id);
    }
     */
}
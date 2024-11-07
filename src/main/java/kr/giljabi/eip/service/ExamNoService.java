package kr.giljabi.eip.service;

import kr.giljabi.eip.model.ExamNo;
import kr.giljabi.eip.repository.ExamNoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamNoService {
    private final ExamNoRepository examNoRepository;

    @Autowired
    public ExamNoService(ExamNoRepository examNoRepository) {
        this.examNoRepository = examNoRepository;
    }

    public List<ExamNo> findAllByOrderByIdAsc() {
        return examNoRepository.findAllByOrderByIdAsc();
    }

    public ExamNo findById(Integer id) {
        return examNoRepository.findById(id).orElse(null);
    }
}

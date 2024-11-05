package kr.giljabi.eip.service;

import kr.giljabi.eip.model.ExamYear;
import kr.giljabi.eip.repository.ExamYearRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ExamYearService {

    private final ExamYearRepository examYearRepository;

    @Autowired
    public ExamYearService(ExamYearRepository examYearRepository) {
        this.examYearRepository = examYearRepository;
    }

    // ID로 특정 ExamYear 조회
    public Optional<ExamYear> findById(Integer id) {
        return examYearRepository.findById(id);
    }
/*
    // 모든 ExamYear 레코드를 조회
    public List<ExamYear> findAll() {
        return examYearRepository.findAll();
    }

    // 새로운 ExamYear 저장
    @Transactional
    public ExamYear save(ExamYear examYear) {
        return examYearRepository.save(examYear);
    }

    // ExamYear 삭제
    @Transactional
    public void deleteById(Integer id) {
        examYearRepository.deleteById(id);
    }

    // ExamYear 업데이트
    @Transactional
    public ExamYear updateExamYear(Integer id, String newName) {
        ExamYear examYear = examYearRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid ExamYear ID: " + id));
        examYear.setName(newName);
        return examYearRepository.save(examYear);
    }
 */
}



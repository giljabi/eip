package kr.giljabi.eip.service;

import kr.giljabi.eip.model.Subject;
import kr.giljabi.eip.repository.SubjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {
    private final SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public List<Subject> findAll() {
        return subjectRepository.findAll();
    }

    public List<Subject> findByQidOrderById(Integer qid) {
        return subjectRepository.findByQidOrderById(qid);
    }

    public Subject findById(Integer id) {
        return subjectRepository.findById(id).orElse(null);
    }
}


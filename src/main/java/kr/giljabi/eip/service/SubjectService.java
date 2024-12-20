package kr.giljabi.eip.service;

import kr.giljabi.eip.dto.request.SubjectQuestionDTO;
import kr.giljabi.eip.model.Subject;
import kr.giljabi.eip.repository.SubjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectService {
    private final SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public List<Subject> findAll() {
        return subjectRepository.findAll();
    }

    //시험과목ID, 과목명
    public List<Subject> findByQidOrderById(Integer qid) {
        return subjectRepository.findByQidOrderById(qid);
    }

    //시험과목 선택시 과목ID, 과목명, 문제수, select에서 받은 후 변환해서 사용해야 함
    public List<SubjectQuestionDTO> getSubjectQuestions(Integer qid) {
        List<SubjectQuestionDTO> subjects  = subjectRepository.findSubjectQuestions(qid);
        return subjects;
    }

    public Subject findById(Integer id) {
        return subjectRepository.findById(id).orElse(null);
    }
}


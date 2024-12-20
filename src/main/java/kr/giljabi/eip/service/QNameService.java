package kr.giljabi.eip.service;

import kr.giljabi.eip.dto.request.SubjectQuestionDTO;
import kr.giljabi.eip.model.QName;
import kr.giljabi.eip.repository.QNameRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QNameService {
    private final QNameRepository qNameRepository;

    public QNameService(QNameRepository qNameRepository) {
        this.qNameRepository = qNameRepository;
    }

    public List<QName> findAll() {
        return qNameRepository.findAll();
    }

    public QName findById(Integer id) {
        return qNameRepository.findById(id).orElse(null);
    }

    public List<SubjectQuestionDTO> findByQnameCount() {
        return qNameRepository.findByQnameCount();
    }
}


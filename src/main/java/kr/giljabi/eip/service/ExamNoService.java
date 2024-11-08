package kr.giljabi.eip.service;

import kr.giljabi.eip.dto.query.ExamnoDayDTO;
import kr.giljabi.eip.model.ExamNo;
import kr.giljabi.eip.repository.ExamNoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    // Get examno and examday by qid, object로 반환되므로 DTO로 변환
    public List<ExamnoDayDTO> getExamnoDay(Integer qid) {
        List<Object[]> examnoDay = examNoRepository.getExamnoDay(qid);
        List<ExamnoDayDTO> examnoDayDTOList = new ArrayList<>();
        for (Object[] objects : examnoDay) {
            Integer id = ((Number) objects[0]).intValue();
            String examday = (String) objects[1];
            ExamnoDayDTO examnoDayDTO = new ExamnoDayDTO(id, examday);
            examnoDayDTOList.add(examnoDayDTO);
        }
        return examnoDayDTOList;
    }
}


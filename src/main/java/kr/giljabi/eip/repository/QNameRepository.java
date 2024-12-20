package kr.giljabi.eip.repository;

import kr.giljabi.eip.dto.request.SubjectQuestionDTO;
import kr.giljabi.eip.model.QName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QNameRepository extends JpaRepository<QName, Integer> {
    List<QName> findAllByOrderByIdAsc();

    //사용가능한 시험종목과 문제수
    @Query(value = "select a.id, a.name, count(*) cnt \n"+
            "from qname a, question b\n"+
            "where a.id=b.qid\n"+
            "and a.useflag=true\n"+
            "group by a.id, a.name\n"+
            "order by a.id\n", nativeQuery = true)
    List<SubjectQuestionDTO> findByQnameCount();
}


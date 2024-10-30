package kr.giljabi.eip.repository;

import kr.giljabi.eip.dto.request.UserResultDTO;
import kr.giljabi.eip.model.Results;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultsRepository extends JpaRepository<Results, Long> {

    @Query(value =
            "SELECT new kr.giljabi.eip.dto.request.UserResultDTO(c.name, COUNT(b), SUM(b.correctflag)) \n" +
                    "FROM Question a\n" +
                    "JOIN Results b ON a.id = b.questionId\n" +
                    "JOIN Subject c ON a.subject.id = c.id\n" +
                    "WHERE b.uuid = :uuid\n" +
                    "GROUP BY c.id, c.name\n" +
                    "ORDER BY c.id")
    List<UserResultDTO> findByUuid(@Param("uuid") String uuid);
}

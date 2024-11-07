package kr.giljabi.eip.repository;

import kr.giljabi.eip.model.QName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QNameRepository extends JpaRepository<QName, Integer> {
    List<QName> findAllByOrderByIdAsc();
}


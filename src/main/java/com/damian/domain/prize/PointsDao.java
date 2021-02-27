package com.damian.domain.prize;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface PointsDao extends CrudRepository<PointScheme, Long> {

    List<PointScheme> findBy();
}



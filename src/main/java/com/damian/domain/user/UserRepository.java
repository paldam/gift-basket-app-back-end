package com.damian.domain.user;

import com.damian.dto.ProductionUserDto;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = "authorities")
    User findOneWithAuthoritiesById(Long id);

    @Query("select NEW com.damian.dto.ProductionUserDto(u.id,u.login) FROM User u join u.authorities a where a.name ='produkcja'")
    List<ProductionUserDto> getAllProductionUser();

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByLogin(String login);



    Optional<User> findOneByLogin(String login);

    @Transactional
    @Modifying
    @Query("update User u set u.password = '$2a$04$FRxFawokSSkDWgv70fm1eOJIl55TPKvI/gS4cUT2tmkHuMp3Gpvkm' where u.login = ?1")
    void resetPassword(String login);

    @Transactional
    @Query("select count(u) FROM User u WHERE u.password = ?1 and u.login = ?2")
    int checkPassword(String password, String login);

    @Transactional
    @Query("select u.password FROM User u WHERE  u.login = ?1")
    String getPassword(String login);


    @Transactional
    @Query("select u FROM User u JOIN u.authorities a WHERE  a.name IN ('magazyn','wysylka','produkcja')")
    List<User> getLogisticWarehouseProductionUsers();

    @Transactional
    @Modifying
    @Query("update User u set u.password = ?1 where u.login = ?2")
    void changePassword(String newPassword, String login);
}



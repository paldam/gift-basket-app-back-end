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

    @Query("select NEW com.damian.dto.ProductionUserDto(u.id,u.login) FROM User u join u.authorities a where a.name " +
        "='produkcja'")
    List<ProductionUserDto> getAllProductionUser();

    @Query("select u FROM User u join u.authorities a where a.name ='punkty' ")
    List<User> getAllProgramUser();

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByLogin(String login);

    Optional<User> findOneByLogin(String login);

    Optional<User> findOneByEmail(String email);

    @Transactional
    @Modifying
    @Query("update User u set u.password = '$2a$04$FRxFawokSSkDWgv70fm1eOJIl55TPKvI/gS4cUT2tmkHuMp3Gpvkm' where u" +
        ".login = ?1")
    void resetPassword(String login);

    @Transactional
    @Query("select count(u) FROM User u WHERE u.password = ?1 and u.login = ?2")
    int checkPassword(String password, String login);

    @Transactional
    @Query("select u.password FROM User u WHERE  u.login = ?1")
    String getPassword(String login);

    @Transactional
    @Modifying
    @Query("update User u set u.name =?3 , u.points = ?2 where u.login = ?1")
    void editSimpleUser(String login, Integer points, String name);

    @Transactional
    @Query("select u.points FROM User u WHERE  u.login = ?1")
    Integer getPoints(String login);

    @Transactional
    @Query("select u.name FROM User u WHERE  u.login = ?1")
    String getName(String login);

    @Transactional
    @Query("select u.ifFirstLogin FROM User u WHERE  u.login = ?1")
    Boolean isFirstLog(String login);

    @Transactional
    @Query("select u FROM User u JOIN u.authorities a WHERE  a.name IN ('magazyn','wysylka','produkcja')")
    List<User> getLogisticWarehouseProductionUsers();

    @Transactional
    @Modifying
    @Query("update User u set u.password = ?1 where u.login = ?2")
    void changePassword(String newPassword, String login);

    @Transactional
    @Modifying
    @Query("update User u set u.ifFirstLogin = false where u.login = ?1")
    void setFirstLoginFalse(String login);

    @Transactional
    @Modifying
    @Query("update User u set u.points = u.points + ?2 where u.login = ?1")
    void updateUserPoints(String login, Integer points);

    @Transactional
    @Modifying
    @Query(value = "UPDATE user set is_archival = true where id = ?1", nativeQuery = true)
    void markUserAsArchival(Long id);


}



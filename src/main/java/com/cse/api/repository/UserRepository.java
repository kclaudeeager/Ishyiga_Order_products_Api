package com.cse.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import com.cse.api.model.User;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE u.email=?1")
    User findByEmailAddress(String emailAddress);
     @Query("SELECT u FROM User u WHERE u.company=?1")
     User findByCompany(String company);

    @Query("SELECT status FROM User u WHERE u.email=?1")
    Integer findstatusbyemail(String emailAddress);

    @Query("SELECT COUNT(u) FROM User u WHERE u.email=?1")
    long getCountByEmail(String email);
    @Query("SELECT company FROM User u WHERE u.role=?1")
    List<String> findCompany(Integer role);
 
    @Query("SELECT email FROM User u WHERE u.company=?1")
    String findEmailByCompany(String company);
    @Transactional
    @Modifying
    @Query("update User u set u.status = ?1 where u.email = ?2")
    int setStatusForUser(Integer status, String email);

    // @Query("SELECT u FROM User u WHERE u.user_Id=?1")
    // User findByUserId(Integer userId);
}

package com.cse.api.repository;

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

    @Query("SELECT status FROM User u WHERE u.email=?1")
    Integer findstatusbyemail(String emailAddress);

    @Query("SELECT COUNT(u) FROM User u WHERE u.email=?1")
    long getCountByEmail(String email);

    @Transactional
    @Modifying
    @Query("update User u set u.status = ?1 where u.email = ?2")
    int setStatusForUser(Integer status, String email);

    // @Query("SELECT u FROM User u WHERE u.user_Id=?1")
    // User findByUserId(Integer userId);
}

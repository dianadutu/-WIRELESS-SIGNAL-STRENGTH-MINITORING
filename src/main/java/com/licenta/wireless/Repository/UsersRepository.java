package com.licenta.wireless.Repository;

import com.licenta.wireless.Entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<UsersEntity, Long> {
    UsersEntity findByEmail(String email);

    @Query("SELECT u FROM UsersEntity u WHERE u.id = :id")
    UsersEntity findUserById(Long id);

}


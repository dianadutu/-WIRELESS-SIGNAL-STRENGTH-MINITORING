package com.licenta.wireless.Repository;

import com.licenta.wireless.Entity.ConnectionHistoryEntity;
import com.licenta.wireless.Entity.UsersEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConnectionHistoryRepository extends JpaRepository<ConnectionHistoryEntity, Long> {
    List<ConnectionHistoryEntity> findByUserId(Long userId);

    @Transactional
    void deleteByUser_Id(Long userId); // Assuming 'user' is a field in ConnectionHistoryEntity

}



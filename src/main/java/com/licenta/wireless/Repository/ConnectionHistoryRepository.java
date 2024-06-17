package com.licenta.wireless.Repository;

import com.licenta.wireless.Entity.ConnectionHistoryEntity;
import com.licenta.wireless.Entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConnectionHistoryRepository extends JpaRepository<ConnectionHistoryEntity, Long> {
    List<ConnectionHistoryEntity> findByUser(UsersEntity user);

}

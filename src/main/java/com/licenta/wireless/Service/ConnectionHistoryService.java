package com.licenta.wireless.Service;

import com.licenta.wireless.Entity.ConnectionHistoryEntity;
import com.licenta.wireless.Repository.ConnectionHistoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConnectionHistoryService {

    private final ConnectionHistoryRepository connectionHistoryRepository;

    public ConnectionHistoryService(ConnectionHistoryRepository connectionHistoryRepository) {
        this.connectionHistoryRepository = connectionHistoryRepository;
    }

    public void saveHistory(ConnectionHistoryEntity historyEntity){
        connectionHistoryRepository.save(historyEntity);
    }

    public List<ConnectionHistoryEntity> findByUserId(Long userId) {
        return connectionHistoryRepository.findByUserId(userId);
    }

    @Transactional
    public void deleteByUserId(Long userId) {
        connectionHistoryRepository.deleteByUser_Id(userId);
    }

}

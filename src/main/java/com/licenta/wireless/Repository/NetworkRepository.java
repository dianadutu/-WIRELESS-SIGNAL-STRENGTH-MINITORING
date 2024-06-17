package com.licenta.wireless.Repository;

import com.licenta.wireless.Model.NetworkInfo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class NetworkRepository {
    private final List<NetworkInfo> networks = new ArrayList<>();

    public void save(NetworkInfo network) {
        networks.add(network);
    }

    public Optional<NetworkInfo> findById(Long id) {
        return networks.stream()
                .filter(network -> id.equals(network.getId()))
                .findFirst();
    }
}

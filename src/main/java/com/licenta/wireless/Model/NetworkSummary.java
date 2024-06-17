package com.licenta.wireless.Model;

import java.util.List;

//pt afisarea cate retele sunt vizibile.
public class NetworkSummary {
    private int totalNetworksVisible;
    private List<NetworkInfo> networks;

    public NetworkSummary(int totalNetworksVisible, List<NetworkInfo> networks) {
        this.totalNetworksVisible = totalNetworksVisible;
        this.networks = networks;
    }

    public NetworkSummary() {
    } //constructor gol

    public int getTotalNetworksVisible() {
        return totalNetworksVisible;
    }

    public void setTotalNetworksVisible(int totalNetworksVisible) {
        this.totalNetworksVisible = totalNetworksVisible;
    }

    public List<NetworkInfo> getNetworks() {
        return networks;
    }

    public void setNetworks(List<NetworkInfo> networks) {
        this.networks = networks;
    }
}

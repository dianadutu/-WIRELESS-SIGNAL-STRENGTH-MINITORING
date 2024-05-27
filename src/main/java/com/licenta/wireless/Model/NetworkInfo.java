package com.licenta.wireless.Model;

import java.util.ArrayList;
import java.util.List;


public class NetworkInfo {
    private String ssid;
    private String networkType;
    private String authentication;
    private String encryption;
    private List<BSSIDInfo> bssids = new ArrayList<>();

    private Long id; // sau orice alt tip corespunzător

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }






    public NetworkInfo() {}
    public NetworkInfo(String ssid, String networkType, String authentication, String encryption) {
        this.ssid = ssid;
        this.networkType = networkType;
        this.authentication = authentication;
        this.encryption = encryption;

    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getNetworkType() {
        return networkType;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

    public String getAuthentication() {
        return authentication;
    }

    public void setAuthentication(String authentication) {
        this.authentication = authentication;
    }

    public String getEncryption() {
        return encryption;
    }

    public void setEncryption(String encryption) {
        this.encryption = encryption;
    }

    public List<BSSIDInfo> getBssids() {
        return bssids;
    }

    public void setBssids(List<BSSIDInfo> bssids) {
        this.bssids = bssids;
    }






    public String getBand() {
        for (BSSIDInfo bssid : bssids) {
            if (bssid.getBand().contains("2.4")) {
                return "2.4GHz";
            } else if (bssid.getBand().contains("5")) {
                return "5GHz";
            }
        }
        return "Unknown";
    }





    // Constructor, getteri și setteri

    public static class BSSIDInfo {
        private String bssid;
        private String signal;
        private String radioType;
        private String band;
        private String channel;
        private String ConnectedStations;
        private String ChannelUtilization;
        private String MediumCapacity;
        private String Basicrates;
        private String Otherrates;

        public BSSIDInfo() {}
        public BSSIDInfo(String bssid, String signal, String radioType, String band, String channel, String connectedStations, String channelUtilization, String mediumCapacity, String basicrates, String otherrates) {
            this.bssid = bssid;
            this.signal = signal;
            this.radioType = radioType;
            this.band = band;
            this.channel = channel;
            ConnectedStations = connectedStations;
            ChannelUtilization = channelUtilization;
            MediumCapacity = mediumCapacity;
            Basicrates = basicrates;
            Otherrates = otherrates;
        }

        public String getBssid() {
            return bssid;
        }

        public void setBssid(String bssid) {
            this.bssid = bssid;
        }

        public String getSignal() {
            return signal;
        }

        public void setSignal(String signal) {
            this.signal = signal;
        }

        public String getRadioType() {
            return radioType;
        }

        public void setRadioType(String radioType) {
            this.radioType = radioType;
        }

        public String getBand() {
            return band;
        }

        public void setBand(String band) {
            this.band = band;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public String getConnectedStations() {
            return ConnectedStations;
        }

        public void setConnectedStations(String connectedStations) {
            ConnectedStations = connectedStations;
        }

        public String getChannelUtilization() {
            return ChannelUtilization;
        }

        public void setChannelUtilization(String channelUtilization) {
            ChannelUtilization = channelUtilization;
        }

        public String getMediumCapacity() {
            return MediumCapacity;
        }

        public void setMediumCapacity(String mediumCapacity) {
            MediumCapacity = mediumCapacity;
        }

        public String getBasicrates() {
            return Basicrates;
        }

        public void setBasicrates(String basicrates) {
            Basicrates = basicrates;
        }

        public String getOtherrates() {
            return Otherrates;
        }

        public void setOtherrates(String otherrates) {
            Otherrates = otherrates;
        }


    }
}

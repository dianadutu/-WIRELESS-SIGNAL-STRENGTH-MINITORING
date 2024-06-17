package com.licenta.wireless.Entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ConnectionHistory")
public class ConnectionHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UsersEntity user;

    @Column(name = "profile_name", length = 100)
    private String profileName;

    @Column(name = "version")
    private Integer version;

    @Column(name = "type", length = 50)
    private String type;

    @Column(name = "control_options", length = 100)
    private String controlOptions;

    @Column(name = "number_of_ssids")
    private Integer numberOfSsids;

    @Column(name = "ssid_name", length = 100)
    private String ssidName;

    @Column(name = "network_type", length = 50)
    private String networkType;

    @Column(name = "radio_type", length = 50)
    private String radioType;

    @Column(name = "vendor_extension", length = 100)
    private String vendorExtension;

    @Column(name = "authentication", length = 50)
    private String authentication;

    @Column(name = "cipher", length = 50)
    private String cipher;

    @Column(name = "security_key_present")
    private Boolean securityKeyPresent;

    @Column(name = "cost", length = 50)
    private String cost;

    @Column(name = "congested")
    private Boolean congested;

    @Column(name = "approaching_data_limit")
    private Boolean approachingDataLimit;

    @Column(name = "over_data_limit")
    private Boolean overDataLimit;

    @Column(name = "roaming")
    private Boolean roaming;

    @Column(name = "cost_source", length = 50)
    private String costSource;

    @Column(name = "connection_mode", length = 50)
    private String connectionMode;

    @Column(name = "network_broadcast", length = 50)
    private String networkBroadcast;

    @Column(name = "auto_switch", length = 50)
    private String autoSwitch;

    @Column(name = "mac_randomization", length = 50)
    private String macRandomization;

    @Column(name = "eap_type", length = 50)
    private String eapType;

    @Column(name = "auth_credential", length = 100)
    private String authCredential;

    @Column(name = "credentials_configured", length = 100)
    private String credentialsConfigured;

    @Column(name = "cache_user_info", length = 100)
    private String cacheUserInfo;

    @Column(name = "connection_time")
    private LocalDateTime connectionTime;

    @Column(name = "disconnection_time")
    private LocalDateTime disconnectionTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UsersEntity getUser() {
        return user;
    }

    public void setUser(UsersEntity user) {
        this.user = user;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getControlOptions() {
        return controlOptions;
    }

    public void setControlOptions(String controlOptions) {
        this.controlOptions = controlOptions;
    }

    public Integer getNumberOfSsids() {
        return numberOfSsids;
    }

    public void setNumberOfSsids(Integer numberOfSsids) {
        this.numberOfSsids = numberOfSsids;
    }

    public String getSsidName() {
        return ssidName;
    }

    public void setSsidName(String ssidName) {
        this.ssidName = ssidName;
    }

    public String getNetworkType() {
        return networkType;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

    public String getRadioType() {
        return radioType;
    }

    public void setRadioType(String radioType) {
        this.radioType = radioType;
    }

    public String getVendorExtension() {
        return vendorExtension;
    }

    public void setVendorExtension(String vendorExtension) {
        this.vendorExtension = vendorExtension;
    }

    public String getAuthentication() {
        return authentication;
    }

    public void setAuthentication(String authentication) {
        this.authentication = authentication;
    }

    public String getCipher() {
        return cipher;
    }

    public void setCipher(String cipher) {
        this.cipher = cipher;
    }

    public Boolean getSecurityKeyPresent() {
        return securityKeyPresent;
    }

    public void setSecurityKeyPresent(Boolean securityKeyPresent) {
        this.securityKeyPresent = securityKeyPresent;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public Boolean getCongested() {
        return congested;
    }

    public void setCongested(Boolean congested) {
        this.congested = congested;
    }

    public Boolean getApproachingDataLimit() {
        return approachingDataLimit;
    }

    public void setApproachingDataLimit(Boolean approachingDataLimit) {
        this.approachingDataLimit = approachingDataLimit;
    }

    public Boolean getOverDataLimit() {
        return overDataLimit;
    }

    public void setOverDataLimit(Boolean overDataLimit) {
        this.overDataLimit = overDataLimit;
    }

    public Boolean getRoaming() {
        return roaming;
    }

    public void setRoaming(Boolean roaming) {
        this.roaming = roaming;
    }

    public String getCostSource() {
        return costSource;
    }

    public void setCostSource(String costSource) {
        this.costSource = costSource;
    }

    public String getConnectionMode() {
        return connectionMode;
    }

    public void setConnectionMode(String connectionMode) {
        this.connectionMode = connectionMode;
    }

    public String getNetworkBroadcast() {
        return networkBroadcast;
    }

    public void setNetworkBroadcast(String networkBroadcast) {
        this.networkBroadcast = networkBroadcast;
    }

    public String getAutoSwitch() {
        return autoSwitch;
    }

    public void setAutoSwitch(String autoSwitch) {
        this.autoSwitch = autoSwitch;
    }

    public String getMacRandomization() {
        return macRandomization;
    }

    public void setMacRandomization(String macRandomization) {
        this.macRandomization = macRandomization;
    }

    public String getEapType() {
        return eapType;
    }

    public void setEapType(String eapType) {
        this.eapType = eapType;
    }

    public String getAuthCredential() {
        return authCredential;
    }

    public void setAuthCredential(String authCredential) {
        this.authCredential = authCredential;
    }

    public String getCredentialsConfigured() {
        return credentialsConfigured;
    }

    public void setCredentialsConfigured(String credentialsConfigured) {
        this.credentialsConfigured = credentialsConfigured;
    }

    public String getCacheUserInfo() {
        return cacheUserInfo;
    }

    public void setCacheUserInfo(String cacheUserInfo) {
        this.cacheUserInfo = cacheUserInfo;
    }

    public LocalDateTime getConnectionTime() {
        return connectionTime;
    }

    public void setConnectionTime(LocalDateTime connectionTime) {
        this.connectionTime = connectionTime;
    }

    public LocalDateTime getDisconnectionTime() {
        return disconnectionTime;
    }

    public void setDisconnectionTime(LocalDateTime disconnectionTime) {
        this.disconnectionTime = disconnectionTime;
    }




}

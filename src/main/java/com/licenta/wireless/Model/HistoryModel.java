/*historymodel var2*/

package com.licenta.wireless.Model;

public class HistoryModel {
    private String profileName;

    private int version;
    private String type;
    private String controlOptions;
    private int numberOfSSIDs;
    private String ssidName;
    private String networkType;
    private String radioType;
    private String vendorExtension;
    private String authentication;
    private String cipher;
    private boolean securityKeyPresent;
    private String cost;
    private boolean congested;
    private boolean approachingDataLimit;
    private boolean overDataLimit;
    private boolean roaming;
    private String costSource;

    private String connectionMode;
    private String networkBroadcast;
    private String autoSwitch;
    private String MACRandomization;

    // Constructori
    public HistoryModel() {
    }

    public HistoryModel(String profileName) {
        this.profileName = profileName;
    }

    // Getters și setters pentru toate atributele

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
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

    public int getNumberOfSSIDs() {
        return numberOfSSIDs;
    }

    public void setNumberOfSSIDs(int numberOfSSIDs) {
        this.numberOfSSIDs = numberOfSSIDs;
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

    public boolean isSecurityKeyPresent() {
        return securityKeyPresent;
    }

    public void setSecurityKeyPresent(boolean securityKeyPresent) {
        this.securityKeyPresent = securityKeyPresent;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public boolean isCongested() {
        return congested;
    }

    public void setCongested(boolean congested) {
        this.congested = congested;
    }

    public boolean isApproachingDataLimit() {
        return approachingDataLimit;
    }

    public void setApproachingDataLimit(boolean approachingDataLimit) {
        this.approachingDataLimit = approachingDataLimit;
    }

    public boolean isOverDataLimit() {
        return overDataLimit;
    }

    public void setOverDataLimit(boolean overDataLimit) {
        this.overDataLimit = overDataLimit;
    }

    public boolean isRoaming() {
        return roaming;
    }

    public void setRoaming(boolean roaming) {
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

    public String getMACRandomization() {
        return MACRandomization;
    }

    public void setMACRandomization(String MACRandomization) {
        this.MACRandomization = MACRandomization;
    }
}

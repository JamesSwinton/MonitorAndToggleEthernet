package com.zebra.jamesswinton.monitorandtogglenetwork.profilemanager;

public class MXProfile {

    private String profileName, profileXml;

    public MXProfile(String profileName, String profileXml) {
        this.profileName = profileName;
        this.profileXml = profileXml;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getProfileXml() {
        return profileXml;
    }

    public void setProfileXml(String profileXml) {
        this.profileXml = profileXml;
    }
}

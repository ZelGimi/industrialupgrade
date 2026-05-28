package com.denfop.client.intro;

public class IntroFormerMember {

    public final String nickname;
    public final String displayName;
    public final String role;
    public final String contribution;
    public final String period;

    public IntroFormerMember(String nickname, String displayName, String role, String contribution, String period) {
        this.nickname = nickname;
        this.displayName = displayName;
        this.role = role;
        this.contribution = contribution;
        this.period = period;
    }

    public String nickname() {
        return nickname;
    }

    public String displayName() {
        return displayName;
    }

    public String role() {
        return role;
    }

    public String contribution() {
        return contribution;
    }

    public String period() {
        return period;
    }


}
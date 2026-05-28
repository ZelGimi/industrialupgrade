package com.denfop.client.intro;

public class IntroTeamMember {

    private final String role;
    private final String nickname;
    private final String displayName;
    private final String contribution;

    public IntroTeamMember(String role, String nickname, String displayName, String contribution) {
        this.role = role;
        this.nickname = nickname;
        this.displayName = displayName;
        this.contribution = contribution;
    }

    public String getRole() {
        return role;
    }

    public String getNickname() {
        return nickname;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getContribution() {
        return contribution;
    }
}
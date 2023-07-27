package com.web.billim.member.type;

import org.springframework.security.core.GrantedAuthority;

public enum MemberGrade implements GrantedAuthority {
    BRONZE(1),
    SILVER(2),
    GOLD(3),
    DIAMOND(4);

    private final int savedPointRate;

    MemberGrade(int savedPointRate) {
        this.savedPointRate = savedPointRate;
    }

    public int getSavedPointRate() {
        return this.savedPointRate;
    }

    @Override
    public String getAuthority() {
        return this.name();
    }

}

package com.angelserrano.gamersroomapi.model;

import javax.persistence.*;

@Entity
@Table(name = "token", schema = "security")
public class SecurityToken {

    public enum SECURITY_LEVEL{
        ADMIN,
        USER
    }

    @Id
    @Column(name = "token")
    private String token;

    @Column(name="securityLevel")
    private SECURITY_LEVEL security_level;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public SECURITY_LEVEL getSecurity_level() {
        return security_level;
    }

    public void setSecurity_level(SECURITY_LEVEL security_level) {
        this.security_level = security_level;
    }
}

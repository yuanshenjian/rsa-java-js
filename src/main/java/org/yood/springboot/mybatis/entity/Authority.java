package org.yood.springboot.mybatis.entity;

import java.io.Serializable;

public class Authority implements Serializable {

    private String username;
    private Role role;

    public enum Role {
        ADMIN,
        USER
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Authority{" +
                "username='" + username + '\'' +
                ", role=" + role +
                '}';
    }
}

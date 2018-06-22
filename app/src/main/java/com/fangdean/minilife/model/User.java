package com.fangdean.minilife.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "user")
public class User {

    @PrimaryKey
    private Long id;

    private String name;

    private String email;

    private String password;

    private Integer login_state;

    private String machine_num;

    private Long updatetime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getLogin_state() {
        return login_state;
    }

    public void setLogin_state(Integer login_state) {
        this.login_state = login_state;
    }

    public String getMachine_num() {
        return machine_num;
    }

    public void setMachine_num(String machine_num) {
        this.machine_num = machine_num;
    }

    public Long getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Long updatetime) {
        this.updatetime = updatetime;
    }
}

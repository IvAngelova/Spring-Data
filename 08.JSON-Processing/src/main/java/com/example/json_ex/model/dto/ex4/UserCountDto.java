package com.example.json_ex.model.dto.ex4;

import com.google.gson.annotations.Expose;

import java.util.List;

public class UserCountDto {
    @Expose
    private long usersCount;
    @Expose
    private List<UserFirstLastAgeSoldProductsDto> users;

    public UserCountDto() {
    }

    public UserCountDto(List<UserFirstLastAgeSoldProductsDto> users) {
        this.users = users;
        this.usersCount = users.size();
    }

    public long getUsersCount() {
        return usersCount;
    }

    public void setUsersCount(long usersCount) {
        this.usersCount = usersCount;
    }

    public List<UserFirstLastAgeSoldProductsDto> getUsers() {
        return users;
    }

    public void setUsers(List<UserFirstLastAgeSoldProductsDto> users) {
        this.users = users;
    }
}

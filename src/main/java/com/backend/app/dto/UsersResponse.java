package com.backend.app.dto;

import com.backend.app.entity.User;
import lombok.Data;

import java.util.List;

@Data
public class UsersResponse {
    private List<User> users;
}

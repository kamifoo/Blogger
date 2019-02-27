package me.kamikid.blog.service;

import me.kamikid.blog.entity.User;

public interface IUserService {

    User checkUser(String username, String password);
}

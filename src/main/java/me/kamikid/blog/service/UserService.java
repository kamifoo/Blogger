package me.kamikid.blog.service;

import me.kamikid.blog.dao.IUserRepository;
import me.kamikid.blog.entity.User;
import me.kamikid.blog.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService implements IUserService {
    private final IUserRepository userRepository;

    @Autowired
    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User checkUser(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, MD5Utils.code(password));
    }
}

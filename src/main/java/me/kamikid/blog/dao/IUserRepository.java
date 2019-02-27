package me.kamikid.blog.dao;

import me.kamikid.blog.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;


public interface IUserRepository extends JpaRepository<User, Long> {

    User findByUsernameAndPassword(String username, String password);

}

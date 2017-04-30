package me.liuwj.site.dao;

import me.liuwj.site.model.User;

/**
 * Created by vince on 2017/4/30.
 */
public interface UserDao {

    User getUserById(int id);

    User getUserByEmail(String email);

    void updateUser(User user);

    void createUser(User user);
}

package me.liuwj.site.service;

import me.liuwj.site.dao.UserDao;
import me.liuwj.site.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Created by vince on 2017/4/30.
 */
@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    @Transactional
    public User createOrUpdateUser(User req) {
        Assert.hasText(req.getName());
        Assert.hasText(req.getEmail());

        User user = userDao.getUserByEmail(req.getEmail());
        if (user == null) {
            userDao.createUser(req);
            return req;
        } else {
            user.setName(req.getName());

            if (StringUtils.isNotBlank(req.getHomepage()) && !req.getHomepage().toLowerCase().replace(" ", "").contains("javascript:void(0)")) {
                user.setHomepage(req.getHomepage());
            }

            userDao.updateUser(user);
            return user;
        }
    }
}

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
    public User createOrUpdateUser(User req, String clientIp) {
        if (req.isGuest()) {
            User user = userDao.getGuestUser(clientIp);
            if (user == null) {
                user = new User();
                user.setName("游客" + clientIp);
                user.setGuest(true);
                user.setRegisterIp(clientIp);
                userDao.createUser(user);
            }

            return user;

        } else {
            Assert.hasText(req.getName());
            Assert.hasText(req.getEmail());

            User user = userDao.getUserByEmail(req.getEmail());
            if (user == null) {
                user = new User();
                user.setName(req.getName());
                user.setEmail(req.getEmail());
                user.setHomepage(StringUtils.defaultIfBlank(req.getHomepage(), null));
                user.setAvatar(StringUtils.defaultIfBlank(req.getAvatar(), null));
                user.setGuest(false);
                user.setRegisterIp(clientIp);
                userDao.createUser(user);
            } else {
                user.setName(req.getName());
                user.setHomepage(StringUtils.defaultIfBlank(req.getHomepage(), user.getHomepage()));
                user.setAvatar(StringUtils.defaultIfBlank(req.getAvatar(), user.getAvatar()));
                userDao.updateUser(user);
            }

            return user;
        }
    }
}

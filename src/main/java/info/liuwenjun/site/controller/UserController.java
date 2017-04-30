package info.liuwenjun.site.controller;

import info.liuwenjun.site.model.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by vince on 2017/3/26.
 */
@RestController
public class UserController {

    @RequestMapping("/users/{email}")
    public User getUser(@PathVariable("email") String email) {
        User user = new User();
        user.setEmail(email);
        user.setName("U" + RandomStringUtils.randomAlphanumeric(6));
        return user;
    }
}

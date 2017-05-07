package me.liuwj.site.service;

import me.liuwj.site.dao.CommentDao;
import me.liuwj.site.dao.UserDao;
import me.liuwj.site.model.Comment;
import me.liuwj.site.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

/**
 * Created by vince on 2017/5/7.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CommentServiceTest.class)
@SpringBootApplication(scanBasePackages = "me.liuwj.site")
@ActiveProfiles("prod")
public class CommentServiceTest {
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private UserDao userDao;

    @Test
    public void createComment() {
        Comment comment = new Comment();
        comment.setPageId("/2016/09/03/java-reflection-black-tech/");
        comment.setFromUser(new User(14));
        comment.setToUser(null);
        comment.setContent("很厉害的方法。");
        comment.setCreateDate(parseTimestamp("2017-03-15T13:56:10+08:00"));
        commentDao.createComment(comment);
    }

    private static Timestamp parseTimestamp(String str) {
        return new Timestamp(ZonedDateTime.parse(str).toInstant().toEpochMilli());
    }

    @Test
    public void createUser() {
        User user = new User();
        user.setName("李保成");
        user.setEmail(null);
        user.setHomepage("http://t.qq.com/libaocheng9342");
        user.setAvatar("http://app.qlogo.cn/mbloghead/50a8af646221d5c7a222/50");
        userDao.createUser(user);
    }
}

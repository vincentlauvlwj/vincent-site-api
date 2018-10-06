package me.liuwj.site.service;

import me.liuwj.site.dao.CommentDao;
import me.liuwj.site.dao.UserDao;
import me.liuwj.site.model.Comment;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by vince on 2017/4/30.
 */
@Service
public class CommentService {
    @Autowired
    private UserService userService;
    @Autowired
    private NotifyService notifyService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private CommentDao commentDao;

    @Transactional
    public Comment createComment(Comment req, String clientIp) {
        Assert.hasText(req.getPageId());
        Assert.hasText(req.getContent());
        Assert.notNull(req.getFromUser());
        if (!req.getFromUser().isGuest()) {
            Assert.hasText(req.getFromUser().getName());
            Assert.hasText(req.getFromUser().getEmail());
        }

        Comment comment = new Comment();
        comment.setPageId(req.getPageId());
        comment.setFromUser(userService.createOrUpdateUser(req.getFromUser(), clientIp));
        comment.setToUser(userDao.getUserById(req.getToUser().getId()));
        comment.setContent(StringEscapeUtils.escapeHtml4(req.getContent()));
        comment.setCreateDate(new Timestamp(System.currentTimeMillis()));
        comment.setClientIp(clientIp);
        commentDao.createComment(comment);

        notifyService.sendNotifications(comment);
        return comment;
    }

    public List<Comment> getComments(String pageId) {
        Assert.hasText(pageId);
        return commentDao.getComments(pageId);
    }

    public int getCommentsCount(String pageId) {
        Assert.hasText(pageId);
        return commentDao.getCommentsCount(pageId);
    }
}

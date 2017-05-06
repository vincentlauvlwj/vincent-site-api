package me.liuwj.site.service;

import me.liuwj.site.dao.CommentDao;
import me.liuwj.site.dao.UserDao;
import me.liuwj.site.model.Comment;
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
    private UserDao userDao;
    @Autowired
    private CommentDao commentDao;

    @Transactional
    public Comment createComment(Comment req) {
        Assert.hasText(req.getPageId());
        Assert.notNull(req.getFromUser());
        Assert.hasText(req.getFromUser().getName());
        Assert.hasText(req.getFromUser().getEmail());
        Assert.hasText(req.getContent());

        Comment comment = new Comment();
        comment.setPageId(req.getPageId());
        comment.setFromUser(userService.createOrUpdateUser(req.getFromUser()));
        comment.setToUser(userDao.getUserById(req.getToUser().getId()));
        comment.setContent(req.getContent());
        comment.setCreateDate(new Timestamp(System.currentTimeMillis()));
        commentDao.createComment(comment);
        return comment;
    }

    public List<Comment> getComments(String pageId) {
        Assert.hasText(pageId);
        return commentDao.getComments(pageId);
    }
}

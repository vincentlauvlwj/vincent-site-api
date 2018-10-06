package me.liuwj.site.dao;

import me.liuwj.site.model.Comment;

import java.util.List;

/**
 * Created by vince on 2017/4/30.
 */
public interface CommentDao {

    List<Comment> getComments(String pageId);

    int getCommentsCount(String pageId);

    void createComment(Comment comment);
}

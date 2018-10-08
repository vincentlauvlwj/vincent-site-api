package me.liuwj.site.dao;

import me.liuwj.site.model.Comment;
import me.liuwj.site.model.CommentStat;

import java.util.List;

/**
 * Created by vince on 2017/4/30.
 */
public interface CommentDao {

    List<Comment> getComments(String pageId);

    List<CommentStat> getCommentStats();

    void createComment(Comment comment);
}

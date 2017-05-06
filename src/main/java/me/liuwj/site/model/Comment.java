package me.liuwj.site.model;

import lombok.Data;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

/**
 * Created by vince on 2017/4/30.
 */
@Data
public class Comment {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private int id;
    private String pageId;
    private User fromUser;
    private User toUser;
    private String content;
    private Timestamp createDate;

    public String getCreateDateStr() {
        return createDate.toLocalDateTime().format(FORMATTER);
    }

    public Comment toFront() {
        Comment comment = new Comment();
        comment.id = id;
        comment.pageId = pageId;
        comment.fromUser = fromUser.toFront();
        comment.toUser = toUser == null ? null : toUser.toFront();
        comment.content = content;
        comment.createDate = new Timestamp(createDate.getTime());
        return comment;
    }
}

package me.liuwj.site.model;

import lombok.Data;

import java.sql.Timestamp;

/**
 * Created by vince on 2017/4/30.
 */
@Data
public class Comment {
    private int id;
    private String pageId;
    private User fromUser;
    private User toUser;
    private String content;
    private Timestamp createDate;
}

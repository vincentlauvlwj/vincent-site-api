package me.liuwj.site.model;

import lombok.Data;

/**
 * Created by vince on 2017/3/26.
 */
@Data
public class User {
    private int id;
    private String name;
    private String email;
    private String homepage;
    private String avatar;
}

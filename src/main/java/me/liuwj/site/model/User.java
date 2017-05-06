package me.liuwj.site.model;

import com.huaying.common.utils.codec.MD5;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

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

    public User toFront() {
        User user = new User();
        user.id = id;
        user.name = name;
        user.email = email;

        if (StringUtils.isBlank(homepage)) {
            user.homepage = "javascript:void(0);";
        } else if (!homepage.startsWith("http")) {
            user.homepage = "http://" + homepage;
        } else {
            user.homepage = homepage;
        }

        if (StringUtils.isNotBlank(avatar)) {
            user.avatar = avatar;
        } else if (StringUtils.isNotBlank(email)) {
            String gravatarId = MD5.generate(email.trim().toLowerCase()).toLowerCase();
            user.avatar = "https://cdn.v2ex.com/gravatar/" + gravatarId + "?d=retro";
        } else {
            user.avatar = "https://cdn.v2ex.com/gravatar/?d=mm&f=y";
        }

        return user;
    }
}

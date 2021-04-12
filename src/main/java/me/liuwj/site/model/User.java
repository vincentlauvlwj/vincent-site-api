package me.liuwj.site.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import me.liuwj.site.utils.MD5;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by vince on 2017/3/26.
 */
@Data
@NoArgsConstructor
public class User {
    private int id;
    private String name;
    private String email;
    private String homepage;
    private String avatar;
    private boolean guest;
    private String registerIp;

    public User(int id) {
        this.id = id;
    }

    public User toFront(boolean hideEmail) {
        User user = new User();
        user.id = id;
        user.name = name;
        user.email = hideEmail ? hideEmail(email) : email;
        user.guest = guest;
        user.registerIp = registerIp;

        String homepage = StringUtils.defaultIfBlank(this.homepage, null);
        if (homepage == null || homepage.startsWith("http")) {
            user.homepage = homepage;
        } else {
            user.homepage = "http://" + homepage;
        }

        if (StringUtils.isNotBlank(avatar)) {
            user.avatar = avatar;
        } else if (StringUtils.isNotBlank(email)) {
            String gravatarId = MD5.generate(email.trim().toLowerCase()).toLowerCase();
            user.avatar = "https://www.liuwj.me/gravatar/" + gravatarId + "?d=retro";
        } else {
            user.avatar = "https://www.liuwj.me/gravatar/default?d=mm&f=y";
        }

        return user;
    }

    private static String hideEmail(String email) {
        if (StringUtils.isBlank(email)) {
            return email;
        }

        String[] arr = email.split("@");
        String name = arr[0];
        String host = arr[1];
        return name.charAt(0)
            + StringUtils.repeat("*", name.length() - 1)
            + "@"
            + host;
    }

    public static void main(String[] args) {
        System.out.println(hideEmail("wenjun0508@qq.com"));
        System.out.println(hideEmail("me@liuwj.me"));
    }
}

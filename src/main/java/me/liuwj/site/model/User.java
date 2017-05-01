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

    public String getGeneratedAvatar() {
        if (StringUtils.isNotBlank(avatar)) {
            return avatar;
        } else if (StringUtils.isNotBlank(email)) {
            String gravatarId = MD5.generate(email.trim().toLowerCase()).toLowerCase();
            return "https://cdn.v2ex.com/gravatar/" + gravatarId + "?d=retro";
        } else {
            return "https://cdn.v2ex.com/gravatar/?d=mm&f=y";
        }
    }

    public String getGeneratedHomepage() {
        if (StringUtils.isBlank(homepage)) {
            return "javascript:void(0);";
        } else if (!homepage.startsWith("http")) {
            return "http://" + homepage;
        } else {
            return homepage;
        }
    }
}

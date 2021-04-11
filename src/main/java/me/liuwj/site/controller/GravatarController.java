package me.liuwj.site.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by vince at Apr 11, 2021.
 */
@Slf4j
@Controller
public class GravatarController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;

    @RequestMapping(path = "/gravatar/{gravatarId}", method = RequestMethod.GET)
    public void gravatar(@PathVariable String gravatarId) throws IOException {
        String url = "https://www.gravatar.com/avatar/" + gravatarId;
        if (StringUtils.isNotBlank(request.getQueryString())) {
            url += "?" + request.getQueryString();
        }

        log.info("Downloading from " + url);

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        try {
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);
            connection.connect();

            int code = connection.getResponseCode();
            if (code != 200) {
                response.setStatus(code);
            } else {
                try (InputStream input = connection.getInputStream()) {
                    IOUtils.copy(input, response.getOutputStream());
                    response.getOutputStream().flush();
                }
            }
        } finally {
            connection.disconnect();
        }
    }
}

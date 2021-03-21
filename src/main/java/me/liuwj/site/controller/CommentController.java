package me.liuwj.site.controller;

import me.liuwj.site.model.Comment;
import me.liuwj.site.model.CommentStat;
import me.liuwj.site.service.CommentService;
import me.liuwj.site.utils.IpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * Created by vince on 2017/3/26.
 */
@RestController
public class CommentController {
    @Autowired
    private CommentService commentService;

    @RequestMapping(path = "/comments/", method = RequestMethod.POST)
    public Comment createComment(@RequestBody Comment comment, HttpServletRequest httpRequest) {
        return commentService.createComment(comment, IpUtils.getClientIp(httpRequest)).toFront(false);
    }

    @RequestMapping(path = "/comments/", method = RequestMethod.GET)
    public List<Comment> getComments(@RequestParam("pageId") String pageId) {
        return commentService.getComments(pageId).stream()
                .map(c -> c.toFront(true))
                .collect(toList());
    }

    @RequestMapping(path = "/comments/stats", method = RequestMethod.GET)
    public Map<String, Object> getCommentStats() {
        List<CommentStat> details = commentService.getCommentStats();

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("stats", details.stream().mapToInt(CommentStat::getCommentCount).summaryStatistics());
        result.put("details", details);
        return result;
    }
}

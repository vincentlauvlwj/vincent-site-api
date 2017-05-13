package me.liuwj.site.service;

import me.liuwj.site.model.Comment;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Created by vince on 2017-05-13.
 */
@Service
public class NotifyService {
    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendNotifications(Comment comment) {
        SimpleMailMessage messageToAdmin = new SimpleMailMessage();
        messageToAdmin.setFrom("Vincent Notification <notify@liuwj.me>");
        messageToAdmin.setTo("me@liuwj.me");
        messageToAdmin.setSubject("Vincent's Site - 新评论提醒");
        messageToAdmin.setText("有新评论，点击查看：" + comment.getUrl());
        mailSender.send(messageToAdmin);

        if (comment.getToUser() != null && StringUtils.isNotBlank(comment.getToUser().getEmail())) {
            SimpleMailMessage messageToUser = new SimpleMailMessage();
            messageToUser.setFrom("Vincent Notification <notify@liuwj.me>");
            messageToUser.setTo(comment.getToUser().getEmail());
            messageToUser.setSubject("Vincent's Site - 评论回复通知");
            messageToUser.setText(comment.getToUser().getName() + ", 您好：\n" +
                    comment.getFromUser() + " 回复了您的评论，点击查看：" + comment.getUrl());
            mailSender.send(messageToUser);
        }
    }
}

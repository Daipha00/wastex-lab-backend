package com.suza.wasteX.member;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendStatusEmail(Member member) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(member.getEmail());

        if (member.getStatus() == Member.MemberStatus.ACCEPTED) {
            message.setSubject("Application Accepted");
            message.setText("Congratulations " + member.getFirstName() +
                    ", your application has been ACCEPTED.");
        } else {
            message.setSubject("Application Rejected");
            message.setText("Dear " + member.getFirstName() +
                    ", we regret to inform you that your application was REJECTED.");
        }

        mailSender.send(message);
    }
}
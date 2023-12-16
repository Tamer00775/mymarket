package kz.kartayev.authorization_service.service;

import kz.kartayev.authorization_service.config.MailConfig;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailService {
  @Autowired
  private final JavaMailSender javaMailSender;

  public void sendMessage(String to, String subject, String text) {
    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
    simpleMailMessage.setSubject(subject);
    simpleMailMessage.setTo(to);
    simpleMailMessage.setText(text);

    javaMailSender.send(simpleMailMessage);
  }

}

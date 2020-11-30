package cz.buben.sre.service;

import cz.buben.sre.data.NotificationEmail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender mailSender;

    public void sendMail(NotificationEmail email) {
        MimeMessagePreparator preparator = mimeMessage -> {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setFrom("test@example.com");
            helper.setTo(email.getRecipient());
            helper.setSubject(email.getSubject());
            helper.setText(email.getBody());
        };

        try {
            this.mailSender.send(preparator);
            log.info("Mail sent");
        } catch (MailException ex) {
            log.error("Error while sending mail to {}: {}", email.getRecipient(), ex.getMessage(), ex);
            throw new RuntimeException("Send mail failed: " + ex.getMessage(), ex);
        }
    }
}

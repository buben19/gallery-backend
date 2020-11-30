package cz.buben.sre.service

import cz.buben.sre.data.NotificationEmail
import org.springframework.mail.MailSendException
import org.springframework.mail.javamail.JavaMailSender
import spock.lang.Specification

class MailServiceSpecification extends Specification {

    JavaMailSender sender = Mock()
    MailService service = new MailService(sender)

    def "send email"() {
        when:
        service.sendMail(new NotificationEmail(
                subject: 'Test subject',
                recipient: 'recipient@example.com',
                body: 'Test body'
        ))

        then:
        1 * sender.send(_)
    }

    def "send mail failed"() {
        when:
        service.sendMail(new NotificationEmail(
                subject: 'Test subject',
                recipient: 'recipient@example.com',
                body: 'Test body'
        ))

        then:
        1 * sender.send(_) >> {throw new MailSendException("Test fail")}

        and:
        thrown(RuntimeException)
    }
}

package InternshipProj.api.spring_security;

import InternshipProj.api.users.Userid;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String text) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setText(text, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setFrom("aeneas.nicholas@gmail.com");
        mailSender.send(mimeMessage);
    }

    public void sendVerify(Userid user, String code) throws MessagingException {
        String subject = "Email Verification";
        String confirmationUrl = "http://localhost:8080/api/verify?code=" + code;
        String message = "Click the link to verify your email: <a href=\"" + confirmationUrl + "\">" + confirmationUrl + "</a>";
        sendEmail(user.getEmail(), subject, message);
    }
}

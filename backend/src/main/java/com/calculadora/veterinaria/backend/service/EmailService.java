package com.calculadora.veterinaria.backend.service;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper; 
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage; 
import java.io.UnsupportedEncodingException;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarEmail(String destinatario, String assunto, String corpo) {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        
        try {

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            String htmlMsg = "<html><body>"
                         + "<p>Olá,</p>"
                         + "<p>Você solicitou a redefinição de sua senha para a Calculadora Veterinária.</p>"
                         + "<p>Por favor, clique no link abaixo para criar uma nova senha:</p>"
                         + "<br>" 
                         
                         + "<a href='" + corpo + "' "
                         + "   style='background-color:#007bff; color:white; padding:10px 15px; text-decoration:none; border-radius:5px;'>"
                         + "   Clique aqui para redefinir sua senha"
                         + "</a>"
                         
                         + "<br><br>" 
                         + "<p>Se você não solicitou isso, por favor, ignore este e-mail.</p>"
                         + "<p>Atenciosamente,<br>Equipe Calculadora Veterinária</p>"
                         + "</body></html>";

            
            helper.setTo(destinatario);
            helper.setSubject(assunto);
            helper.setText(htmlMsg, true); 
            
            helper.setFrom("suporte.calcvet@gmail.com", "Calculadora Veterinária");

            mailSender.send(mimeMessage);

        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException("Falha ao enviar e-mail HTML: " + e.getMessage());
        }
    }
}
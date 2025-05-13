package comun;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class MailUtil {
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String USERNAME  = "hospitalprivadopedregal@gmail.com";    
    private static final String PASSWORD  = "crud hmfw xlzg ybpg";          

    private static Session getSession() {
        Properties props = new Properties();
        props.put("mail.smtp.auth",       "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host",       SMTP_HOST);
        props.put("mail.smtp.port",       SMTP_PORT);

        return Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });
    }

    public static void enviarCorreo(String destino, String asunto, String cuerpo) {
        try {
            Message msg = new MimeMessage(getSession());
            msg.setFrom(new InternetAddress(USERNAME, "Hospital Privado Pedregal"));
            msg.setRecipients(Message.RecipientType.TO,
                               InternetAddress.parse(destino, false));
            msg.setSubject(asunto);
            msg.setText(cuerpo);
            Transport.send(msg);
            System.out.println("[MailUtil] Correo enviado a " + destino);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

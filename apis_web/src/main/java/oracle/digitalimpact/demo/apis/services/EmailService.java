package oracle.digitalimpact.demo.apis.services;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import oracle.digitalimpact.demo.apis.exceptions.APIException;
import oracle.digitalimpact.demo.apis.utils.MapFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EmailService implements Serializable {

    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1L;
    
    private transient Log logger = LogFactory.getLog(this.getClass());
    
    private boolean htmlMime = false;
    private String smtpHost = null;
    private int smtpPort    = 0;
    private String authUser = null;
    private String authPwd  = null;
    private String fromAddress = null;
    private Properties props = null;
    private String emailTemplateUrl = null;

    private HttpClientService httpClientService = null;
    
    public void init () throws APIException {
        logger.info("Entered");
        try {
            props = new Properties();
            props.put("mail.smtp.host", getSmtpHost());
            props.put("mail.smtp.socketFactory.port", String.valueOf(getSmtpPort()));
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", String.valueOf(getSmtpPort()));
        }
        catch (Exception expGeneral) {
            throw new APIException ("Exception instantiating " + this, expGeneral);
        }
        logger.info("Leaving");
    }
    
    public void destroy () {
        
    }
    
    public void email(String toAddress, String firstName, String lastName, String username, String password) throws APIException {
        logger.info("Entered");
        try {

            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(getAuthUser(), getAuthPwd());
                    }
                });
            
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(getFromAddress()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress));
            message.setSubject("Welcome to Digital Impact Demo");
            
            if (isHtmlMime()) {
                
                // Create a Multipart Email
                MimeMultipart multiPart = new MimeMultipart ("related");
                
                String htmlContent = new String (getHttpClientService().getContent(getEmailTemplateUrl()), "utf-8");
                Map<String, String> emailAttributes =   new HashMap <String, String>();
                emailAttributes.put("firstname", firstName);
                emailAttributes.put("lastname", lastName);
                emailAttributes.put("username", username);
                emailAttributes.put("passwordpin", password);
                htmlContent = MapFormat.format(htmlContent, emailAttributes);
                
                // Create and add the Body Content Part
                BodyPart bodyPart = new MimeBodyPart ();
                bodyPart.setContent (htmlContent, "text/html");
                multiPart.addBodyPart (bodyPart);
                
                // Add the Multipart into the Message
                message.setContent (multiPart);
            }
            else {
                StringBuilder builder = new StringBuilder ();
                builder.append ("Dear " + firstName + " " + lastName + "," + "\n");
                builder.append ("Welcome to Digital Impact Demo. Please find your username and password pin attached." + "\n");
                builder.append ("User Name:" + username + "\n");
                builder.append ("Password PIN:" + password + "\n");
                builder.append ("Thanks," + "\n");
                builder.append ("Digital Impact Demo Administrator" + "\n");
                message.setText(builder.toString());
            }
            Transport.send(message);
        } catch (Exception expGeneral) {
            throw new APIException ("Exception sending email to " + toAddress, expGeneral);
        }
        logger.info("Leaving");
    }


    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    public String getSmtpHost() {
        return smtpHost;
    }

    public void setSmtpPort(int smtpPort) {
        this.smtpPort = smtpPort;
    }

    public int getSmtpPort() {
        return smtpPort;
    }

    public void setAuthUser(String authUser) {
        this.authUser = authUser;
    }

    public String getAuthUser() {
        return authUser;
    }

    public void setAuthPwd(String authPwd) {
        this.authPwd = authPwd;
    }

    public String getAuthPwd() {
        return authPwd;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getFromAddress() {
        return fromAddress;
    }
    
    public void setHtmlMime(boolean htmlMime) {
        this.htmlMime = htmlMime;
    }

    public boolean isHtmlMime() {
        return htmlMime;
    }

    public void setHttpClientService(HttpClientService httpClientService) {
        this.httpClientService = httpClientService;
    }

    public HttpClientService getHttpClientService() {
        return httpClientService;
    }

    public void setEmailTemplateUrl(String emailTemplateUrl) {
        this.emailTemplateUrl = emailTemplateUrl;
    }

    public String getEmailTemplateUrl() {
        return emailTemplateUrl;
    }
}

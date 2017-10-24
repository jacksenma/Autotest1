package com.nj.ts.autotest.email;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;


public class MailConfig {
    //configs
    private String host = "mail.thundersoft.com";
    private String port = "465";
    private String secType;
    private String senderName = "ThunderSoftAutoTest";
    //TODO
    private String senderEmail = "ruantf0801@thundersoft.com";
    private String senderPassword = "rtf19920629!@#";
    private String recipients = "sunfei0401@thundersoft.com";
//    private String recipients = "455414173@qq.com";

    //variables
    private final String needAuth = "true";
    private final String protocol = "smtp";
    private Session mSession;

    public static MailConfig sMailConfig;
    public static MailConfig getInstance() {
        if (sMailConfig == null) {
            sMailConfig = new MailConfig();
        }
        return sMailConfig;
    }

    private MailConfig() {
        initFromPreference();
        initMailProperties();
    }

    private void initFromPreference() {
        //TODO
    }

    private void initMailProperties() {
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", protocol);
        properties.setProperty("mail.host", host);
        properties.put("mail.smtp.auth", needAuth);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.socketFactory.port", port);
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); //SSL
        properties.put("mail.smtp.socketFactory.fallback", "false");
        properties.setProperty("mail.smtp.quitwait", "false");

        mSession = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });
        mSession.setDebug(true);
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getSenderPassword() {
        return senderPassword;
    }

    public void setSenderPassword(String senderPassword) {
        this.senderPassword = senderPassword;
    }

    public String getRecipients() {
        return recipients;
    }

    public void setRecipients(String recipients) {
        this.recipients = recipients;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getSecType() {
        return secType;
    }

    public void setSecType(String secType) {
        this.secType = secType;
    }

    public Session getSession() {
        return mSession;
    }
}

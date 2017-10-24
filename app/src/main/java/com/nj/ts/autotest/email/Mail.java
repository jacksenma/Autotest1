package com.nj.ts.autotest.email;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
/**
 * Created by root on 10/17/17.
 */

public class Mail {

    private String subject = "ThunderSoftAutoTest";
    private ArrayList<Attachment> attaches = new ArrayList<>();
    private String content = "ThundersoftAutoTest Email, Please DO NOT REPLY!";

    public Mail() {}

    public Mail(String subject, String content) {
        this.subject = subject;
        this.content = content;
    }

    public void addAttachment(Attachment attachment) {
        this.attaches.add(attachment);
    }

    public void addAttachment(ArrayList<Attachment> attaches) {
        this.attaches.addAll(attaches);
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void send() throws IOException, MessagingException {
        // 声明一个邮件体
        MimeMessage msg = new MimeMessage(MailConfig.getInstance().getSession());
        //发件人信息
        msg.setFrom(new InternetAddress(
                "\""+MimeUtility.encodeText(MailConfig.getInstance().getSenderName())
                + "\"" + "<" + MailConfig.getInstance().getSenderEmail() + ">")
        );
        //设置邮件主题
        msg.setSubject(subject);
        // 设置收件人（可多人，用“,”分割）
        msg.setRecipients(
                MimeMessage.RecipientType.TO,
                InternetAddress.parse(MailConfig.getInstance().getRecipients())
        );

        // 邮件正文
        MimeMultipart msgMultipart = new MimeMultipart();
        BodyPart html = new MimeBodyPart();
        html.setContent(content, "text/html; charset=GBK");
        msgMultipart.addBodyPart(html);
        // 设置邮件体
        msg.setContent(msgMultipart);

        // 设置附件
        for (int i = 0; i < attaches.size(); i++) {
            MimeBodyPart attach = new MimeBodyPart();
            msgMultipart.addBodyPart(attach);
            // 指定附件的数据源
            DataSource ds = new FileDataSource(attaches.get(i).getPath());
            // 附件的信息
            DataHandler dh = new DataHandler(ds);
            // 指定附件
            attach.setDataHandler(dh);
            // 指定附件名称
            attach.setFileName(attaches.get(i).getName());
        }

        // 保存邮件
        msg.saveChanges();

        // 发送邮件
        Transport.send(msg);
    }
}

package com.singularity.EamilClassifivationSystem.services;

import java.io.File;

import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.singularity.EamilClassifivationSystem.beans.Email;



@Service
public class MailServiceImpl implements MailService {


    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String mailUserName;

    Logger log = LoggerFactory.getLogger(MailServiceImpl.class);

    public void sendMail(Email email) {
        long start = System.currentTimeMillis();
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            //true表示需要创建一个multipart message
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(mailUserName);
            helper.setTo(email.getToAddress().toArray(new String[email.getToAddress().size()]));
            helper.setSubject(email.getSubject());
            helper.setText(email.getContent(), true);

            if (null != email.getAttachments() && email.getAttachments().size() > 0) {
                for (File curFile : email.getAttachments()) {
                    FileSystemResource file = new FileSystemResource(curFile);
                    helper.addAttachment(MimeUtility.encodeWord(file.getFilename(), "utf-8", "B"), file);
                }
            }
            log.info("start send");
            javaMailSender.send(mimeMessage);
            long sendMillTimes = System.currentTimeMillis() - start;
            log.info("mail sended,sendTimes=" + sendMillTimes);
        } catch (Exception e) {
            log.error("error！", e);
        }
    }

	
	

}

package com.gridone.scraping.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class MailClient {

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private TemplateEngine templateEngine;
	
	@Value("${email.from}")
	String emailFrom;
	
	public String build(String message) {
		Context context = new Context();
		context.setVariable("message", message);
		return templateEngine.process("email", context);
	}
	
	public void prepareAndSend(String recipient, String message) {
		try {
			MimeMessagePreparator messagePreparator = miemMessage -> {
				MimeMessageHelper messageHelper = new MimeMessageHelper(miemMessage);
				messageHelper.setFrom(emailFrom);
				messageHelper.setTo(recipient);
				messageHelper.setSubject("그리드원 데일리 뉴스 모니터링");
				String content = this.build(message);
				content = content.replaceAll("&lt;", "<");
				content = content.replaceAll("&gt;", ">");
				content = content.replaceAll("&#39;", "'");
				messageHelper.setText(content,true);
			};
			mailSender.send(messagePreparator);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

package com.unicap.tcc.usability.api.service;

import com.unicap.tcc.usability.api.models.assessment.Assessment;
import com.unicap.tcc.usability.api.models.review.Review;
import com.unicap.tcc.usability.api.properties.AmazonSASProperties;
import com.unicap.tcc.usability.api.repository.UserRepository;
import com.unicap.tcc.usability.api.utils.HtmlUtils;
import com.unicap.tcc.usability.api.utils.LoggerUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Objects;

@Component("MailSender")
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class MailSender {

    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;
    private final AmazonSASProperties amazonSASProperties;

    public void send(String[] recipients, String subject, String text, ByteArrayOutputStream source, String projectName) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true,"utf-8");
        if (Objects.nonNull(source)){
            helper.addAttachment(projectName.replaceAll("\\s+",""),
                    new ByteArrayResource(source.toByteArray()));
        }
        try {
            helper.setText(text, true);
            helper.setTo(recipients);
            helper.setSubject(subject);
            helper.setFrom(amazonSASProperties.getFrom(), amazonSASProperties.getFromName());
            javaMailSender.send(message);
        } catch (Exception e) {
            LoggerUtil.logError(log, e, text);
        }
    }

    public void sendAvailableReviewEmail(Assessment assessment, Review review, List<String> emails) {
        emails.forEach(email -> {
            try {
                String htmlMailText = HtmlUtils.setHtmlMailNewReview(assessment, review);
                if (!htmlMailText.equals("")) {
                    String subject = "[Usabivalid] - NEW PLAN REVIEW AVAILABLE";
                    send(new String[]{email}, subject, htmlMailText, null, assessment.getProjectName());
                }
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        });
    }

    public void sendFinishedReviewEmail(Review review, List<String> emails, ByteArrayOutputStream source) {
        emails.forEach(email -> {
            try {
                String htmlMailText = HtmlUtils.setHtmlMailFinishedReview(review);
                if (!htmlMailText.equals("")) {
                    String subject = "[Usabivalid] - COMPLETED PLAN REVIEW";
                    send(new String[]{email}, subject, htmlMailText, source,
                            review.getAssessment().getProjectName() + "-Plan-Review.pdf");
                }
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        });
    }

    public void sendCollaboratorEmail(Assessment assessment, String assessmentUid, List<String> emails) {
        emails.forEach(email -> {
            try {
                String htmlMailText;
                var userOptional = userRepository.findByEmail(email);
                htmlMailText = userOptional.map(user ->
                        HtmlUtils.setHtmlMailCollaborator(assessment, assessmentUid, user))
                        .orElseGet(() -> HtmlUtils.setHtmlMailNewCollaborator(assessment, assessmentUid));
                if (!htmlMailText.equals("")) {
                    String subject = "[Usabivalid] - COLLABORATOR INVITE";
                    send(new String[]{email}, subject, htmlMailText, null, assessment.getProjectName());
                }
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        });
    }

    public void sendPlanExportEmail(Assessment assessment, List<String> emails, ByteArrayOutputStream source) {
        emails.forEach(email -> {
            try {
                String htmlMailText = HtmlUtils.setHtmlSendPlan(assessment);
                if (!htmlMailText.equals("")) {
                    String subject = "[Usabivalid] - PLAN FILE";
                    if (Objects.nonNull(source)) {
                        send(new String[]{email}, subject, htmlMailText, source, assessment.getProjectName()  + "Plan.pdf");
                    }
                }
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        });
    }
}

package com.unicap.tcc.usability.api.service;

import com.unicap.tcc.usability.api.models.assessment.Assessment;
import com.unicap.tcc.usability.api.properties.AmazonSASProperties;
import com.unicap.tcc.usability.api.repository.UserRepository;
import com.unicap.tcc.usability.api.utils.HtmlUtils;
import com.unicap.tcc.usability.api.utils.LoggerUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.util.List;

@Component("MailSender")
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class MailSender {

    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;
    private final AmazonSASProperties amazonSASProperties;

    public void send(String[] recipients, String subject, String text) {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
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

//    public void sendReturnFileErrorEmail(ReturnFile returnFile) {
//        var returnFileImpl = returnFile.getReturnFileImpl();
//        var dealershipName = returnFileImpl
//                .getDealershipConfig().getFilePath();
//        var steps = returnFile.getSteps();
//        var fileNameWithPath = returnFileImpl.getDealershipConfig().getFilePath()
//                .concat("/")
//                .concat(returnFile.getFilename());
//
//        var environmentName = environmentProperties.getName().toUpperCase();
//        var htmlMailText = HtmlUtils.setHtmlMailReturnFileErrorLayout(environmentName, fileNameWithPath,
//                dealershipName, returnFileImpl.getReference(), steps);
//
//        LocalDate processingDate = LocalDate.now();
//        String subject = String.format("[%s - %s] - ERRO ARQUIVO DE RETORNO %s", environmentName, dealershipName,
//                DateUtils.formatLocalDate(processingDate, "dd-MM-yyyy"));
//        send(returnFileImpl.getEmails(), subject, htmlMailText);
//    }

    public void sendCollaboratorEmail(Assessment assessment, String assessmentUid, List<String> emails) {
        emails.forEach(email -> {
            String htmlMailText;
            var userOptional = userRepository.findByEmail(email);
            htmlMailText = userOptional.map(user ->
                    HtmlUtils.setHtmlMailCollaborator(assessment, assessmentUid, user))
                    .orElseGet(() -> HtmlUtils.setHtmlMailNewCollaborator(assessment, assessmentUid));
            if (!htmlMailText.equals("")) {
                String subject = "[VALID USABILITY ASSESSMENT] - COLLABORATOR INVITE";
                send(new String[]{email}, subject, htmlMailText);
            }
        });
    }
}

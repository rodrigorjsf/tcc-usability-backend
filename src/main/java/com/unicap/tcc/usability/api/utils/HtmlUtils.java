package com.unicap.tcc.usability.api.utils;

import com.google.common.io.Files;
import com.unicap.tcc.usability.api.models.User;
import com.unicap.tcc.usability.api.models.assessment.Assessment;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class HtmlUtils {

    public static String setHtmlMailCollaborator(Assessment assessment, String assessmentUid, User collaborator) {
        File input = new File("src/main/resources/templates/CollaboratorMail.html");
        try {
            var mailTemplate = Files.asCharSource(input, StandardCharsets.UTF_8).read();
            return mailTemplate.replace(":collaboratorName", collaborator.getName())
                    .replace(":authorName", assessment.getSystemUser().getName())
                    .replace(":assessmentUid", assessmentUid);
        } catch (IOException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return "";
    }

    public static String setHtmlMailNewCollaborator(Assessment assessment, String assessmentUid) {
        File input = new File("src/main/resources/templates/NewCollaboratorMail.html");
        try {
            var mailTemplate = Files.asCharSource(input, StandardCharsets.UTF_8).read();
            return mailTemplate.replace(":authorName", assessment.getSystemUser().getName())
                    .replace(":assessmentUid", assessmentUid);
        } catch (IOException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return "";
    }

    public static String setHtmlSendPlan(Assessment assessment) {
        File input = new File("src/main/resources/templates/SendPlansToEmail.html");
        try {
            var mailTemplate = Files.asCharSource(input, StandardCharsets.UTF_8).read();
            return mailTemplate.replace(":plan", assessment.getProjectName());
        } catch (IOException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return "";
    }


}

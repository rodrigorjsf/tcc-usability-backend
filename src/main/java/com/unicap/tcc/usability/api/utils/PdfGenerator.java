package com.unicap.tcc.usability.api.utils;

import com.amazonaws.util.CollectionUtils;
import com.google.gson.internal.LinkedTreeMap;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.unicap.tcc.usability.api.models.Scale;
import com.unicap.tcc.usability.api.models.assessment.Assessment;
import com.unicap.tcc.usability.api.models.assessment.Attribute;
import com.unicap.tcc.usability.api.models.assessment.UsabilityGoal;
import com.unicap.tcc.usability.api.models.enums.ScalesEnum;
import com.unicap.tcc.usability.api.models.enums.SmartCityAttribute;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class PdfGenerator {

    public static final String THREATS_SECTION_KEY = "tv";
    private static Font catFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    private static final String PARAGRAPH_SPACE = "   ";

    private static Font getBoldFormat(Integer size) {
        return new Font(Font.FontFamily.HELVETICA, size, Font.BOLD);
    }

    private static Paragraph getBoldParagraph(String paragraphContent, Integer size) {
        return new Paragraph(paragraphContent, getBoldFormat(size));
    }

    private static Paragraph getItalicParagraph(String paragraphContent, Integer size) {
        Font italic = new Font(Font.FontFamily.HELVETICA, size, Font.ITALIC);
        return new Paragraph(paragraphContent, italic);
    }

    private static Paragraph getSingleParagraph(String paragraphContent) {
        return new Paragraph(paragraphContent);
    }

    private static PdfPTable getHeader(Assessment assessment) {
        PdfPTable table = new PdfPTable(2);

        PdfPCell cell;

        cell = new PdfPCell(new Phrase("ValidEPlan - Validity-Driven Software Engineering Experiments Planning Tool"));
        cell.setColspan(2);
        table.addCell(cell);

        table.addCell("Project name:");
        table.addCell(assessment.getProjectName());
        table.addCell("Project Description");
        table.addCell(assessment.getProjectDescription());
        table.addCell(getKeyValueParagraph("Created by: ", assessment.getSystemUser().getName()));
        table.addCell(getKeyValueParagraph("Created by: ", assessment.getSystemUser().getName()));
        table.addCell(getKeyValueParagraph("Creation date: ", assessment.getSystemUser().getCreationDate().toLocalDate().toString()));
        table.addCell(getKeyValueParagraph("Creation date: ", assessment.getSystemUser().getCreationDate().toLocalDate().toString()));


        return table;
    }

    private static Paragraph getKeyValueParagraph(String label, String value) {
        Chunk keyChunk = new Chunk(label, getBoldFormat(12));
        Chunk valueChunk = new Chunk(value);
        Paragraph p1 = new Paragraph();
        p1.add(keyChunk);
        p1.add(valueChunk);

        return p1;
    }

    public static ByteArrayOutputStream generatePlan(Assessment assessment) {
        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, baos);

            document.open();

            // report header
            document.add(getBoldParagraph("ValidEPlan - Validity-Driven Software Engineering Experiments Planning Tool", 18));
            document.add(Chunk.NEWLINE);
            document.add(getKeyValueParagraph("Project name: ", assessment.getProjectName()));
            document.add(getKeyValueParagraph("Project description: ", assessment.getProjectDescription()));
            document.add(Chunk.NEWLINE);
            document.add(getKeyValueParagraph("Created by: ", assessment.getSystemUser().getName()));
            document.add(getKeyValueParagraph("Creation date: ", assessment.getSystemUser().getCreationDate().toLocalDate().toString()));
            document.add(Chunk.NEWLINE);

            /////////GOALS
            Anchor anchor = new Anchor("GOALS", catFont);
            anchor.setName("GOALS");

            // Second parameter is the number of the chapter
            Chapter catPart = new Chapter(new Paragraph(anchor), 1);

            Paragraph subPara = new Paragraph("Usability attributes goals that will be evaluated in this assessment", subFont);
            Section subCatPart;
            subCatPart = catPart.addSection(subPara);
            for (UsabilityGoal usabilityGoal : assessment.getUsabilityGoals()) {
                if (StringUtils.isNotEmpty(usabilityGoal.getGoal()))
                    subCatPart.add(new Paragraph(PARAGRAPH_SPACE + usabilityGoal.getAttribute().getDescription() + ": " + usabilityGoal.getGoal()));
            }
            addEmptyLine(subPara, 2);
            subPara = new Paragraph("Smart city factor", subFont);
            subCatPart = catPart.addSection(subPara);
            subCatPart.add(new Paragraph(getKeyValueParagraph(PARAGRAPH_SPACE + "Percentage result: ", assessment.getSmartCityPercentage().toString() + "%")));
            addEmptyLine(subPara, 1);
            addEmptyLine(subCatPart, 1);
            PdfPTable table = new PdfPTable(2);
            addTableHeader(table, new String[]{"Smart City Functional Requirement", "Answer"});
            addRows(table, SmartCityAttribute.AEE.getDescription(), assessment.getSmartCityQuestionnaire()
                    .getHasAppExecution() ? "YES" : "NO");
            addRows(table, SmartCityAttribute.DCM.getDescription(), assessment.getSmartCityQuestionnaire()
                    .getDefineCityModel() ? "YES" : "NO");
            addRows(table, SmartCityAttribute.DMN.getDescription(), assessment.getSmartCityQuestionnaire()
                    .getHasDataManagement() ? "YES" : "NO");
            addRows(table, SmartCityAttribute.DPR.getDescription(), assessment.getSmartCityQuestionnaire()
                    .getHasDataProcessing() ? "YES" : "NO");
            addRows(table, SmartCityAttribute.DTA.getDescription(), assessment.getSmartCityQuestionnaire()
                    .getHasDataAccess() ? "YES" : "NO");
            addRows(table, SmartCityAttribute.SMN.getDescription(), assessment.getSmartCityQuestionnaire()
                    .getHasServiceManagement() ? "YES" : "NO");
            addRows(table, SmartCityAttribute.TSD.getDescription(), assessment.getSmartCityQuestionnaire()
                    .getHasSoftwareTools() ? "YES" : "NO");
            addRows(table, SmartCityAttribute.SNM.getDescription(), assessment.getSmartCityQuestionnaire()
                    .getHasSensorNetwork() ? "YES" : "NO");
            subCatPart.add(table);

            Paragraph paragraph = new Paragraph();
            addEmptyLine(paragraph, 1);
            subCatPart.add(paragraph);

            // now add all this to the document
            document.add(catPart);

            ////////////VARIABLES AND MEASUREMENT
            document.newPage();

            anchor = new Anchor("VARIABLES AND MEASUREMENT", catFont);
            anchor.setName("VARIABLES AND MEASUREMENT");

            catPart = new Chapter(new Paragraph(anchor), 2);
            subPara = new Paragraph("Usability attributes variables to be measured", subFont);
            for (Attribute attribute : assessment.getAttributes()) {
                if (StringUtils.isNotEmpty(attribute.getVariables())) {
                    subCatPart = catPart.addSection(subPara);
                    subCatPart.add(new Paragraph(PARAGRAPH_SPACE + attribute.getUsabilityAttribute().getDescription() + " variable(s): ", smallBold));
                    subCatPart.add(new Paragraph(PARAGRAPH_SPACE + PARAGRAPH_SPACE + attribute.getVariables()));
                    addEmptyLine(subCatPart, 1);
                    subCatPart.add(new Paragraph(PARAGRAPH_SPACE + "How will the " +
                            attribute.getUsabilityAttribute().getDescription() +
                            " variables be obtained (methods and criteria for measuring description): ", smallBold));
                    subCatPart.add(new Paragraph(PARAGRAPH_SPACE + PARAGRAPH_SPACE + attribute.getObtainedBy()));
                    addEmptyLine(subCatPart, 1);
                }
            }
            subPara = new Paragraph(PARAGRAPH_SPACE + "Which scales will be used: ", subFont);
            subCatPart = catPart.addSection(subPara);
            var scaleList = new com.itextpdf.text.List();
            assessment.getScale().forEach(s -> scaleList.add(new ListItem(PARAGRAPH_SPACE + PARAGRAPH_SPACE + s.getName() +
                    " (" + s.getAcronym().toString() + ")")));
            subCatPart.add(scaleList);
            addEmptyLine(subCatPart, 2);
            document.add(catPart);

            document.newPage();

            anchor = new Anchor("SUGGESTED SCALES FOR USABILITY ASSESSMENT", catFont);
            anchor.setName("SUGGESTED SCALES FOR USABILITY ASSESSMENT");

            catPart = new Chapter(new Paragraph(anchor), 3);
            addEmptyLine(catPart, 2);
            for (Scale scale : assessment.getScale()) {
                document.newPage();
                try {
                    if (!scale.getAcronym().equals(ScalesEnum.SUMI)) {
                        var title = new Paragraph(new Phrase(scale.getName() +
                                " (" + scale.getAcronym().toString() + ")", smallBold));
                        title.setAlignment(Element.ALIGN_CENTER);
                        catPart.add(title);
                        catPart.add(new Phrase(scale.getName()));
                        Path path = Paths.get("src/main/resources/scales-image/" +
                                scale.getAcronym() + "Scale.jpg");
                        Image img = Image.getInstance(path.toAbsolutePath().toString());
                        img.scaleToFit(PageSize.A4);
                        img.setAlignment(Element.ALIGN_CENTER);
                        catPart.add(img);
                    } else {
                        var title = new Paragraph(new Phrase(scale.getName() +
                                " (" + scale.getAcronym().toString() + ")", smallBold));
                        title.setAlignment(Element.ALIGN_CENTER);
                        catPart.add(title);
                        Path path = Paths.get("src/main/resources/scales-image/" +
                                scale.getAcronym() + "Scale1.jpg");
                        Image firstImg = Image.getInstance(path.toAbsolutePath().toString());
                        firstImg.scaleToFit(PageSize.A4);
                        firstImg.setAlignment(Element.ALIGN_CENTER);
                        catPart.add(firstImg);
                        path = Paths.get("src/main/resources/scales-image/" +
                                scale.getAcronym() + "Scale2.jpg");
                        Image secondImg = Image.getInstance(path.toAbsolutePath().toString());
                        secondImg.scaleToFit(PageSize.A4);
                        secondImg.setAlignment(Element.ALIGN_CENTER);
                        catPart.add(secondImg);
                    }
                } catch (IOException | DocumentException e) {
                    e.printStackTrace();
                }
            }
            document.add(catPart);

            //////////// PARTICIPANTS
            document.newPage();

            anchor = new Anchor("PARTICIPANTS", catFont);
            anchor.setName("PARTICIPANTS");

            catPart = new Chapter(new Paragraph(anchor), 4);
            subPara = new Paragraph("Participants information", subFont);


            subCatPart = catPart.addSection(subPara);
            PdfPTable participantTable = new PdfPTable(2);
            addTableHeader(participantTable, new String[]{"Question", "Answer"});
            addRows(participantTable, "Number of participants:", assessment.getParticipant()
                    .getParticipantsQuantity().toString());
            addRows(participantTable, "Participate method", assessment.getParticipant()
                    .getParticipationLocalType().getDescription().toUpperCase());
            addRows(participantTable, "Participants will be compensated", assessment.getParticipant()
                    .getHasCompensation() ? "YES" : "NO");
            addEmptyLine(subCatPart, 1);
            subCatPart.add(participantTable);
            addEmptyLine(subCatPart, 1);
            if (assessment.getParticipant().getHasCompensation()) {
                subCatPart.add(new Paragraph(PARAGRAPH_SPACE + "Form of compensation:", smallBold));
                subCatPart.add(new Paragraph(assessment.getParticipant().getCompensationDescription()));
            }
            subCatPart.add(new Paragraph(PARAGRAPH_SPACE + "Eligibility criteria: ", smallBold));
            subCatPart.add(new Paragraph(PARAGRAPH_SPACE + PARAGRAPH_SPACE + assessment.getParticipant().getCriteria()));
            addEmptyLine(subCatPart, 1);
            subCatPart.add(new Paragraph(PARAGRAPH_SPACE + "Will have use of demographic questionnaire to collect information from the participants:  ", smallBold));
            subCatPart.add(new Paragraph(PARAGRAPH_SPACE + PARAGRAPH_SPACE + (assessment.getParticipant().getHasCollectedInformation() ? "YES" : "NO")));
            addEmptyLine(subCatPart, 1);

            if (assessment.getParticipant().getHasCollectedInformation()) {
                subCatPart.add(new Paragraph(PARAGRAPH_SPACE + "How the data will be used: ", smallBold));
                subCatPart.add(new Paragraph(PARAGRAPH_SPACE + PARAGRAPH_SPACE + assessment.getParticipant()
                        .getCollectedInformationUse()));
                addEmptyLine(subCatPart, 1);
            }
            subCatPart.add(new Paragraph(PARAGRAPH_SPACE + "How will the participants be instructed:", smallBold));
            subCatPart.add(new Paragraph(PARAGRAPH_SPACE + PARAGRAPH_SPACE + assessment.getParticipant()
                    .getInstructions()));
            addEmptyLine(subCatPart, 1);
            subPara = new Paragraph("Questions to be asked to the participants", subFont);
            subCatPart = catPart.addSection(subPara);
            var questionList = new com.itextpdf.text.List();
            assessment.getParticipant().getQuestions().forEach(s -> questionList.add(new ListItem(s)));
            subCatPart.add(questionList);

            document.add(catPart);

            //////////// TASKS AND MATERIALS
            document.newPage();

            anchor = new Anchor("TASKS AND MATERIALS", catFont);
            anchor.setName("TASKS AND MATERIALS");

            catPart = new Chapter(new Paragraph(anchor), 5);
            subPara = new Paragraph("Instruments, materials, technology and tools that will be used", subFont);
            subCatPart = catPart.addSection(subPara);
            var toolList = new com.itextpdf.text.List();
            assessment.getAssessmentTools().getTools().forEach(s -> toolList.add(new ListItem(s)));
            subCatPart.add(toolList);
            subCatPart.add(new Paragraph(PARAGRAPH_SPACE + "Tools usage:", subFont));
            subCatPart.add(new Paragraph(PARAGRAPH_SPACE + PARAGRAPH_SPACE + assessment.getAssessmentTools().getToolsUsageDescription()));
            addEmptyLine(subCatPart, 1);
            subPara = new Paragraph("Task to be performed by participants", subFont);
            subCatPart = catPart.addSection(subPara);
            addEmptyLine(subCatPart, 1);
            PdfPTable tasksTable = new PdfPTable(3);
            addTableHeader(tasksTable, new String[]{"Task Description", "Execution Time", "Acceptance Criteria"});
            assessment.getAssessmentTools().getTasks().forEach(task ->
                    addRows(tasksTable, task.getDescription(), task.getTaskExecutionTime(), task.getAcceptanceCriteria()));
            addEmptyLine(subCatPart, 1);
            subCatPart.add(tasksTable);
            addEmptyLine(subCatPart, 1);

            document.add(catPart);

            //////////// PROCEDURE
            document.newPage();

            anchor = new Anchor("PROCEDURE", catFont);
            anchor.setName("PROCEDURE");

            catPart = new Chapter(new Paragraph(anchor), 6);
            subPara = new Paragraph("Procedure Guide", subFont);
            subCatPart = catPart.addSection(subPara);
            subCatPart.add(new Paragraph(PARAGRAPH_SPACE + "When it will take place", smallBold));
            subCatPart.add(new Paragraph(PARAGRAPH_SPACE + PARAGRAPH_SPACE + assessment.getAssessmentProcedure().getOccurDate().toString()));
            addEmptyLine(subCatPart, 1);
            subCatPart.add(new Paragraph(PARAGRAPH_SPACE + "Where it will take place", smallBold));
            subCatPart.add(new Paragraph(PARAGRAPH_SPACE + PARAGRAPH_SPACE + assessment.getAssessmentProcedure().getOccurLocal()));
            addEmptyLine(subCatPart, 1);
            subCatPart.add(new Paragraph(PARAGRAPH_SPACE + "How it will occur", smallBold));
            subCatPart.add(new Paragraph(PARAGRAPH_SPACE + PARAGRAPH_SPACE + assessment.getAssessmentProcedure().getOccurDetail()));
            addEmptyLine(subCatPart, 1);
            subCatPart.add(new Paragraph(PARAGRAPH_SPACE + "How long will it take", smallBold));
            subCatPart.add(new Paragraph(PARAGRAPH_SPACE + PARAGRAPH_SPACE + (NumberUtils.isCreatable(assessment.getAssessmentProcedure().getOccurTime()) ?
                    assessment.getAssessmentProcedure().getOccurTime() + " minutes" : assessment.getAssessmentProcedure().getOccurTime())));
            addEmptyLine(subCatPart, 1);
            subPara = new Paragraph("Design of the assessment", subFont);
            subCatPart = catPart.addSection(subPara);
            addEmptyLine(subCatPart, 1);

            PdfPTable stepsTable = new PdfPTable(2);
            addTableHeader(stepsTable, new String[]{"Step Name", "Step Description"});
            assessment.getAssessmentProcedure().getAssessmentProcedureSteps().forEach(step ->
                    addRows(stepsTable, step.getName(), step.getDescription()));
            addEmptyLine(subCatPart, 1);
            subCatPart.add(stepsTable);
            addEmptyLine(subCatPart, 1);
            subPara = new Paragraph("Participants will be able to ask questions", subFont);
            subCatPart = catPart.addSection(subPara);
            subCatPart.add(new Paragraph(PARAGRAPH_SPACE +
                    (assessment.getAssessmentProcedure().getQuestionsAllowed() ? "YES" : "NO")));
            addEmptyLine(subCatPart, 1);
            subPara = new Paragraph("Will there be a pilot assessment?", subFont);
            subCatPart = catPart.addSection(subPara);
            subCatPart.add(new Paragraph(PARAGRAPH_SPACE +
                    (assessment.getAssessmentProcedure().getIsPilotAssessment() ? "YES" : "NO")));
            addEmptyLine(subCatPart, 1);
            if (assessment.getAssessmentProcedure().getIsPilotAssessment()) {
                subCatPart.add(new Paragraph(PARAGRAPH_SPACE + "How the pilot will be conducted", subFont));
                subCatPart.add(new Paragraph(PARAGRAPH_SPACE + PARAGRAPH_SPACE + assessment.getAssessmentProcedure()
                        .getPilotDescription()));
            }
            document.add(catPart);

            //////////// DATA COLLECTION AND DATA ANALYSIS
            document.newPage();

            anchor = new Anchor("DATA COLLECTION AND DATA ANALYSIS", catFont);
            anchor.setName("DATA COLLECTION AND DATA ANALYSIS");

            catPart = new Chapter(new Paragraph(anchor), 7);
            subPara = new Paragraph("How will the data be collected", subFont);
            subCatPart = catPart.addSection(subPara);
            subCatPart.add(new Paragraph(PARAGRAPH_SPACE + assessment.getAssessmentData().getDataCollectionProcedure()));
            addEmptyLine(subCatPart, 1);
            subPara = new Paragraph("How will the data collected be analyzed", subFont);
            subCatPart = catPart.addSection(subPara);
            subCatPart.add(new Paragraph(PARAGRAPH_SPACE + assessment.getAssessmentData().getAnalysisDescription()));
            addEmptyLine(subCatPart, 1);
            subPara = new Paragraph("Will statistical methods be used?", subFont);
            subCatPart = catPart.addSection(subPara);
            subCatPart.add(new Paragraph(PARAGRAPH_SPACE + (assessment.getAssessmentData().getStatisticalMethods() ? "YES" : "NO")));
            addEmptyLine(subCatPart, 1);
            if (assessment.getAssessmentData().getStatisticalMethods()) {
                subCatPart.add(new Paragraph(PARAGRAPH_SPACE + "Statistical methods use description", subFont));
                subCatPart.add(new Paragraph(PARAGRAPH_SPACE + PARAGRAPH_SPACE + "- " + assessment.getAssessmentData()
                        .getStatisticalMethodsDescription()));
                addEmptyLine(subCatPart, 1);
            }
            document.add(catPart);

            //////////// THREATS TO VALIDITY
            document.newPage();

            anchor = new Anchor("THREATS TO VALIDITY", catFont);
            anchor.setName("THREATS TO VALIDITY");

            catPart = new Chapter(new Paragraph(anchor), 8);
            subPara = new Paragraph("Are there any threats to the validity of the assessment?", subFont);
            subCatPart = catPart.addSection(subPara);
            subCatPart.add(new Paragraph(PARAGRAPH_SPACE +
                    (CollectionUtils.isNullOrEmpty(assessment.getAssessmentThreat().getThreats()) ? "NO" : "YES")));
            addEmptyLine(subCatPart, 1);
            if (!CollectionUtils.isNullOrEmpty(assessment.getAssessmentThreat().getThreats())){
                subPara = new Paragraph("What are the threats to the validity of the assessment?", subFont);
                subCatPart = catPart.addSection(subPara);
                var threatList = new com.itextpdf.text.List();
                assessment.getAssessmentThreat().getThreats().forEach(s -> threatList.add(new ListItem(s)));
                subCatPart.add(threatList);
                addEmptyLine(subCatPart, 1);
            }
            subPara = new Paragraph("How will the threats to validity be controlled?", subFont);
            subCatPart = catPart.addSection(subPara);
            subCatPart.add(new Paragraph(PARAGRAPH_SPACE +
                    (CollectionUtils.isNullOrEmpty(assessment.getAssessmentThreat().getThreats()) ? "NO" : "YES")));
            addEmptyLine(subCatPart, 1);

            if (!CollectionUtils.isNullOrEmpty(assessment.getAssessmentThreat().getLimitations())){
                subPara = new Paragraph("Assessment limitations:", subFont);
                subCatPart = catPart.addSection(subPara);
                var limitationList = new com.itextpdf.text.List();
                assessment.getAssessmentThreat().getLimitations().forEach(s -> limitationList.add(new ListItem(s)));
                subCatPart.add(limitationList);
                addEmptyLine(subCatPart, 1);
            }
            subPara = new Paragraph("Are the ethical aspects of the assessment well defined for the participants?", subFont);
            subCatPart = catPart.addSection(subPara);
            subCatPart.add(new Paragraph(PARAGRAPH_SPACE +
                    (assessment.getAssessmentThreat().getEthicalAspectsDefined() ? "YES" : "NO")));
            addEmptyLine(subCatPart, 1);
            if (assessment.getAssessmentThreat().getEthicalAspectsDefined()) {
                subCatPart.add(new Paragraph(PARAGRAPH_SPACE + "Ethical aspects definition", subFont));
                subCatPart.add(new Paragraph(PARAGRAPH_SPACE + PARAGRAPH_SPACE + "- " + assessment.getAssessmentThreat()
                        .getEthicalAspectsDescription()));
                addEmptyLine(subCatPart, 1);
            }
            subPara = new Paragraph("Bias of the assessment", subFont);
            subCatPart = catPart.addSection(subPara);
            subCatPart.add(new Paragraph(PARAGRAPH_SPACE +
                    assessment.getAssessmentThreat().getBiasDescription()));
            addEmptyLine(subCatPart, 1);

            document.add(catPart);

            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);

        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        }
        document.close();

        return baos;
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    private static void addEmptyLine(Section section, int number) {
        for (int i = 0; i < number; i++) {
            section.add(new Paragraph(" "));
        }
    }

    private static PdfPTable buildAnswerTable(Object obj, Document document) throws DocumentException {
        document.add(Chunk.NEWLINE);
        PdfPTable table = null;
        List<LinkedTreeMap<String, String>> answerTable = (ArrayList<LinkedTreeMap<String, String>>) obj;

        if (answerTable.size() > 0) {
            // each item is a table row <tr>
            for (LinkedTreeMap<String, String> item : answerTable) {
                Set<Map.Entry<String, String>> entries = item.entrySet();

                if (table == null) {
                    table = new PdfPTable(entries.size());
                }

                // each entry is a table data <td>
                for (Map.Entry<String, String> entry : entries) {
                    String value = entry.getValue();

                    buildTableCell(table, value);
                }
            }
        }

        return table;
    }

    private static void addTableHeader(PdfPTable table, String[] titles) {
        Stream.of(titles)
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

    private static void addRows(PdfPTable table, String firstRow, String secondRow) {
        PdfPCell firstCell = new PdfPCell(new Phrase(firstRow));
        firstCell.setBackgroundColor(BaseColor.YELLOW);
        firstCell.setVerticalAlignment(Element.ALIGN_CENTER);
        firstCell.setBorderWidth(2);
        table.addCell(firstCell);
        PdfPCell answerCell = new PdfPCell(new Phrase(secondRow));
        answerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(answerCell);
    }

    private static void addRows(PdfPTable table, String firstRow, String secondRow, String thirdRow) {
        PdfPCell firstCell = new PdfPCell(new Phrase(firstRow));
        firstCell.setBackgroundColor(BaseColor.YELLOW);
        firstCell.setVerticalAlignment(Element.ALIGN_CENTER);
        firstCell.setBorderWidth(2);
        table.addCell(firstCell);
        PdfPCell secondCell = new PdfPCell(new Phrase(secondRow));
        secondCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        secondCell.setVerticalAlignment(Element.ALIGN_CENTER);
        table.addCell(secondCell);
        PdfPCell thirdCell = new PdfPCell(new Phrase(thirdRow));
        thirdCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        thirdCell.setVerticalAlignment(Element.ALIGN_CENTER);
        table.addCell(thirdCell);
    }

    private void addCustomRows(PdfPTable table)
            throws URISyntaxException, BadElementException, IOException {
        Path path = Paths.get(ClassLoader.getSystemResource("Java_logo.png").toURI());
        Image img = Image.getInstance(path.toAbsolutePath().toString());
        img.scalePercent(10);

        PdfPCell imageCell = new PdfPCell(img);
        table.addCell(imageCell);

        PdfPCell horizontalAlignCell = new PdfPCell(new Phrase("row 2, col 2"));
        horizontalAlignCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(horizontalAlignCell);

        PdfPCell verticalAlignCell = new PdfPCell(new Phrase("row 2, col 3"));
        verticalAlignCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        table.addCell(verticalAlignCell);
    }

    private static void buildTableCell(PdfPTable table, String value) {
        PdfPCell cell = new PdfPCell(new Phrase(value.length() != 0 ? value : " "));
        table.addCell(cell);
    }

}

package com.unicap.tcc.usability.api.utils;

import com.amazonaws.util.CollectionUtils;
import com.google.gson.internal.LinkedTreeMap;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.unicap.tcc.usability.api.models.assessment.*;
import com.unicap.tcc.usability.api.models.enums.ParticipationType;
import com.unicap.tcc.usability.api.models.enums.SmartCityAttribute;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PdfGenerator {

    public static final String THREATS_SECTION_KEY = "tv";
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);

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

    public static ByteArrayOutputStream generatePlanReport(Assessment assessment) {
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
            document.newPage();

            /////////GOALS
            Anchor anchor = new Anchor("GOALS", catFont);
            anchor.setName("GOALS");

            // Second parameter is the number of the chapter
            Chapter catPart = new Chapter(new Paragraph(anchor), 1);
//            document.add(getBoldParagraph("Usability attributes goals that will be evaluated in this assessment", 14));

            Paragraph subPara = new Paragraph("Usability attributes goals that will be evaluated in this assessment", subFont);
            Section subCatPart;
            for (UsabilityGoal usabilityGoal : assessment.getUsabilityGoals()) {
                subCatPart = catPart.addSection(subPara);
                subCatPart.add(new Paragraph("   " + usabilityGoal.getAttribute().getDescription() + ": " + usabilityGoal.getGoal()));
                addEmptyLine(subPara, 1);
            }

            subPara = new Paragraph("Smart city factor", subFont);
            subCatPart = catPart.addSection(subPara);
            addEmptyLine(subPara, 1);
            subCatPart.add(new Paragraph(getKeyValueParagraph("Percentage result: ", assessment.getSmartCityPercentage().toString() + "%")));
            PdfPTable table = new PdfPTable(2);

            PdfPCell c1 = new PdfPCell(new Phrase("Funcional Requirement"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("Answer"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
            table.setHeaderRows(1);

            table.addCell(SmartCityAttribute.AEE.getDescription());
            table.addCell(assessment.getSmartCityQuestionnaire().getHasAppExecution() ? "YES" : "NO");
            table.addCell(SmartCityAttribute.DCM.getDescription());
            table.addCell(assessment.getSmartCityQuestionnaire().getDefineCityModel() ? "YES" : "NO");
            table.addCell(SmartCityAttribute.DMN.getDescription());
            table.addCell(assessment.getSmartCityQuestionnaire().getHasDataManagement() ? "YES" : "NO");
            table.addCell(SmartCityAttribute.DPR.getDescription());
            table.addCell(assessment.getSmartCityQuestionnaire().getHasDataProcessing() ? "YES" : "NO");
            table.addCell(SmartCityAttribute.DTA.getDescription());
            table.addCell(assessment.getSmartCityQuestionnaire().getHasDataAccess() ? "YES" : "NO");
            table.addCell(SmartCityAttribute.SMN.getDescription());
            table.addCell(assessment.getSmartCityQuestionnaire().getHasServiceManagement() ? "YES" : "NO");
            table.addCell(SmartCityAttribute.TSD.getDescription());
            table.addCell(assessment.getSmartCityQuestionnaire().getHasSoftwareTools() ? "YES" : "NO");
            table.addCell(SmartCityAttribute.SNM.getDescription());
            table.addCell(assessment.getSmartCityQuestionnaire().getHasSensorNetwork() ? "YES" : "NO");
            subCatPart = catPart.addSection(subPara);


            Paragraph paragraph = new Paragraph();
            addEmptyLine(paragraph, 1);
            subCatPart.add(paragraph);

            // add a table

            // now add all this to the document
            document.add(catPart);

            ////////////VARIABLES AND MEASUREMENT
            document.newPage();

            anchor = new Anchor("VARIABLES AND MEASUREMENT", catFont);
            anchor.setName("VARIABLES AND MEASUREMENT");

            catPart = new Chapter(new Paragraph(anchor), 2);
            subPara = new Paragraph("Usability attributes variables to be measured", subFont);

            for (AttributeVariable attributeVariable : assessment.getAttributeVariables()) {
                subCatPart = catPart.addSection(subPara);
                subCatPart.add(new Paragraph(" " + attributeVariable.getUsabilityAttribute().getDescription() + ": "));
                var list = new com.itextpdf.text.List();
                attributeVariable.getVariableList().forEach(s -> list.add(new ListItem(s)));
                subCatPart.add(list);
                addEmptyLine(subPara, 1);
                subCatPart.add(new Paragraph(" How will the variables be obtained: "));
                subCatPart.add(new Paragraph(attributeVariable.getObtainedBy()));
                addEmptyLine(subPara, 1);
                subCatPart.add(new Paragraph(" Methods and criteria for measuring these variables: "));
                subCatPart.add(new Paragraph(attributeVariable.getMethods()));
                addEmptyLine(subPara, 1);
                subCatPart.add(new Paragraph(" Methods and criteria for measuring these variables: "));
                subCatPart.add(new Paragraph(attributeVariable.getMethods()));
                addEmptyLine(subPara, 1);
                subCatPart.add(new Paragraph(" Which scale will be used: "));
                subCatPart.add(new Paragraph(attributeVariable.getScale().getName() +
                        " (" + attributeVariable.getScale().getAcronym() + ")"));
            }
            document.add(catPart);

            //////////// PARTICIPANTS
            document.newPage();

            anchor = new Anchor("PARTICIPANTS", catFont);
            anchor.setName("PARTICIPANTS");

            catPart = new Chapter(new Paragraph(anchor), 3);
            subPara = new Paragraph("Participants information", subFont);

            subCatPart = catPart.addSection(subPara);
            addEmptyLine(subPara, 1);
            subCatPart.add(getKeyValueParagraph(" Number of participants: ", assessment.getParticipant()
                    .getParticipantsQuantity().toString()));
            subCatPart.add(getKeyValueParagraph(" Participate method: ", assessment.getParticipant()
                    .getParticipationLocalType().getDescription().toUpperCase()));
            subCatPart.add(getKeyValueParagraph(" Participation type: ", assessment.getParticipant()
                    .getParticipationType().getDescription().toUpperCase()));
            if (assessment.getParticipant().getParticipationType().equals(ParticipationType.C)) {
                subCatPart.add(getKeyValueParagraph("  Form of compensation: ", assessment.getParticipant()
                        .getCompensationDescription()));
            }
            subCatPart.add(new Paragraph(" Eligibility criteria: "));
            var criteriaList = new com.itextpdf.text.List();
            assessment.getParticipant().getCriteriaList().forEach(s -> criteriaList.add(new ListItem(s)));
            subCatPart.add(criteriaList);
            subCatPart.add(getKeyValueParagraph(" Use of demographic questionnaire to collect information from the participants: ",
                    assessment.getParticipant().getHasCollectedInformation() ? "YES" : "NO"));
            if (assessment.getParticipant().getHasCollectedInformation()) {
                subCatPart.add(getKeyValueParagraph("  How the data will be used: ", assessment.getParticipant()
                        .getCollectedInformationUse()));
            }
            subCatPart.add(getKeyValueParagraph(" How will the participants be instructed: ", assessment.getParticipant()
                    .getInstructions()));
            subCatPart.add(new Paragraph(" Questions to be asked to the participants: "));
            var questionList = new com.itextpdf.text.List();
            assessment.getParticipant().getQuestions().forEach(s -> questionList.add(new ListItem(s)));
            subCatPart.add(questionList);

            document.add(catPart);

            //////////// TASKS AND MATERIALS
            document.newPage();

            anchor = new Anchor("TASKS AND MATERIALS", catFont);
            anchor.setName("TASKS AND MATERIALS");

            catPart = new Chapter(new Paragraph(anchor), 4);
            subPara = new Paragraph("Task and materials information", subFont);

            subCatPart = catPart.addSection(subPara);
            addEmptyLine(subPara, 1);
            subCatPart.add(new Paragraph(" Instruments, materials, technology and tools that will be used: "));
            var toolList = new com.itextpdf.text.List();
            assessment.getAssessmentTools().getTools().forEach(s -> toolList.add(new ListItem(s)));
            subCatPart.add(toolList);
            subCatPart.add(getKeyValueParagraph(" Tools usage: ", assessment.getAssessmentTools().getToolsUsageDescription()));
            subCatPart.add(new Paragraph(" Tasks to be performed by participants ", smallBold));
            for (Task task : assessment.getAssessmentTools().getTasks()) {
                subCatPart.add(getKeyValueParagraph(" Task description: ", task.getDescription()));
                subCatPart.add(getKeyValueParagraph(" Task execution time: ", task.getTaskExecutionTime().toString() + " minutes"));
                subCatPart.add(getKeyValueParagraph(" Task criteria: ", task.getAcceptanceCriteria()));
                addEmptyLine(subPara, 1);
            }

            document.add(catPart);

            //////////// PROCEDURE
            document.newPage();

            anchor = new Anchor("PROCEDURE", catFont);
            anchor.setName("PROCEDURE");

            catPart = new Chapter(new Paragraph(anchor), 5);
            subPara = new Paragraph("Procedure guide", subFont);

            subCatPart = catPart.addSection(subPara);
            addEmptyLine(subPara, 1);
            subCatPart.add(getKeyValueParagraph(" Time when it will take place: ", assessment.getAssessmentProcedure().getOccurDate().toString()));
            subCatPart.add(getKeyValueParagraph(" Place where it will take place: ", assessment.getAssessmentProcedure().getOccurLocal()));
            subCatPart.add(getKeyValueParagraph(" How it will occur: ", assessment.getAssessmentProcedure().getOccurDetail()));
            subCatPart.add(getKeyValueParagraph(" How long will it take: ", assessment.getAssessmentProcedure().getOccurTime().toString() + " minutes"));
            subCatPart.add(new Paragraph(" Design of the assessment ", smallBold));
            for (AssessmentProcedureStep assessmentProcedureStep : assessment.getAssessmentProcedure().getAssessmentProcedureSteps()) {
                subCatPart.add(getKeyValueParagraph(" Step name: ", assessmentProcedureStep.getName()));
                subCatPart.add(getKeyValueParagraph(" Step description: ", assessmentProcedureStep.getDescription()));
                addEmptyLine(subPara, 1);
            }
            subCatPart.add(getKeyValueParagraph(" There will be training for participants: ",
                    assessment.getAssessmentProcedure().getWillHaveTraining() ? "YES" : "NO"));
            subCatPart.add(getKeyValueParagraph(" Will there be a pilot assessment? ",
                    assessment.getAssessmentProcedure().getIsPilotAssessment() ? "YES" : "NO"));
            if (assessment.getAssessmentProcedure().getIsPilotAssessment()) {
                subCatPart.add(getKeyValueParagraph("  How the pilot will be conducted: ", assessment.getAssessmentProcedure()
                        .getPilotDescription()));
            }
            document.add(catPart);

            //////////// DATA COLLECTION AND DATA ANALYSIS
            document.newPage();

            anchor = new Anchor("DATA COLLECTION AND DATA ANALYSIS", catFont);
            anchor.setName("DATA COLLECTION AND DATA ANALYSIS");

            catPart = new Chapter(new Paragraph(anchor), 6);
            subPara = new Paragraph("Data collection and usage information", subFont);

            subCatPart = catPart.addSection(subPara);
            addEmptyLine(subPara, 1);
            subCatPart.add(getKeyValueParagraph(" How will the data be collected: ", assessment.getAssessmentData().getDataCollectionProcedure()));
            subCatPart.add(getKeyValueParagraph(" Participants will be able to ask questions: ",
                    assessment.getAssessmentData().getQuestionsAllowed() ? "YES" : "NO"));
            subCatPart.add(getKeyValueParagraph(" How will the data collected be analyzed: ", assessment.getAssessmentData().getAnalysisDescription()));
            subCatPart.add(getKeyValueParagraph(" Will statistical methods be used? ",
                    assessment.getAssessmentData().getStatisticalMethods() ? "YES" : "NO"));
            if (assessment.getAssessmentData().getStatisticalMethods()) {
                subCatPart.add(getKeyValueParagraph("  Statistical methods description: ", assessment.getAssessmentData()
                        .getStatisticalMethodsDescription()));
            }
            document.add(catPart);

            //////////// THREATS TO VALIDITY
            document.newPage();

            anchor = new Anchor("THREATS TO VALIDITY", catFont);
            anchor.setName("THREATS TO VALIDITY");

            catPart = new Chapter(new Paragraph(anchor), 7);
            subPara = new Paragraph("Threats information", subFont);
            subCatPart = catPart.addSection(subPara);
            addEmptyLine(subPara, 1);

            subCatPart.add(new Paragraph(" Are there any threats to the validity of the assessment? ", smallBold));
            if (CollectionUtils.isNullOrEmpty(assessment.getAssessmentThreat().getThreats())) {
                subCatPart.add(new Paragraph(" NO"));
            } else {
                subCatPart.add(new Paragraph(" YES"));
                addEmptyLine(subPara, 1);
                subCatPart.add(new Paragraph(" What are the threats to the validity of the assessment: "));
                var threatList = new com.itextpdf.text.List();
                assessment.getAssessmentThreat().getThreats().forEach(s -> threatList.add(new ListItem(s)));
                subCatPart.add(threatList);
                subCatPart.add(getKeyValueParagraph(" How will the threats to validity be controlled: ",
                        assessment.getAssessmentThreat().getControlMeasure()));
                subCatPart.add(new Paragraph(" Assessment limitations: "));
                if (CollectionUtils.isNullOrEmpty(assessment.getAssessmentThreat().getLimitations())) {
                    subCatPart.add(new Paragraph(" No limitations."));
                } else {
                    var limitationList = new com.itextpdf.text.List();
                    assessment.getAssessmentThreat().getLimitations().forEach(s -> limitationList.add(new ListItem(s)));
                    subCatPart.add(limitationList);
                }
                subCatPart.add(getKeyValueParagraph(" Are the ethical aspects of the assessment well defined for the participants? ",
                        assessment.getAssessmentThreat().getEthicalAspectsDefined() ? "YES" : "NO"));
                if (assessment.getAssessmentThreat().getEthicalAspectsDefined()) {
                    subCatPart.add(getKeyValueParagraph("  Ethical aspects definition: ", assessment.getAssessmentThreat()
                            .getEthicalAspectsDescription()));
                }
                subCatPart.add(getKeyValueParagraph(" Bias of the assessment: ",
                        assessment.getAssessmentThreat().getBiasDescription()));
            }
            document.add(catPart);

//            for (InstrumentSection section : instrumentQuestions) {
//                document.add(getBoldParagraph(section.getSection(), 12));
//                document.add(Chunk.NEWLINE);

//                for (InstrumentQuestion question : section.getQuestions()) {
//                    String questionStatement =
//                            String.format("%d. %s", questionNumber, question.getTitle());
//                    document.add(getItalicParagraph(questionStatement, 12));
//
//                    Object auxObject = detailsMap.get(question.getProjectKey());
//
//                    if (auxObject instanceof String) {
//                        String statement = auxObject.toString();
//                        document.add(getSingleParagraph(statement));
//                    } else if (auxObject instanceof LinkedTreeMap) {
//                        // if hits here, the field has text and tables
//                        LinkedTreeMap<String, Object> auxMap
//                                = (LinkedTreeMap<String, Object>) auxObject;
//
//                        // write text field
//                        String textField = (String) auxMap.get("text");
//                        document.add(getSingleParagraph(textField));
//
//                        // draw table
//                        Object tableObject = auxMap.get("table");
//                        if (tableObject != null) {
//                            PdfPTable table = buildAnswerTable(tableObject, document);
//                            document.add(table);
//                        }
//
//                    } else {
//                        PdfPTable table = buildAnswerTable(auxObject, document);
//                        document.add(table);
//                    }
//
//                    document.add(Chunk.NEWLINE);
//
//                    if (section.getKey().equals(THREATS_SECTION_KEY)) {
//                        buildSuggestedThreats(document, groupedThreats);
//                    }
//
//                    questionNumber++;
//                }

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

    private static void buildTableCell(PdfPTable table, String value) {
        PdfPCell cell = new PdfPCell(new Phrase(value.length() != 0 ? value : " "));
        table.addCell(cell);
    }

}

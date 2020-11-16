package com.unicap.tcc.usability.api.service;


import com.unicap.tcc.usability.api.models.SmartCityQuestionnaire;
import com.unicap.tcc.usability.api.models.dto.SmartCityResponse;
import org.springframework.stereotype.Service;

@Service
public class AssessmentService {

    public SmartCityResponse calculateSmartCityPercentage(SmartCityQuestionnaire questionnaire) {
        var resultList = questionnaire.getListOfResults();
        var resultsQuantity = Long.valueOf(resultList.size());
        var positiveResults = Long.valueOf(resultList.stream().filter(aBoolean -> aBoolean.equals(true)).count());
        return SmartCityResponse.builder()
                .smartCityPercentage(resultsQuantity.doubleValue() / positiveResults.doubleValue())
                .build();
    }
}

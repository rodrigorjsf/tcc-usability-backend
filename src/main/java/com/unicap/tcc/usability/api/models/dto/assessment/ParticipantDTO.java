package com.unicap.tcc.usability.api.models.dto.assessment;

import com.unicap.tcc.usability.api.models.enums.ParticipationLocalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipantDTO{

    private Integer participantsQuantity;
    private ParticipationLocalType participationLocalType;
    private Boolean hasCompensation;
    private String compensationDescription;
    private List<String> criteriaList;
    private Boolean hasCollectedInformation;
    private String collectedInformationUse;
    private String instructions;
    private List<String> questions;

}

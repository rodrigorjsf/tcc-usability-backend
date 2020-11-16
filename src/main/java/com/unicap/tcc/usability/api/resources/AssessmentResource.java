package com.unicap.tcc.usability.api.resources;

import com.unicap.tcc.usability.api.models.SmartCityQuestionnaire;
import com.unicap.tcc.usability.api.models.dto.SmartCityResponse;
import com.unicap.tcc.usability.api.service.AssessmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/assessment")
@Api("Assessment API")
@RequiredArgsConstructor
public class AssessmentResource {

    private final AssessmentService assessmentService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Smart city percentage calculation.")
    @ApiResponse(code = 200, message = "Calculo realizado.")
    public ResponseEntity<SmartCityResponse> calculateSmartCityPercentage(@RequestBody @Valid SmartCityQuestionnaire questionnaire) {
        return ResponseEntity.ok()
                .body(assessmentService.calculateSmartCityPercentage(questionnaire));
    }
}

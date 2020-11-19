package com.unicap.tcc.usability.api.resources;

import com.unicap.tcc.usability.api.exception.ApiException;
import com.unicap.tcc.usability.api.models.SmartCityQuestionnaire;
import com.unicap.tcc.usability.api.models.assessment.Assessment;
import com.unicap.tcc.usability.api.models.constants.ApplicationConstants;
import com.unicap.tcc.usability.api.models.dto.assessment.AssessmentVariablesDTO;
import com.unicap.tcc.usability.api.models.dto.assessment.AssessmentCreationDTO;
import com.unicap.tcc.usability.api.models.dto.SmartCityResponse;
import com.unicap.tcc.usability.api.models.dto.assessment.CollaboratorDTO;
import com.unicap.tcc.usability.api.models.dto.assessment.UsabilityGoalDTO;
import com.unicap.tcc.usability.api.service.AssessmentService;
import com.unicap.tcc.usability.api.utils.UUIDUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Objects;

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

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Assessment creation.")
    @ApiResponse(code = 200, message = "Assessment created.")
    public ResponseEntity<Assessment> createAssessment(@RequestBody @Valid AssessmentCreationDTO assessmentCreationDTO) {
        Assessment assessment = assessmentService.createAssessment(assessmentCreationDTO);
        if (Objects.isNull(assessment)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(assessment);
    }

    @PostMapping("/add/collaborator")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Add collaborator to assessment.")
    @ApiResponse(code = 200, message = "Collaborator added.")
    public ResponseEntity<Assessment> addCollaborators(@RequestBody @Valid CollaboratorDTO collaboratorDTO) {
        Assessment assessment = assessmentService.addCollaborator(collaboratorDTO);
        if (Objects.isNull(assessment)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(assessment);
    }

    @PostMapping("/add/goals")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Add goals to assessment.")
    @ApiResponse(code = 200, message = "Goals Added.")
    public ResponseEntity<Assessment> addAssessmentGoals(@RequestBody @Valid UsabilityGoalDTO usabilityGoals) {
        Assessment assessment = assessmentService.addAssessmentGoals(usabilityGoals);
        if (Objects.isNull(assessment)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(assessment);
    }

//    @PostMapping("/add/attributes-variables")
//    @ResponseStatus(HttpStatus.OK)
//    @ApiOperation("Add atributes variables to assessment.")
//    @ApiResponse(code = 200, message = "Variables added.")
//    public ResponseEntity<Assessment> addAssessmentAttributeVariable(@RequestBody @Valid AssessmentVariablesDTO assessmentVariablesDTO) {
//        Assessment assessment = assessmentService.addAssessmentAttributeVariables(assessmentVariablesDTO);
//        if (Objects.isNull(assessment)) {
//            return ResponseEntity.badRequest().build();
//        }
//        return ResponseEntity.ok().body(assessment);
//    }

    @PostMapping("/add/participant")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Assessment creation.")
    @ApiResponse(code = 200, message = "Calculo realizado.")
    public ResponseEntity<Assessment> addAssessmentParticipant(@RequestBody @Valid AssessmentCreationDTO assessmentCreationDTO) {
        Assessment assessment = assessmentService.createAssessment(assessmentCreationDTO);
        if (Objects.isNull(assessment)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(assessment);
    }

    @PostMapping("/add/tools")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Assessment creation.")
    @ApiResponse(code = 200, message = "Calculo realizado.")
    public ResponseEntity<Assessment> addAssessmentTools(@RequestBody @Valid AssessmentCreationDTO assessmentCreationDTO) {
        Assessment assessment = assessmentService.createAssessment(assessmentCreationDTO);
        if (Objects.isNull(assessment)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(assessment);
    }

    @PostMapping("/add/procedure")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Assessment creation.")
    @ApiResponse(code = 200, message = "Calculo realizado.")
    public ResponseEntity<Assessment> addAssessmentProcedure(@RequestBody @Valid AssessmentCreationDTO assessmentCreationDTO) {
        Assessment assessment = assessmentService.createAssessment(assessmentCreationDTO);
        if (Objects.isNull(assessment)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(assessment);
    }

    @PostMapping("/add/data")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Assessment creation.")
    @ApiResponse(code = 200, message = "Calculo realizado.")
    public ResponseEntity<Assessment> addAssessmentData(@RequestBody @Valid AssessmentCreationDTO assessmentCreationDTO) {
        Assessment assessment = assessmentService.createAssessment(assessmentCreationDTO);
        if (Objects.isNull(assessment)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(assessment);
    }

    @PostMapping("/add/threats")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Assessment creation.")
    @ApiResponse(code = 200, message = "Calculo realizado.")
    public ResponseEntity<Assessment> addAssessmentThreats(@RequestBody @Valid AssessmentCreationDTO assessmentCreationDTO) {
        Assessment assessment = assessmentService.createAssessment(assessmentCreationDTO);
        if (Objects.isNull(assessment)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(assessment);
    }


    @GetMapping("/download-report/{id}")
    @Produces({ApplicationConstants.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM})
    public Response generateReport(@PathVariable @Pattern(regexp = UUIDUtils.UUID_REGEXP, message = "Invalid id") String id) throws ApiException {
        try {
            return assessmentService.getReportStreamingOutput(id);
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(Response.Status.INTERNAL_SERVER_ERROR,
                    "Internal server error. It was not possible to download this assessment");
        }
    }

}

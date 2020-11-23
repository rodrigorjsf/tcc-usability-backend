package com.unicap.tcc.usability.api.resources;

import com.unicap.tcc.usability.api.exception.ApiException;
import com.unicap.tcc.usability.api.models.Scale;
import com.unicap.tcc.usability.api.models.SmartCityQuestionnaire;
import com.unicap.tcc.usability.api.models.assessment.Assessment;
import com.unicap.tcc.usability.api.models.constants.ApplicationConstants;
import com.unicap.tcc.usability.api.models.dto.AssessmentListDTO;
import com.unicap.tcc.usability.api.models.dto.SmartCityResponse;
import com.unicap.tcc.usability.api.models.dto.assessment.AssessmentCreationDTO;
import com.unicap.tcc.usability.api.models.dto.assessment.CollaboratorDTO;
import com.unicap.tcc.usability.api.models.dto.assessment.SmartCityQuestionnaireDTO;
import com.unicap.tcc.usability.api.models.dto.assessment.UsabilityGoalDTO;
import com.unicap.tcc.usability.api.service.AssessmentService;
import com.unicap.tcc.usability.api.utils.UUIDUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/assessment")
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
    @ApiOperation("Assessment plan creation.")
    @ApiResponse(code = 200, message = "Assessment plan created.")
    public ResponseEntity<Assessment> createAssessment(@RequestBody @Valid AssessmentCreationDTO assessmentCreationDTO) {
        Assessment assessment = assessmentService.createAssessment(assessmentCreationDTO);
        if (Objects.isNull(assessment)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(assessment);
    }

    @GetMapping("/list/by-user-uid/{uid}")
    @ApiOperation("Search list of assessment plans.")
    public ResponseEntity<List<AssessmentListDTO>> findPlanListByUid(@PathVariable(value = "uid") @ApiParam("User uid") String uid) {
        var assessmentList = assessmentService.findUserAssessmentList(UUID.fromString(uid));
        if (assessmentList.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok().body(assessmentList);
    }

    @GetMapping("/by-uid/{uid}")
    @ApiOperation("Search list of assessment plans.")
    public ResponseEntity<Assessment> findAssessmentPlanByUid(@PathVariable(value = "uid") @ApiParam("Assessment plan uid") String uid) {
        Optional<Assessment> assessmentOptional = assessmentService.findAssessmentPlanByUid(UUID.fromString(uid));
        if (assessmentOptional.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok().body(assessmentOptional.get());
    }


    @PostMapping("/add/smartcity-questionnaire")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Add smartcity questionnaire to assessment plan.")
    @ApiResponse(code = 200, message = "Smartcity questionnaire added.")
    public ResponseEntity<Assessment> addSmartCityQuestionnaire(@RequestBody @Valid SmartCityQuestionnaireDTO questionnaire) {
        Assessment assessment = assessmentService.addSmartCityQuestionnaire(questionnaire);
        if (Objects.isNull(assessment)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(assessment);
    }

    @PostMapping("/add/collaborator")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Add collaborator to assessment plan.")
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

    @GetMapping("/scales")
    @ApiOperation("Get list of scales.")
    public ResponseEntity< List<Scale>> getScaleList() {
        List<Scale> scaleList = assessmentService.getScaleList();
        if (scaleList.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok().body(scaleList);
    }

    @PutMapping("/delete/{uid}")
    @ApiOperation("Delete assessment plan.")
    public ResponseEntity<Object> deleteAssessmentPlan(@Valid @PathVariable(value = "uid") @ApiParam("Assessment plan uid") String uid) {
        Optional<Assessment> assessmentOptional = assessmentService.deleteAssessmentPlan(UUID.fromString(uid));
        if (assessmentOptional.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok().build();
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

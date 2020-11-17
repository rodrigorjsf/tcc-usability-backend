package com.unicap.tcc.usability.api.resources;

import com.unicap.tcc.usability.api.exception.ApiException;
import com.unicap.tcc.usability.api.models.SmartCityQuestionnaire;
import com.unicap.tcc.usability.api.models.constants.ApplicationConstants;
import com.unicap.tcc.usability.api.models.dto.SmartCityResponse;
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
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

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

    @GET
    @Path("/download-report/{id}")
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

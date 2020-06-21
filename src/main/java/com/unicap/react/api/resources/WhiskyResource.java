package com.unicap.react.api.resources;

import com.unicap.react.api.models.Whisky;
import com.unicap.react.api.repository.WhiskyRepository;
import com.unicap.react.api.utils.UUIDUtils;
import com.unicap.react.api.utils.UuidGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@RestController
@RequestMapping(value = "/api/whisky")
@Api("Api whisky")
@CrossOrigin(origins = "*")
public class WhiskyResource {

    @Autowired
    WhiskyRepository whiskyRepository;


    @GetMapping("/whisky-list")
    @ApiOperation("Busca todos os Whiskies")
    public ResponseEntity<List<Whisky>> getWhiskiesList() {
        return ResponseEntity.ok()
                .body(whiskyRepository.findAll());
    }

    @GetMapping("/get-whisky/{uuid}")
    @ApiOperation("Busca Whisky por UUID")
    public ResponseEntity<Whisky> getWhisky(@PathVariable(value = "uuid")
                                            @Pattern(regexp = UUIDUtils.UUID_REGEXP, message = "Invalid id")
                                            @ApiParam("UUID do Whisky") String uuid) {
        return ResponseEntity.ok()
                .body(whiskyRepository.findByUuid(uuid));
    }

    @PostMapping("/insert")
    @ApiOperation("Inserir um Whisky")
    public ResponseEntity<Whisky> insertWhisky(@RequestBody Whisky whisky) {
        return ResponseEntity.ok()
                .body(whiskyRepository.save(whisky));
    }

    @PostMapping("/insert-whisky-list")
    @ApiOperation("Inserir lista de Whiskies")
    public ResponseEntity<List<Whisky>> insertWhiskiesList(@RequestBody List<Whisky> whiskyList) {
        if (!whiskyList.isEmpty()) {
            whiskyList.forEach(whisky -> whisky.setUuid(UuidGenerator.generateUuid(whisky.getName() + whisky.getType() + whisky.getStyle()).toString()));
        }
        return ResponseEntity.ok()
                .body(whiskyRepository.saveAll(whiskyList)) ;
    }

    @PutMapping("/update")
    @ApiOperation("Atualização de Whisky")
    public ResponseEntity<Whisky>  updateWhisky(@RequestBody Whisky whisky) {
        return ResponseEntity.ok()
                .body(whiskyRepository.save(whisky)) ;
    }

    @PutMapping("delete/{uuid}")
    @ApiOperation("Deleta Whisky por UUID")
    public ResponseEntity<String> deleteWhisky(@PathVariable(value = "uuid")
                             @Pattern(regexp = UUIDUtils.UUID_REGEXP, message = "Invalid id")
                             @ApiParam("UUID do Whisky") String uuid) {
        whiskyRepository.logicDelete(uuid, LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));
        return ResponseEntity.ok()
                .body("Whisky deletado com sucesso!");
    }
}

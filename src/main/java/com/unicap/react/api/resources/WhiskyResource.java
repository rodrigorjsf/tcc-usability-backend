package com.unicap.react.api.resources;

import com.unicap.react.api.models.Whisky;
import com.unicap.react.api.repository.WhiskyRepository;
import com.unicap.react.api.utils.UuidGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@RestController
@RequestMapping(value = "/api/whisky")
@Api("Api whisky")
public class WhiskyResource {

    @Autowired
    WhiskyRepository whiskyRepository;

	@CrossOrigin
    @GetMapping("/whisky-list")
    @ApiOperation("Busca todos os Whiskies")
    public List<Whisky> getWhiskiesList() {
        return whiskyRepository.findAll();
    }

	@CrossOrigin
    @GetMapping("/get-whisky/{uuid}")
    @ApiOperation("Busca Whisky por UUID")
    public Whisky getWhisky(@PathVariable(value = "uuid") @ApiParam("UUID do Whisky") String uuid) {
        return whiskyRepository.findByUuid(uuid);
    }

	@CrossOrigin
    @PostMapping("/insert")
    @ApiOperation("Inserir um Whisky")
    public Whisky insertWhisky(@RequestBody Whisky whisky) {
        return whiskyRepository.save(whisky);
    }

	@CrossOrigin
    @PostMapping("/insert-whisky-list")
    @ApiOperation("Inserir lista de Whiskies")
    public List<Whisky> insertWhiskiesList(@RequestBody List<Whisky> whiskyList) {
        if (!whiskyList.isEmpty()) {
            whiskyList.forEach(whisky -> whisky.setUuid(UuidGenerator.generateUuid(whisky.getName() + whisky.getType() + whisky.getStyle()).toString()));
        }
        return whiskyRepository.saveAll(whiskyList);
    }

	@CrossOrigin
    @PutMapping("/update")
    @ApiOperation("Atualização de Whisky")
    public Whisky updateWhisky(@RequestBody Whisky whisky) {
        return whiskyRepository.save(whisky);
    }

	@CrossOrigin
    @PutMapping("delete/{uuid}")
    @ApiOperation("Deleta Whisky por UUID")
    public void deleteWhisky(@PathVariable(value = "uuid") @ApiParam("UUID do Whisky") String uuid) {
        whiskyRepository.logicDelete(uuid, LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));
    }
}

package com.unicap.react.api.resources;

import com.unicap.react.api.models.Whisky;
import com.unicap.react.api.repository.WhiskyRepository;
import com.unicap.react.api.utils.UuidGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class WhiskyResource {

    @Autowired
    WhiskyRepository whiskyRepository;


    @GetMapping("/whisky/whisky-list")
    public List<Whisky> insertProduct() {
        return whiskyRepository.findAll();
    }

    @GetMapping("/whisky/get-whisky/{uuid}")
    public Whisky insertProduct(@PathVariable(value = "uuid") String uuid) {
        return whiskyRepository.findByUuid(uuid);
    }

    @PostMapping("/whisky/insert")
    public Whisky insertProduct(@RequestBody Whisky whisky) {
        return whiskyRepository.save(whisky);
    }

    @PostMapping("/whisky/insert-whisky-list")
    public List<Whisky> insertProduct(@RequestBody List<Whisky> whiskyList) {
        if (!whiskyList.isEmpty()) {
            whiskyList.forEach(whisky -> whisky.setUuid(UuidGenerator.generateUuid(whisky.getName() + whisky.getType() + whisky.getStyle()).toString()));
        }
        return whiskyRepository.saveAll(whiskyList);
    }

    @PutMapping("/whisky")
    public Whisky updateWhisky(@RequestBody Whisky whisky) {
        return whiskyRepository.save(whisky);
    }

    @PutMapping("/whisky/delete/{uuid}")
    public void deleteWhisky(@PathVariable(value = "uuid") String uuid) {
        System.out.println(uuid);
        whiskyRepository.logicDelete(uuid, LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));
    }
}

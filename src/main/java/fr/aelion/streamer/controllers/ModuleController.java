package fr.aelion.streamer.controllers;

import fr.aelion.streamer.dto.ModuleAddDto;
import fr.aelion.streamer.dto.ModuleDto;
import fr.aelion.streamer.services.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/module")

public class ModuleController {
    @Autowired
    private ModuleService service;

    @PostMapping
    public ResponseEntity<ModuleDto> add(@RequestBody ModuleAddDto module) {
        ModuleDto moduleDto = this.service.add(module);
        return ResponseEntity.ok(moduleDto);
    }


}

package fr.aelion.streamer.controllers;

import fr.aelion.streamer.dto.ModuleAddDto;
import fr.aelion.streamer.dto.ModuleDto;
import fr.aelion.streamer.services.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ModuleDto> findAll() {return service.findAll();}
}

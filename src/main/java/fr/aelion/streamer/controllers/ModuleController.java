package fr.aelion.streamer.controllers;

import fr.aelion.streamer.dto.ModuleAddDto;
import fr.aelion.streamer.dto.ModuleDto;
import fr.aelion.streamer.entities.Course;
import fr.aelion.streamer.entities.Module;
import fr.aelion.streamer.entities.Student;
import fr.aelion.streamer.services.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.NoSuchElementException;

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


    @DeleteMapping("/{id}")
    public ResponseEntity<?> remove(@PathVariable() int id) {
        try {
            service.remove(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> update(@RequestBody Module module, @PathVariable int id) {
        try {
            service.update(module, id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}

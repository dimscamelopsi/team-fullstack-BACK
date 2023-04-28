package fr.aelion.streamer.controllers;

import fr.aelion.streamer.dto.TypeMediaDto;
import fr.aelion.streamer.entities.TypeMedia;
import fr.aelion.streamer.services.TypeMediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * @RestController
 * @RequestMapping("/api/v1/typesMedia") public class TypeMediaController {
 * @Autowired private TypeMediaService typeMediaService;
 * @GetMapping
 * @ResponseStatus(HttpStatus.OK) public List<TypeMedia> getAllMediaTypes() {
 * return typeMediaService.getAllTypesMedia();
 * <p>
 * }
 * }
 */


@RestController
@RequestMapping("api/v1/typeMedia")


public class TypeMediaController {
    @Autowired
    private TypeMediaService service;

    @GetMapping
    public List<TypeMediaDto> finAll() {
        return service.findAll();
    }

    @GetMapping ("{title}")
   // public TypeMedia findByTitle (String title) { return service.findByTitle(title);}
    public ResponseEntity<?> findByTitle(@PathVariable String title) {
        try {
            return ResponseEntity.ok( service.findByTitle(title));
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>( "TypeMedia with " + title + " was not found", HttpStatus.NOT_FOUND);
        }
    }
}



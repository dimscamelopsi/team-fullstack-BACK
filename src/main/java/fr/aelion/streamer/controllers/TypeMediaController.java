package fr.aelion.streamer.controllers;

import fr.aelion.streamer.dto.TypeMediaDto;
import fr.aelion.streamer.services.TypeMediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}



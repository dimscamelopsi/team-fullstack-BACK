package fr.aelion.streamer.controllers;

import fr.aelion.streamer.dto.MediaAddDto;
import fr.aelion.streamer.dto.MediaDto;
import fr.aelion.streamer.entities.Media;
import fr.aelion.streamer.services.MediaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/media")
public class MediaController {
    @Autowired
    private MediaService mediaService;

    @PostMapping
    public ResponseEntity<MediaDto> add(@RequestBody MediaAddDto media) {
        MediaDto mediaDto = this.mediaService.add(media);
        return ResponseEntity.ok(mediaDto);
    }
    @GetMapping
    public List<MediaDto> findAll() {
        return mediaService.findAll();
    }

}

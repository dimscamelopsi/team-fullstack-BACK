package fr.aelion.streamer.controllers;

import fr.aelion.streamer.dto.MediaDto;
import fr.aelion.streamer.entities.Media;
import fr.aelion.streamer.repositories.ModuleRepository;
import fr.aelion.streamer.services.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api/v1/media")
public class MediaController {

    @Autowired
    private ModuleRepository moduleRepository;
    @Autowired
    private MediaService mediaService;



    @GetMapping("")
    public ResponseEntity<List<MediaDto>> getAllMedia() {
        List<MediaDto> mediaList = mediaService.findAll();
        return ResponseEntity.ok(mediaList);
    }

    @PostMapping
    public ResponseEntity<?> createMedia(
         //  @RequestParam("moduleId") Integer moduleId,
            @RequestParam("mediaType") String mediaType,
            @RequestParam("title") String title,
            @RequestParam("summary") String summary,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("duration") String duration,
            @RequestParam("mediaUrl") String mediaUrl


    ) throws IOException {

        if (mediaType.equals("Video")) {

            Media media = mediaService.createMedia(title, summary, mediaType, mediaUrl, duration);
            return ResponseEntity.status(HttpStatus.CREATED).body("add a video");
        } else {
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body("{\"error\": \"Le fichier est manquant ou vide\"}");
            }
            //String mediaUrl = saveMediaFile(file);
            mediaService.save(file);
            Media media = mediaService.createMedia(title, summary, mediaType, mediaUrl, duration);
            // Module module = moduleRepository.findById(moduleId).orElse(null);

            return ResponseEntity.status(HttpStatus.CREATED).body("Media created and linked to the module");
        }
    }

    private String saveMediaFile(MultipartFile file) {
        return null;
    }

}

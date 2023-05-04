package fr.aelion.streamer.controllers;

import fr.aelion.streamer.dto.MediaDto;
import fr.aelion.streamer.entities.Media;
import fr.aelion.streamer.entities.Module;
import fr.aelion.streamer.entities.ModuleMedia;
import fr.aelion.streamer.repositories.ModuleRepository;
import fr.aelion.streamer.services.MediaService;
import fr.aelion.streamer.services.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/media")
public class MediaController {

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private MediaService mediaService;

    @Autowired
    private ModuleService moduleService;

    @PostMapping
    public ResponseEntity<?> createMedia(@RequestParam(value = "moduleId", required = false) Integer moduleId, @RequestParam("typeMedia") String typeMedia, @RequestParam("title") String title, @RequestParam("summary") String summary, @RequestParam(value = "file", required = false) MultipartFile file, @RequestParam("duration") String duration, @RequestParam(value = "url", required = false) String url) throws IOException {
        if (typeMedia.equals("Video")) {
            Media media = mediaService.createMedia(title, summary, typeMedia, url, duration);

            if (moduleId != null) {

                Module module = moduleRepository.findById(moduleId).orElse(null);
                ModuleMedia moduleMedia = moduleService.addMediaToModule(module, media);

                if (moduleMedia != null) {

                    return ResponseEntity.status(HttpStatus.CREATED).body("Media created and linked to the module");

                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error linking media to module");
                }
            }

            return ResponseEntity.status(HttpStatus.CREATED).body("Media created ");

        } else {
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body("{\"error\": \"Le fichier est manquant ou vide\"}");
            }

            url = mediaService.save(file);
            Media media = mediaService.createMedia(title, summary, typeMedia, url, duration);

            if (moduleId != null) {

                Module module = moduleRepository.findById(moduleId).orElse(null);
                ModuleMedia moduleMedia = moduleService.addMediaToModule(module, media);
                if (moduleMedia != null) {
                    return ResponseEntity.status(HttpStatus.CREATED).body("Media created in to the module");

                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error linking media to module");

                }
            }
            return ResponseEntity.status(HttpStatus.CREATED).body("Media created in to the module");
        }
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MediaDto> findAll() {
        return mediaService.findAll();
    }
}
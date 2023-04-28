package fr.aelion.streamer.services;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.aelion.streamer.entities.TypeMedia;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import fr.aelion.streamer.dto.MediaAddDto;
import fr.aelion.streamer.dto.MediaDto;
import fr.aelion.streamer.entities.Media;
import fr.aelion.streamer.repositories.MediaRepository;
@Service
public class MediaService {
@Autowired
    private  MediaRepository mediaRepository;
    @Autowired
    private  TypeMediaService typeService;
    @Autowired
    private ModelMapper modelMapper;
    private final Path root = Paths.get("uploads");


    public Media createMedia(String title, String summary, String mediaType, String mediaUrl, String duration) {
        Media media = new Media();

        TypeMedia typeMedia = new TypeMedia();
        typeMedia = typeService.findByTitle(mediaType);

        media.setTitle(title);
        media.setSummary(summary);
        media.setTypeMedia(typeMedia);
        media.setUrl(mediaUrl);
        media.setDuration(Float.valueOf(duration));

        return mediaRepository.save(media);
    }


    public List<MediaDto> findAll() {
        List<Media> mediaList = mediaRepository.findAll();
        return mediaList.stream()
                .map(media -> modelMapper.map(media, MediaDto.class))
                .collect(Collectors.toList());
    }

    public MediaDto add(MultipartFile file, String mediaAddDtoJson) {

        ObjectMapper objectMapper = new ObjectMapper();
        MediaAddDto mediaAddDto;
        try {
            mediaAddDto = objectMapper.readValue(mediaAddDtoJson, MediaAddDto.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse mediaAddDto JSON");
        }

        mediaAddDto.setFile(file);

        var newMedia = new Media();
        var newMediaType = new TypeMedia();

        try {
            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("A file of that name already exists.");
            }
            throw new RuntimeException(e.getMessage());
        }
        newMediaType.setId(mediaAddDto.getTypeMedia().getId());

        newMedia = modelMapper.map(mediaAddDto, Media.class);
        newMediaType = modelMapper.map(mediaAddDto.getTypeMedia(),TypeMedia.class);

        newMedia.setTypeMedia(newMediaType);

        newMedia = mediaRepository.save(newMedia);

        return modelMapper.map(newMedia, MediaDto.class);
    }


    public String save(MultipartFile file) {
        try {
            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("A file of that name already exists.");
            }

            throw new RuntimeException(e.getMessage());
        }
        String originalFilename = file.getOriginalFilename();
        String[] parts = originalFilename.split("\\.");
        String extension = parts[parts.length-1];
        String filename = System.currentTimeMillis() + "." + extension;

        String mediaUrl = "/" + root + "/" +filename;
        return mediaUrl;
    }
  /*
    public Resource load(String filename) {
        Path file = Paths.get(uploadDir + filename);
        Resource resource;
        try {
            resource = new UrlResource(file.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not load the file: " + e.getMessage());
        }
        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new RuntimeException("Could not load the file: " + filename);
        }
        [15:07] Lina EL AMRANI





package fr.aelion.streamer.controllers;

import fr.aelion.streamer.dto.MediaDto;
import fr.aelion.streamer.entities.Media;
import

fr.aelion.streamer.entities.Module;
import fr.aelion.streamer.entities.ModuleMedia;
import fr.aelion.streamer.repositories.ModuleRepository;
import fr.aelion.streamer.services.MediaService;
import fr.aelion.streamer.services.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/v1/media")
public class MediaController {
    @Autowired
    private MediaService mediaService;
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private ModuleRepository moduleRepository;

    @PostMapping
    public ResponseEntity<?> createMedia(
            @RequestParam("moduleId") Integer moduleId,
            @RequestParam("mediaType") String mediaType,
            @RequestParam("title") String title,
            @RequestParam("summary") String summary,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("duration") String duration
    ) throws IOException {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("Le fichier est manquant ou vide");
        }

        String mediaUrl = saveMediaFile(file);
        Media media = mediaService.createMedia(title, summary, mediaType, mediaUrl, duration);
        Module module = moduleRepository.findById(moduleId).orElse(null);
        if (module == null) {
            return ResponseEntity.badRequest().body("Module not found");
        }

        ModuleMedia moduleMedia = moduleService.addMediaToModule(module, media);
        if (moduleMedia != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Media created and linked to the module");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error linking media to module");
        }
    }

    private String saveMediaFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return "Le fichier est manquant ou vide";
        }

        Path mediaDir = Paths.get("media");
        if (!Files.exists(mediaDir)) {
            try {
                Files.createDirectories(mediaDir);
            } catch (IOException e) {
                return "Erreur lors de la création du dossier media";
            }
        }

        String originalFilename = file.getOriginalFilename();
        String[] parts = originalFilename.split("\\.");
        String extension = parts[parts.length-1];
        String filename = System.currentTimeMillis() + "." + extension;

        String mediaUrl = "/media/" + filename; //

        String absolutePath = new File(".").getAbsolutePath();
        String filePathString = absolutePath + File.separator + "media" + File.separator + filename;
        Path filepath = Paths.get(filePathString);

        try {
            Files.copy(file.getInputStream(), filepath);
        } catch (IOException e) {
            return "Erreur lors de la sauvegarde du fichier";
        }
        return mediaUrl;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMedia(@PathVariable("id") Integer id) {
        mediaService.deleteMedia(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public List<MediaDto> getAllMedia(){
        return mediaService.findAll();
    }

}

 

 



    }*/


}

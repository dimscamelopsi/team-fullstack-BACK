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

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


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
    ModelMapper modelMapper;

    public MediaDto add(MediaAddDto media) {
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
        newMediaType.setId(media.getTypeMedia().getId());
        newMediaType.setId(mediaAddDto.getTypeMedia().getId());

        newMedia = modelMapper.map(media, Media.class);
        newMediaType = modelMapper.map(media.getTypeMedia(), TypeMedia.class);
        newMedia = modelMapper.map(mediaAddDto, Media.class);
        newMediaType = modelMapper.map(mediaAddDto.getTypeMedia(),TypeMedia.class);

        newMedia.setTypeMedia(newMediaType);

        newMedia = mediaRepository.save(newMedia);

        return modelMapper.map(newMedia, MediaDto.class);
    }


    public List<MediaDto> findAll() {
        return mediaRepository.findAll()
                .stream()
                .map(media -> modelMapper.map(media, MediaDto.class))
                .collect(Collectors.toList());
    }

    @PostMapping("/uploadFile")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";
        try {


            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    private final Path root = Paths.get("uploads");

    public void init(String mediaUrl) {
        try {
            URL url = new URL(mediaUrl);
            InputStream in = url.openStream();
            Files.copy(in, this.root.resolve(url.getFile()));
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    public void save(MultipartFile file) {
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

    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }
}

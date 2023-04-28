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



    /**
     *
     * @param title @Autowired
     *     public MediaService(MediaRepository mediaRepository, TypeMediaService typeService, ModelMapper modelMapper) {
     *         this.mediaRepository = mediaRepository;
     *         this.typeService = typeService;
     *         this.modelMapper = modelMapper;
     *     }
     * @param summary
     * @param mediaType
     * @param mediaUrl
     * @param duration
     * @return
     */
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

   /* public void save(MultipartFile file) {
        try {
            Files.copy(file.getInputStream(), Paths.get(uploadDir + file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Could not save the file: " + e.getMessage());
        }
    }

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
    }*/


}

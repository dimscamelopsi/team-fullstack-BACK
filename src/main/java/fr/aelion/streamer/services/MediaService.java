package fr.aelion.streamer.services;

import fr.aelion.streamer.dto.MediaDto;
import fr.aelion.streamer.entities.Media;
import fr.aelion.streamer.entities.TypeMedia;
import fr.aelion.streamer.repositories.MediaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MediaService {

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private TypeMediaService typeService;
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

    public void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    public String save(MultipartFile file) {
        init();

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
        String extension = parts[parts.length - 1];
        String filename = System.currentTimeMillis() + "." + extension;

        String mediaUrl = originalFilename;
        return mediaUrl;
    }

    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }

    public Resource load(String fileName) {
        try {
        /*    String newFile = fileName.substring(9);
            int extensionIndex = newFile.lastIndexOf(".");
            newFile = newFile.substring(0, extensionIndex);
            System.out.println("####### "+newFile);*/
            Path file = root.resolve(fileName);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
}

package fr.aelion.streamer.services;
import fr.aelion.streamer.dto.MediaAddDto;
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

@Service
public class MediaService {
    @Autowired
    MediaRepository mediaRepository;
    @Autowired
    ModelMapper modelMapper;
    public MediaDto add(MediaAddDto media)  {


        var newMedia = new Media();
        var newMediaType = new TypeMedia();
        MultipartFile file = media.getFile();

        try {
            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("A file of that name already exists.");
            }
            throw new RuntimeException(e.getMessage());
        }
       // newMediaType.setId( media.getTypeMedia().getId());

        newMedia = modelMapper.map(media, Media.class);
        newMediaType = modelMapper.map(media.getTypeMedia(),TypeMedia.class);

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
        try {
            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("A file of that name already exists.");
            }

            throw new RuntimeException(e.getMessage());
        }
    }

    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
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
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }
}

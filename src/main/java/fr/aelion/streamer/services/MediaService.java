package fr.aelion.streamer.services;
import fr.aelion.streamer.dto.MediaAddDto;
import fr.aelion.streamer.dto.MediaDto;
import fr.aelion.streamer.entities.Media;
import fr.aelion.streamer.entities.TypeMedia;
import fr.aelion.streamer.exceptions.message.ResponseMessage;
import fr.aelion.streamer.repositories.MediaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
       newMediaType.setId( media.getTypeMedia().getId());

        newMedia = modelMapper.map(media, Media.class);
        newMediaType = modelMapper.map(media.getTypeMedia(),TypeMedia.class);

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

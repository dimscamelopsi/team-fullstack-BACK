package fr.aelion.streamer.services;
import fr.aelion.streamer.dto.CourseAddDto;
import fr.aelion.streamer.dto.FullCourseDto;
import fr.aelion.streamer.dto.MediaAddDto;
import fr.aelion.streamer.dto.MediaDto;
import fr.aelion.streamer.entities.Course;
import fr.aelion.streamer.entities.Media;
import fr.aelion.streamer.entities.Module;
import fr.aelion.streamer.entities.TypeMedia;
import fr.aelion.streamer.repositories.MediaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class MediaService {
    @Autowired
    MediaRepository repository;
    @Autowired
    ModelMapper mapper;
    public MediaDto add(MediaAddDto media) {

        var newMedia = new Media();
        var newMediaType = new TypeMedia();

       // newMediaType.setId( media.getTypeMedia().getId());

        newMedia = mapper.map(media, Media.class);
        newMediaType = mapper.map(media.getTypeMedia(),TypeMedia.class);

        newMedia.setTypeMedia(newMediaType);

        newMedia = repository.save(newMedia);

        return mapper.map(newMedia, MediaDto.class);
    }
}

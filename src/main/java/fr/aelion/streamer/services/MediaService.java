package fr.aelion.streamer.services;
import fr.aelion.streamer.dto.MediaAddDto;
import fr.aelion.streamer.dto.MediaDto;
import fr.aelion.streamer.entities.Media;
import fr.aelion.streamer.entities.TypeMedia;
import fr.aelion.streamer.repositories.MediaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MediaService {
    @Autowired
    MediaRepository mediaRepository;
    @Autowired
    ModelMapper modelMapper;
    public MediaDto add(MediaAddDto media) {

        var newMedia = new Media();
        var newMediaType = new TypeMedia();

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
}

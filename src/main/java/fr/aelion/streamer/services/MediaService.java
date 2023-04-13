package fr.aelion.streamer.services;
import fr.aelion.streamer.dto.MediaDto;
import fr.aelion.streamer.repositories.MediaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MediaService {
    @Autowired
    private MediaRepository mediaRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<MediaDto> findAll() {
        return mediaRepository.findAll()
                .stream()
                .map(media -> modelMapper.map(media, MediaDto.class))
                .collect(Collectors.toList());
    }


}

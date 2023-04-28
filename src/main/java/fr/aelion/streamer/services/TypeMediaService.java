package fr.aelion.streamer.services;

import fr.aelion.streamer.dto.TypeMediaDto;
import fr.aelion.streamer.entities.TypeMedia;
import fr.aelion.streamer.repositories.TypeMediaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TypeMediaService {
    /*    @Autowired
        private TypeMediaRepository typeMediaRepository;

        public List<TypeMedia> getAllTypesMedia() {
            return typeMediaRepository.findAll();
        }*/
    @Autowired
    private TypeMediaRepository typeMediaRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<TypeMediaDto> findAll() {
        var typeMedias = typeMediaRepository.findAll()
                .stream()
                .map((typeMedia) -> {
                    var typeMediaDto = modelMapper.map(typeMedia, TypeMediaDto.class);
                    return typeMediaDto;
                })
                .collect((Collectors.toList()));
        return typeMedias;
    }
    public TypeMedia findByTitle(String title) {
        return typeMediaRepository.findByTitle(title) ;
    }
}


package fr.aelion.streamer.services;

import fr.aelion.streamer.dto.ModuleAddDto;
import fr.aelion.streamer.dto.ModuleDto;
import fr.aelion.streamer.entities.Course;
import fr.aelion.streamer.entities.Media;
import fr.aelion.streamer.entities.Module;
import fr.aelion.streamer.repositories.ModuleMediaRepository;
import fr.aelion.streamer.repositories.ModuleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ModuleService {
    @Autowired
    private ModuleRepository repository;
    @Autowired
    private ModuleMediaRepository moduleMediaRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CourseServiceImpl courseService;
    public ModuleDto add(ModuleAddDto moduleAddDto) {
        Module newModule = new Module();
        newModule.setName(moduleAddDto.getName());
        newModule.setObjective(moduleAddDto.getObjective());
        Course course = new Course();
        if (moduleAddDto.getCourse() != null) {
            course.setId(moduleAddDto.getCourse().getId());
            newModule.setCourse(course);
        }
        if (moduleAddDto.getMedia() != null) {
            List<Media> medias = new ArrayList<>();
            moduleAddDto.getMedia().forEach(mDto -> {

                var media = modelMapper.map(mDto, Media.class);
                medias.add(media);
            });
            newModule.setMedias(medias);

        }
        newModule = repository.save(newModule);

        return modelMapper.map(newModule, ModuleDto.class);
    }

    /**
     *
     * @return
     */
    public List<ModuleDto> findAll() {
        var modules = repository.findAll()
                .stream()
                .map(module -> {
                    var moduleDto = modelMapper.map(module, ModuleDto.class);
                    var medias = moduleDto.getMedias();
                    moduleDto.setTotalTime(courseService.convertToTime(medias));
                    return moduleDto;
                })
                .collect(Collectors.toList());

        return modules;
    }

    public void remove(int id) {
        var aModule = repository.findById(id);

        if (aModule.isPresent()) {
            repository.delete(aModule.get());}
        else {
            throw new NoSuchElementException();}
    }



}

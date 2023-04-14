package fr.aelion.streamer.services;

import fr.aelion.streamer.dto.ModuleAddDto;
import fr.aelion.streamer.dto.ModuleDto;
import fr.aelion.streamer.entities.Course;
import fr.aelion.streamer.entities.Module;
import fr.aelion.streamer.repositories.ModuleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModuleService {
    @Autowired
    private ModuleRepository repository;
    @Autowired
    private ModelMapper modelMapper;

    public ModuleDto add(ModuleAddDto moduleAddDto) {
        Module newModule = new Module();
        newModule.setName(moduleAddDto.getName());
        newModule.setObjective(moduleAddDto.getObjective());
        Course course = new Course();
        if (moduleAddDto.getCourse() != null) {
            course.setId(moduleAddDto.getCourse().getId());
            newModule.setCourse(course);
        }
        newModule = repository.save(newModule);
        return modelMapper.map(newModule, ModuleDto.class);
    }


}

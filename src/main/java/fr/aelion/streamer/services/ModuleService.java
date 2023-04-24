package fr.aelion.streamer.services;

import fr.aelion.streamer.dto.CourseUserDto;
import fr.aelion.streamer.dto.ModuleAddDto;
import fr.aelion.streamer.dto.ModuleDto;
import fr.aelion.streamer.entities.Course;
import fr.aelion.streamer.entities.Module;
import fr.aelion.streamer.entities.Student;
import fr.aelion.streamer.repositories.ModuleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ModuleService {
    @Autowired
    private ModuleRepository repository;
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
        newModule = repository.save(newModule);
        return modelMapper.map(newModule, ModuleDto.class);
    }

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

    ;

    public List<Module> findByCourse(Integer id) {

        List<CourseUserDto> courses = courseService.findCoursesByStudent(id);
        List<Module> momo = new ArrayList<>();
        courses.stream().map(courseUserDto -> {
            Course course = modelMapper.map(courseUserDto, Course.class);
            Set<Module> modules = course.getModules();
             modules.stream().map(momo::add);
            return courses;
        });
        return  momo;
    }


}

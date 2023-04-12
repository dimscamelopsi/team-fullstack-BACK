package fr.aelion.streamer.services;

import fr.aelion.streamer.dto.*;
import fr.aelion.streamer.entities.Course;
import fr.aelion.streamer.entities.Media;
import fr.aelion.streamer.entities.Module;
import fr.aelion.streamer.entities.Student;
import fr.aelion.streamer.repositories.CourseRepository;
import fr.aelion.streamer.repositories.MediaRepository;
import fr.aelion.streamer.repositories.ModuleRepository;
import fr.aelion.streamer.repositories.StudentRepository;
import fr.aelion.streamer.services.interfaces.CourseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseRepository repository;
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private ModuleRepository moduleRepository;
    @Autowired
    ModelMapper modelMapper;
    public List<FullCourseDto> findAll() {
        var fullCourses = repository.findAll()
                .stream()
                .map(c -> {
                    var fullCourseDto = modelMapper.map(c, FullCourseDto.class);
                    return fullCourseDto;
                })
                .collect(Collectors.toList());
        // Compute media time
        for (FullCourseDto fc : fullCourses) {
            for (ModuleDto m : fc.getModules()) {
                var medias = m.getMedias();
                m.setTotalTime(convertToTime(medias));
            }
        }
        return fullCourses;
    }

    public List<FullCourseDto> getListCourseByAutor(String login){
        Student student = studentRepository.findByLogin(login);

        var listCourses = repository.findByPersonneId(student.getId())
                .stream()
                .map(c -> {
                    var fullCourseDto = modelMapper.map(c, FullCourseDto.class);
                    return fullCourseDto; })
                .collect(Collectors.toList());
        return listCourses;
    }

    @Override
    public FullCourseDto findOne(int id) {
       return repository.findById(id)
               .map((c) -> {
                    var fullCourseDto =  modelMapper.map(c, FullCourseDto.class);
                    return fullCourseDto;
               })
               .orElseThrow();

    }

    @Override
    public void remove(int id) {
        // Préambule : récupérer le cours dans son intégralité
        var oCourse = repository.findById(id);

        if (oCourse.isPresent()) {
            // 1. Update all medias of all modules to null module
            for (Module module : oCourse.get().getModules()) {
                for (Media media : module.getMedias()) {
                    media.setModules(null);
                    mediaRepository.save(media);
                }
                moduleRepository.delete(module);
            }

            // 3. Remove course
            repository.delete(oCourse.get());
        } else {
            throw new NoSuchElementException();
        }

    }

    @Override
    public FullCourseDto add(CourseAddDto course) {
        var newCourse = new Course();
        newCourse.setTitle(course.getTitle());
        newCourse.setObjective(course.getObjective());

        newCourse = repository.save(newCourse);

        if (course.getModules().size() > 0) {
            Course finalNewCourse = newCourse;
            Set<Module> courseModules = new HashSet<>();
            course.getModules().forEach(mDto -> {
                var module = modelMapper.map(mDto, Module.class);
                module.setCourse(finalNewCourse);
                module = moduleRepository.save(module);
                courseModules.add(module);
            });
            finalNewCourse.setModules(courseModules);
        }
        return modelMapper.map(newCourse, FullCourseDto.class);
    }

    private String convertToTime(Set<MediaDto> medias) {
        Float time = medias.stream()
                .map(m -> {
                    m.setTotalTime(LocalTime.MIN.plusSeconds(m.getDuration().longValue()).toString());
                    return m;
                })
                .map(m -> m.getDuration())
                .reduce(Float.valueOf(0), (subtotal, duration) -> subtotal + duration);

        var timeAsLong = Math.round(time);

        return LocalTime.MIN.plusSeconds(timeAsLong).toString();

    }
}

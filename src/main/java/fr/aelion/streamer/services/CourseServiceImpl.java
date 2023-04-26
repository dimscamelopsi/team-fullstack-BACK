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

import java.time.LocalDate;
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
    private StudentService studentService;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private ModelMapper modelMapper;

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

    @Override
    public List<FullCourseDto> getListCourseByAutor(int id) {
        Optional<Student> student = studentRepository.findById(id);
        System.out.println("#######   getListCourseByAutor : " + student.get().getId());

        var listCoursesManage = repository.findByPersonneId(student.get().getId())
                .stream()
                .map(c -> {
                    var fullCourseDto = modelMapper.map(c, FullCourseDto.class);
                    return fullCourseDto;
                }).collect(Collectors.toList());

        System.out.println("#######   getListCourseByAutor :" + listCoursesManage);
        for (FullCourseDto fc : listCoursesManage) {
            for (ModuleDto m : fc.getModules()) {
                var medias = m.getMedias();
                m.setTotalTime(convertToTime(medias));
            }
        }
        return listCoursesManage;
    }

    @Override
    public FullCourseDto findOne(int id) {
        return repository.findById(id)
                .map((c) -> {
                    var fullCourseDto = modelMapper.map(c, FullCourseDto.class);
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
        newCourse.setStudent(course.getStudent());

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

    public String convertToTime(Set<MediaDto> medias) {
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

    /**
     * Returned all courses associated with a user
     *
     * @return
     */
    public List<CourseUserDto> findCoursesByStudent(int id) {
        // TODO : Trouver la liste des courses a partir de ID de Student/User
        Student currentUser = studentService.findOne(id);
        // System.out.println(currentUser.getId());
        List<Course> coursesList = new ArrayList<>();
        // TODO MAP
        try {
            coursesList = repository.findAllCourseUsersNative(currentUser.getId());
            // System.out.println(coursesList);
        } catch (Exception e) {
            // System.out.println(e);
        }
        List<CourseUserDto> newCoursList = new ArrayList<>();
        coursesList.stream().map(
                course -> {
                    return newCoursList.add(modelMapper.map(course, CourseUserDto.class));
                }).collect(Collectors.toList());

        return newCoursList;
    }

    @Override
    public void update(Course course, Student student) throws Exception {
        try {
            course.setStudent(student);
            course.setUpdatedAt(LocalDate.now());
            repository.save(course);
        } catch (Exception e) {
            throw new Exception("Something went wrong while updating Student");
        }
    }
}
package fr.aelion.streamer.services.interfaces;

import fr.aelion.streamer.dto.CourseAddDto;
import fr.aelion.streamer.dto.CourseUserDto;
import fr.aelion.streamer.dto.FullCourseDto;
import fr.aelion.streamer.entities.Course;
import fr.aelion.streamer.entities.Student;

import java.util.List;

public interface CourseService {
    List<FullCourseDto> findAll();

    FullCourseDto findOne(int id);

    List<CourseUserDto> findCoursesByStudent(int id);

    void remove(int id);

    FullCourseDto add(CourseAddDto course);

    List<FullCourseDto> getListCourseByAutor(String auth);

    void update(Course course, Student student) throws Exception;
}

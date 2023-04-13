package fr.aelion.streamer.services.interfaces;

import fr.aelion.streamer.dto.CourseAddDto;
import fr.aelion.streamer.dto.CourseUserDto;
import fr.aelion.streamer.dto.FullCourseDto;
import fr.aelion.streamer.entities.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    List<FullCourseDto> findAll();

    FullCourseDto findOne(int id);
    List<CourseUserDto> findCoursesByStudent(int id);

    void remove(int id);

    FullCourseDto add(CourseAddDto course);
}

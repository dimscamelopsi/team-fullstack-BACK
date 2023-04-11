package fr.aelion.streamer.services.interfaces;

import fr.aelion.streamer.dto.CourseAddDto;
import fr.aelion.streamer.dto.FullCourseDto;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    List<FullCourseDto> findAll();

    FullCourseDto findOne(int id);


    void remove(int id);

    FullCourseDto add(CourseAddDto course);


}

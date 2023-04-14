package fr.aelion.streamer.dto;

import fr.aelion.streamer.entities.Course;
import fr.aelion.streamer.entities.Module;
import fr.aelion.streamer.entities.Student;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Getter
@Setter
public class FullCourseDto {
    private int id;
    private String title;
    private LocalDate createdAt;

    private LocalDate updatedAt;

    private String objective;

    private Boolean publish;

//    private Student student;

    private Set<ModuleDto> modules = new HashSet<>();



}

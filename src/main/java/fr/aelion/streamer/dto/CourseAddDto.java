package fr.aelion.streamer.dto;

import fr.aelion.streamer.entities.Student;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class CourseAddDto {
    private String title;
    private String objective;
    private Set<ModuleAddDto> modules;

    private Student student;
}

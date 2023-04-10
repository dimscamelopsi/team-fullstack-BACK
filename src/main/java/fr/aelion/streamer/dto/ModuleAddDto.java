package fr.aelion.streamer.dto;

import fr.aelion.streamer.entities.Course;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModuleAddDto {
    private String name;
    private String objective;
    private Course course;
}

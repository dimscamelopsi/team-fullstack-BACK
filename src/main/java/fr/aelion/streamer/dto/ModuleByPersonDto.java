package fr.aelion.streamer.dto;

import fr.aelion.streamer.entities.Course;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class ModuleByPersonDto {
    private String name;
    private String objective;
    private int courseId;
    private int orderModule;
}

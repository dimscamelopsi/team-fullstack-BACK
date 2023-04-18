package fr.aelion.streamer.dto;

import fr.aelion.streamer.entities.Course;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class ModuleAddDto {
    private String name;
    private String objective;
    @Column(nullable = false)
    private Course course;
    private Set<MediaDto> media;

}

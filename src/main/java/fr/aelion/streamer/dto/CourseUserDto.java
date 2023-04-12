package fr.aelion.streamer.dto;

import fr.aelion.streamer.entities.Course;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseUserDto {
    private int id;
    private String title;
}

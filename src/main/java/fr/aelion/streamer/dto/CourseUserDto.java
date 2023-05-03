package fr.aelion.streamer.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CourseUserDto {

    private int id;

    private String title;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    private String objective;

    private Boolean publish;
}

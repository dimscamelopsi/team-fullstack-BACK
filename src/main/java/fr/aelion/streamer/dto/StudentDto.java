package fr.aelion.streamer.dto;

import fr.aelion.streamer.entities.enums.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class StudentDto {

    private int id;

    private String lastName;

    private String firstName;

    private String email;

    private String phoneNumber;

    private String login;

    private String password;

    private String answer;

    private Role role;

    private Set<FullCourseDto> courses;
}

package fr.aelion.streamer.dto;

import fr.aelion.streamer.entities.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    private int id;

    private String login;

    private String password;

    private Role role;
}

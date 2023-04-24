package fr.aelion.streamer.repositories;

import fr.aelion.streamer.dto.ModuleDto;
import fr.aelion.streamer.entities.Module;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModuleRepository extends JpaRepository<Module, Integer> {


}

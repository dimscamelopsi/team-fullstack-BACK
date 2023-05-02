package fr.aelion.streamer.repositories;

import fr.aelion.streamer.entities.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ModuleRepository extends JpaRepository<Module, Integer> {

    @Query(value = "SELECT module.*, course.title AS title_course FROM public.course INNER JOIN module ON course.id = module.course_id AND personne_id= :id", nativeQuery = true)
    public List<Module> getListModuleByPersonId(@Param("id") int id);

}

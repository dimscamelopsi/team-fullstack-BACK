package fr.aelion.streamer.repositories;

import fr.aelion.streamer.dto.CourseUserDto;
import fr.aelion.streamer.dto.SimpleStudentProjection;
import fr.aelion.streamer.entities.Course;
import fr.aelion.streamer.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    @Query(
            value = "SELECT c.* FROM Course c WHERE c.personne_id = :personne_id",
            nativeQuery = true)
    List<Course> findAllCourseUsersNative(@Param("personne_id") int personne_id);
    List<Course> findCoursesByStudent(Student student);

}

package fr.aelion.streamer.repositories;

import fr.aelion.streamer.entities.TypeMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TypeMediaRepository extends JpaRepository <TypeMedia, Integer>  {



    @Query (value =" SELECT * FROM type_media WHERE title = :title", nativeQuery = true)
    TypeMedia findByTitle(@Param("title")String title);
}


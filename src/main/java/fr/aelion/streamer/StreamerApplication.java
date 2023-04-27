package fr.aelion.streamer;

import fr.aelion.streamer.services.MediaService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class StreamerApplication {

	public static void main(String[] args) {
		SpringApplication.run(StreamerApplication.class, args);
	}

	MediaService mediaService;

}




package ma.emsi.cinema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ma.emsi.cinema.service.ICinemaInitService;

@SpringBootApplication
public class CinemaApplication implements CommandLineRunner {
	@Autowired
	private ICinemaInitService cinemaInitServices;
	public static void main(String[] args) {
		SpringApplication.run(CinemaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		cinemaInitServices.initVilles();
		cinemaInitServices.initCinemas();
		cinemaInitServices.initSalles();
		cinemaInitServices.initPlaces();
		cinemaInitServices.initSeances();
		cinemaInitServices.initCategories();
		cinemaInitServices.initfilms();
		cinemaInitServices.initProjections();
		cinemaInitServices.initTickets();
		
	}

}
